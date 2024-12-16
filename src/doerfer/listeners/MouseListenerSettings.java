package doerfer.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import doerfer.preset.graphics.GElement;
import doerfer.preset.graphics.GPanel;

/**
 * Klasse zur Implementierung von Funktionen über die Maus bezüglich des
 * InformationsIcons (oben rechts)
 * 
 * @author Lennart S.
 */
public class MouseListenerSettings extends MouseAdapter {

    /**
     * Zugriff auf das Hauptfenster (Top-Level-Window der GUI-Anwendung)/
     * Informationsfenster
     */
    private JFrame frame, infoFrame;
    /** SVG-Element */
    private GElement settingsSvgE;
    /** Breite des Containers */
    private float VP_W = GPanel.VIEWPORT_WIDTH * 0.975f;
    /** Höhe des Containers */
    private float VP_H = GPanel.VIEWPORT_HEIGHT;


    /**
     * Konstruktor für das InfoFrame
     * 
     * @param frame        Zugriff auf das Hauptfenster (Top-Level-Window der
     *                     GUI-Anwendung)
     * @param infoFrame    Informationsfenster
     * @param settingsSvgE SVG-Element für die grafische Anzeige
     */
    public MouseListenerSettings(JFrame frame, JFrame infoFrame, GElement settingsSvgE) {

        this.frame = frame;
        this.infoFrame = infoFrame;
        this.settingsSvgE = settingsSvgE;
    }

    /**
     * Beim Klicken wird das Informations-Fenster aufgerufen
     * 
     * @param e MouseEvent
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {

        infoFrame.setVisible(true);
    }

    /**
     * Sobald über das Element gefahren wird, vergrößert es sich
     * 
     * @param e MouseEvent
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {

        settingsSvgE.transform().scale(1.2f);
        settingsSvgE.transform().translate(VP_W * 0.967f, VP_H * 0.016f);
        frame.repaint();
    }

    /**
     * Sobald von dem Element gefahren wird, verkleinert es sich wieeder
     * 
     * @param e MouseEvent
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {

        settingsSvgE.transform().scale(1f);
        settingsSvgE.transform().translate(VP_W * 0.967f, VP_H * 0.02f);
        frame.repaint();
    }
}
