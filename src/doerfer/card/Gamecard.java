package doerfer.card;

import doerfer.preset.Tile;
import doerfer.preset.Biome;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse Gamecard erstellt eine hexagonale Spielkarte die Ecken (edges) und ein
 * Zentrum (center) besitzt.
 * 
 * @author Simon Sommer, Joshua H.
 */
public class Gamecard extends Tile {
  /** Biome im Zentrum der Karte */
  private final Biome center;

  /**
   * Konstruktor von Gamecard setzt die Ecken der übergebenen Biomeliste über den
   * Superkontruktor und setzte das Zentrum an ahnand er übergebenen Ecken.
   * 
   * @param edges Liste an Biomen.
   */
  public Gamecard(List<Biome> edges) {
    super(edges);
    center = setCenter();
  }

  /**
   * Der zweite Konstruktor dreht die Ecken des übergeben Tiles mit der
   * übergebenen Rotation.
   * 
   * @param tile     tile das zu drehen ist
   * @param rotation int wert der Rotation.
   */
  public Gamecard(Tile tile, int rotation) {
    this(shiftEdges(tile, rotation));
  }

  /**
   * dreht die Ecken des Tiles mit der übergebenen Rotation.
   * 
   * @param tile     das zu drehende Tile
   * @param rotation wert der Rotation
   * @return die Karte mit den gedrehten Ecken.
   */
  private static List<Biome> shiftEdges(Tile tile, int rotation) {
    List<Biome> shiftedges = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      shiftedges.add(tile.getEdge((i + 6 - rotation) % 6));
    }
    return shiftedges;
  }

  /**
   * legt das Zentrum nach den übergebenen Ecken fest:
   * 1.zuerst wird geprüft wie oft welches Biome in den Ecken vorkommt.
   * 2.wenn mindestens eine Schiene vorkommt soll die Mitte Schiene sein
   * 3.es wird gepürft welches Biome am häufigsten vorkommt.
   * 4.prüfe ob es nicht noch ein anderes Biom gibt das ebenfalls so groß ist.
   * 5.Wenn es nicht gleich ist Abfrage der Größe nach wie in den Spielregeln.
   * 6.Wenn das es gleich ist wird das Zentrum nach dem Biom häfigsten Biome
   * gesetzt.
   * 
   * @return das Biome des Zentrums.
   */
  private Biome setCenter() {
    // 1.
    int[] biom = new int[6];
    for (int i = 0; i < 6; i++) {
      switch (getEdge(i)) {
        case TRAINTRACKS:
          biom[0]++;
          break;
        case WATER:
          biom[1]++;
          break;
        case PLAINS:
          biom[2]++;
          break;
        case FIELDS:
          biom[3]++;
          break;
        case FOREST:
          biom[4]++;
          break;
        case HOUSES:
          biom[5]++;
          break;
        default:
          break; // should not happen
      }
    }
    // 2.
    if (biom[0] > 0)
      return Biome.TRAINTRACKS;
    int maxbiom = 0;
    boolean equal = false;
    // 3.
    for (int i = 1; i < 6; i++) {
      if (maxbiom < biom[i])
        maxbiom = i;
    } // 4.
    for (int i = 1; i < 6; i++) {
      if (i != maxbiom)
        if (biom[maxbiom] == biom[i])
          equal = true;
    } // 5.
    if (!equal) {
      switch (maxbiom) {
        case 1:
          return Biome.WATER;
        case 2:
          return Biome.PLAINS;
        case 3:
          return Biome.FIELDS;
        case 4:
          return Biome.FOREST;
        case 5:
          return Biome.HOUSES;
        default:
          break; // should not happen
      }
    } // 6.
    if (biom[maxbiom] == biom[1])
      return Biome.WATER;
    if (biom[maxbiom] == biom[2])
      return Biome.PLAINS;
    if (biom[maxbiom] == biom[4])
      return Biome.FOREST;
    if (biom[maxbiom] == biom[5])
      return Biome.HOUSES;
    return Biome.FIELDS;
  }

  /**
   * Überschriebene Methode aus der abstrackten Klasse Tile
   * @return das Zentrum der Karte
   */
  @Override
  public Biome getCenter() {
    return this.center;
  }
}
