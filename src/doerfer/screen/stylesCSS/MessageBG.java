package doerfer.screen.stylesCSS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import doerfer.preset.graphics.GPanel;
import doerfer.preset.graphics.GRect;

/**
 * Klasse für Anzeige von GAME_OVER oder SpielerScore (Hintergrund)
 * 
 * @author Lennart S.
 */
public class MessageBG extends GRect {

    /** Breite des Containers */
    private static float VP_width = GPanel.VIEWPORT_WIDTH * 1.05f;
    /** Höhe des Containers */
    private static float VP_height = GPanel.VIEWPORT_HEIGHT;
    /** Button zum Beenden der Anwendung */
    private JButton closeBTN = new JButton("Beenden");
    /** Container auf dem der Button zum Beenden der Anwendung angezeigt wird */
    private JPanel frameBTN = new JPanel();

    /**
     * Construktor für Anzeige der GAME_OVER Nachricht (Hintergund + BeendenButton)
     * 
     * @param panel   Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param frame   Zugriff auf das Hauptfenster (Top-Level-Window der
     *                GUI-Anwendung)
     * @param message Übergebener String der grafisch angegeben werden soll
     * @param colorBG Farbe des Hintergrundes
     */
    public MessageBG(GPanel panel, JFrame frame, GTextImpl message, Color colorBG) {

        super(VP_width, VP_height);
        setFill(colorBG);
        setOpacity(0.8f);

        frameBTN.setOpaque(false);
        frameBTN.add(closeBTN);
        frame.add(frameBTN, BorderLayout.SOUTH);

        panel.getLayerHUD().addChild(this);
        panel.getLayerHUD().addChild(message);

        closeBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
        panel.repaint();
    }

    /**
     * Construktor für Anzeige der Error Nachricht (Hintergund + BeendenButton)
     * 
     * @param panel   Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param frame   Zugriff auf das Hauptfenster (Top-Level-Window der
     *                GUI-Anwendung)
     * @param colorBG Farbe des Hintergrundes
     * @param message Übergebener String der grafisch angegeben werden soll
     */
    public MessageBG(GPanel panel, JFrame frame, Color colorBG, GTextImpl message) {

        super(VP_width, VP_height);
        setFill(colorBG);
        setOpacity(0.8f);

        frameBTN.setOpaque(false);
        frameBTN.add(closeBTN);
        frame.add(frameBTN, BorderLayout.SOUTH);

        panel.getLayerHUD().addChild(this);
        panel.getLayerHUD().addChild(message);

        closeBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
        panel.repaint();
    }

    /**
     * Construktor für Anzeige des PlayerScores (Hintergund + BeendenButton)
     * 
     * @param panel Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param color Farbe des Hintergrundes
     */
    public MessageBG(GPanel panel, Color color) {

        super(VP_width, VP_height);
        setFill(color);
        setOpacity(0.8f);
        panel.getLayerHUD().addChild(this);
    }
}
