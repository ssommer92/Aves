package doerfer.screen;

import java.awt.*;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import doerfer.main.Game;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.awt.event.ActionEvent;

/**
 * Klasse zum erzeugen des Start Frame. Hier kann man auswählen ob man noch
 * Einstelleungen machen will, oder direkt starten will.
 */
public class StartFrame extends JFrame {

    /** Wird zur serialisierung verwendet */
    private static final long serialVersionUID = 869403;

    /**
     * Default Konstruktor um den Startframe zu erzeugen und anzuzeigen der ein
     * Auswahlmenü präsentiert.
     * 
     * @param game Um das Game aufzuwecken
     */
    public StartFrame(Game game) {
        JButton start = new JButton("Start");
        JButton settings = new JButton("Einstellungen");
        JPanel buttonPanel;
        SettingsFrame settingsFrame = new SettingsFrame(this);
        setSize(720, 480);
        setLocationRelativeTo(null); // Zentriert den Frame in der Mitte des Bildschirms
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // Setzt das GridBagLayout, damit alles mittig angezeigt werden kann

        // Lädt das Bild und knüpft es an ein Label
        BufferedImage myPicture = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        try {
            myPicture = ImageIO.read(new File("resources/logo.png"));
        } catch (IOException e) {
            System.out.println("Error: Startlogo konnte nicht gelesen werden.");
            System.exit(0);
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));

        // Erstellt das mittige Panel
        buttonPanel = createCenterVerticalPanel(10, picLabel, start, settings);
        add(buttonPanel);
        setVisible(true);

        // Start Action Listner
        // Startet das Spiel und schließt das aktuelle Fenster
        class StartListner implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
                game.setGameVisible();
            }
        }

        // Einstellung Action Listner
        // Öffnet die Einstelungen und schließt das aktuelle Fenster
        class SettingsListner implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
                settingsFrame.setVisible(true);
            }
        }
        start.addActionListener(new StartListner());
        settings.addActionListener(new SettingsListner());

        // Startframe überspringen
        // start.doClick();
    }

    /**
     * Erstellt ein mittiges Panel
     * @param spaceBetweenComponents Der vertikale Pixelabstand zwischen den
     *                               einzelnen Komponenten.
     * @param components             Die Komponenten die in der Mitte zentriert
     *                               werden sollen.
     * @return Das gelayoutete Panel.
     */
    private static JPanel createCenterVerticalPanel(final int spaceBetweenComponents, final JComponent... components) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Arrays.stream(components).forEach(component -> {
            component.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            panel.add(component);
            panel.add(Box.createRigidArea(new Dimension(0, spaceBetweenComponents)));
        });

        return panel;
    }

}
