package doerfer.listeners;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import doerfer.preset.graphics.GPanel;
import doerfer.screen.Display;
import doerfer.screen.stylesCSS.MessageBG;

/**
 * Klasse zur Implementierung von Funktionen/Befehlen über die Tastatureingabe
 * 
 * @author Lennart S.
 */
public class KeyListenerImpl implements KeyListener {

  /** Zugriff auf das Hauptfenster (Top-Level-Window der GUI-Anwendung) */
  private Display display;
  /** Variable fürs Anzeigen des PlayerScores über Tab "Halten" */
  private int clickScore = 0;
  /** Position der Startkarte zu Beginn (0,0) */
  private float positionX, positionY = 0;
  /**
   * Position die grafisch (0,0) wiedergeben soll nach setzen einer neuen Karte
   */
  private float positionOriginX, positionOriginY = 0;
  /** Aktuelle Zoomstufe */
  private float scale = 1.0f;
  /** Zoomstufe für Ursprungspunktberechnung */
  private int levelScale = 0;
  /** Angepasste Positionsverschiebungsgeschwindigkeit je nachZoomstufe */
  private int levelSpeed = 2;
  /** Zugriff auf Container um GUI-Komponenten hinzufügen zu können */
  private GPanel panel;
  /** Anzeige für aktuellen Playerscore über TAB */
  private MessageBG blackBG;

  /**
   * Construktor für das Freischalten von Tastatureingaben
   * 
   * @param display Hauptfenster
   * @param panel   Container
   */
  public KeyListenerImpl(Display display, GPanel panel) {
    this.display = display;
    this.panel = panel;
  }

  /**
   * Aufruf von Tastatureingaben
   * 
   * @param event KeyEvent
   * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
   */
  @Override
  public void keyPressed(KeyEvent event) {

    /** Tastatur-Eingabe Pfeiltasten und TAB */
    int keyPosition = event.getKeyCode();
    /** Tastatur-Eingabe Zeichen, etc. */
    char keyScale = event.getKeyChar();

    // Eingabe linke Pfeiltaste -> Verschiebung Layer rechts
    if (keyPosition == KeyEvent.VK_LEFT) {
      positionX = positionX + (levelSpeed * 25);
      panel.getLayerMain().transform().translate(positionX, positionY);
      panel.repaint();
    }

    // Eingabe rechte Pfeiltaste -> Verschiebung Layer links
    if (keyPosition == KeyEvent.VK_RIGHT) {
      positionX = positionX - (levelSpeed * 25);
      panel.getLayerMain().transform().translate(positionX, positionY);
      panel.repaint();
    }

    // Eingabe obere Pfeiltaste -> Verschiebung Layer runter
    if (keyPosition == KeyEvent.VK_UP) {
      positionY = positionY + (levelSpeed * 25);
      panel.getLayerMain().transform().translate(positionX, positionY);
      panel.repaint();
    }

    // Eingabe untere Pfeiltaste -> Verschiebung Layer hoch
    if (keyPosition == KeyEvent.VK_DOWN) {
      positionY = positionY - (levelSpeed * 25);
      panel.getLayerMain().transform().translate(positionX, positionY);
      panel.repaint();
    }

    // Eingabe TAB-Taste -> Anzeige aktuelle Spieler & Punkte
    if (keyPosition == KeyEvent.VK_TAB) {
      if (clickScore == 0) {
        try {
          blackBG = new MessageBG(panel, Color.BLACK);
          display.showPlayerScore();
        } catch (Exception exp) {
          display.displayError(exp);
        }
      }
      clickScore = 1;
    }

    // Eingabe Plus -> Zoomstufe erhöhen
    if (keyScale == '+') {
      if (scale < 5f) {
        scale = scale + 0.5f;
        // Verschiebung für flüssigen Zoom
        positionX = positionX - 420;
        positionY = positionY - 250;
        panel.getLayerMain().transform().scale(scale);
        panel.getLayerMain().transform().translate(positionX, positionY);
        panel.repaint();
        levelScale++;
      } else {
        System.out.println("Maximale Anzeigegröße erreicht.");
      }
    }

    // Eingabe Minus -> Zoomstufe verringern
    if (keyScale == '-') {
      if (scale != 0.5f) { // Maximum Rauszoomen
        scale = scale - 0.5f;
        // Verschiebung für flüssiges Rauszoomen
        positionX = positionX + 420;
        positionY = positionY + 250;
        panel.getLayerMain().transform().scale(scale);
        panel.getLayerMain().transform().translate(positionX, positionY);
        panel.repaint();
        levelScale--;
      } else {
        System.out.println("Minimale Anzeigegröße erreicht.");
      }
    }

    /*
     * Eingabe 0 -> Reset View auf zuletzt gesetzte Karte ODER Reset auf zuerst
     * gesetzter Karte + Reset auf Scale 1.0 (Je nach Interpretation der
     * Aufgabenstellung)
     */
    if (keyScale == '0') {

      // VARIANTE 1) Reset auf selbem Scale und Fokus auf ZULETZT gesetzter Karte
      /*
       * if (scale == 1.0f) {
       * positionX = positionOriginX;
       * positionY = positionOriginY;
       * } else { // Jede Erhöhung der Zoomstufe verdoppelt die benötigte Verschiebung
       * des Layers
       * positionX = levelScale * -420 + positionOriginX;
       * positionY = levelScale * -250 + positionOriginY;
       * }
       * panel.getLayerMain().transform().translate(positionX, positionY);
       * panel.repaint();
       */

      // VARIANTE 2) Reset auf Anfangsscale und Fokus auf ZUERST gesetzter Karte
      scale = 1.0f;
      positionOriginX = 0;
      positionOriginY = 0;
      positionX = 0;
      positionY = 0;
      levelScale = 0;
      panel.getLayerMain().transform().scale(scale);
      panel.getLayerMain().transform().translate(positionOriginX, positionOriginY);

      panel.repaint();
    }
  }

  /**
   * Loslassen von Tastatureingaben
   * 
   * @param event KeyEvent
   * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
   */
  @Override
  public void keyReleased(KeyEvent event) {

    // Loslassen TAB-Taste -> Anzeige aktuelle Spieler & Punkte verlassen
    int keyPosition = event.getKeyCode();
    if (keyPosition == KeyEvent.VK_TAB) {
      try {
        display.removePlayerScore(blackBG);
        clickScore = 0;

      } catch (Exception exp) {
        display.displayError(exp);
      }
    }
  }

  /**
   * Methode die die Fokus-Methode aus dem Display unterstützt, um die
   * Tastatureingaben auf die neue (0,0) Position anzupassen, da durch die
   * Fokusmethode die neue Karte den neuen Nullpunkt bzw. die Mitte des
   * Bildschirms darstellen soll
   * 
   * @param positionXnew neue X-Position von der gesetzten Karte
   * @param positionYnew neue Y-Position von der gesetzten Karte
   */
  public void setPositionAfterSetTile(float positionXnew, float positionYnew) {

    // Verschiebung der Grafik auf Fokus der neu gesetzten Karte
    panel.getLayerMain().transform().translate(positionXnew, positionYnew);
    // Setzen des neuen Urspungspunkt
    positionOriginX = positionXnew;
    positionOriginY = positionYnew;
    // Setzten der "Bewegungsvariablen" auf Ursprungspunkt
    positionX = positionXnew;
    positionY = positionYnew;
    // Falls im Zoom Karten gesetzt werden, angepasste Verschiebung an Zoomstufe
    if (scale != 1.0f) {
      positionX = levelScale * -420 + positionOriginX;
      positionY = levelScale * -250 + positionOriginY;

      panel.getLayerMain().transform().translate(positionX, positionY);
      panel.repaint();
    }
  }

  /**
   * Wenn das durch diese Taste dargestellte Unicode-Zeichen von der Tastatur an
   * die Systemeingabe gesendet wird
   * 
   * @param event KeyEvent
   * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
   */
  @Override
  public void keyTyped(KeyEvent event) {

    // System.out.println("Eingabe von Taste: " + e.getKeyChar());
  }
}
