package doerfer.screen.stylesCSS;

import java.awt.Color;

import doerfer.preset.graphics.GPanel;
import doerfer.preset.graphics.GText;
import doerfer.preset.graphics.TextAnchor;

/**
 * Klasse für grafische Formatierung der Anzeige von Spielerinformationen/namen
 * Setzt Formatierung für "Anzahl der Spieler" und Position im Container
 * 
 * @author Lennart S.
 */
public class GTextPlayer extends GText {

    /**
     * Grafische Anzeige Spielername oben links
     * 
     * @param panel    Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param text     Übergebener String der grafisch angegeben werden soll (Name)
     * @param color    Farbe des Spielers
     * @param position Position des Spielers in der Liste
     */
    public GTextPlayer(GPanel panel, String text, Color color, int position) {

        // Abstand zwischen Namen + Abstand linker Rand, Abstand oberer Rand, Name
        super(position * GPanel.VIEWPORT_WIDTH / 9 + GPanel.VIEWPORT_WIDTH * 0.02f, GPanel.VIEWPORT_HEIGHT * 0.05f,
                text);
        setAlignment(TextAnchor.START);
        setFontFamily("sans-serif");
        setFill(color);
        setFontSize(30f);
        setBold(true);
        panel.getLayerHUD().addChild(this);
    }
}
