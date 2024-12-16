package doerfer.screen.stylesCSS;

import java.util.Timer;
import java.util.TimerTask;

import doerfer.preset.graphics.GPanel;
import doerfer.screen.Display;

/**
 * Klasse für temporäre Anzeigen von Nachrichten an den Spieler
 * 
 * @author Lennart S.
 */
public class TimerCustom extends Timer {

    /**
     * Timer für kurzzeitiges Anzeigen von message
     * 
     * @param panel   Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param message Übergebener String der grafisch angegeben werden soll
     * @param time    Zeit des Timers
     */
    public TimerCustom(GPanel panel, GTextImpl message, int time) {

        // Timer entfernt message nach Zeit
        this.schedule(new TimerTask() {
            @Override
            public void run() {
                panel.getLayerHUD().removeChild(message);
                panel.repaint();
            }
        }, time);
    }

    /**
     * Timer für PlayerScore bei GameOver + GameOver-Verschiebung nach oben
     * 
     * @param display Zugriff auf das Hauptfenster (Top-Level-Window der
     *                GUI-Anwendung)
     * @param panel   Zugriff auf Container um GUI-Komponenten hinzufügen zu können
     * @param message Übergebener String der grafisch angegeben werden soll
     */
    public TimerCustom(Display display, GPanel panel, GTextImpl message) {

        this.schedule(new TimerTask() {
            @Override
            public void run() {
                // Spiel Beendet Message on Top
                message.setPosition(GPanel.VIEWPORT_WIDTH / 2 * 1.05f, GPanel.VIEWPORT_HEIGHT * 0.25f);
                panel.setVisible(true); // Panel mit Beendenbutton anzeigen
                try {
                    display.showPlayerScore();
                } catch (Exception exp) {
                    display.displayError(exp);
                }
            }
        }, 4000); // 4 Sekunden
    }
}
