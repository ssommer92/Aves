package doerfer.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import doerfer.board.Spot;
import doerfer.preset.TilePlacement;
import doerfer.preset.graphics.GPanel;
import doerfer.screen.Display;
import doerfer.screen.DrawTile;

/**
 * Klasse zur Implementierung von Funktionen über die Mauseinigaben
 * 
 * @author Lennart S.
 */
public class MouseListenerImpl extends MouseAdapter {

  /** Zugriff auf das Hauptfenster (Top-Level-Window der GUI-Anwendung) */
  private Display display;
  /** Zugriff auf Container um GUI-Komponenten hinzufügen zu können */
  private GPanel panel;
  /** Spot für Koordinate auf Spielbrett */
  private Spot p;
  /** Clone vom aktuellen DT für Anzeige auf LayerMain */
  private DrawTile dtMAIN;
  /** MouseWheelListener */
  private MouseWheelListenerImpl mouseWheelListener;
  /** Aktuelle Rotation */
  private int rotation;

  /**
   * Konstruktor für den MouseListener
   * 
   * @param display            Zugriff auf das Hauptfenster (Top-Level-Window der
   *                           GUI-Anwendung)
   * @param panel              Zugriff auf Container um GUI-Komponenten hinzufügen
   *                           zu können
   * @param p                  Spot für Koordinate auf Spielbrett
   * @param dtMAIN             Clone vom aktuellen DT für Anzeige auf LayerMain
   * @param mouseWheelListener Übergabe MouseWheelListener
   * @param rotation           Aktuelle Rotation
   */
  public MouseListenerImpl(Display display, GPanel panel, Spot p, DrawTile dtMAIN,
      MouseWheelListenerImpl mouseWheelListener, int rotation) {
        
    this.display = display;
    this.panel = panel;
    this.p = p;
    this.dtMAIN = dtMAIN;
    this.mouseWheelListener = mouseWheelListener;
    this.rotation = rotation;
  }

  /**
   * Durch das Triggern von einem MouseEntered wird garantiert, dass immer die
   * richitge Position beim Setzten eingehalten wird und verhindert Probleme beim
   * Layerverschieben mit Pfeiltasten)
   * 
   * @param e MouseEvent
   * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseReleased(MouseEvent e) {

    mouseEntered(e); //Triggern
    if (rotation == mouseWheelListener.getRotation()) {
      TilePlacement place = new TilePlacement(p.pixelToHex().getRow(), p.pixelToHex().getColumn(), rotation);
      display.notifyTilePlacement(place);
    }
  }

  /**
   * Aktion beim Rüberfahren/Entern auf GreyTiles
   * Sobald ein GreyTile mit der Maus überfahren wird, wird dieses sichtbar
   * gemacht. zuvor wird dieser Klon auf die Stellr bewegt auf die gerade gesetzt
   * werden soll
   * 
   * @param e MouseEvent
   * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseEntered(MouseEvent e) {

    if (rotation == mouseWheelListener.getRotation()) {
      dtMAIN.transform().translate(p.getX(), p.getY());
      dtMAIN.setOpacity(1f);
      panel.repaint();
    }
  }

  /**
   * Aktion bei Verlassen und Wechsel der möglichen Felder (GreyTiles)
   * Sobald ein GreyTile mit der Maus verlassen wird, wird dieses unsichtbar
   * gemacht
   * 
   * @param e MouseEvent
   * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseExited(MouseEvent e) {

    if (rotation == mouseWheelListener.getRotation()) {
      dtMAIN.setOpacity(0f);
      panel.repaint();
    }
  }
}
