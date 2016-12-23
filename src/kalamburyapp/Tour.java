package kalamburyapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

public class Tour implements Serializable{
    public int playerId;
    public String word;
    
    public Tour(int playerId, String word) {
        this.playerId = playerId;
        this.word = word;
    }
}
