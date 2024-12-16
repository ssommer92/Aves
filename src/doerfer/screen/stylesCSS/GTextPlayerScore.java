package doerfer.screen.stylesCSS;

import java.awt.Color;

import doerfer.preset.graphics.GPanel;
import doerfer.preset.graphics.GText;
import doerfer.preset.graphics.TextAnchor;

/**
 * Klasse für Formatierung bei der PlayerScoreAnzeige bei Drücken von Tab
 * 
 * @author Lennart S.
 */
public class GTextPlayerScore extends GText {

    /**
     * Grafische Formatierung für die Anzeige von Spielern und Spielerpunkten
     * 
     * @param panel          Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param playerAndScore Spielername und Spielerscore
     * @param posX           Position X-Achse
     * @param posY           Position Y-Achse 
     */
    public GTextPlayerScore(GPanel panel, String playerAndScore, float posX, float posY) {

        super(posX, posY, playerAndScore);
        setAlignment(TextAnchor.MIDDLE);
        setFontFamily("sans-serif");
        setFill(Color.WHITE);
        setFontSize(50f);
        setBold(true);
        panel.getLayerHUD().addChild(this);
    }
}
