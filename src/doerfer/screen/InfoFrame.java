package doerfer.screen;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import doerfer.configuration.Config;
import doerfer.preset.Biome;
/** Klasse um während des Spiels die Informationen anzuzeigen
 * Die Informationen bestehen aus den Biomchancen
 */
public class InfoFrame extends JFrame{

    /** Wird zur serialisierung verwendet */
    private static final long serialVersionUID = 758304;
    
    /**
     * Erzeugt ein nicht sichtbares Info Frame
     * @param frame der Startframe
     */
    public InfoFrame(JFrame frame){
        setVisible(false);
        setSize(720, 480);
        setLocationRelativeTo(null); // zentriert den Frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        //config wird geladen
        Config config= new Config();
        
        JPanel panel;
        JButton back = new JButton("Zurück");
        JLabel chancesLabel = new JLabel("Wahrscheinlichkeiten der einzelnen Biome:");
        JLabel forestLabel = new JLabel("FOREST: " + config.getBiomeChances().get(Biome.FOREST) + "%");
        JLabel waterLabel = new JLabel("WATER: " + config.getBiomeChances().get(Biome.WATER) + "%");
        JLabel plainsLabel = new JLabel("PLAINS: " + config.getBiomeChances().get(Biome.PLAINS) + "%");
        JLabel housesLabel = new JLabel("HOUSES: " + config.getBiomeChances().get(Biome.HOUSES) + "%");
        JLabel fieldsLabel = new JLabel("FIELDS: " + config.getBiomeChances().get(Biome.FIELDS) + "%");
        JLabel traintracksLabel = new JLabel("TRAINTRACKS: " + config.getBiomeChances().get(Biome.TRAINTRACKS) + "%");
        //vertikales Panel wird erzeugt
        panel = createCenterVerticalPanel(10, 
                    chancesLabel, forestLabel,
                    waterLabel, plainsLabel, housesLabel, fieldsLabel, traintracksLabel, back);
                
        add(panel);
        //ZurückListner
        class BackListner implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
                frame.setVisible(true);
            }
        }
        //fügt den zurück Listner dem Button hinzu
        back.addActionListener(new BackListner());
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
