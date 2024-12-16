package doerfer.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import doerfer.card.Gamecard;
import doerfer.exception.IllegalConfigFileException;
import doerfer.preset.Biome;
import doerfer.preset.GameConfiguration;
import doerfer.preset.Tile;
import doerfer.preset.TilePlacement;

/**
 * Diese Klasse lädt die Config und implementiert GameConfiguration
 * 
 * @author Joshua H.
 */
public class Config implements GameConfiguration {
    /** Pfad wo die Config liegt */
    final private String fileName = "resources/Config.txt";
    /** Erste Zeile in der Config muss genau so lauten */
    final private String firstline = "DoerferGameConfigurationv1";
    /** Alle Zeilen der Config */
    private List<String> lines;
    /** Anzahl der Karten auf dem Stapel */
    private int tilesnumber;
    /** Anzahl der Spieler */
    private int numberPlayer;
    /** Beschreibung des Spiels */
    private String description;
    /** Liste der IDs der Player der vorgelegten Tiles */
    private List<Integer> idList = new ArrayList<Integer>();
    /** Anzahl der vorgelegten Karten */
    private int numberCards;
    /** Liste der Biomchancen */
    private Map<Biome, Integer> biomeChances = new HashMap<Biome, Integer>();
    /** Liste der Biomgewichtungen */
    private Map<Biome, Integer> biomeWeights = new HashMap<Biome, Integer>();
    /** Liste der TilePlacements der vorgelegten Karten */
    private List<TilePlacement> tilePlacementList = new ArrayList<TilePlacement>();
    /** Liste der vorgelegten Karten */
    private List<Tile> preplacedTilesList = new ArrayList<Tile>();

    /**
     * Der Konstruktor lädt das Config File und speichert es ab.
     * Wenn die erste Zeile nicht exakt "DoerferGameConfigurationv1" lautet dann
     * beendet sich das Program.
     * Beendet das Programm, wenn die Configdatei nicht gelesesen werden konnte.
     */
    public Config() {
        // Ließt die einzelnen Zeilen ein und speicher sie in einem Array
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            //Exception wenn das File nicht gelesen werden konnte
            System.out.println(e.getMessage());
            System.exit(0);
        }
        //Überprüfung der ersten Zeile
        if (!(lines.get(0).equals(firstline))) {
            try {
                throw new IllegalConfigFileException("Configdatei ist fehlerhaft.");
            } catch (IllegalConfigFileException e) {
                //Beendet das Spiel, wenn die erste Zeile nicht genau der Vorgabe ist
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }
        try {
            readNumTiles();
            readNumberCards();
            readNumPlayers();
            readDescription();
            readPreplacedTilesPlayerIDs();
            readPreplacedTilesPlacements();
            readBiomeChances();
            readBiomeWeights();
            readPreplacedTiles();
        } catch (NumberFormatException | IllegalConfigFileException e) {
            //Beendet das Spiel, wenn eine der konvertierungen nicht möglich war
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }

    /**
     * Liest die Spielkartenanzahl aus
     * Muss eine Zahl sein ,die größer als die Spieleranzahl ist, ansonsten beendet
     * sich das Programm.
     * Beendet das Programm, wenn die Zahl nicht eingelesen werden konnte.
     * 
     * @throws IllegalConfigFileException Wenn die Kartenanzahl + 1 kleiner gleich der Spieleranzahl ist
     * @throws NumberFormatException Wenn die Kartenanzahl keine Zahl ist
     */
    private void readNumTiles() throws IllegalConfigFileException, NumberFormatException {
        int numberplayer = getNumPlayers();
        try {
            tilesnumber = Integer.parseInt(lines.get(3));
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Spielkartenanzahl ist fehlerhaft.");
        }
        if (tilesnumber <= numberplayer) {
            throw new IllegalConfigFileException("Spielkartenanzahl ist zu klein.");
        }
    }

    /**
     * Liest eine beliebige Beschreibung aus.
     */
    private void readDescription() {
        description = lines.get(1);
    }

    /**
     * Setzt die Spieleranzahl.
     * Die Spieleranzahl muss 1, 2, 3 oder 4 sein.
     * Beendet das Programm, wenn die Spieleranzahl fehlerhaft ist.
     * 
     * @throws IllegalConfigFileException Wenn die Spieleranzahl kleiner 1 oder größer 4 ist
     * @throws NumberFormatException Wenn die Spieleranzahl keine Zahl ist
     */
    private void readNumPlayers() throws IllegalConfigFileException, NumberFormatException {
        try {
            this.numberPlayer = Integer.parseInt(lines.get(2));
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Spieleranzahl fehlerhaft.");
        }
        if (numberPlayer < 1 || numberPlayer > 4) {
            throw new IllegalConfigFileException("Spieleranzahl muss zwischen 1 und 4 sein.");
        }
    }

    /**
     * Liest die SpielerIDs der vorgelegten Karten aus.
     * Beendet das Programm, wenn die IDs der gesetzen Karten fehlerhaft ist.
     * 
     * @throws NumberFormatException Wenn die IDs keine Zahlen sind
     * @throws IllegalConfigFileException Wenn die SpielerIDs außerhalb der Range sind
     */
    private void readPreplacedTilesPlayerIDs() throws NumberFormatException,IllegalConfigFileException {
        for (int i = 0; i < getNumberCards(); i++) {
            String[] line = lines.get(11 + i).split(" ");
            try {
                int playerID = Integer.parseInt(line[0]);
                if(playerID < 1 || playerID > numberPlayer)
                    throw new IllegalConfigFileException("Die IDs der gesetzten Karten ist fehlerhaft.");
                idList.add(i, playerID);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("Die IDs der gesetzten Karten ist fehlerhaft.");
            }
        }
    }

    /**
     * Liest die Anzahl der vorgelegten Karten aus.
     * Wenn die Anzahl fehlerhaft ist, dann wird das Programm beendet.
     * 
     * @throws NumberFormatException Wenn die Kartenanzahl keine Zahl ist
     */
    private void readNumberCards() throws NumberFormatException {
        try {
            numberCards = Integer.parseInt(lines.get(10));
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Vorgelegte Spielkartenanzahl ist fehlerhaft.");

        }
    }

    /**
     * Liest die Wahrscheinlichkeiten aus, die die einzelnen Biome haben.
     * Wenn die Wahrscheinlichkeiten fehlerhaft sind, dann wird das Programm
     * beendet.
     * 
     * @throws IllegalConfigFileException Wenn die Biome nicht richtig benannt sind
     * @throws IllegalArgumentException Wenn die Wahrscheinlichkeit außerhalb der Range liegt
     */
    private void readBiomeChances() throws IllegalConfigFileException, IllegalArgumentException {
        Biome biom = null;
        for (int i = 4; i < 10; i++) {
            // Zeile wird getrennt bei Leerzeichen
            String[] line = lines.get(i).split(" ");
            try {
                biom = Biome.valueOf(line[0]);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Die Biome sind fehlerhaft benannt.");
            }
            int chance = Integer.parseInt(line[1]);
            // Die individuelle Chance muss zwischen 0 und 100 sein
            if (chance < 0 || chance > 100) {
                throw new IllegalConfigFileException("Chance der Biome fehlerhaft.");
            }
            biomeChances.put(biom, chance);
        }
    }

    /**
     * Liest die Gewichtung der einzelnen Biome aus.
     * Wenn die Gewichtung keine Zahl ist, dann beendet sich das Programm.
     * 
     * @throws IllegalConfigFileException Wenn die Gewichtung kleiner 1 ist
     * @throws IllegalArgumentException Wenn die Biome nicht richtig benannt wurden
     */
    private void readBiomeWeights() throws IllegalConfigFileException, IllegalArgumentException {
        Biome biom = null;
        for (int i = 4; i < 10; i++) {
            String[] line = lines.get(i).split(" ");
            try {
                biom = Biome.valueOf(line[0]);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Die Biome sind fehlerhaft benannt.");
            }
            int weight = Integer.parseInt(line[2]);
            // Gewichtung darf nicht kleiner 0 sein
            if (weight < 0) {
                throw new IllegalConfigFileException("Gewichtung der Biome ist fehlerhaft.");
            }
            biomeWeights.put(biom, weight);
        }
    }

    /**
     * Liest den Standort der vorgelegten Karten aus.
     * Das Programm beendet sich, wenn der Standort keine Zahl ist.
     * 
     * @throws NumberFormatException Wenn das TilePlacement keine Zahl ist
     */
    private void readPreplacedTilesPlacements() throws NumberFormatException {
        String[] line;
        for (int i = 0; i < getNumberCards(); i++) {
            // Die Zeile wird getrennt und bei Leerzeichen
            line = lines.get(11 + i).split(" ");
            try {
                TilePlacement place = new TilePlacement(Integer.parseInt(line[1]), Integer.parseInt(line[2]),
                        Integer.parseInt(line[3]));
                // Die TilePlacements werden der Liste hinzugefügt
                tilePlacementList.add(i, place);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Tile Placement fehlerhaft.");
            }
        }
    }

    /**
     * Liest die vorgelegten Karten ein mit ihren jeweiligen Kanten.
     * Beendet das Programm, wenn die Kantenbeizeichnung fehlerhaft ist.
     * 
     * @throws IllegalArgumentException Wenn die Biome nicht richtig bennant wurden
     */
    private void readPreplacedTiles() throws IllegalArgumentException {
        for (int i = 0; i < getNumberCards(); i++) {
            // Zeile wird getrennt
            String[] line = lines.get(11 + i).split(" ");
            List<Biome> edges = new ArrayList<Biome>();
            String[] edge = line[4].split(",");
            try {
                for (int j = 0; j < 6; j++) {
                    // Biome Liste wird erzeugt
                    edges.add(j, Biome.valueOf(edge[j]));
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Kantenbezeichnung der vorgelegten Karten ist falsch.");
            }
            // Gamecard wird erzeugt und hinzugefügt
            Gamecard tile = new Gamecard(edges);
            preplacedTilesList.add(i, tile);
        }
    }

    /**
     * @return Biome und ihre jeweiligen Chancen
     */
    @Override
    public Map<Biome, Integer> getBiomeChances() {
        return biomeChances;
    }

    /**
     * @return Biome und ihre jeweiligen Gewichtunge
     */
    @Override
    public Map<Biome, Integer> getBiomeWeights() {
        return biomeWeights;
    }

   
    /**
     * @return Beschreibung des Spiels
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * @return Anzahl der Spieler
     */
    @Override
    public int getNumPlayers() {
        return numberPlayer;
    }

    /**
     * @return Anzahl der Karten zu beginn des Spiels
     */
    @Override
    public int getNumTiles() {
        return tilesnumber;
    }

    /**
     * @return Die vorgelegten Karten
     */
    @Override
    public List<Tile> getPreplacedTiles() {
        return preplacedTilesList;
    }

    /**
     * @return Placement der vorgelegten Karten
     */
    @Override
    public List<TilePlacement> getPreplacedTilesPlacements() {
        return tilePlacementList;
    }

    /**
     * @return Liste mit PlayerIDs der der vorplatzierten Karten
     */
    @Override
    public List<Integer> getPreplacedTilesPlayerIDs() {
        return idList;
    }

    /**
     * Gibt die Anzahl der Karten zurück.
     *
     * @return Anzahl der Karten
     */
    private int getNumberCards() {
        return numberCards;
    }
}
