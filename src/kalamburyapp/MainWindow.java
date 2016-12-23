package kalamburyapp;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class MainWindow extends JFrame{
    private Chat chat;
    private PaintArea paintArea;
    private Menu menu;
    
    public MainWindow() throws UnknownHostException{
        super("Kalambury");      
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 830);
        setLayout(new FlowLayout(FlowLayout.CENTER));
                                                       
        menu = new Menu(this);
        add(menu); 
    }
   
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            try {   
                new MainWindow();
            } catch (UnknownHostException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });       
    }
}
