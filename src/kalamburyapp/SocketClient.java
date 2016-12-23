package kalamburyapp;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

public class SocketClient extends Thread{
    public int id;
    public boolean client, server;
    private int port;
    private String ip;
    public Chat chat;
    private PaintArea paintArea;
    private Toolbar toolbar;
    private ArrayList<String> dictionary = new ArrayList<String>();
    private Random random = new Random();
        
    Socket clientSocket;
    OutputStream outputStream;
    InputStream inputStream;
        
    ObjectOutputStream objOutput;
    ObjectInputStream objInput;
        
    public Timer timer;
    public int roundTime = 60;
    public int timeLeft;
    public Tour tour;

    public SocketClient(String hostname, int port, Chat chat, PaintArea paintArea, Toolbar toolbar) throws IOException {
        client = true;
        this.toolbar = toolbar;
        this.ip = hostname;
        this.port = port;
        this.chat = chat;
        this.paintArea = paintArea;
        start();
    }
        
    public SocketClient(int port, Chat chat, PaintArea paintArea, Toolbar toolbar) throws IOException {
        server = true;
        this.toolbar = toolbar;
        this.port = port;
        this.chat = chat;
        this.paintArea = paintArea;
        toolbar.word.setVisible(true);
        toolbar.word.setText("Waiting for client");
        start();
    }
        
    // Rozpoczyna pracę wątku
    public void run() {
        try {
            if (client) 
                joinServer();
            else if (server) 
                startServer();
            while (true) {
                readStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    // Ustawia podstawowe obiekty
    private void setComponents() {
        try {
            outputStream = clientSocket.getOutputStream();
            inputStream = clientSocket.getInputStream();
                
            objOutput = new ObjectOutputStream(outputStream);
            objInput = new ObjectInputStream(inputStream);
            
            paintArea.setSocketClient(this);
            chat.setServer(this);   
            loadDictionary();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    // Funkcja rozpoczyna nową turę w grze
    public void startNewTour(int id) {
        try {
            paintArea.clearBoard();
            Tour newTour = new Tour(id, getRandomWord());
            this.tour = newTour;
            Cargo cargo = new Cargo(tour);
            objOutput.writeObject(cargo);
            objOutput.flush();               
        } catch(Exception e) {
            chat.getTextArea().append(e.toString());
        }
    }
        
    // Funkcja wyświetla ip gracza na chacie
    public void showIp() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();
            chat.getTextArea().append("IP: " + ip + "\n");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
        
    // Funkcja tworząca server i akceptująca klienta
    public void startServer() {	          
        try {
            showIp();
            id = 1;
            ServerSocket serverSocket = new ServerSocket(port);               
            clientSocket = serverSocket.accept();
            toolbar.word.setText("");
            setComponents();               
            startNewTour(2);                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    // Funkcja, która pozwala dołączyć klientowi do servera utworzonego przez hosta
    public void joinServer() throws IOException {
        id = 2;
        clientSocket = new Socket(ip, port);           
        setComponents();
    } 
       
    public void loadDictionary() {
        String line;
        InputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;  

        try {
            fis = new FileInputStream("baza.txt");
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);   
            dictionary.clear();
            while ((line = br.readLine()) != null)
                dictionary.add(line);
        } catch(IOException ex) {
              chat.getTextArea().append("Problem z odczytaniem pliku baza.txt.\n");
        }
    }
    
    public String getRandomWord() {
        int randomLine;        
        randomLine = random.nextInt(dictionary.size());
        String randomWord = dictionary.get(randomLine);
        dictionary.remove(randomLine);
        if (dictionary.size() == 0)
            loadDictionary();

        return randomWord;
    }
     
    public void startTimer() {
        int delay = 1000; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                timeLeft--;
                toolbar.word.setText(tour.word + " " + Integer.toString(timeLeft));
                if (timeLeft < 0) {
                    stopTimer();
                    chat.getTextArea().append("Czas upłynął.\n");
                    Cargo cargo = new Cargo("Czas upłynął.", "SYSTEM");
                    try {
                        objOutput.writeObject(cargo);
                        objOutput.flush(); 
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (id == 1) 
                        startNewTour(2);
                    else if (id == 2)
                        startNewTour(1);
                    }
                }
            };
        timer = new Timer(delay, taskPerformer);
        timer.start();
    }
     
    public void stopTimer() {
        timer.stop();
        toolbar.word.setText("");
    }
    
    // Funkcja odczytująca wiadomości
    public void readMessage(Cargo userInput) {
        if (userInput.nick.equals("SYSTEM"))
            chat.getTextArea().append(userInput.message + "\n");
        else
            chat.getTextArea().append(userInput.nick + ": " + userInput.message + "\n");            
        String msg = userInput.message.toLowerCase();
        String word = tour.word.toLowerCase();
        tour.word.toLowerCase();
        if (msg.equals(word) && tour.playerId == id) {
            stopTimer();
            chat.getTextArea().append("Hasło zostało odgadnięte.\n");
            paintArea.clearBoard();
            try {
                toolbar.word.setText("");
                Cargo cargo = new Cargo("Gratulacje. Odgadłeś hasło.", "SYSTEM");
                objOutput.writeObject(cargo);
                objOutput.flush();                   
                if (id == 1) 
                    startNewTour(2);
                else if (id == 2)
                    startNewTour(1);
            } catch(Exception e) {
                chat.getTextArea().append(e.toString());
            }
        }
        chat.scrollToBottom();
    }
           
    // Funkcja odbierająca rysunek
    public void readDrawing(Cargo userInput) {
        try {
            if (userInput.arrayNumber > paintArea.currentArray) {
                paintArea.currentArray++;
                paintArea.addArray();
            }
            if (userInput.arrayNumber < 0) {
                paintArea.arraysArray.clear();
                paintArea.currentArray = userInput.arrayNumber;
            }                  
            else
                paintArea.addToArray(userInput);
                            
            paintArea.repaint();
        } catch (Exception e) {
            chat.getTextArea().append(e.toString() + "\n");
        }
    }

    // Funkcja odbierająca informacje o turze
    public void readTour(Cargo userInput) {
        this.tour = userInput.tour;
        if (userInput.tour.playerId == this.id) {
            timeLeft = roundTime;
            chat.getTextArea().append("Twoja tura! Zacznij rysować\n");
            chat.getTextArea().append("Hasło: " + userInput.tour.word + "\n");
            toolbar.word.setText(tour.word + " " + Integer.toString(timeLeft));
        }
        else {
            chat.getTextArea().append("Zgaduj hasło!\n");
        }
        startTimer();
        chat.scrollToBottom();           
    }
     
    // Główna funkcja odczytująca wszystkie typy ze strumienia
    public void readStream() {
        try {
            Cargo userInput = null;
            while((userInput = (Cargo)objInput.readObject()) != null) {
                switch (userInput.dataType) {
                    case 1:
                        readMessage(userInput);
                        break;
                    case 2:
                        readDrawing(userInput);
                        break;
                    case 3:
                        readTour(userInput);
                        break;
                }
            }
        } catch(Exception e) {
            chat.getTextArea().append(e.toString() + "\n");
        }
    }      
}
