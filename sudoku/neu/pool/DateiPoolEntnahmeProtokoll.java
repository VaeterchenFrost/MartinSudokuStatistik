package sudoku.neu.pool;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import sudoku.kern.exception.Exc;
import sudoku.kern.info.InfoSudoku;
import sudoku.logik.Schwierigkeit;
import sudoku.schwer.daten.AnalysatorKlare;
import sudoku.tools.Verzeichnis;

/**
 * @author heroe Verwaltet das Protokoll der aus dem DateiPool (zuletzt)
 *         entnommenen Sudokus
 */
public class DateiPoolEntnahmeProtokoll {
	/**
	 * Name des Unterverzeichnisses im DateiPool
	 */
	private static String unterVerzeichnisName = "Entnahme";
	/**
	 * Gr��te Anzahl der Sudokus im Speicher
	 */
	private static int nMax = 200;
	/**
	 * Format der Protokoll-Zeit im Dateinamen
	 */
	private static String protokollZeitFormat = "yyyy-MM-dd HH.mm";
	/**
	 * Kompletter Verzeichnisnahme des Protokoll-Verzeichnisses
	 */
	private static String pfadName = null;

	/**
	 * @param wieSchwer
	 * @param loesungsZeit
	 *            in Minuten
	 * @return Vollst�ndiger Pfad
	 */
	private static String gibDateiName(Schwierigkeit wieSchwer, int loesungsZeit) {
		String wieSchwerName = Schwierigkeit.gibName(wieSchwer);
		int zeitGerastert = AnalysatorKlare.gibAnzeigeZeit(loesungsZeit * 60, true);
		String protokollZeitString = gibProtokollZeitString();
		String zielDateiName = String.format("%s%s %s %3d", pfadName, protokollZeitString, wieSchwerName,
				zeitGerastert);
		String zielDateiPfad = gibDateiNameFrei(zielDateiName, InfoSudoku.dateiErweiterung);
		return zielDateiPfad;
	}

	/**
	 * @param name
	 *            Pfadname ohne Dateierweiterung
	 * @param erweiterung
	 *            Dateierweiterung
	 * @return Name einer noch nicht existierenden Datei
	 */
	private static String gibDateiNameFrei(String name, String erweiterung) {
		{
			File f = new File(name + erweiterung);
			if (!f.exists()) {
				return name + erweiterung;
			}
		}
		for (int i = 1;; i++) {
			String pfad = name + "-" + Integer.toUnsignedString(i) + erweiterung;
			File f = new File(pfad);
			if (!f.exists()) {
				return pfad;
			}
		}
	}

	/**
	 * @param wieSchwer
	 * @param loesungsZeit
	 * @return null wenn keine der geforderten Sudokus protokolliert sind.
	 *         Ansonsten das Array. Auf Index 0 steht das zuletzt entnommene
	 *         Sudoku
	 */
	public static InfoSudoku[] gibEntnommene(Schwierigkeit wieSchwer, Integer loesungsZeit) {
		File dir = new File(pfadName);
		File[] fArray = dir.listFiles();
		// Sortieren aufsteigend: Auf Index=0 �lteste Entnahme
		Arrays.sort(fArray);
		if (fArray.length == 0) {
			return null;
		}

		// Die gesuchten ausfiltern
		ArrayList<String> gesuchte = new ArrayList<>();
		for (int i = fArray.length - 1; i >= 0; i--) {
			File f = fArray[i];
			if (istGesucht(f, wieSchwer, loesungsZeit)) {
				gesuchte.add(f.getAbsolutePath());
			}
		}
		if (gesuchte.isEmpty()) {
			return null;
		}

		// Die Infosudokus aus den Dateien erstellen
		ArrayList<InfoSudoku> sudokuList = new ArrayList<>(gesuchte.size());
		for (int i = 0; i < gesuchte.size(); i++) {
			String pfadName = gesuchte.get(i);
			try {
				InfoSudoku infoSudoku = InfoSudoku.lade(pfadName);
				// String titel1 = Schwierigkeit.gibName(wieSchwer);
				// String titel2 = "";
				// if (loesungsZeit != null){
				// titel2 = Integer.toUnsignedString(loesungsZeit);
				// }
				// infoSudoku.setzeTitel(titel1, titel2);

				LocalDateTime protokollZeit = gibProtokollZeit(pfadName);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd.MM.yyyy HH:mm");
				String titel = protokollZeit.format(formatter);
				infoSudoku.setzeTitel(titel);

				sudokuList.add(infoSudoku);
			} catch (IOException | Exc e) {
				e.printStackTrace();
			}
		}

		InfoSudoku sudokus[] = new InfoSudoku[sudokuList.size()];
		sudokuList.toArray(sudokus);

		// System.out.println("DateiPoolEntnahmeProtokoll.gibEntnommene()");
		// for (int i = 0; i < sudokus.length; i++) {
		// InfoSudoku infoSudoku = sudokus[i];
		// System.out.println(infoSudoku.gibTitel1()); // + " " +
		// infoSudoku.gibTitel2());
		// }
		return sudokus;
	}

	static int gibLoesungsZeit(String pfadName) {
		int zeitIndex = pfadName.lastIndexOf(" ") + 1;
		String zeitString = pfadName.substring(zeitIndex);

		int bindeStrichIndex = zeitString.indexOf("-");
		if (bindeStrichIndex > 0) {
			zeitString = zeitString.substring(0, bindeStrichIndex);
		} else {
			zeitString = zeitString.substring(0, zeitString.indexOf("."));
		}

		zeitString = zeitString.trim();
		int loesungsZeit = Integer.parseUnsignedInt(zeitString);
		return loesungsZeit;
	}

	/**
	 * @return Vollst�ndiger Pfad
	 */
	static String gibPfadName() {
		return pfadName;
	}

	/**
	 * @param dateiName
	 *            vollst�ndiger Pfad
	 * @return Die im dateiNamen dokumentierte Zeit der �bernahme des Sudoku in
	 *         das Entnahmeprotokoll
	 */
	private static LocalDateTime gibProtokollZeit(String dateiName) {
		int indexUnterVerzeichnis = dateiName.indexOf(unterVerzeichnisName);
		int indexProtokollZeit = indexUnterVerzeichnis + unterVerzeichnisName.length() + 1;
		String zeitString = dateiName.substring(indexProtokollZeit, indexProtokollZeit + protokollZeitFormat.length());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(protokollZeitFormat);
		LocalDateTime protokollZeit = LocalDateTime.parse(zeitString, formatter);
		return protokollZeit;
	}

	private static String gibProtokollZeitString() {
		LocalDateTime now = LocalDateTime.now();

		DateTimeFormatter df = DateTimeFormatter.ofPattern(protokollZeitFormat);
		String zeitString = now.format(df);

		zeitString = String.format("%s Uhr", zeitString);
		return zeitString;
	}

	/**
	 * @param typ
	 * @return Alle protokollierten Zeiten diesen Typs oder null falls keine
	 *         Sudokus diesen Typs vorliegen
	 */
	public static Integer[] gibZeiten(Schwierigkeit wieSchwer) {
		File dir = new File(pfadName);
		File[] fArray = dir.listFiles();
		if (fArray.length == 0) {
			return null;
		}

		ArrayList<Integer> zeitenListe = new ArrayList<>();
		for (int i = 0; i < fArray.length; i++) {
			File f = fArray[i];
			if (istGesucht(f, wieSchwer, null)) {
				Integer zeit = gibLoesungsZeit(f.getAbsolutePath());
				if (!zeitenListe.contains(zeit)) {
					zeitenListe.add(zeit);
				}
			}
		}
		Integer zeitenArray[] = new Integer[zeitenListe.size()];
		zeitenListe.toArray(zeitenArray);
		Arrays.sort(zeitenArray);
		return zeitenArray;
	}

	/**
	 * @param f
	 *            Datei
	 * @param wieSchwer
	 *            Das Sudoku der Datei soll diese Schwierigkeit besitzen
	 * @param loesungsZeit
	 *            Das Sudoku der Datei soll diese L�sungszeit besitzen. Falls
	 *            null �bergeben wird, bleibt die L�sungszeit unber�cksichtigt.
	 * @return
	 */
	static boolean istGesucht(File f, Schwierigkeit wieSchwer, Integer loesungsZeit) {
		String name = f.getAbsolutePath();
		String wieSchwerString = Schwierigkeit.gibName(wieSchwer);
		int schwerIndex = name.indexOf(wieSchwerString);

		if (schwerIndex < 0) {
			return false; // NIcht die gesuchte Schwierigkeit
		}

		if (loesungsZeit == null) {
			return true; // L�sungszeit soll egal sein
		}

		int fLoesungsZeit = gibLoesungsZeit(name);
		boolean ret = fLoesungsZeit == loesungsZeit;
		return ret;
	}

	static void kontrolliereAnzahl() {
		File f = new File(pfadName);
		File[] fArray = f.listFiles();
		// Sortieren aufsteigend: Auf Index=0 �lteste Entnahme
		Arrays.sort(fArray);
		if (fArray.length > nMax) {
			int loeschMax = fArray.length - nMax;
			for (int i = 0; i < loeschMax; i++) {
				fArray[i].delete();
			}
		}
	}

	/**
	 * Verschiebt die Datei des Sudoku in das EntnahmeProtokoll-Verzeichnis
	 * 
	 * @param wieSchwer
	 * @param quellDatei
	 * @return true wenn die Datei verschoben wurde in das
	 *         EntnahmeProtokoll-Verzeichnis
	 */
	static boolean protokolliere(Schwierigkeit wieSchwer, int loesungsZeit, File quellDatei) {
		String zielDateiName = gibDateiName(wieSchwer, loesungsZeit);
		File zielFile = new File(zielDateiName);
		boolean verschoben = quellDatei.renameTo(zielFile);
		return verschoben;
	}

	// ------------------------------------------------------------------------------------------------
	static void sichereVerzeichnis(String poolPfadName) throws Exc {
		pfadName = Verzeichnis.gibUnterverzeichnis(poolPfadName, unterVerzeichnisName, false) + "\\";
	}

}
