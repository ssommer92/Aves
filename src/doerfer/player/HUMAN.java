package doerfer.player;

import java.util.Set;

import doerfer.preset.GameView;
import doerfer.preset.TilePlacement;

/**
 * Klasse für den Human Player. Dieser benötigt eine graphische Oberfläche zum spielen
 * 
 * @author Joshua H.
 */
public class HUMAN extends Competitor {
    
    /** Grafische Oberfäche für den HUMAN */
    private GameView gameView;

    /**
     * Konstruktor des HUMANS benötigt auch eine graphische Oberfläche zusätlich zum Namen
     * @param name Name des Spielers
     * @param gameView Grafische Oberfläche des Spielers
     */
    public HUMAN(String name, GameView gameView) {
        super(name);
        this.gameView = gameView;
    }

    /* 
     * Berechnet mit Hilfe des Boards die legalen Placements auf dem Board
     * Mit Hilfe der graphischen Oberfläche wählt er ein Tileplacement aus
     * @param der Zug des Spielers
     */
    @Override
    protected TilePlacement getTilePlacement() {
        //bekommt die legalen Spielzüge übergeben
        Set<TilePlacement> validTilePlacements= ownBoard.validPlacements(openCards.peek(), playerID);
        //Überprüfung ob welche möglich sind
        if(validTilePlacements.size() == 0)
            return null;
        //Die Spielzüge dem Display übergeben
        gameView.setValidTilePlacements(validTilePlacements);
        return gameView.requestTilePlacement();
    }

}