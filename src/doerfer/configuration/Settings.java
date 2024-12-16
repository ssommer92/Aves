package doerfer.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



/**
 * Diese Klasse lädt die Settings
 * @author Joshua H.
 */
public class Settings {
    /** Finaler Name an dem die Settings gespeichert werden */
    private final String fileName = "resources/Settings.txt";
    /** Die Zeilen aus dem Settings File */
    private List<String> lines = new ArrayList<String>();
    /** Die x-Achse */
    private int x;
    /** Die y-Achse */
    private int y;
    /** Sound on/off */
    private boolean isSound;
    /** Spielernamen in einem Stringarray */
    private String[] playerNames = new String[4];
    /** Boolean ob der Spieler menschlich ist */
    private boolean[] isHUMAN = new boolean[4];
    /** um auf die Config zuzugreifen */
    private Config c;
    /** delay in Sekunden */
    private int delay;
    
    /**
     * Defaut Konstruktor um die Einstellungen einzulesen.
     */
    public Settings() {
        //einlesen aus dem File
        try {
            lines = Files.readAllLines(Paths.get(fileName)); //lesen des Settings Files
        } catch (IOException e) {
            System.out.println("Einstellungen konnten nicht geladen werden.");
            System.exit(0);
        }

        c = new Config();
        readX();
        readY();
        readSound();
        readPlayerNames();
        readIsHUMAN();
        readDelay();
    }

    /**
     * Einlesen des Delays der Random AI
     */
    private void readDelay(){
        delay = Integer.parseInt(lines.get(3));
    }
    /**
     * Einlesen der ob der Spieler menschlich ist
     */
    private void readIsHUMAN(){
        for(int i = 0 ; i < c.getNumPlayers(); i++)
        {
            isHUMAN[i] = Boolean.parseBoolean(lines.get(c.getNumPlayers()+4+i));
        }
    }

    /**
     * Gibt ein Array mit dein Einstellungen ob der Spieler menschlich ist
     * @return Array mit dein Einstellungen
     */
    public boolean[] getIsHUMAN(){
        return isHUMAN;
    }
    /**
     * Lesen der Spielernamen
     */
    private void readPlayerNames()
    {
        for(int i = 0 ; i < c.getNumPlayers(); i++)
        {
            playerNames[i] = lines.get(4+i);
        }
    }

    /**
     * Gibt Spielernamen zurück
     * @return Spielernamen
     */
    public String[] getPlayerNames()
    {
        return playerNames;
    }

    /**
     * Lesen der Breite des Spielframes
     */
    private void readX() {
        x = Integer.parseInt(lines.get(0));
    }

    /**
     * Lesen der Höhe des Spielframes
     */
    private void readY() {
        y = Integer.parseInt(lines.get(1));
    }

    /**
     * Lesen ob der Sound eingeschaltet ist
     */
    private void readSound() {
        isSound = Boolean.parseBoolean(lines.get(2));
    }

    /**
     * Gibt die Breite des Frames zurück
     * @return Breite des Spielframes
     */
    public int getX() {
        return x;
    }

    /**
     * Gibt die Höhe des Frames zurück
     * @return Höhe des Spielframes
     */
    public int getY() {
        return y;
    }

    /**
     * Gibt die Soundeinstellungen zurück
     * @return Soundeinstellung
     */
    public boolean isSound() {
        return isSound;
    }

    /**
     * Gibt den Delay der Random AI zurück
     * @return delay in Sekunden
     */
    public int getDelay()
    {
        return delay;
    }
}
