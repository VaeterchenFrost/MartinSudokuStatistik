package sudoku.tools;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Comparator;

import sudoku.kern.exception.Exc;

/**
 * @author heroe Tool f�r die Verzeichnis-Arbeit
 */
public class Verzeichnis {
	/**
	 * @author heroe Vergleicht die L�sungszeit die im Dateinamen angegeben ist
	 */
	private class DateiNameKomparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			int zeit1 = gibSudokuLoesungsZeit(o1);
			int zeit2 = gibSudokuLoesungsZeit(o2);
			if (zeit1 < zeit2) {
				return -1;
			} else if (zeit1 > zeit2) {
				return 1;
			}
			return 0;
		}
	}

	private class SudokuFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return name.endsWith(".sdk");
		}
	}

	/**
	 * @return F�r das laufende Programm sein "current directory"
	 */
	public static String gibAktuellesVerzeichnis() {
		File f0 = new File(".");
		String p0 = f0.getAbsolutePath();
		String pfadName = p0.substring(0, p0.length() - 1);
		return pfadName;
	}

	/**
	 * @param path
	 *            vollst�ndiger Pfad
	 * @return Dateiname ohne Verzeichnis
	 */
	static public String gibDateiname(String path) {
		int iLast = path.lastIndexOf("\\");
		String ergebnis = path.substring(iLast + 1, path.length());
		return ergebnis;
	}

	/**
	 * @param path
	 *            vollst�ndiger Pfad
	 * @return Letztes (rechtes) Unterverzeichnis
	 */
	static public String gibLetztesUnterverzeichnis(String path) {
		int iLast = path.lastIndexOf("\\");
		String str1 = path.substring(0, iLast);
		iLast = str1.lastIndexOf("\\");
		String ergebnis = str1.substring(iLast + 1, str1.length());
		return ergebnis;
	}

	/**
	 * @param name
	 *            Dateiname schon ohne Pfad
	 * @return Die L�sungszeit, die Bestandteil des Dateinamens ist
	 */
	static public int gibSudokuLoesungsZeit(String name) {
		int indexLeerzeichen = name.indexOf(" ");

		String zeitString = null;
		if (indexLeerzeichen > 0) {
			zeitString = name.substring(0, indexLeerzeichen);
		} else {
			// Zeit steht komplett vor der Datei-Erweiterung
			int indexPunkt = name.indexOf(".");
			zeitString = name.substring(0, indexPunkt);
		}

		int zeit = Integer.valueOf(zeitString);
		return zeit;
	}

	/**
	 * @param verzeichnisName
	 *            Vollst�ndiger Pfad
	 * @return Alle Dateinamen "*.sdk"
	 */
	static public String[] gibSudokuNamen(String verzeichnisName) {
		String[] dateiNamen = null;
		File dir = new File(verzeichnisName);
		dateiNamen = dir.list(new Verzeichnis().new SudokuFilter());
		return dateiNamen;
	}

	/**
	 * Garantiert das erstellte und optional leere Unterverzeichnis zum Pfad
	 * 
	 * @param unterVerzeichnis
	 * @return kompletter Pfadname des vorhandenen Unterverzeichnis wenn alles
	 *         geklappt hat
	 * @throws Exc
	 */
	public static String gibUnterverzeichnis(String pfad, String unterVerzeichnis, boolean leer) throws Exc {
		String pfadName = pfad + unterVerzeichnis;
		File f = new File(pfadName);
		if (f.exists()) {
			if (leer) {
				File[] fArray = f.listFiles();
				for (int i = 0; i < fArray.length; i++) {
					boolean r = fArray[i].delete();
					if (r == false) {
						throw Exc.verzeichnisLeeren(pfadName);
					}
				}
			}
		} else {
			boolean r = f.mkdir();
			if (r == false) {
				throw Exc.verzeichnisErstellen(pfadName);
			}
		}
		return pfadName;
	}

	/**
	 * @param path
	 *            vollst�ndiger Pfad
	 * @return Verzechnisname ohne Dateinamen
	 */
	static public String gibVerzeichnis(String path) {
		String dateiname = gibDateiname(path);
		String verzeichnis = path.substring(0, path.length() - dateiname.length());
		return verzeichnis;
	}

	static public boolean istSudoku(String dateiName) {
		SudokuFilter sudokuFilter = new Verzeichnis().new SudokuFilter();
		boolean istSudoku = sudokuFilter.accept(null, dateiName);
		return istSudoku;
	}

	static public void sortiereNachLoesungsZeit(String[] sudokuNamen) {
		java.util.Arrays.sort(sudokuNamen, new Verzeichnis().new DateiNameKomparator());
	}
}
