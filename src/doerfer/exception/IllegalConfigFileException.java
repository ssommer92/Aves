package doerfer.exception;

/**
 * Diese Exception wird geworfen, wenn das Config File Fehler beinhaltet
 * @author Joshua H.
 */
public class IllegalConfigFileException extends Exception{
    /** wird zur serialisierung verwendet */
    private static final long serialVersionUID = 934867;

    /**
     * Konstuktor für die Exception
     * @param message Nachricht für das Display
     */
    public IllegalConfigFileException(String message) {
        super(message);
    }


}
