package kalamburyapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Chat extends JPanel{
    private Player player;
    private BufferedReader reader;
    private PrintWriter writer;
        
    public Player getPlayer() {
        return player;
    }
        
    private JTextArea textArea;
    public JTextArea getTextArea() {
        return textArea;
    }    
        
    private JTextField input;
    public JTextField getInput() {
        return input;
    }
        
    private SocketClient socketClient;
    public void setServer(SocketClient socketServer) {
        this.socketClient = socketServer;
    }
	
    public Chat(Player player) {
    	this.player = player;   	
    	initializeChat();    
    }
    
    // Funkcja ustawia kursor chatu na sam dół
    public void scrollToBottom() {
        getTextArea().setCaretPosition(getTextArea().getDocument().getLength());
    }
    
    // Funkcja wysyła wiadomość przez strumień
    public void sendMessage(String message, boolean playerMsg) {               
        if (playerMsg) {
            textArea.append(player.getNick() + ": " + message + "\n");
            input.setText("");
            scrollToBottom();
        }
        else {
            textArea.append(message + "\n");
        }           
                
        try {
            Cargo cargo = new Cargo(message, player.getNick());
            socketClient.objOutput.writeObject(cargo);
            socketClient.objOutput.flush();
        } catch(Exception e) {
            //
        }
    } 
    
    // Funkcja inicjalizuje komponenty chatu
    public void initializeChat() {
        setVisible(true);
        this.setBackground(Color.WHITE);
    	setLayout(new FlowLayout(FlowLayout.CENTER));
    	setPreferredSize(new Dimension(1024, 150));
    	
    	// TEXT AREA (CHAT)
    	textArea = new JTextArea();
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(1000, 115));
        add(scrollPane);                        
        Border scrollPaneBorder = BorderFactory.createLineBorder(Color.WHITE);
        textArea.setBorder(BorderFactory.createCompoundBorder(scrollPaneBorder, 
                    		BorderFactory.createEmptyBorder(5, 5, 10, 10)));
        textArea.setFont(new Font("Calibri", Font.BOLD, 13));
        
        // INPUT AREA
        input = new JTextField();
        input.setPreferredSize(new Dimension(904, 25));
        add(input);
       
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        input.setBorder(BorderFactory.createCompoundBorder(border, 
                    		BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        
        // SEND BUTTON
        JButton sendButton = new JButton(new ImageIcon("images/send.png"));
        sendButton.setPreferredSize(new Dimension(90, 25));
        sendButton.setFocusable(false);
        sendButton.setBorder(null);
        sendButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isRollover()) {
                    sendButton.setIcon(new ImageIcon("images/sendOn.png"));
                } else {
                    sendButton.setIcon(new ImageIcon("images/send.png"));
                }
            }
        });
        add(sendButton); 
        
        input.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent arg0) {	
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
		{
                    if(!input.getText().isEmpty())
                    {
                        sendMessage(input.getText(), true);
                    } 
		}
            }
			
            @Override
            public void keyReleased(KeyEvent arg0) {				
            }

            @Override
            public void keyTyped(KeyEvent arg0) {								
            }      	
        });
        
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	if(!input.getText().isEmpty())
            	{
                    sendMessage(input.getText(), true);
            	}             
            }
          });       
    }
}
