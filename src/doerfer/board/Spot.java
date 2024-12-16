package doerfer.board;

/**
 * Die Klasse Spot speichert die umgewandelten Hex Koordinaten als Bildpunkte.
 * 
 *@author Simon Sommer
 */

public class Spot {
  /** Bildpunkt Koordinaten x und y */
  private final float x, y;
  /** Abstand der Mitte zu jeder Ecke */
  private final static Spot size = new Spot(50, 50);
  /** Ursrpung des Layouts */
  private final static Spot origin = new Spot(780, 450);

  /**
   * Konstruktor speichert x und y als Bildpunkt.
   * 
   * @param x Bildpunkt Koordinate
   * @param y Bildpunkt Koordinate
   */
  public Spot(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * zweiter Kontruktor wandelt ein übergebenes HexCord um.
   * mit this wird der erste Konstruktor aufgerufen und mit hextoPixel
   * umgerechnet.
   * 
   * @param cord Koordinate die umgewandelt wird.
   */
  public Spot(HexCord cord) {
    this(hexToPixel(cord).x, hexToPixel(cord).y);
  }

  /**
   * liefert den x Wert zurück.
   * 
   * wird im display, Mouselistenerimpl, GreyTile und DrawTile verwendet.
   * 
   * @return x wert als float.
   */
  public float getX() {
    return x;
  }

  /**
   * liefert den y Wert zurück.
   * wird im display, Mouselistenerimpl, GreyTile und DrawTile verwendet.
   *
   * @return y wert als float.
   */
  public float getY() {
    return y;
  }

  /**
   * Wandelt die Hex offset Koordinaten in einen Bildpunkt um.
   * Wird im zweiten Konstruktor verwendet.
   * 
   * https://www.redblobgames.com/grids/hexagons/#hex-to-pixel
   * 
   * @param cord umzuwandelnde Koordinate
   * @return neuer Punkt auf dem Bildschirm.
   */
  private static Spot hexToPixel(HexCord cord) {
    float x = size.x * 3 / 2 * cord.getColumn();
    float y = size.y * (float) Math.sqrt(3) * (cord.getRow() + (float) 0.5 * (cord.getColumn() & 1));
    return new Spot(x + origin.x, y + origin.y);
  }

  /**
   * wandelt die pixel Koordinaten zuerst in Fractional Axial Koordinaten und
   * danach in offset odd q Koordinaten als HexCord um.
   * wird im Mouselistenerimpl benutzt.
   * 
   * https://www.redblobgames.com/grids/hexagons/#pixel-to-hex
   * 
   * @return neuer Punkt auf dem Bildschirm.
   */
  public HexCord pixelToHex() {
    // umwandlung in Axial Koordinaten
    Spot pt = new Spot((x - origin.x) / size.x, (y - origin.y) / size.y);
    double q_frac = 2.0 / 3.0 * pt.x;
    double r_frac = -1.0 / 3.0 * pt.x + Math.sqrt(3.0) / 3.0 * pt.y;
    int q = (int) Math.round(q_frac);
    int r = (int) Math.round(r_frac);

    // umwandlung der Axial Koordinaten in offset q.
    int col = q;
    int row = r + (q - (q & 1)) / 2;
    // Koordinaten getauscht. Nachprüfen.

    return new HexCord(row, col);
  }

}
