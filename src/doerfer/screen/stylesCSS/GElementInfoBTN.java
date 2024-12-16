package doerfer.screen.stylesCSS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import doerfer.listeners.MouseListenerSettings;
import doerfer.preset.graphics.GElement;
import doerfer.preset.graphics.GPanel;
import doerfer.screen.InfoFrame;

/**
 * Klasse für Infobutton der ein neues Frame öffnet indem den Spielern
 * Informationen über das aktuelle Spiel gibt, wie die Wahrscheinlichkeiten der
 * einzelnen Biome
 * 
 * @author Lennart S.
 */
public class GElementInfoBTN extends GElement {
    
    /** Breite des Containers */
    private float VP_W = GPanel.VIEWPORT_WIDTH * 0.975f;
    /** Höhe des Containers */
    private float VP_H = GPanel.VIEWPORT_HEIGHT;

    /**
     * Button um das InfoFenster aufzurufen
     * 
     * @param frame   Zugriff auf das Hauptfenster (Top-Level-Window der
     *                GUI-Anwendung)
     * @param panel   Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param infoSvg Übergebenes SVG Element Quelle: <a target="_blank" href=
     *                "https://icons8.com/icon/2800/info">Info</a> icon by
     *                <a target="_blank" href="https://icons8.com">Icons8</a>
     * @throws FileNotFoundException Falls File nicht gefunden wurde
     * @throws IOException Fängt grundlegende Fehler ab falls SVG nicht geladen werden kann
     */
    public GElementInfoBTN(JFrame frame, GPanel panel, File infoSvg)
            throws FileNotFoundException, IOException {

        super(infoSvg);
        try {
            File infoSvgBg = new File("resources/grey.svg");
            GElement infoSvgE = new GElement(infoSvg);
            GElement infoSvgBgE = new GElement(infoSvgBg);
            panel.getLayerHUD().addChild(infoSvgBgE);
            panel.getLayerHUD().addChild(infoSvgE);
            InfoFrame infoFrame = new InfoFrame(frame);
            infoSvgE.transform().translate(VP_W * 0.97f, VP_H * 0.02f);
            infoSvgBgE.transform().translate(VP_W * 0.96f, VP_H * 0.01f);
            infoSvgBgE.transform().scale(0.8f);
            infoSvgBgE.setFillOpacity(0);
            infoSvgBgE.setMouseListener(new MouseListenerSettings(frame, infoFrame, infoSvgE));

        } catch (IOException f) {
            System.out.println("Error: Datei konnte nicht gefunden werden!");
            System.exit(0);
        }
    }
}
