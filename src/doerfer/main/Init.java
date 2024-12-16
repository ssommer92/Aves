package doerfer.main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import doerfer.preset.ArgumentParser;
import doerfer.preset.OptionalFeature;

/**
 * Diese Klasse initalisiert das Spiel
 * Erstelt den Resources Ordner und kopiert aus dem Jar File die benötigten
 * Dateien in diesen Ordner
 * 
 * @author Joshua H.
 */
public class Init {

    /**
     * Konstruktor der Klasse
     * @param args Agrumente von der Komandozeile
     */
    public Init(String[] args) {
        /* Erstellen der Dateien */
        copy("res/Settings.txt", "Settings.txt");
        copy("res/Config.txt", "Config.txt");
        copy("res/logo.png", "logo.png");
        copy("res/grey.svg", "grey.svg");
        copy("res/logo.svg", "logo.svg");
        copy("res/info.svg", "info.svg");
        copy("res/MainMusic.wav", "MainMusic.wav");
        //copy("res/sounds/StartMusic.wav", "StartMusic.wav");
        //copy("res/sounds/ClickMusic.wav", "ClickMusic.wav");
        /* Projektname */
        String name = "Aves";
        /* Projektversion */
        String projectVersion = "0.9";
        List<OptionalFeature> implementedOptionalFeatures = new ArrayList<OptionalFeature>(
                List.of(OptionalFeature.SOUNDEFFECTS, OptionalFeature.LAUNCHER));
        /* Autoren */
        List<String> projectAuthors = new ArrayList<String>(List.of("H.", "Sommer", "S."));
        /* 
         * ArgumentParser bekommt Argumente übergeben, allerdings passiert mit diesen nichts
         * Auf diese wird im säteren Spielverlauf nicht mehr zugegriffen
         */
        try {
            new ArgumentParser(args, name, projectVersion, projectAuthors, implementedOptionalFeatures);
        } catch (Exception e) {
            System.out.println("Error: Argumente konnten nicht geparsed werden.");
            System.exit(0);
        }

    }

    /**
     * Kopiert Dateien aus dem Jarfile in den Ordner "resources"
     * 
     * @param srcName  relativer Pfad zu dem File welches kopiert werden soll
     * @param destName realtiver Pfad an dem das File abgelegt werden soll im dem
     *                 resources Ordner
     */
    public void copy(String srcName, String destName) {
        ClassLoader classLoader = getClass().getClassLoader();
        /* liest das File ein, welches im Jar Verzeichnis liegt */
        InputStream is = classLoader.getResourceAsStream(srcName);
        File dir = new File("resources");
        File file = new File(dir, destName);
        try {
            /* erstellt den resources Ordner wenn noch nicht vorhanden */
            dir.mkdirs();
            /* erstellt das File */
            file.createNewFile();
            /* schreibt den Inhalt in das File */
            is.transferTo(new FileOutputStream(file));
        } catch (IOException e1) {
            System.out.println("Can not copy the File!");
            System.exit(0);
        }
    }
}
