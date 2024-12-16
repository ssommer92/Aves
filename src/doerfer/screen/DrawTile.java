package doerfer.screen;

import java.awt.Color;
import java.io.IOException;

import com.kitfox.svg.SVGElementException;

import doerfer.board.Spot;
import doerfer.preset.Biome;
import doerfer.preset.Tile;
import doerfer.preset.graphics.DrawableTile;
import doerfer.preset.graphics.DrawableTileComponent;
import doerfer.preset.graphics.GPanel;
import doerfer.preset.graphics.PresetTileComponent;

/**
 * Klasse die aus einem übergebenen Tile ein DrawableTile
 * erzeugt, welche SVG Datein beinhaltet GGroup#addComponent und auf dem
 * GPanel gesetzt
 * werden kann.
 * 
 * @author Simon Sommer
 */
public class DrawTile extends DrawableTile {

  /**
   * Konstruktor nimmt panel, ein Tile und einen Spot entgegen.
   * das panel wird wegen der Verwendung als GGroup übergeben.
   * tile wird benötigt um die richtigen SVG Datein der Karte zu laden für edges
   * und center.
   * Der Spot erzeugt das DrawTile an der übergebene Stelle auf dem panel.
   * 
   * @param panel dort wo DrawTile erzeugt wird.
   * @param tile  ecken und mitte die als svg erzeugt werden.
   * @param p     Ort auf dem panel an dem das DrawTile erzeugt wird.
   */
  public DrawTile(GPanel panel, Tile tile, Spot p) {
    super(panel);
    genDrawTile(tile, p);
  }

  /**
   * Erzeugt aus den Ecken und der Mitter des Tile jeweils ein
   * DrawableTileComponent
   * die dem DrawTile als GElement hinzugefügt werden und SVG Datein
   * beinhalten.
   * Der Spot fügt das DrawTile als GGroup mit Transform#translate
   * dem panel hinzu.
   * 
   * @param tile ecken und mitte die als svg erzeugt werden.
   * @param p    Ort auf dem panel an dem das DrawTile erzeugt wird.
   */
  private void genDrawTile(Tile tile, Spot p) {

    try {
      addComponent(new DrawableTileComponent(PresetTileComponent.valueOf(drawCenter(tile.getCenter())), 0f));
      // 6 Edges erstellen
      for (int i = 0; i <= 5; i++) {
        addComponent(new DrawableTileComponent(PresetTileComponent.valueOf(drawEdge(tile.getEdge(i))), i * 60f));
      }

      transform().translate(p.getX(), p.getY());
      setStroke(Color.BLACK);

    }
    // --- Fehlerabfang ---
    catch (SVGElementException e) {
      System.out.println("Error: DrawTile konnte nicht erzeugt werden!");
      System.exit(0);
    } catch (IOException f) {
      System.out.println("Error: Datei konnte nicht gefunden werden!");
      System.exit(0);
    }
  }

  /**
   * Bringt eine Biomeecke in eine Form, so dass sie von
   * {@link PresetTileComponent} als SVG Datei gefunden werden kann.
   * 
   * @param biome soll umgewandelt werden
   * @return der umgewandelte String
   */
  private static String drawEdge(Biome biome) {
    return biome.toString() + "_EDGE";
  }

  /**
   * Bringt ein Biomezentrum in eine Form, so dass sie von
   * {@link PresetTileComponent} als SVG Datei gefunden werden kann.
   * 
   * @param biome soll umgewandelt werden
   * @return der umgewandelte String
   */
  private static String drawCenter(Biome biome) {
    return biome.toString() + "_CENTER";
  }
}
