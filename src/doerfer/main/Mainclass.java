package doerfer.main;

import doerfer.screen.*;

/**
 * Hauptprogramm zum starten des Spiels.
 * 
 * @author Joshua H., Lennart S., Simon Sommer
 */

public class Mainclass {

  /** Default Konstruktor wird nicht genutzt*/
  public Mainclass() {
  }

  /**
   * das Spiel wird initialisiert
   * die grafische Ein-und Ausgabe wird erzeugt.
   * der Spielablauf wird gestartet.
   * @param args Kommandozeilen Argumente
   */
  public static void main(String[] args) {
    // -- Initialisierung des Spiels
    new Init(args);
    Game game = new Game();
    // -- Grafische Ein-und Ausgabe erzeugen.
    new StartFrame(game);
    // display.showStartFrame();
    try {
      game.initGame();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
