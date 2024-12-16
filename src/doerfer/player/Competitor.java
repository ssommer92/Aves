package doerfer.player;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import doerfer.board.Board;
import doerfer.exception.IllegalCallPlayerException;
import doerfer.exception.IllegalGameException;
import doerfer.exception.IllegalPlacementException;
import doerfer.preset.GameConfiguration;
import doerfer.preset.Player;
import doerfer.preset.RNG;
import doerfer.preset.Tile;
import doerfer.preset.TilePlacement;

/**
 * Abstrakte Klasse für die verschiedenen Arten von Spielern.
 * An sich verhalten sich alle Spieler gleich, sie unterscheiden sich nur in der getTilePlacement() Methode
 * 
 * @author Joshua H.
 */
public abstract class Competitor implements Player {

    /**
     * Kontruktor der Player
     * 
     * @param name Name des Spielers
     */
    public Competitor(String name) {
        this.name = name;
        rng = new RNG();

    }

    /** Startstatus */
    private Status status = Status.INIT;
    /** PlayerID */
    protected int playerID;
    /** Name des Spielers */
    private final String name;
    /** Random Number Generator */
    private final RNG rng;
    /** Eigenes Board */
    protected Board ownBoard;
    /** offene Karten */
    Queue<Tile> openCards = new LinkedList<Tile>();
    /** Zähler um die Anzahl die am Anfang gezogenen Karten zu zählen */
    private int initialtiles;
    /** aktueller Spieler */
    private int currentplayerID;
    /** Anzahl er Spieler */
    private int numberplayer;
    /** Anzahl der bereits gelegten Karten */
    private int tilecount;
    /** Anzahl der Skips */
    private int skipcount;

    /**
     * Initalisierung des Players. Darf maximal einmal aufgerufen werden.
     * 
     */
    @Override
    public void init(GameConfiguration config, int playerID) throws IllegalCallPlayerException, IllegalPlacementException {
        // Überprüfung des Status
        if (status != Status.INIT) {
            throw new IllegalCallPlayerException("Player bereits intitalisiert.");
        }
        this.playerID = playerID;
        // Board wird initalisiert
        ownBoard = new Board(config);
        initialtiles = 0;
        currentplayerID = 1;
        numberplayer = config.getNumPlayers();
        tilecount = config.getNumTiles();
        updateStatus();
    }

    /**
     * Gibt den Namen zurück
     * 
     */
    @Override
    public String getName() throws IllegalCallPlayerException {
        if (status == Status.INIT) {
            throw new IllegalCallPlayerException("Player nicht intitalisiert.");
        }
        return name;
    }

    /**
     * Gibt den Score zurück
     * 
     */
    @Override
    public int getScore() throws IllegalCallPlayerException {
        if (status == Status.INIT) {
            throw new IllegalCallPlayerException("Player nicht intitalisiert.");
        }
        return ownBoard.getPlayerIdScore()[playerID];
    }

    /**
     * Gibt die nächste Random Number zurück
     * 
     */
    @Override
    public long requestNextRandomNumber() throws IllegalCallPlayerException {
        if (status != Status.REQUESTNUMBER) {
            throw new IllegalCallPlayerException("Methode requestNextRandomNumber falsch aufgerufen.");
        }
        updateStatus();
        return rng.next();
    }

    /**
     * Teilt dem Spieler mit, dass eine neue Karte aufgedeckt wurde
     * 
     */
    @Override
    public void notifyNewUncoveredTile(Tile tile) throws IllegalCallPlayerException {
        if (status != Status.NOTIFYTILE) {
            throw new IllegalCallPlayerException("Methode notifyNewUncoveredTile falsch aufgerufen.");
        }
        openCards.add(tile);
        updateStatus();
    }

    /**
     * Teilt dem Spieler mit, dass eine neue Karte gelegt wurde
     * @param place Placement des Tiles
     */
    @Override
    public void notifyTilePlacement(TilePlacement place) throws IllegalCallPlayerException, IllegalPlacementException {
        if (status != Status.NOTIFYPLACEMENT) {
            throw new IllegalCallPlayerException("Methode notifyTilePlacement falsch aufgerufen.");
        }
        if (place == null) {
            skipcount++;
        } else {
            // Spieler fügt die Karte seinem Board hinzu und nimmt die oberste Karte von dem
            // offenen Stapel
            ownBoard.addCard(place, openCards.poll(), currentplayerID);
            skipcount = 0;
            tilecount--;
        }
        updateStatus();
    }

    /**
     * Der Spieler gibt an, an welchem Platz er die Karte platzieren will. Wenn er
     * null zurück gibt kann er die Karte nicht setzen
     * @return Tileplacement
     */
    @Override
    public TilePlacement requestTilePlacement() throws IllegalCallPlayerException, IllegalPlacementException {
        if (status != Status.REQUESTPLACEMENT) {
            throw new IllegalCallPlayerException("Methode requestTilePlacement falsch aufgerufen.");
        }
        // Spieler schaut, wo er platzieren kann
        TilePlacement place = getTilePlacement();
        // Überprüfung ob geskipt wurde
        if (place == null) {
            skipcount++;
        } else {
            skipcount = 0;
            tilecount--;
            // Spieler fügt die Karte seinem Board hinzu und nimmt die oberste Karte von dem
            // offenen Stapel
            ownBoard.addCard(place, openCards.poll(), currentplayerID);
        }
        updateStatus();
        return place;
    }

    /**
     * Erfragt den Seed des Random Number Generators
     * @return Seed
     */
    @Override
    public long requestRandomNumberSeed() throws IllegalCallPlayerException {
        if (status != Status.REQUESTSEED) {
            throw new IllegalCallPlayerException("Methode requestRandomNumberSeed falsch aufgerufen.");
        }
        updateStatus();
        return rng.getSeed();
    }

    /**
     * Verifiziert das Spiel
     * Wenn das Spiel nicht verfiziert werden konnte, dann wird eine Exception
     * geworfen.
     * @param seeds Liste der Seeds der einzelnen Spieler
     * @param scores List der Scores der einzelnen Spieler
     */
    @Override
    public void verifyGame(List<Long> seeds, List<Integer> scores) throws IllegalCallPlayerException, IllegalGameException {
        if (status != Status.VERIFY) {
            throw new IllegalCallPlayerException("Methode verifyGame falsch aufgerufen.");
        }
        updateStatus();
        if (!ownBoard.verifyGame(seeds, scores))  
            throw new IllegalGameException("Spiel fehlerhaft verifiziert.");
            
    }

    /**
     * Update des Status. Dies wird benötigt um zu überprüfen, ob überhaupt die
     * Methoden aufgerufen werden dürfen.
     * Bei jedem Methodenaufruf wird der Status überprüft.
     */
    @SuppressWarnings("fallthrough")
    private void updateStatus() {
        switch (status) {
            case INIT:
                status = Status.REQUESTNUMBER;
                break;
            case REQUESTNUMBER:
                status = Status.NOTIFYTILE;
                break;
            case NOTIFYTILE:
                // do while Schleife die mit 0 startet (Spieleranzahl + 1)
                if (initialtiles < numberplayer) {
                    initialtiles++;
                    status = Status.REQUESTNUMBER;
                } else {
                    // Der Spieler ist als nächstes dran
                    if (currentplayerID == playerID) {
                        status = Status.REQUESTPLACEMENT;
                    }
                    // Ein anderer Spieler ist am Zug
                    else {
                        status = Status.NOTIFYPLACEMENT;
                    }
                }
                break;
            // Gleicher Ablauf
            case REQUESTPLACEMENT:
            case NOTIFYPLACEMENT:
                currentplayerID = (currentplayerID % numberplayer) + 1;
                // Abgrage ob Spiel zuende ist
                if (skipcount == numberplayer || tilecount == 0) {
                    status = Status.REQUESTSEED;
                } else {
                    // Überprüfung wer als nächstes am Zug ist
                    if (tilecount < numberplayer + 1  || skipcount != 0) {
                        if (currentplayerID == playerID) {
                            status = Status.REQUESTPLACEMENT;
                        } else {
                            status = Status.NOTIFYPLACEMENT;
                        }

                    } else {
                        status = Status.REQUESTNUMBER;
                    }
                }
                break;
            case REQUESTSEED:
                status = Status.VERIFY;
                break;
            // Endstatus inklusive Verify, weil dies auch nicht nochmal aufgerufen werden
            // darf
            default:
                status = Status.DEAD;
                break;
        }
    }

    /**
     * Gibt das den Platz zurück an dem die Karte gelegt wurde
     * @return Tileplacement der gelegten Karte
     */
    protected abstract TilePlacement getTilePlacement();

}