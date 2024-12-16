package doerfer.main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.Color;

import doerfer.card.Gamecard;
import doerfer.configuration.Config;
import doerfer.configuration.Settings;
import doerfer.music.Music;
import doerfer.player.HUMAN;
import doerfer.player.RANDOM_AI;
import doerfer.preset.Player;
import doerfer.preset.Tile;
import doerfer.preset.TileGenerator;
import doerfer.preset.TilePlacement;
import doerfer.screen.Display;
/**
 * Diese Klasse steuert den Spielablauf und das Display.
 * Hier werden die Spieler initalisiert und das Display gestartet.
 */
public class Game {
    /** Das Display */
    private Display display;
    /** Liste mit Referenzen auf die Spieler */
    private List<Player> listplayer = new ArrayList<Player>();
    /** Liste mit den offenen Karten */
    private LinkedList<Tile> uncoveredTiles = new LinkedList<Tile>();
    /** Gameconfiguration */
    private Config config;
    /** Presettings */
    private Settings setting;
    /** Kartenzähler */
    private int cardcount;
    /** Zeiger auf den aktuellen Spieler */
    private int currentplayer;
    /** aktuelle TilePlacement */
    private TilePlacement placement;
    /** Skipzähler */
    private int skipcounter = 0;
    /** ist der Player skipped */
    private boolean playerskipped = false;
    /** ist der Spiel vorbei */
    private boolean isgameover = false;
    /** Der Tilegenerator */
    private TileGenerator tg;
    /** Liste der Seeds */
    private List<Long> seeds = new ArrayList<Long>();
    /** Liste der Scores */
    private List<Integer> scores = new ArrayList<Integer>();

    /**
     * Konstruktor zum erstellen des Spiels.
     * Das Spiel wird noch nicht initalisiert, weil noch Einstellungen vorher
     * gemacht werden können.
     */
    public Game() {}

    /**
     * Thread kann aufgeweckt werden, weil die Einstellungen abgeschlossen wurden.
     * Das Spiel kann nun initalisert werden.
     */
    public synchronized void setGameVisible() {
        notify();
    }

    /**
     * Methode zum initalisieren des Spiels. Die Initalisierung wird sofort
     * unterbrochen, weil noch Einstellungen gemacht werden können.
     */
    public synchronized void initGame() {
        try {
            wait();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
            System.exit(0);
        }
        // Configuration wird geladen
            config = new Config();
        // Einstellungen werden geladen
        setting = new Settings();
        // Tilegeneartor wird mit Hilfe der Config erzeugt
        tg = new TileGenerator(config);
        // Der Cardcount wird initalisiert
        cardcount = config.getNumTiles();
        // Das graphischen Oberfläche für den HUMAN wird erzeugt
        try {
            display = new Display();
        } catch (Exception e) {
            // TODO System.out.println("Display konnte nicht erzeugt werden.");
            System.exit(-1);
        }
        // Der Spielablauf beginnt
        try {
            startGame();
        } catch (Exception e) {
            // Zeigt die gefangenen Exceptions auf dem Display an
            display.displayError(e);
        }
    }

    /**
     * Methode zum Steuern des Spielablaufs.
     * 
     * @throws Exception Diese Exceptions werden auf dem Display ausgegeben.
     */
    private void startGame() throws Exception {
        // --- Musik & Soundeffekte ---
        Music.playMainMusic(setting.isSound());
        // Lädt die Spielernamen aus den Settings
        for (int i = 0; i < config.getNumPlayers(); i++) {
            if (setting.getIsHUMAN()[i])
                listplayer.add(new HUMAN(setting.getPlayerNames()[i], display));
            else
                listplayer.add(new RANDOM_AI(setting.getPlayerNames()[i],setting));
        }
        // Initalisierung der Farben der Spieler
        List<Color> playerColors = new ArrayList<Color>();
        playerColors.add(Color.BLUE);
        playerColors.add(Color.RED);
        playerColors.add(Color.GREEN);
        playerColors.add(Color.PINK);
        // Spieler werden initalisiert
        initplayers();
        // Die ersten Karten werden aufgedeckt
        inituncoveredTiles();
        // Dem Display werden die Spieler mit ihren Farben übergeben
        display.setPlayers(listplayer, playerColors);

        // Spiel beginnt
        while (!isgameover) {
            // Anzahl der Karten wird auf dem Bildschirm angezeigt
            display.setTilesLeft(cardcount);
            display.setUncoveredTiles(uncoveredTiles);
            // Der aktuelle Spieler wird bestimmt und gesetzt auf dem Display
            setcurrentplayer(); // interne Berechnung
            display.setActivePlayer(listplayer.get(currentplayer));
            // Spieler macht seinen Zug
            playerSetTile();
            // Neu Karte wird erzeugt, wenn nötig
            if (skipcounter == 0 && cardcount > config.getNumPlayers())
                generateNewCard();
            // Überprüfung ob das Spiel vorbei ist
            if (skipcounter == config.getNumPlayers() || cardcount == 0)
                isgameover = true;
            // Spielerzähler wird aktualisiert
            currentplayer++;
        }
        //Spielüberprüfung und Endscreen
        if(isgameover)
        {
            for(int i = 0; i < config.getNumPlayers(); i++)
            {
                seeds.add(listplayer.get(i).requestRandomNumberSeed());
                scores.add(listplayer.get(i).getScore());
            }
            for(int i = 0; i < config.getNumPlayers(); i++)
            {
                listplayer.get(i).verifyGame(seeds, scores);
            }
            display.setTilesLeft(cardcount);
            display.setUncoveredTiles(uncoveredTiles);
            TimeUnit.SECONDS.sleep(1);
            display.setGameOver(isgameover);
        }
        
        // Endbildschirm wird angezeigt
        
    }

    /**
     * Zieht die ersten Karten der Spieler. (Spieleranzahl + 1)
     * 
     * @throws Exception Die Methoden der Spieler wurden in falscher Reihenfolge
     *                   aufgerufen.
     */
    private void inituncoveredTiles() throws Exception {
        // Spieleranzahl + 1 Karten werden aufgedeckt
        for (int i = 0; i < config.getNumPlayers() + 1; i++) {
            long rng = 0;
            // Holt von jedem Spieler die Randomnumber
            for (int j = 0; j < config.getNumPlayers(); j++) {
                // XOR Randomnumber
                rng = rng ^ listplayer.get(j).requestNextRandomNumber();
            }
            // erzeugt das Tile
            TileGenerator tg = new TileGenerator(config);
            Gamecard card = new Gamecard(tg.generateTile(rng));
            uncoveredTiles.add(card);
            // teilt jedem Spieler das Tile mit
            for (int j = 0; j < config.getNumPlayers(); j++) {
                listplayer.get(j).notifyNewUncoveredTile(card);
            }
        }
    }

    /**
     * Initalisiert die Spieler und übergibt ihnen die Config und ihre SpielerID
     * @throws Exception Die Initalisierung durfte nicht aufgerufen werden
     */
    private void initplayers() throws Exception {
        for (int i = 0; i < config.getNumPlayers(); i++) {
            listplayer.get(i).init(config, i + 1);
        }
    }

    /**
     * Der aktuelle Spieler wird bestimmt.
     * Die Range besteht aus Spieleranzahl - 1
     */
    private void setcurrentplayer() {
        if (currentplayer <= config.getNumPlayers()) {
            currentplayer = currentplayer % config.getNumPlayers();
        }
    }

    /** 
     * Fragt die Spieler nach einem Tileplacement.
     * @throws Exception Die Methoden der Spieler wurden nicht in der richtigen Reihenfolge aufgerufen.
     */
    private void playerSetTile() throws Exception {
        // Abrage nach möglichen möglichen Tileplacements
        placement = listplayer.get(currentplayer).requestTilePlacement();
        // Abfrage ob der Spieler einen Zug machen kann
        if (placement == null) {
            playerskipped = true;
            skipcounter++;
        } else {
            cardcount--;
            skipcounter = 0;
            playerskipped = false;
        }
        // Playerskipped wird wenn nötig auf dem Display angezeigt
        display.setPlayerSkipped(listplayer.get(currentplayer), playerskipped);
        // Wenn der Spieler einen Zug machen konnte wird er auf dem Display angezeigt
        if (!playerskipped) {
            display.placeTile(uncoveredTiles.get(0), placement, listplayer.get(currentplayer));
            display.focus(placement);
            uncoveredTiles.poll();
        }
        // die anderen Spieler werden über den Zug informiert, egal ob er konnte oder
        // nicht
        for (int i = 0; i < config.getNumPlayers(); i++) {
            if (i != currentplayer)
                listplayer.get(i).notifyTilePlacement(placement);
        }
    }

    /** Neue Karte wird erzeugt und den Spielern mitgeteilt
     * @throws Exception Methode NextRandomNumber oder NotifyNewUncoveredTile wurden falsch aufgerufen
     */
    private void generateNewCard() throws Exception {
        long rng = 0;
        for (int j = 0; j < config.getNumPlayers(); j++) {
            rng = rng ^ listplayer.get(j).requestNextRandomNumber();
        }
        Gamecard card = new Gamecard(tg.generateTile(rng));
        uncoveredTiles.add(card);
        // Die neue Karte wird allen zur Verfügung gestellt
        for (int j = 0; j < config.getNumPlayers(); j++) {
            listplayer.get(j).notifyNewUncoveredTile(card);
        }

    }
}
