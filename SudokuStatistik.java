import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.EnumMap;

import sudoku.bedienung.SudokuBedienung;
import sudoku.kern.exception.Exc;
import sudoku.kern.info.InfoSudoku;
import sudoku.logik.Schwierigkeit;
import sudoku.neu.GeneratorStatistik;
import sudoku.neu.NeuTyp;
import sudoku.neu.SudokuPool;
import sudoku.neu.pool.PoolInfo;
import sudoku.neu.pool.TopfInfo;

public class SudokuStatistik implements GeneratorStatistik {

	/*
	 * f: Beschreibbarer File
	 * 
	 * line: positiv, Aus Sicherheitsgründen auf 10.000 beschränkt
	 * 
	 */
	private static boolean incrementOnLine(RandomAccessFile f, final int line)
			throws IOException, NumberFormatException {
		final int max_lines = 10000;
		final String ws = "          ";
		if (line < 1) {
			throw new IllegalArgumentException("Line number starts at 1.");
		}
		if (line > max_lines) {
			throw new IllegalArgumentException("Line number for safety purposes smaller " + max_lines);
		}
		// ----------------------Start:
		f.seek(0);

		long pos_prevline, pos_currline, currline;
		String newline = ws + System.getProperty("line.separator");

		// DEBUG:
		System.out.println("SudokuStatistik.incrementOnLine schreibe in " + line);
		// Gehe zu benötigter Line, zählend bei 1
		// Erstelle erste Zeile
		if (f.length() < newline.length()) {
			f.writeBytes(newline);
		}
		// reset
		f.seek(0);
		pos_currline = 0;
		pos_prevline = 0;
		currline = 0;
		// Prüfe ob Zeile existiert:
		while (currline < line && f.readLine() != null) {
			// System.out.println("readline" + f.getFilePointer());
			currline++;
			pos_prevline = pos_currline;
			pos_currline = f.getFilePointer();
			/* keep reading */ }
		while (currline < line) {
			// System.out.println("writeline" + currline);
			f.writeBytes(newline);
			currline++;
			pos_prevline = pos_currline;
			pos_currline = f.getFilePointer();
		}

		// Lese alten Inhalt:
		Integer oldnum = 0;
		f.seek(pos_prevline);
		String oldline = f.readLine();
		oldline = oldline.trim();
		if (oldline.length() > 0) {
			oldnum = Integer.decode(oldline);
			// System.out.println("num=" + oldline + "dec=" + oldnum);
		}
		// Überschreibe alten Inhalt:
		f.seek(pos_prevline);
		f.writeBytes(Integer.toString(oldnum + 1));

		return true;
	}

	static public void main(String args[]) throws Exc {
		SudokuStatistik statistik = new SudokuStatistik();
		SudokuPool.setzeGeneratorStatistik(statistik);
		SudokuBedienung sudokuBedienung = new SudokuBedienung(null);

		// ==Auslesen Fuellstand============
		PoolInfo pinfo = sudokuBedienung.gibSudokuPoolInfo();
		EnumMap<Schwierigkeit, TopfInfo> verfuegbare = pinfo.verfuegbare;
		Collection<TopfInfo> values = verfuegbare.values();
		TopfInfo summe = PoolInfo.gibSumme(values);

		System.out.println("Gesamtzahl Sudokus aller Toepfe: " + summe.gibAnzahl());
		// ===================================
		while (true) {
		}
	}

	static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

	static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	// ===================================================
	public SudokuStatistik() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sudoku.neu.GeneratorStatistik#neuesSudoku(sudoku.neu.NeuTyp,
	 * sudoku.kern.info.InfoSudoku, java.lang.Boolean, int) Alles basiert auf
	 * NICHT NULL von infoSudoku
	 */
	@Override
	public void neuesSudoku(final NeuTyp forderung, final InfoSudoku infoSudoku, final NeuTyp neuTyp,
			final int laufNummer, final Boolean istErstesDerLoesungsZeit, final int loesungsZeit,
			final String topfName) {
		// Wenn null -> return ohne Funktionalität.
		if (infoSudoku == null || forderung == null || neuTyp == null) {
			return;
		}
		if (loesungsZeit < 1) {
			return;
		}
		// ---------------------------------
		String dname = neuTyp.gibName();

		try (RandomAccessFile f = new RandomAccessFile(String.format("%s%s%s", topfName, dname, ".txt"), "rws");) {

			String sSudoku = forderung.gibName().equals(dname) ? "-ok-" : "-> " + dname.toUpperCase() + "";

			String sGespeichert = "nicht gespeichert";
			if (istErstesDerLoesungsZeit != null) {
				if (istErstesDerLoesungsZeit) {
					sGespeichert = "gespeichert als 1.";
				} else {
					sGespeichert = "gespeichert als 2.";
				}
			}

			DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss O");
			String zeitString = ZonedDateTime.now().format(df); // ZonedDateTime
																// fuer
																// time-zone
																// information
																// (offsets)
			// Log Console:
			System.out.println("(" + zeitString + ") Hallo SudokuStatistik :: Forderung="
					+ padRight(forderung.gibName(), 7) + " " + padLeft(sSudoku, 10) + " ("
					+ padLeft("x" + laufNummer, 3) + ")" + ": " + sGespeichert + " " + new Integer(loesungsZeit));
			// Log Datei:
			System.out.println("Logge t=" + loesungsZeit + " in " + topfName);
			incrementOnLine(f, loesungsZeit);

		} catch (Exception e) { // ========Exceptions========
			try {
				// TODO: handle exception
				System.err.println("Error occured while trying to log: " + neuTyp + ": " + new Integer(loesungsZeit));
				System.err.println("To " + topfName);
			} catch (Exception e2) {
				// TODO: handle output-exception
			}
			e.printStackTrace();
		}
	}
}
