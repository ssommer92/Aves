package doerfer.screen;

import com.kitfox.svg.SVGElementException;
import doerfer.preset.graphics.*;
import doerfer.board.Spot;
import java.io.*;

/**
 * Klasse für das Anzeigen der grauen Felder (validen Stellen) auf der GUI
 * 
 * @author Lennart S.
 */
public class GreyTile extends DrawableTile {

  /**
   * Construktor für GreyTiles
   * 
   * @param panel Zugriff auf Container um GUI-Komponenten hinzufügen zu können
   */
  public GreyTile(GPanel panel) {
    super(panel);
  }

  /**
   * Setzen der GreyTiles auf dem Spielfeld
   * 
   * @param p     An welcher Stelle
   * @param panel Auf welchem Panel
   */
  public GreyTile(Spot p, GPanel panel) {
    
    super(panel);
    try {
      File grey = new File("resources/grey.svg");
      addComponent(new DrawableTileComponent(grey));
      // Kleiner machen als Spielkarten um Fehler/Problematik von MouseEntered und
      // MouseExited zu beheben
      transform().scale(0.8f);
      float offsetX = DrawTile.WIDTH * (1 - 0.8f) / 2;
      float offsetY = DrawTile.HEIGHT * (1 - 0.8f) / 2;
      transform().translate(p.getX() + offsetX, p.getY() + offsetY);
      panel.getLayerMain().addChild(this);
      panel.repaint();
    } catch (SVGElementException e) {
      System.out.println("Error: Karte konnte nicht gezogen werden!");
      System.exit(0);
    } catch (IOException f) {
      System.out.println("Error: Datei konnte nicht gefunden werden!");
      System.exit(0);
    }
  }
}
