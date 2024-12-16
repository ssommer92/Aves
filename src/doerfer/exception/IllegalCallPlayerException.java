package doerfer.exception;


/**
 * Diese Exception wird geworfen, wenn eine der Methoden im Player falsch aufgerufen wurden.
 * @author Joshua H.
 */
public class IllegalCallPlayerException extends Exception {

    /** Wird zur serialisierung verwendet */
    private static final long serialVersionUID = 759304;

    /**
     * Konstruktor für die Exception
     * @param message Nachricht für das Display
     */
    public IllegalCallPlayerException(String message) {
        super(message);
    }
    
}
