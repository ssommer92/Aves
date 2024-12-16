package doerfer.board;

import doerfer.card.Gamecard;
import doerfer.exception.IllegalPlacementException;
import doerfer.preset.*;
import java.util.*;
import doerfer.preset.Biome;

/**
 * Die Klasse Board repräsentiert ein Spielbrett auf dem auf hexagonalen Gittern
 * Spielkarten gesetzt werden können.
 * 
 * @author Simon Sommer
 */

public class Board {

  /**
   * grids beinhaltet eine Liste der verschiedenen Gitter für jede SpielerID.
   * Jedes Gitter verknüfpt die hexagonalen Koordinaten mit den gesetzen Karten.
   */
  private final ArrayList<Map<HexCord, Tile>> grids;
  /**
   * biomarea speichert die Biomverknüpfungen der ecken jeder Karte als Array
   * und verknüpft diese mit den hexagonalen Koordinaten auf dem Spielbrett.
   */
  private final Map<HexCord, Node[]> biomarea;
  /**
   * playerscoreid berechnet die Punkte der abgeschlossenen Biomflächen.
   * Jede Position im Array beinhaltet die Punkte für die jeweilige Spielerid.
   */
  private int[] playeridscore;
  /**Spielkonfiguration für die verifygame methode*/
  private GameConfiguration gameconfig;
  /**tiles speichert die Karten die während des Spiel, gesetzt werden für die verifygame methode.  */
  private List<Tile> tiles;

  /**
   * Konstruktor von Board holt sich aus der Spielkonfiguration die vorplatzierten
   * Karten und deren Position, sowie die Anzahl der Spieler.
   * Mit der Spieleranzahl werden die benötigten Gitter erstellt.
   * Spieleranzahl+1 wegen der nullten id.
   * Die vorplatzierten Karten werden mit ihren Positionen und ihrer id dem
   * jeweiligen grid hinzugefügt.
   * biomarea wird gesetzt, um den Abschluss der Biomflächen zu prüfen.
   * 
   * @param config vorplatzierte Karten und die Spieleranzahl.
   * @throws IllegalPlacementException bei ungültiger Platzierung.
   */
  public Board(GameConfiguration config) throws IllegalPlacementException {
    tiles = new ArrayList<>();
    biomarea = new HashMap<>();
    grids = new ArrayList<>();
    playeridscore = new int[config.getNumPlayers() + 1];
    gameconfig = config;

    for (int j = 0; j < config.getNumPlayers() + 1; j++) {
      grids.add(new HashMap<>());
    }

    for (int i = 0; i < config.getPreplacedTiles().size(); i++) {
      Tile tile = config.getPreplacedTiles().get(i);
      TilePlacement place = config.getPreplacedTilesPlacements().get(i);
      addCard(place, tile, config.getPreplacedTilesPlayerIDs().get(i));
    }
    tiles.clear();//nur für verified game
  }

  /**
   * prüft ob sich die übergebene Koordinate in allen existierenden Gittern
   * befindet und gibt die zugehörige Karte zurück falls vorhanden.
   * 
   * @param cord Koordinate die überprüft werden soll.
   * @return liefert null zurück, falls der Platz nicht enthalten ist.
   */
  private Tile getTileInGrid(HexCord cord) {
    for (Map<HexCord, Tile> grid : grids) {
      if (grid.containsKey(cord))
        return grid.get(cord);
    }
    return null;
  }

  /**
   * addCard platziert die übergebene Spielkarte auf dem jeweiligen Gitter. Das
   * Gitter wird mit der Spielerid identifiziert.
   * Wenn der übergebene Platz nicht vorhanden ist, wird das hinzufügen
   * übersprungen.
   * Falls der Platz auf einem Gitter schon belegt ist, wird eine custom exception
   * geworfen.
   * Es werden die Ecken der Gamecard mit der Rotation für die platzierung
   * angepasst.
   * Die Karte wird dem jeweiligen Gitter die Karte und die knotenliste aus den
   * Ecken der Karte für die biomarea hinzugeüft.
   * 
   * @param place    der Ort, an dem die Karte platziert werden soll.
   * @param tile     die zu platzierende Karte
   * @param playerID die id des Spielers der gerade die Karte setzt.
   * @throws IllegalPlacementException falls der Platz bereits belegt ist.
   * 
   */
  public void addCard(TilePlacement place, Tile tile, int playerID) throws IllegalPlacementException {
    // prüft ob ein gültiger Platz übergeben wurde.
    if (place == null) {
      return; 
    }

    HexCord cord = new HexCord(place.getRow(), place.getColumn());
    // prüft ob Koordinaten nicht belegt sind.
    if (getTileInGrid(cord) != null) {
      throw new IllegalPlacementException("Platz ist bereits belegt!");
    }
    // fügt die Objekte dem grit hinzu
    tiles.add(tile);
    Tile shiftedtile = new Gamecard(tile, place.getRotation());
    grids.get(playerID).put(cord, shiftedtile);
    biomarea.put(cord, tileToNodes(shiftedtile, playerID));
    NodeNeighbor(cord);
  }

  /**
   * vergleicht die Ecken zweier Karten auf Biomekompatiblität.
   * Wasser lässt sich nur an Wasser und Schienen nur an Schienen anlegen.
   * Alle anderen Biome sind kompatibel.
   * 
   * @param edge1 Ecke der ersten Karte.
   * @param edge2 Ecke der zweiten Karte.
   * @return false falls die Biome nicht kompatibel sind.
   */
  protected static boolean compareEdge(Biome edge1, Biome edge2) {
    if (edge1 != edge2) {
      if (edge1 == Biome.WATER || edge1 == Biome.TRAINTRACKS
          || edge2 == Biome.WATER || edge2 == Biome.TRAINTRACKS)
        return false;
    }
    return true;
  }

  /**
   * Bestimmt die validen Kartenplatzierungen anhand der übergebenen Karte, der
   * Spielerid und für die jeweilige Rotation.
   * 
   * @param tile     die zu setztende Karte.
   * @param playerID die id des Spielers der gerade die Karte setzt.
   * @return Set an allen validen Positionen.
   */
  public Set<TilePlacement> validPlacements(Tile tile, int playerID) {

    Set<TilePlacement> validplace = new HashSet<TilePlacement>();

    // Iteration durch die Koordinaten aller gesetzen Karten.
    for (HexCord key : grids.get(playerID).keySet()) {
      // Iteration durch alle Nachbarn der Koordinaten um freie Plätze zu finden.
      for (int i = 0; i <= 5; i++) {
        for (int rot = 0; rot <= 5; rot++) {
          HexCord neighborcord = key.getNeighbor(i);
          // prüfe ob der Nachbarplatz frei ist.
          if (getTileInGrid(neighborcord) == null) {
            // prüfe ob die gezogene Karte sich an die freien Stellen setzen lässt.
            if (checkNeighbors(neighborcord, tile, rot)) {
              validplace.add(
                  new TilePlacement(neighborcord.getRow(), neighborcord.getColumn(), rot));
            }
          }
        }
      }
    }
    return validplace;
  }

  /**
   * Überprüft alle Nachbarn mit der übergebenen Koordinate, ob sich die
   * übergebene Karte an diese Koordinate setzen lässt.
   * 
   * @param cord     Koordinate die überprüft werden soll
   * @param tile     Karte dessen Ecken verglichen werden
   * @param rotation aktuelle drehung der Karte
   * @return false falls ein Nachbar nicht passt.
   */
  private boolean checkNeighbors(HexCord cord, Tile tile, int rotation) {
    // durch alle Nachbarn iterieren
    for (int i = 0; i <= 5; i++) {
      Tile neighborTile = getTileInGrid(cord.getNeighbor(i));
      // prüfe ob Nachbar enthalten ist
      if (neighborTile != null) {
        // prüfe bei einem vorhandenen Nachbarn ob die ecken übereinstimmen.
        if (!compareEdge(
            tile.getEdge((i + 6 - rotation) % 6),
            neighborTile.getEdge((i + 3) % 6)))
          return false;
      }
    }
    return true;
  }

  /**
   * Erstellt eine Liste von Knoten aus den jeweiligen Ecken der übergebenen
   * Karte. Die Spielerid wird in jedem Knoten gespeichert.
   * Die Knoten werden miteinander vereint, falls die beieinander liegenden
   * Ecken die gleichen Biome haben.
   * Am Ende noch das Zentrum der Karte mit möglichen Ecken vereint die das
   * gleiche Biome haben.
   * 
   * @param tile     Karte aus der die Knotenliste erstellt wird.
   * @param playerID id der setzenden Karte
   * @return Liste der Biomknoten.
   */
  private Node[] tileToNodes(Tile tile, int playerID) {
    Node[] nodes = new Node[6];

    // jede Ecke zu einer node machen
    for (int i = 0; i <= 5; i++) {
      nodes[i] = new Node(tile.getEdge(i), playerID, false);
    }

    // jede Node mit zusammenliegender Node mit passenden Biome vereinen.
    for (int i = 0; i <= 5; i++) {
      if (nodes[i].getBiome() == nodes[(i + 1) % 6].getBiome())
        nodes[i].unionTile(nodes[(i + 1) % 6]);
    }

    // center mit den edge nodes prüfen
    Node nodecenter = new Node(tile.getCenter(), playerID, true);

    for (int i = 0; i <= 5; i++) {
      if (nodecenter.getBiome() == nodes[i].getBiome()) {
        nodecenter.unionTile(nodes[i]);
      }
    }
    return nodes;
  }

  /**
   * Prüft ob sich alle Nachbarknoten einer übergebenen Koordinate vereinen
   * lassen.
   * Wirft einen Fehler, falls bei unionneighbor die Biome nicht kompatibel sind.
   * Vereint die Nachbarbiome Knoten, falls sie kompatibel sind.
   * Falls es nach der Vereinigung keine offenen Enden mehr gibt werden die Punkte
   * der abgeschlossenen Biomfläche berechnet.
   * 
   * @param cord Koordinate deren Nachbarn geprüft werden sollen.
   * @throws IllegalPlacementException Falls die Biome nicht kompatibel sind.
   */
  private void NodeNeighbor(HexCord cord) throws IllegalPlacementException {

    for (int i = 0; i <= 5; i++) {
      HexCord neighbor = cord.getNeighbor(i);
      if (biomarea.containsKey(neighbor)) {
        Node placednode = biomarea.get(cord)[i];
        Node neighbornode = biomarea.get(neighbor)[(i + 3) % 6];
        placednode.unionNeighbor(neighbornode);

        if (placednode.getOpenends() == 0) {
          updateScore(neighbornode);
        }
        if (neighbornode.getOpenends() == 0 && placednode.getBiome() != neighbornode.getBiome()) {
          updateScore(neighbornode);
        }
      }
    }
  }

  /**
   * Berechnet die Punktzahl der abgeschlossenen Biomfläche und updated die Punkte
   * der Spieler in playeridscore.
   * 
   * @param node Knoten hat die Daten der Biomfläche
   */
  private void updateScore(Node node) {
    // Formel (n*A)+floor(A^1.5)
    int score = node.getSize() * node.getPlayers().size() + (int) Math.floor(Math.pow(node.getSize(), 1.5));
    for (int playerid : node.getPlayers()) {
      playeridscore[playerid] += score;
    }
  }

  /**
   * Liefert den Array der gespeicherten Punkte zurück.
   * 
   * @return Punktzahl der einzelnen Spieler.
   */
  public int[] getPlayerIdScore() {
    return playeridscore;
  }

  /**
   * prüft am Ende des Spiels, ob sich mit der übergebenen Seed liste die Karten
   * erzeugen lassen, die bisher gelegt wurden.
   * 
   * @param seeds  Liste mit seeds.
   * @param scores Puntzahl der Spieler.
   * @return false falls das Spiel nicht verifiziert ist.
   */
  public boolean verifyGame(List<Long> seeds, List<Integer> scores) {
    // TileGenerators mit der aktuellen Spielkonfiguration.
    TileGenerator tileGenerator = new TileGenerator(gameconfig);
    // Liste von RNGs (Zufallszahlengeneratoren) aus den übergebenen Seeds.
    List<RNG> rngs = new ArrayList<>();

    // Anzahl der Scores und Spieler müssen übereinstimmen.
    if(scores.size() != gameconfig.getNumPlayers()) {
      return false;
    }

    // Initialisierung der RNG-Objekte mit den Seeds.
    for (long seed : seeds) {
      rngs.add(new RNG(seed)); // Erstellen eines neuen RNG für jeden Seed.
    }

    // Überprüfe, ob sich jedes bisher gelegte Tile durch die RNG-Objekte neu erzeugen lässt.
    for (Tile tile : tiles) {
      long rng = 0; // Initialisierung des RNG-Werts.
      // Kombinieren der RNGs für jeden Spieler durch XOR-Operation.
      for (int j = 0; j < gameconfig.getNumPlayers(); j++) {
        rng = rng ^ rngs.get(j).next();
      }

      Tile newtile = new Gamecard(tileGenerator.generateTile(rng));

      if (!newtile.equals(tile)){
        return false;
      }
    }
    // Übergebenen Spielergebnisse müssen mit den gespeicherten übereinstimmen.
    for(int k = 0; k < scores.size(); k++){
      if(scores.get(k) != playeridscore[k+1]){
        return false;
      }
    }
    return true;
  }
}
