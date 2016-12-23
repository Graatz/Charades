package kalamburyapp;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
 
public class PaintArea extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 600;
 
    ArrayList<ArrayList<Tool>> arraysArray = new ArrayList<ArrayList<Tool>>();
    SocketClient socketClient;
    int currentArray = -1;
    public Color toolColor;
    
    ObjectOutputStream objOutput;
    ObjectInputStream objInput;
 
    public PaintArea() {  	
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);              
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setFocusable(true);
    }
    
    public class Tool {
        public Point point;
        public Color color;
        public Tool(Point point, Color color) {
            this.point = point;
            this.color = color;
        }
    }
    
    // Funkcja dodaje część do rysunku
    public void addArray() {
        arraysArray.add(new ArrayList<Tool>());
    }
    
    // Funkcja dodaje linię do częsci rysunku
    public void addToArray(Cargo userInput) {
        arraysArray.get(currentArray).add(new PaintArea.Tool(new Point(userInput.point.x, userInput.point.y), userInput.color));
    }
    
    // Funkcja ustawia Socketa dla tej klasy
    public void setSocketClient(SocketClient socketClient) throws IOException {
        this.socketClient = socketClient;
        objOutput = new ObjectOutputStream(socketClient.outputStream);
        objInput = new ObjectInputStream(socketClient.inputStream);
    }
    
    // Funkcja rysująca
    private void drawLines(Graphics2D g2d) {
        double x1, y1, x2, y2;
        for(int i = 0; i < arraysArray.size(); i++)
            for(int j = 0; j < arraysArray.get(i).size(); j++)
                if (j > 0) {
                    g2d.setColor(arraysArray.get(i).get(j).color);
                    x1 = arraysArray.get(i).get(j).point.x;
                    y1 = arraysArray.get(i).get(j).point.y;
                	
                    x2 = arraysArray.get(i).get(j - 1).point.x;
                    y2 = arraysArray.get(i).get(j - 1).point.y;
                	
                    g2d.draw(new Line2D.Double(x1, y1, x2, y2));
                }         		
    }
    
    // Funkcja czyści rysunek z tablicy
    public void clearBoard() {
        arraysArray.clear();
        currentArray = -1;
        repaint();           
        sendPoint(null, currentArray);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        drawLines(g2d);
    }
    
    @Override
    public void keyPressed(KeyEvent mouse) {
    	if(mouse.getKeyCode() == KeyEvent.VK_C) {
            clearBoard();
    	}
    }
    
    @Override
    public void mouseDragged(MouseEvent mouse) {
        try {
            if (currentArray >= 0 && (socketClient.tour.playerId == socketClient.id)) {
                arraysArray.get(currentArray).add(new Tool(new Point(mouse.getX(), mouse.getY()), toolColor));
                repaint();
                sendPoint(new Point(mouse.getX(), mouse.getY()), currentArray);
            }
        } catch (Exception ex){
            //
        }
    }
    
    @Override
    public void mousePressed(MouseEvent mouse) {
        try {
            if (socketClient.tour.playerId == socketClient.id) {
                currentArray ++;
                if (currentArray >= 0) {               
                    arraysArray.add(new ArrayList<Tool>());
                    arraysArray.get(currentArray).add(new Tool(new Point(mouse.getX(), mouse.getY()), toolColor));
                    repaint(); 
                    grabFocus();
                    sendPoint(new Point(mouse.getX(), mouse.getY()), currentArray);
                }
            }
        } catch (Exception ex){
            //
        } 
    }
    
    // Funkcja wysyła rysunek do strumienia
    public void sendPoint(Point point, int currentArray) {   
        try {
            Cargo cargo = new Cargo(point, currentArray, toolColor);
            socketClient.objOutput.writeObject(cargo);
            socketClient.objOutput.flush();
        } 
        catch (Exception e) {
            socketClient.chat.getTextArea().append(e.toString() + "\n");
        }
   }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {	
    }
    
    @Override
    public void keyReleased(KeyEvent arg0) {	
    }	
    
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }
 
    @Override
    public void mouseEntered(MouseEvent e) {
    }
 
    @Override
    public void mouseExited(MouseEvent e) {
    }   
}
