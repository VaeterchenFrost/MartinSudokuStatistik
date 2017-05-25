package sudoku.tools;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import sudoku.kern.exception.EndeDurchAusnahme;
import sudoku.tools.TextDatei;

public class AusnahmeBehandlung implements UncaughtExceptionHandler {
	protected static String sCR = String.format("%n"); // "\n";

	/**
	 * @param throwable
	 * @param gibAlle
	 *            false Es werden nur die Sudoku-Instanzen und eine Java-Instanz
	 *            aufgelistet. true Es werden alle StackTraceElemente
	 *            aufgelistet.
	 * @return Ein String der Zeilenvorsch�be enth�lt.
	 */
	static public String gibStackInfo(Throwable throwable, boolean gibAlle) {
		boolean warSudokuMethode = false;
		String text = new String();
		StackTraceElement[] stackInfos = throwable.getStackTrace();
		for (StackTraceElement stackInfo : stackInfos) {
			String text1 = stackInfo.toString();
			text += sCR;
			text += "     ";
			text += text1;
			boolean reicht = !gibAlle & warSudokuMethode & text1.startsWith("java");
			if (reicht) {
				break;
			}
			if (!text1.startsWith("java")) {
				warSudokuMethode = true;
			}
		}
		return text;
	}

	/**
	 * Schreibt die Ausnahme in die Datei Sudoku.log. Diese wird im aktuellen
	 * Pfad der Anwendung abgelegt.
	 * 
	 * @param ausnahme
	 * @param stackInfo
	 * @return dateiname
	 */
	static public String log(String ausnahme, String stackInfo) {
		// Pfad zum Speichern
		// File f0 = new File( ".");
		// String p0 = f0.getAbsolutePath();
		// String pfadName = p0.substring(0, p0.length()-1);
		String pfadName = Verzeichnis.gibAktuellesVerzeichnis();

		String dateiname = pfadName + "Sudoku.log";
		String text = new String(ausnahme);
		text += sCR;
		text += stackInfo;

		try {
			TextDatei.erstelle(dateiname, text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dateiname;
	}

	// =============================================================
	private UncaughtExceptionHandler uncaughtExceptionHandler;

	public AusnahmeBehandlung() {
		this.uncaughtExceptionHandler = null;
	}

	/**
	 * Diese Ausnahmebehandlung im Thread innerhalb von Thread.run() einklinken.
	 */
	public void einklinken() {
		this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		if (this.uncaughtExceptionHandler != null) {
			this.uncaughtExceptionHandler.uncaughtException(thread, throwable);
		}
		handle(throwable);
	}

	/**
	 * Die Ableitung kann die Infos zur Ausnahme anzeigen:
	 * 
	 * @param throwable
	 * @param textAusnahme
	 * @param logDateiName
	 */
	protected void zeigeAusnahme(Throwable throwable, String textAusnahme, String logDateiName) {
	}

	protected void handle(Throwable throwable) {
		if (throwable.getClass().equals(EndeDurchAusnahme.class)) {
			// EndeDurchAusnahme ist nur Transporteur der aufgetretenen Ausnahme
			while (true) {
				throwable = throwable.getCause();
				if (!throwable.getClass().equals(EndeDurchAusnahme.class)) {
					break;
				}
			}
		}

		String textAusnahme = new String(throwable.getClass().getName());
		textAusnahme += ": ";
		String botschaft = throwable.getMessage();
		if (botschaft != null) {
			textAusnahme += botschaft;
		}

		String logDateiName = log(textAusnahme, gibStackInfo(throwable, true));

		zeigeAusnahme(throwable, textAusnahme, logDateiName);

		System.exit(1);
	}

}
