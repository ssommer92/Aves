package doerfer.player;
/**
 * Enum Status für die Klasse Competitor 
 * 
 * @author Joshua H.
 */
enum Status {
    /** Erster Status, darf nur einmal aufgerufen werde, geht in Requestnumber über */
    INIT,
    /** Zweiter Status, geht in Notifytile über */
    REQUESTNUMBER,
    /** Dritter Status, geht in Notifytile über */
    NOTIFYTILE,
    /** Vierter Status, geht in Requestplacement oder Notifyplacement über */
    REQUESTPLACEMENT,
    /** Fünfter Status, geht in Requestnumber oder Requestseed über*/
    NOTIFYPLACEMENT,
    /** Sechster Stauts, geht in Requestnumber oder Requestseed über */
    REQUESTSEED,
    /** Siebter Status, geht in Verify über */
    VERIFY,
    /** Achter Status, ist der Deadlock */
    DEAD
}
