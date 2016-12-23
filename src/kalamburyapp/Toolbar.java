
package kalamburyapp;

import java.awt.Cursor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Toolbar extends JPanel implements MouseListener, MouseMotionListener {
    public JLabel word;
    
    public Toolbar(PaintArea paintArea) {
        this.paintArea = paintArea;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 25));
        addMouseListener(this);
        addMouseMotionListener(this);
        colors = new ArrayList<Rect>();
        word = new JLabel();
        word.setForeground(Color.BLACK);
        word.setFont(new Font("Calibri", Font.PLAIN, 15));
        add(word);
        setFocusable(true);
    }
    
    ArrayList<Rect> colors;
    PaintArea paintArea;

    @Override
    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (int i = 0; i < colors.size(); i++) {
            if ((e.getX() >= colors.get(0).rectangle.x) && (e.getY() >= colors.get(i).rectangle.y)
                 && (e.getX() <= colors.size() * (25 + 3) - 3) && (e.getY() <= colors.get(i).rectangle.y + 25)) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));                
            }
            else
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private class Rect {
        Rectangle rectangle;
        Color color;
        public Rect(Rectangle rectangle, Color color) {
            this.rectangle = rectangle;
            this.color = color;
        }
    }
    
    public void createRectangle(int x1, int y1, int x2, int y2, Color color) {
        Rectangle rectangle = new Rectangle(x1, y1, x2, y2);
        Rect rect = new Rect(rectangle, color);
        colors.add(rect);
    }
    
    public void drawColor(Graphics g, Color color, int x1, int y1, int x2, int y2) {
        g.setColor(color);
        g.fillRect(x1, y1, x2, y2);
        createRectangle(x1, y1, x2, y2, color);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 16; i++) {
            switch (i) {
                case 0:
                    drawColor(g, Color.WHITE, i * 25 + i * 3, 0, 25, 25);
                    break;
                case 1:
                    drawColor(g, Color.BLACK, i * 25 + i * 3, 0, 25, 25);
                    break;
                case 2:
                    drawColor(g, new Color(204, 0, 0), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 3:
                    drawColor(g, new Color(255, 0, 0), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 4:
                    drawColor(g, new Color(153, 76, 0), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 5:
                    drawColor(g, new Color(255, 128, 0), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 6:
                    drawColor(g, new Color(255, 255, 0), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 7:
                    drawColor(g, new Color(0, 204, 0), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 8:
                    drawColor(g, new Color(102, 204, 0), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 9:
                    drawColor(g, new Color(0, 255, 128), i * 25 + i * 3, 0, 25, 25);
                    break;  
                case 10:
                    drawColor(g, new Color(0, 0, 255), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 11:
                    drawColor(g, new Color(0, 128, 255), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 12:
                    drawColor(g, new Color(0, 255, 255), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 13:
                    drawColor(g, new Color(127, 0, 255), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 14:
                    drawColor(g, new Color(255, 0, 255), i * 25 + i * 3, 0, 25, 25);
                    break;
                case 15:
                    drawColor(g, new Color(255, 0, 127), i * 25 + i * 3, 0, 25, 25);
                    break;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < colors.size(); i++) {
            if ((e.getX() >= colors.get(i).rectangle.x) && (e.getY() >= colors.get(i).rectangle.y)
                 && (e.getX() <= colors.get(i).rectangle.x + 25) && (e.getY() <= colors.get(i).rectangle.y + 25)) {
                System.out.println("Clicked: " + i);
                paintArea.toolColor = colors.get(i).color;
                
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
       
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
