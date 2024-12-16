package doerfer.music;

import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Klasse zur Implementierung von Musik und Soundeffekten
 * 
 * @author Lennart S.
 */
public class Music {

	/**
	 * Standardconstruktor wird nicht verwendet
	 */
	public Music() {

	}

	/**
	 * Hinzufügen der Startmusik
	 * @throws FileNotFoundException falls die Musikdatei nicht gefunden werden kann.
	 */
	public static void playStartMusic() throws FileNotFoundException {
		try {
			File sound = new File("resources/StartMusic.wav"); // Sound from Zapsplat.com
			Clip startMusic = AudioSystem.getClip();
			startMusic.open(AudioSystem.getAudioInputStream(sound));
			startMusic.start();
			startMusic.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			throw new FileNotFoundException("ERROR: Musikdatei konnte nicht geladen werden.");
		}
	}

	/**
	 * Hinzufügen der Spielmusik
	 * 
	 * @param play ob Musik gespielt werden soll oder nicht
	 * @throws FileNotFoundException falls die Musikdatei nicht gefunden werden kann.
	 */
	public static void playMainMusic(boolean play) throws FileNotFoundException {
		if (play) {
			try {
				File sound = new File("resources/MainMusic.wav");
				Clip startMusic = AudioSystem.getClip();
				startMusic.open(AudioSystem.getAudioInputStream(sound));
				startMusic.start();
				startMusic.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (Exception e) {
				throw new FileNotFoundException("ERROR: Musikdatei konnte nicht geladen werden.");
			}
		}
	}

	/**
	 * 
	 * Hinzufügen des Soundeffekts beim Drehen einer Karte
	 * @throws FileNotFoundException falls die Musikdatei nicht gefunden werden kann.
	 */
	public static void turnDtSound() throws FileNotFoundException {
		try {
			File sound = new File("resources/ClickSound.wav"); // Sound from Zapsplat.com
			Clip turnDt = AudioSystem.getClip();
			turnDt.open(AudioSystem.getAudioInputStream(sound));
			FloatControl volume = (FloatControl) turnDt.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-30);
			turnDt.start();
		} catch (Exception e) {
			throw new FileNotFoundException("ERROR: Musikdatei konnte nicht geladen werden.");
		}
	}
}
