package doerfer.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import doerfer.configuration.Config;
import doerfer.configuration.Settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Klasse zum eingeben und speichern der Einstellungen
 * 
 * @author Joshua H.
 */
public class SettingsFrame extends JFrame { 

    /** Wird zur serialisierung verwendet */
    private static final long serialVersionUID = 758304;
    
    /**
     * Default Konstruktor um den Einstellungsframe zu erzeugen und ihn anzuzeigen.
     * Folgende Einstellungen können vorgenommen werden:
     * - Größe des Spielframes
     * - Sound (on/off)
     * - Spielernamen
     * - Random_AI oder Human
     * Ausgewählte Settings werden zusätzlich angezeigt
     * 
     * @param startframe Um den Startframe wieder auf Visible zu setzen
     */
    public SettingsFrame(StartFrame startframe){
        ArrayList<JTextField> playernamesTextFields = new ArrayList<>();;
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        setVisible(false);
        setSize(900, 480);
        setLocationRelativeTo(null); // zentriert den Frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        int numPlayers = 0;
        int numTiles = 0;
        Settings set = new Settings();
        Config config;
            config = new Config();
            numPlayers = config.getNumPlayers();
            numTiles = config.getNumTiles();
        
        
        // erzeugt erstes Label
        JLabel sizelabel = new JLabel("Größe: ");
        JTextField col = new JTextField(String.valueOf(set.getX()), 4);
        JTextField row = new JTextField(String.valueOf(set.getY()), 4);
        JPanel sizePanel = new JPanel();
        sizePanel.add(sizelabel);
        sizePanel.add(col);
        sizePanel.add(row);

        // erzeugt zweites Label
        JPanel soundPanel = new JPanel();
        JLabel soundLabel = new JLabel("Sound: ");
        JCheckBox checkBoxOn = new JCheckBox("An");
        JCheckBox checkBoxOff = new JCheckBox("Aus");
        ButtonGroup buttonGroup = new ButtonGroup(); // erzeugt ein Gruppe, damit nur eine Check Box ausgewählt sein
                                                     // kann
        checkBoxOn.setSelected(set.isSound());
        checkBoxOff.setSelected(!set.isSound());
        buttonGroup.add(checkBoxOn);
        buttonGroup.add(checkBoxOff);
        soundPanel.add(soundLabel);
        soundPanel.add(checkBoxOn);
        soundPanel.add(checkBoxOff);

        // viertes Label
        JLabel numbercardslabel = new JLabel("Anzahl der Karten: " + numTiles);

        // Spielerlabel
        JPanel playerPanel = new JPanel();
        for (int i = 0; i < numPlayers; i++) {
                JTextField playerlabel = new JTextField(set.getPlayerNames()[i], 10);
                playernamesTextFields.add(playerlabel);
                JCheckBox checkBoxOfHuman = new JCheckBox("HUMAN");
                checkBoxes.add(checkBoxOfHuman);
                playerPanel.add(checkBoxOfHuman);
                playerPanel.add(playerlabel);
                checkBoxOfHuman.setSelected(set.getIsHUMAN()[i]);
                
        }

        JLabel delayLabel = new JLabel("Delay der Random AI in Millisekunden");
        JTextField delayTimeField = new JTextField(String.valueOf(set.getDelay()), 5);
        
        JPanel delayPanel = new JPanel();
        delayPanel.add(delayLabel);
        delayPanel.add(delayTimeField);

        JButton back = new JButton("Zurück");
        JButton save = new JButton("Speichern");
        



        // Das Center wird zusammengesetzt
        JPanel panel = createCenterVerticalPanel(10, sizePanel, soundPanel, numbercardslabel, playerPanel, delayPanel,
                    save, back);
        add(panel);

        // Zurück Action Listner
        // Geht zurück zum Start Frame und schließt das aktuelle Fenster
        class BackListner implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                    setVisible(false);
                    startframe.setVisible(true);
            }
        }
        String fileName = "resources/Settings.txt";
        // Speichern Action Listner
        // Speichert die Einstellungen
        // Geht zurück zum Start Frame und schließt das aktuelle Fenster
        class SaveListner implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
                try(PrintWriter pWriter = new PrintWriter(new FileWriter(fileName));) {
                    pWriter.println(col.getText());
                    pWriter.println(row.getText());
                    pWriter.println(String.valueOf(checkBoxOn.isSelected()));
                    pWriter.println(delayTimeField.getText());
                    //foreach Schleife für die Playernamen
                    for (JTextField jTextField : playernamesTextFields) {
                        pWriter.println(jTextField.getText());
                    }
                    //foreach Schleife für die Checkboxen der Player
                    for (JCheckBox checkbox : checkBoxes) {
                        pWriter.println(String.valueOf(checkbox.isSelected()));
                    }
                } catch (IOException ioe) {
                    System.out.println("Error: Konnte die Settings nicht speichern.");
                }
                startframe.setVisible(true);
            }
        }
        back.addActionListener(new BackListner());
        save.addActionListener(new SaveListner());
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
