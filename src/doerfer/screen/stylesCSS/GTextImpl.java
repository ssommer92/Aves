package doerfer.screen.stylesCSS;

import java.awt.Color;

import doerfer.preset.graphics.GPanel;
import doerfer.preset.graphics.GText;
import doerfer.preset.graphics.TextAnchor;

/**
 * Klasse für grafische Formatierung von Nachrichten für den Spieler mittig im
 * Bild sowie Formatierung der Zahl bezüglich der restlichen Karten die gesetzt
 * werden können
 * 
 * @author Lennart S.
 */
public class GTextImpl extends GText {

    /**
     * Grafische Message-Anzeige (mittig)
     * 
     * @param panel    Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param text     Übergebener String der grafisch angegeben werden soll
     * @param fontSize Schriftgröße
     * @param color    Schriftfarbe
     */
    public GTextImpl(GPanel panel, String text, float fontSize, Color color) {

        super(GPanel.VIEWPORT_WIDTH / 2 * 1.05f, GPanel.VIEWPORT_HEIGHT / 2, text);
        setFontFamily("Comic Sans MS");
        setAlignment(TextAnchor.MIDDLE);
        setFill(color);
        setFontSize(fontSize);
        setBold(true);
    }

    /**
     * Anzeige Anzahl restlicher Tiles
     * 
     * @param panel Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param xPos  X-Koordite String-Anzeige
     * @param yPos  Y-Koordite String-Anzeige
     * @param text  Übergebener String der grafisch angegeben werden soll
     */
    public GTextImpl(GPanel panel, float xPos, float yPos, String text) {

        super(xPos * 1.031f, yPos * 0.95f, text);
        setAlignment(TextAnchor.MIDDLE);
        setFill(Color.BLACK);
        setFontSize(40f);
        setBold(true);
        panel.getLayerHUD().addChild(this);
    }
}
