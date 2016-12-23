package kalamburyapp;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

public class Cargo implements Serializable{
    public int dataType;
    
    public String message;   
    public String nick;
    public Cargo(String message, String nick) {
        this.dataType = 1;
        this.message = message;
        this.nick = nick;
    }
      
    public Point point;
    public int arrayNumber;
    public Color color;
    public Cargo(Point point, int arrayNumber, Color color) {
        this.dataType = 2;
        this.point = point;
        this.arrayNumber = arrayNumber;
        this.color = color;
    }
    
    public Tour tour;
    public Cargo(Tour tour) {
        this.dataType = 3;
        this.tour = tour;
    }
}
