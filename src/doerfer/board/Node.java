package doerfer.board;

import java.util.Set;
import java.util.TreeSet;

import doerfer.exception.IllegalPlacementException;
import doerfer.preset.Biome;

/**
 * Die Klasse Node repräsentiert einzelne Biomknoten die zu Biomflächen
 * vereinigt werden.
 * Dabei wird zum speichern der Biomfläche die Union find Struktur verwendet.
 * Alle relevanten Informationen werden in der Wurzel gespeichert.
 * 
 * @author Simon Sommer
 */
class Node {
    /** Wurzel des Baums in dem alle Datein gespeichert sind */
    private Node parent;
    /** offene Biomenden */
    private int openends;
    /** die Größe der kartenzusammenliegenden Biome */
    private int size;
    /** Biome des Knotens */
    private final Biome biome;
    /** Set von Spieler ids die zu dem Knoten gehören */
    private final Set<Integer> playeridset;

    /**
     * Der Konstruktor setzt die übergebenen Werte Biome und die Spielerid.
     * Jeder Knoten hat sich selbst als Wurzel.
     * Eckknoten haben ein offenes Ende
     * Mittelknoten sind bereits abgeschlossen.
     * 
     * @param biome    biome des Knotens.
     * @param playerid id des Spielers
     * @param isCenter prüft ob Knoten eine Ecke oder Mitte ist.
     */
    Node(Biome biome, int playerid, boolean isCenter) {
        this.biome = biome;
        parent = this;
        playeridset = new TreeSet<>();
        playeridset.add(playerid);
        size = 0;
        // die offenen enden sollen bei einer Edge 1 und bei dem center 0 betragen.
        if (isCenter)
            openends = 0;
        else
            openends = 1;
    }

    /**
     * gibt rekursiv immer das parent wieder.
     * 
     * @return die Wurzel des Baumes.
     */
    private Node findRoot() {
        if (parent == this) {
            return this;
        }

        parent = parent.findRoot();
        return parent;
    }

    /**
     * gibt die offenen Enden die in der Wurzel gespeichert sind zurück.
     * 
     * @return alle offenen Enden der Fläche
     */
    int getOpenends() {
        return findRoot().openends;
    }

    /**
     * Methode vereinigt zwei Knoten die innerhalb einer Karte liegen.
     * Beide Knoten werden auf ihr parent verkürzt
     * Wenn parent und other nicht schon vereint wurden, other wird an parent
     * angehängt.
     * 
     * @param other Knoten mit dem vereint wird.
     */
    void unionTile(Node other) {
        // Vereinigung mit other
        // other bekommt neues parent
        other = other.findRoot();
        findRoot();

        if (parent != other) {
            other.parent = parent;
            parent.openends += other.openends;
        }
    }

    /**
     * Vereint die Knoten mit den Knoten einer Nachbarkarte.
     * Beim anlegen werden die offenden Enden beider Knoten immer um 1 verrringert.
     * Beim vereinen werden alle Werte geupdated.
     * falls other schon an parent hängt wird beim setzen nur die Größe um 1 erhöht.
     * dies passiert wenn z.B. ein Kreis von Biomflächen gelegt wird.
     * 
     * @param other knoten der Nachbarkarte
     * @throws IllegalPlacementException falls die Biome inkompatibel sind.
     */
    void unionNeighbor(Node other) throws IllegalPlacementException {

        other = other.findRoot();// other wird auf die root gekürzt
        findRoot();// parent wird auf die root gekürtzt.

        if (!Board.compareEdge(biome, other.biome)) {
            throw new IllegalPlacementException("ungültige Biome Kombination!");
        }
        // die offenen enden werden um 1 verringert egal ob die Biome zueinander passen.
        parent.openends -= 1;
        other.openends -= 1;

        if (biome == other.biome) {
            if (parent != other) {
                other.parent = parent; // other an parent anhängen.
                parent.size += other.size + 1; // biomflächen addieren.
                parent.openends += other.openends; // offene enden addieren.
                parent.playeridset.addAll(other.playeridset);
            } else {
                parent.size += 1;
            }
        }
    }

    /**
     * gibt die Größe der kartenzusammenliegenden Biome wieder.
     * diese ist im parent gespeichert.
     * 
     * @return Größe der Biomfläche.
     */
    int getSize() {
        return findRoot().size;
    }

    /**
     * gibt die an der Biomfläche beteiligten Spieler wieder.
     * 
     * @return beteiligte Spieler.
     */
    Set<Integer> getPlayers() {
        return findRoot().playeridset;
    }

    /**
     * da jeder Knoten das gleiche Biome hat
     * muss nicht das Biome von parent zurückgegeben werden.
     * 
     * @return biome des Knotens.
     */
    Biome getBiome() {
        return biome;
    }

}
