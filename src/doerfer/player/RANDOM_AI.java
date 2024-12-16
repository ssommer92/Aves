package doerfer.player;

import java.util.Random;
import java.util.Set;

import doerfer.configuration.Settings;
import doerfer.preset.TilePlacement;

/**
 * Klasse für die Random AI. Dieser benötigt keine graphische Oberfläche zum spielen
 * 
 * @author Joshua H.
 */

public class RANDOM_AI extends Competitor {
    /** Zufallszahlengenerator zum auswählen der Zufallskarte */
    private Random randomnumber = new Random();
    /** wird für das Dealy benötigt */
    private Settings settings;
    /**
     * Konstruktor der RANDOM AI
     * @param name Name des Spielers
     * @param settings Einstellung Delay
     */
    public RANDOM_AI(String name, Settings settings) {
        super(name);
        this.settings = settings;
    }

    /** 
     * Gibt ein zufälliges Tileplacement zurück aus der Menge der möglichen Tileplacements
     * @return zufälliges TilePlacement
     */
    @Override
    protected TilePlacement getTilePlacement() {
        Set<TilePlacement> validTilePlacements = ownBoard.validPlacements(openCards.peek(), playerID);
        if (validTilePlacements.size() != 0) {
            //Zufallszahl mit der Range der Größe der Menge der möglichen Tileplacements
            int choice = randomnumber.nextInt(validTilePlacements.size());
            int i = 0;
            //Tileplacement wird herausgezofen aus der Menge
            for (TilePlacement tilePlacement : validTilePlacements) {
                if (i == choice) {
                    try {
                        Thread.sleep(settings.getDelay());
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    return tilePlacement;
                }
                //nächstes im Set
                i++;
            }
        }
        return null;
    }
}
