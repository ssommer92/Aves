package doerfer.screen;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import com.kitfox.svg.SVGElementException;

import doerfer.board.HexCord;
import doerfer.board.Spot;
import doerfer.card.Gamecard;
import doerfer.configuration.Config;
import doerfer.configuration.Settings;
import doerfer.listeners.KeyListenerImpl;
import doerfer.listeners.MouseListenerImpl;
import doerfer.listeners.MouseWheelListenerImpl;
import doerfer.preset.Biome;
import doerfer.preset.GameView;
import doerfer.preset.Player;
import doerfer.preset.Tile;
import doerfer.preset.TilePlacement;
import doerfer.preset.graphics.GGroup;
import doerfer.preset.graphics.GPanel;
import doerfer.screen.stylesCSS.GElementInfoBTN;
import doerfer.screen.stylesCSS.GTextImpl;
import doerfer.screen.stylesCSS.GTextPlayer;
import doerfer.screen.stylesCSS.GTextPlayerScore;
import doerfer.screen.stylesCSS.MessageBG;
import doerfer.screen.stylesCSS.TimerCustom;

/**
 * Klasse für die grafische Oberfläche
 * 
 * @author Lennart S.
 */
public class Display implements GameView {

    /** Config erzeugen um dieser Daten entnehmen zu können */
    Config config = new Config();
    /** Einstellungen für bspw. Musik (An/Aus) */
    Settings settings = new Settings();
    /** Zugriff auf das Hauptfenster (Top-Level-Window der GUI-Anwendung) */
    private JFrame frame;
    /** Breite des Containers */
    private float VP_W = GPanel.VIEWPORT_WIDTH * 0.975f;
    /** Höhe des Containers */
    private float VP_H = GPanel.VIEWPORT_HEIGHT;
    /** Zugriff auf Container um GUI-Komponenten hinzufügen zu können */
    private GPanel panel;
    /** Keylistener für Eingabe über die Tastatur */
    private KeyListenerImpl keylistener;

    /** Aktuell aktiver Spieler (Name, Score, etc.) */
    private Player activePlayer;
    /** Nummer des aktiven Spielers */
    private int activePlayerNumber;
    /** Aktuelle Platzierung der Karte */
    private TilePlacement place;
    /** Zahl der noch möglichen plazierbaren Karten (unten rechts) */
    private GTextImpl numberTilesLeft;
    /** Clone vom aktuellen DT für Anzeige auf LayerMain */
    private DrawTile dtMAIN;

    /** Liste von Spielerfarben */
    private List<Color> playerColors;
    /** Liste der Spieler (Name, Score, etc.) */
    private List<Player> playerList = new ArrayList<Player>();
    /** Liste der Vorschau-Tiles */
    private List<DrawTile> drawableTiles = new ArrayList<DrawTile>();
    /** Liste der Spielernamen als Text für grafische Ausgabe */
    private List<GTextPlayer> playerNames;
    /** Liste der Punkte/Scores der einzelnen Spieler */
    private List<GTextPlayerScore> scores = new ArrayList<GTextPlayerScore>();
    /** Liste an GreyTiles die greyValidRotations hinzugefügt werden */
    private List<GreyTile> gTiles = new ArrayList<>();
    /** Mögliche setzbaren Stellen je nach Rotation (Logik) */
    private GGroup[] greyValidRotations = new GGroup[6]; // Setzbaren Stellen (Logik)
    /** MouseWheelListener für das Frame */
    private MouseWheelListenerImpl mouseWheelListener;

    /**
     * Aufruf grafischer Wiedergabe des Spiels
     * 
     * @throws SVGElementException   Wenn er das SVG Element nicht erstellt oder
     *                               geladen werden konnte
     * @throws FileNotFoundException Wenn eine Datei nicht gefunden wurde
     * @throws IOException           Wenn eine Datei nicht gelsen werden konnte
     */
    public Display() throws SVGElementException, FileNotFoundException, IOException {

        frame = new JFrame();
        frame.setTitle("Aves");
        frame.setSize(settings.getX(), settings.getY());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Mittig
        frame.setFocusable(true); // Aktion auf Frame anzeigbar

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                panel.updateScale();
                panel.repaint();
            }
        });

        // --- Panel einbinden ---
        panel = new GPanel();
        panel.setBgColor(Color.WHITE);
        frame.add(panel);

        // --- InfoBTN einbinden ---
        new GElementInfoBTN(frame, panel, new File("resources/info.svg"));

        // --- KeyListener einbinden ---
        keylistener = new KeyListenerImpl(this, panel); // Ansprechbar für Deaktivierung am Ende des Spiels
        frame.addKeyListener(keylistener);
        frame.setFocusTraversalKeysEnabled(false); // Tab-Taste aktivieren

        frame.setVisible(true);
        panel.repaint();
        panel.updateScale();
        panel.repaint();
    }

    //////////////////////////////////// TILES ////////////////////////////////////

    /**
     * Setzt alle möglichen gültigen Züge
     *
     * @param allValidPlaces Liste der validen Züge
     * @see requestTilePlacement
     */
    @Override
    public void setValidTilePlacements(Set<TilePlacement> allValidPlaces) {

        // Erstellt für jede mögliche Rotation eine GGroup mit GreySvgElementen für
        // valide Setzmöglichkeiten
        for (int i = 0; i < 6; i++) {
            greyValidRotations[i] = new GGroup(panel.getDiagram());
            panel.getLayerMain().addChild(greyValidRotations[i]);
        }

        // --- MouseWheelListener einbinden ---
        // Wird bewusst ansprechbar intitialisiert um diesen bei der Methode
        // removeValidTilePLacements von den validen GreySvgElementen wieder zu
        // entfernen
        mouseWheelListener = new MouseWheelListenerImpl(settings.isSound(),
                panel, dtMAIN, drawableTiles.get(0), greyValidRotations);
        frame.addMouseWheelListener(mouseWheelListener);

        // Durchlaufen von jeder einzelnen möglichen validen Stelle auf dem Spielbrett
        // und fügt dieser ein GreyTile grafisch hizu
        for (TilePlacement validPlace : allValidPlaces) {
            Spot gs = new Spot(new HexCord(validPlace.getRow(), validPlace.getColumn()));
            GreyTile gTile = new GreyTile(gs, panel);
            gTiles.add(gTile);
            MouseListenerImpl gListener = new MouseListenerImpl(
                    this, panel, gs, dtMAIN, mouseWheelListener, validPlace.getRotation());
            gTile.setMouseListener(gListener);
            greyValidRotations[validPlace.getRotation()].addChild(gTile);
        }
        // Hinzufügen der obersten Karte sichtbar zum Spielfeld
        panel.getLayerMain().addChild(dtMAIN);
        panel.repaint();
    }

    /**
     * Wird aufgerufen um die grauen Tiles vom Display zu entfernen
     * Hierbei werden die Liste der grauen Tiles durchiteriert und alle vom
     * Spielfeld entfernt, wonach anschließend die gesamte Liste gelöscht wird.
     * Dabei wird auch der MouseWheelListener entfernt, um diesen im Verlauf erneut
     * hinzuzufügen
     */
    public void removeValidTilePlacements() {
        // Löscht alle grauen Tiles von dem LayerMain
        for (GreyTile gTile : gTiles) {
            panel.getLayerMain().removeChild(gTile);
        }
        // Löscht die komplette Liste
        gTiles.clear();
        panel.repaint();
        for (int i = 0; i < 6; i++) {
            if (greyValidRotations[i] != null) { // Check ob initialisiert
                panel.getLayerMain().removeChild(greyValidRotations[i]);
                greyValidRotations[i] = null; // Sonst wird eventuell versucht greyValid nochmal zu entfernen
            }
        }
        // --- MouseWheelListener entfernen ---
        if (mouseWheelListener != null) { // Check ob dieser bereits initialisiert wurde
            frame.removeMouseWheelListener(mouseWheelListener);
            mouseWheelListener = null; // Fehlervorsorge
            panel.getLayerMain().removeChild(dtMAIN); // Wird ebenso durch MouseWheelListenerCheck abgedeckt
            dtMAIN = null; // Fehlervorsorge
        }
    }

    /**
     * Setzen von Tile auf dem Spielbrett.
     * Zuerst werden aus der übergebenen Stelle die Hexkorrdinaten gezogen und
     * daraus ein Spot gebildet. Anschließend wird daraus ein DrawTile erstellt, an
     * die aktuelle Rotation angepasst, Farbe des aktuellen Spielers gegeben und
     * schließlich dem Spielbrett grafisch hinzugefügt
     * 
     * @param tile  Das jeweilige Tile
     * @param place Ort wo das Tile plaziert wurde
     * @param owner Wer das Tile gesetzt hat, oder "null", wenn das Tile von
     *              niemandem plaziert wurde
     */
    @Override
    public void placeTile(Tile tile, TilePlacement place, Player owner) {

        HexCord cord = new HexCord(place.getRow(), place.getColumn());
        Spot p = new Spot(cord);
        DrawTile dt = new DrawTile(panel, tile, p);
        dt.rotate(place.getRotation() * 60);
        if (activePlayer != null) { // Farbe geben beim Setzen
            dt.setStroke(playerColors.get(activePlayerNumber));
        }
        panel.getLayerMain().addChild(dt); // setzen
        removeValidTilePlacements();
        panel.repaint();
    }

    /**
     * Aktualisiert die Liste der derzeit nicht aufgedeckten Tiles aus dem Tilestack
     * Hierbei wird überprüft ob die Liste, die in "showGraficTilesLeft" gefüllt
     * wird leer ist und anschließend gelöscht falls dies nicht der fall ist. Diese
     * Liste dient zum Anzeigen der Karten rechts unten
     * 
     * @param tiles Liste der derzeit noch nicht aufgedeckten Tiles
     */
    @Override
    public void setUncoveredTiles(List<Tile> tiles) {

        if (drawableTiles != null) {
            for (DrawTile dt : drawableTiles) {
                panel.getLayerHUD().removeChild(dt);
            }
            drawableTiles.clear();
        }
        if (tiles.size() != 0) {
            showGraficTilesLeft(tiles); // Vorschau welche Tiles als nächstes gesetzt werden können
        }
    };

    /**
     * Grafische Anzeige TilesLeft. Zeigt im HUD welche Tiles als nächstes gesetzt
     * werden können. Aus den Tiles werden die einzelnen Biome gezogen, anschließend
     * daraus eine Gamecard eerstellt aus der wiederum ein DrawTile erstellt wird,
     * welche zum Feld hinzugefügt werden. Von der ersten Karte wird ein Klon
     * erstellt, der erst auf den GreyTiles erscheint
     * 
     * @param uncoveredTiles eine Liste der bereits aufgedeckten Karten
     */
    public void showGraficTilesLeft(List<Tile> uncoveredTiles) {

        int hight = uncoveredTiles.size() - 1;
        for (int i = 0; i < uncoveredTiles.size(); i++) {
            List<Biome> biomelist = new ArrayList<Biome>();
            for (int j = 0; j < 6; j++) {
                biomelist.add(uncoveredTiles.get(i).getEdge(j));
            }
            Gamecard card = new Gamecard(biomelist);
            Spot p = new Spot(VP_W * 0.95f, VP_H * 0.82f - ((hight * 74)));
            DrawTile dt = new DrawTile(panel, card, p);
            dt.transform().scale(1f, 0.8f);
            drawableTiles.add(dt);
            panel.getLayerHUD().addChild(dt);
            // Erzeuge vom ersten Tile der Liste einen Klon & erscheint erst auf GreyTile
            if (i == 0) {
                dtMAIN = new DrawTile(panel, card, p);
                dtMAIN.setOpacity(0f);
            }
            hight--;
        }
        panel.repaint();
    }

    /**
     * Anzahl verbleibender Tiles
     *
     * @param n Anzahl der Tiles die noch platziert werden können
     */
    @Override
    public void setTilesLeft(int n) {

        if (numberTilesLeft != null) {
            panel.getLayerHUD().removeChild(numberTilesLeft);
        }
        numberTilesLeft = new GTextImpl(panel, VP_W * 0.951f, VP_H, String.valueOf(n));
        panel.repaint();
    };

    /**
     * Fokussiert das Ansichtsfenster auf ein bestimmtes Tile, um auch Tiles die
     * außerhalb des aktuellen Bildschirms platziert wurden zu sehen
     *
     * @param location Ort worauf fokussiert werden soll
     */
    @Override
    public void focus(TilePlacement location) {

        HexCord cord = new HexCord(location.getRow(), location.getColumn());
        Spot activeLocation = new Spot(cord);
        float activePosX = activeLocation.getX();
        float activePosY = activeLocation.getY();
        activePosX = 780 - activePosX; // Layout Mitte X-Kord
        activePosY = 450 - activePosY; // Layout Mitte Y-Kord
        keylistener.setPositionAfterSetTile(activePosX, activePosY); // Setzt neuen Ursprungspunkt
    };

    ////////////////////////////////// PLAYER //////////////////////////////////

    /**
     * Zeigt grafisch wieviele Spieler das Spiel spielen
     *
     * @param players Spieler sortiert nach playerid
     * @param colors  Farben der aktuellen Spieler
     */
    @Override
    public void setPlayers(List<Player> players, List<Color> colors) {

        // Liste mit allen Spielern
        playerList = players; // ((getName, getScore()))
        playerColors = colors;
        playerNames = new ArrayList<GTextPlayer>();
        // Füllen der Liste mit allen Spielen + zugehörige Farbe
        for (int i = 0; i < players.size(); i++) {
            try {
                playerNames.add(new GTextPlayer(panel, players.get(i).getName(), colors.get(i), i));
            } catch (Exception exp) {
                displayError(exp);
            }
        }

        // --- Setzen von Starttile ---
        for (int i = 0; i < config.getPreplacedTiles().size(); i++) {
            // Menge an Keys erstellen und durchiterieren
            Tile tile = config.getPreplacedTiles().get(i);
            TilePlacement cord = config.getPreplacedTilesPlacements().get(i);
            int playerID = config.getPreplacedTilesPlayerIDs().get(i);
            if (playerID == 0)
                placeTile(tile, cord, null);
            else {
                Player owner = playerList.get(playerID - 1);
                placeTile(tile, cord, owner);
            }
        }
        panel.repaint();
    };

    /**
     * Zeigt grafisch welcher Spieler gerade am Zug ist
     * Hierbei werden diese zu Beginn des Spiels einmalig erzeugt und im weiteren
     * Verlauf alle bis auf den aktuellen Spieler grafisch angezeigt
     * 
     * @param activePlayer Aktueller Spieler
     */
    @Override
    public void setActivePlayer(Player activePlayer) {

        // Alle aktuell angezeigten Spieler entfernen
        this.activePlayer = activePlayer;
        for (int i = 0; i < playerNames.size(); i++) {
            panel.getLayerHUD().removeChild(playerNames.get(i));
        }
        try {
            for (int i = 0; i < playerList.size(); i++) {
                if (playerList.get(i).getName() == activePlayer.getName()) {
                    panel.getLayerHUD().addChild(playerNames.get(i));
                    activePlayerNumber = i;
                }
            }
        } catch (Exception exp) {
            displayError(exp);
        }
        panel.repaint();
    };

    /**
     * Methode zum Anzeigen des aktuellen PlayerScores
     *
     */
    public void showPlayerScore() {

        try {
            for (int i = 0; i < playerList.size(); i++) {
                float posX = GPanel.VIEWPORT_WIDTH / 2 * 1.04f;
                float posY = GPanel.VIEWPORT_HEIGHT / 2f - i * 100;
                // PlayerScore bei Drücken von TAB
                String playerAndScore = playerList.get(i).getName().toString() + ": " + playerList.get(i).getScore()
                        + " Punkte";
                GTextPlayerScore playerScore = new GTextPlayerScore(panel, playerAndScore, posX, posY);
                scores.add(i, playerScore);
                panel.repaint();
            }
        } catch (Exception exp) {
            displayError(exp);
        }
    }

    /**
     * Entfernen von Anzeige des Playerscores durch Loslassen der Taste "TAB"
     * 
     * @param blackBG Hintergrund
     */
    public void removePlayerScore(MessageBG blackBG) {

        for (int i = 0; i < scores.size(); i++) {
            panel.getLayerHUD().removeChild(scores.get(i));
            panel.getLayerHUD().removeChild(blackBG);
            panel.repaint();
        }
    }

    /**
     * Zeigt temporär grafisch mittig im Bild wenn ein Spieler übersprungen wurde
     *
     * @param player  Aktueller Spieler
     * @param skipped Gibt an ob ein Spieler übersprungen wurde
     */
    @Override
    public void setPlayerSkipped(Player player, boolean skipped) {

        try {
            if (skipped) {
                String playerSkipped = "Spieler " + player.getName() + " setzt aus";
                GTextImpl playerSkippedText = new GTextImpl(panel, playerSkipped, 60f, Color.BLACK);
                panel.getLayerHUD().addChild(playerSkippedText);
                panel.repaint();

                // Meldung das Spieler X übersprungen wurde wird nach 2,5 Sekunden entfernt
                new TimerCustom(panel, playerSkippedText, 2500);
            }
        } catch (Exception exp) {
            displayError(exp);
        }
    };

    /**
     * Wartet auf Setzten des Spielers eines Tiles auf eine gültige Position
     * "requestTilePlacement" und "notifyTilePlacement" sind hierbei von sich
     * abhängig
     *
     * @return Aktuell gewähltes Tile
     * @see setValidTilePlacements
     */
    @Override
    public synchronized TilePlacement requestTilePlacement() {

        try {
            wait(); // wartet auf Setzen (notify())
        } catch (InterruptedException exp) {
            displayError(exp);
        }
        return place;
    }

    /**
     * Benachrichtigung/Abspeicherung wenn ein Tile gesetzt wurde
     * 
     * @param place Ort an dem das Tile abgelegt wurde vom Player
     */
    public synchronized void notifyTilePlacement(TilePlacement place) {

        this.place = place; // abspeichern
        notify(); // Tile wurde gesetzt (durch MouseListener)
    }

    ////////////////////////// GRAFISCHE SCREENMESSAGES //////////////////////////

    /**
     * Zeigt grafisch dem/den Spieler/n einen GameOverBildschirm der/die verloren
     *
     * @param gameover Wird übergeben, wenn das Spiel vorbei ist
     */
    @Override
    public void setGameOver(boolean gameover) {

        if (gameover == true) {
            try {
                // Textnachricht GAME_OVER - siehe Klasse GTextImpl
                GTextImpl message = new GTextImpl(panel, "Spiel Beendet", 80f, Color.WHITE);
                new MessageBG(panel, frame, message, Color.BLACK);
                new TimerCustom(this, panel, message);
            } catch (Exception exp) {
                displayError(exp);
            }
            frame.removeKeyListener(keylistener);
        }
    };

    /**
     * Zeigt grafisch eine Fehlermeldung
     *
     * @param error Beeinhaltet den jeweiligen Fehler
     */
    @Override
    public void displayError(Exception error) {

        try {
            // Textnachricht Error - siehe Klasse GTextImpl
            String errorMessage = "Fehler: " + error.getMessage();
            GTextImpl message = new GTextImpl(panel, errorMessage, 30f, Color.WHITE);
            new MessageBG(panel, frame, new Color(180, 0, 0), message);

        } catch (Exception exp) {
            System.out.println("ERROR - Es ist folgender Fehler ist aufgetreten: " + error.getMessage() + ".");
        }
        frame.removeKeyListener(keylistener);
    }
}