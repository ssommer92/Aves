package doerfer.exception;

/**
 * Diese Exception wird geworfen, wenn das Game nicht verifiziert werden konnte
 * @author Joshua H.
 */
public class IllegalGameException extends Exception {

    /** wird zur serialisierung verwendet */
    private static final long serialVersionUID = 90409044;


    /**
     * Konstruktor für die Exception
     * @param message Nachricht für das Display
     */
    public IllegalGameException(String message) {
        super(message);
    }
    
}
