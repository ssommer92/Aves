package doerfer.exception;

/**
 * Die Klasse IllegalPlacementException wirft eine Exception wenn durch die
 * Spielregeln eine Karte nicht platziert werden kann.
 * 
 * @author Simon Sommer
 */
public class IllegalPlacementException extends Exception {
    /** wird zur serialisierung verwendet */
    private static final long serialVersionUID = 97236l;

    /**
     * Konstruktor erstellt neue Exception die eine Nachricht für eine falsche
     * Platzierung auf dem Spielbrett wirft.
     * 
     * @param message Hinweis: Nachricht für eine falsche PLatzierung.
     */
    public IllegalPlacementException(String message) {
        super(message);
    }

}
