package doerfer.listeners;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileNotFoundException;

import doerfer.music.Music;
import doerfer.preset.graphics.GGroup;
import doerfer.preset.graphics.GPanel;
import doerfer.screen.DrawTile;

/**
 * Klasse zur Implementierung von Funktionen über Mauseingaben
 * 
 * @author Lennart S.
 */
public class MouseWheelListenerImpl implements MouseWheelListener {

  /** Mögliche setzbaren Stellen je nach Rotation (Logik) */
  private GGroup[] greyValidRotations;
  /** Aktuelle Rotation 0-5 der Karte */
  private int rotation;
  /** Aktuelle Karte (rechts-unten) und aktueller Klon der Karte im HUD */
  private DrawTile dtMAIN, dtHUD;
  /** Zugriff auf Container um GUI-Komponenten hinzufügen zu können */
  private GPanel panel;
  /** Einstellung Sound Drehen */
  private boolean isSound;

  /**
   * Konstruktor für die zu setztenden Tiles (Nur per Mausraddrehen)
   * 
   * @param isSound            Einstellungen für den Sound (True/False)
   * @param panel              Panel des Displays
   * @param dtMAIN             DrawableTile auf dem LayerMAIN
   * @param dtHUD              DrawableTile auf dem LayerHUD
   * @param greyValidRotations Array mit grauen Tiles als GGroup mit den
   *                           verschiedenen Rotationen als index
   */
  public MouseWheelListenerImpl(boolean isSound, GPanel panel, DrawTile dtMAIN, DrawTile dtHUD,
      GGroup[] greyValidRotations) {

    this.isSound = isSound;
    this.panel = panel;
    this.dtMAIN = dtMAIN;
    this.dtHUD = dtHUD;
    this.greyValidRotations = greyValidRotations;
    showValidTilePlacements();
  }

  /**
   * Methode zur Rotation in beide Richtungen beim Mausrad hoch/runter
   * Drehen der aktuellen Karte auf dem Spielfeld und Vorschaukarte rechts unten.
   * Aufruf der "showValidTilePlacements" Methode um je nach Rotation die
   * möglichen Spielzüge anzeigen zu lassen
   * 
   * @param e MouseWheelEvent
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {

    if (e.getWheelRotation() > 0) {
      rotation++;
    } else {
      rotation--;
    }
    if (isSound) {
      try {
        Music.turnDtSound();
      } catch (FileNotFoundException e1) {
        // Musikdatei wird nicht abgespielt.
      }
    }
    rotation = rotation % 6;
    rotation = Math.abs(rotation);
    dtMAIN.rotate(rotation * 60);
    dtHUD.rotate(rotation * 60);
    dtMAIN.setOpacity(0);
    showValidTilePlacements();
    panel.repaint();
  }

  /**
   * Mögliche gültige Züge werden grafisch angezeigt
   * Hierbei werden alle validen Stellen entsprechend der aktuellen Rotation
   * angezeigt.
   */
  private void showValidTilePlacements() {

    for (int i = 0; i < 6; i++) {
      greyValidRotations[i].setOpacity(0f);
    }
    greyValidRotations[Math.abs(rotation)].setOpacity(1f);
  }

  /**
   * Rotation für MouseListener
   * 
   * @return rotation
   */
  public int getRotation() {

    return rotation;
  }
}
