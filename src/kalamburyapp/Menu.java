package kalamburyapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Menu extends JPanel{   
    private GridBagConstraints grid;
    private JFrame mainWindow;
    
    public Menu(JFrame mainWindow) {  
        this.mainWindow = mainWindow;
        
        setBackground(Color.WHITE);
        setVisible(true);
        grid = new GridBagConstraints();       
    	setLayout(new GridBagLayout());         
        InitializeMenu();       
    }
    
    public void InitializeMenu() {  
        // EMPTY
        grid.gridx = 0;
        grid.gridy = 0;
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(10, 270));
        emptyPanel.setBackground(Color.WHITE);
        add(emptyPanel, grid);
        
        grid.gridx = 0;
        grid.gridy = 2;
        JLabel nickLabel = new JLabel("NICK:   ");
        add(nickLabel, grid);
        
        grid.gridx = 0;
        grid.gridy = 3;
        JLabel portLabel = new JLabel("PORT:   ");
        add(portLabel, grid);
        
        grid.gridx = 0;
        grid.gridy = 4;
        JLabel serverLabel = new JLabel("SERVER:   ");
        add(serverLabel, grid);
        
        // EMPTY
        grid.gridx = 1;
        grid.gridy = 2;
        JPanel emptyPanel1 = new JPanel();
        emptyPanel1.setPreferredSize(new Dimension(10, 10));
        emptyPanel1.setBackground(Color.WHITE);
        add(emptyPanel1, grid);
        
        // ***** HOST *****
        grid.gridx = 2;
        grid.gridy = 1;       
        JRadioButton hostButton = new JRadioButton("HOST");
        hostButton.setBackground(Color.WHITE);
        hostButton.setSelected(true);
        hostButton.setFocusable(false);
        add(hostButton, grid);
        
        grid.gridx = 2;
        grid.gridy = 2;       
        JTextField hostInput = new JTextField("Host");        
        hostInput.setPreferredSize(new Dimension(200, 25));       
        add(hostInput, grid);
               
        
        grid.gridx = 2;
        grid.gridy = 3;       
        JTextField hostPortInput = new JTextField("50000");
        hostPortInput.setPreferredSize(new Dimension(200, 25));        
        add(hostPortInput, grid);
                 
        // EMPTY
        grid.gridx = 3;
        grid.gridy = 2;
        JPanel emptyPanel2 = new JPanel();
        emptyPanel2.setPreferredSize(new Dimension(10, 10));
        emptyPanel2.setBackground(Color.WHITE);
        add(emptyPanel2, grid);
        
        // ***** CLIENT *****       
        grid.gridx = 4;
        grid.gridy = 1;       
        JRadioButton clientButton = new JRadioButton("CLIENT");
        clientButton.setBackground(Color.WHITE);
        clientButton.setSelected(false); 
        clientButton.setFocusable(false);
        add(clientButton, grid);
        
        grid.gridx = 4;
        grid.gridy = 2;
                
        JTextField clientInput = new JTextField("Client");
        clientInput.setPreferredSize(new Dimension(200, 25));        
        add(clientInput, grid);
        
        grid.gridx = 4;
        grid.gridy = 3;       
        JTextField clientPortInput = new JTextField("50000");
        clientPortInput.setPreferredSize(new Dimension(200, 25));        
        add(clientPortInput, grid);
        
        grid.gridx = 4;
        grid.gridy = 4;       
        JTextField serverInput = new JTextField("localhost");
        serverInput.setPreferredSize(new Dimension(200, 25));        
        add(serverInput, grid);

        // EMPTY
        grid.gridx = 4;
        grid.gridy = 5;
        JPanel emptyPanel3 = new JPanel();
        emptyPanel3.setPreferredSize(new Dimension(10, 10));
        emptyPanel3.setBackground(Color.WHITE);
        add(emptyPanel3, grid);
        
        // START BUTTON
        grid.gridx = 4;
        grid.gridy = 6;       
        JButton startButton = new JButton(new ImageIcon("images/start.png"));
        startButton.setBorder(null);
        startButton.setFocusable(false);
        startButton.setPreferredSize(new Dimension(90,25));
        
        startButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isRollover()) {
                    startButton.setIcon(new ImageIcon("images/startOn.png"));
                } else {
                    startButton.setIcon(new ImageIcon("images/start.png"));
                }
            }
        });
        
        add(startButton, grid);

        // ***** START BUTTON EVENT *****
        startButton.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
                if (hostButton.isSelected()) {                    
                    Player player = new Player(hostInput.getText());
                    Chat chat = new Chat(player);
                    PaintArea paintArea = new PaintArea();
                    
                    Toolbar toolbar = new Toolbar(paintArea);
                    mainWindow.add(toolbar);
                    
                    mainWindow.add(paintArea);
                    mainWindow.add(chat);
                    
                    setVisible(false);
                    chat.setVisible(true);
                    paintArea.setVisible(true);                    
                    mainWindow.revalidate();
                    
                    try {
                        SocketClient socketServer = new SocketClient(Integer.parseInt(hostPortInput.getText()), chat, paintArea, toolbar);
                    } catch (IOException ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if (clientButton.isSelected()) {
                    
                    Player player = new Player(clientInput.getText());
                    Chat chat = new Chat(player);
                    PaintArea paintArea = new PaintArea();

                    Toolbar toolbar = new Toolbar(paintArea);
                    mainWindow.add(toolbar);
                    
                    mainWindow.add(paintArea);
                    mainWindow.add(chat);
                    
                    setVisible(false);
                    chat.setVisible(true);
                    paintArea.setVisible(true);                  
                    mainWindow.revalidate();
                                      
                    try {
                        SocketClient socketClient = new SocketClient(serverInput.getText(), Integer.parseInt(clientPortInput.getText()), chat, paintArea, toolbar);  
                    } catch (IOException ex) {
                        System.err.println("Nie można nawiązać połączenia");
                    }                    
                }
            } 
        });
        
        // ***** RADIO BUTTON EVENTS *****       
        ButtonGroup group = new ButtonGroup();
        group.add(hostButton);
        group.add(clientButton);   
        
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });       
        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
 
    }
}
