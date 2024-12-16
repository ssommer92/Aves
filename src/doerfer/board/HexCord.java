package doerfer.board;

/**
 * Die Klasse HexCord speichert die Koordinaten des hexagonalen Gitters im
 * Offset odd-q.
 * 
 * https://www.redblobgames.com/grids/hexagons/#coordinates-offset
 * 
 * @author Simon Sommer
 */

public class HexCord {
	/** Koordinaten des Gitters */
	private final int row, col;
	/** verschiedene Richtungsänderungen in den Offset odd-q Koordinaten */
	private final static int[][][] direction_diff = {

			// even cols
			{ { 0, -1 }, { 1, -1 }, { +1, 0 }, { 0, +1 }, { -1, 0 }, { -1, -1 } },
			// odd cols
			{ { 0, -1 }, { +1, 0 }, { +1, +1 }, { 0, +1 }, { -1, 1 }, { -1, 0 } } };

	/**
	 * Konstruktor speichter die Koordinaten in der Reihenfolge row und Colunm.
	 * 
	 * @param row Koordinate des Gitters.
	 * @param col Kordinate des Gitters.
	 */
	public HexCord(int row, int col) {

		this.row = row;
		this.col = col;
	}

	/**
	 * Gibt Coulnm zurück.
	 * wird für die umwandlung von HexCord in Tileplacement benötigt.
	 * 
	 * @return colunm
	 */
	public int getColumn() {
		return col;
	}

	/**
	 * Gibt die RoW zurück.
	 * Wird für die umwandlung von HexCord in Tileplacement benötigt.
	 * 
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * equals Methode wird für die Verwendung für HashMap in der
	 * Board Klasse überschrieben.
	 * @param o das object das verglichen werden soll.
	 * @return Wahr wenn die Objekte gleich sind.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof HexCord)) {
			return false;
		}

		HexCord place = (HexCord) o;

		return Integer.compare(col, place.col) == 0
				&& Integer.compare(row, place.row) == 0;
	}

	/**
	 * Hashcode Methode wird für die Verwendung für HashMap in der
	 * Board Klasse überschrieben.
	 * der Hashcode wird aus zwei Primzahlen gebildet.
	 * @return hashcode 
	 */
	@Override
	public int hashCode() {
		int hc = 17;
		int hashMultiplier = 59;
		hc = hc * hashMultiplier + col;
		hc = hc * hashMultiplier + row;
		return hc;
	}

	/**
	 * Die direction Zahlen geben die Koordinaten der folgenden Nachbarn wieder:
	 *      __0__
	 *     /     \
	 *  5 /       \ 1
	 *   /         \
	 *   \         /
	 *  4 \       / 2
	 *     \_____/
	 *        3
	 * 
	 * https://www.redblobgames.com/grids/hexagons/#neighbors.
	 * 
	 * @param direction Richtungszahl
	 * @return Koordinate die in dieser Richtung liegt.
	 */
	HexCord getNeighbor(int direction) {
		int parity = col & 1;
		int[] diff = direction_diff[parity][direction]; // direction difference

		return new HexCord(row + diff[1], col + diff[0]);
	}

}
