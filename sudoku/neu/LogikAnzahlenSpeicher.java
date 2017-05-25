package sudoku.neu;

import java.io.File;
import java.io.IOException;

import sudoku.logik.Logik_ID;
import sudoku.schwer.daten.LogikAnzahlen;
import sudoku.tools.TextDatei;
import sudoku.tools.Verzeichnis;

class LogikAnzahlenSpeicher {

	static private String gibLogText(LogikAnzahlen logikAnzahlen) {
		final String sCR = String.format("%n");
		Logik_ID[] logiken = logikAnzahlen.gibLogiken();
		String s = "";

		for (int iLogik = 0; iLogik < logiken.length; iLogik++) {
			Logik_ID logik = logiken[iLogik];
			int n = logikAnzahlen.gibAnzahl(logik);

			s += " �" + n + "=" + logik.name() + "$ " + sCR;
		}
		return s;
	}

	static private LogikAnzahlen gibLogiken(String text) {
		LogikAnzahlen logikAnzahlen = new LogikAnzahlen();

		while (text.length() > 3) {
			text = text.trim();
			int indexStart = text.indexOf("�");
			if (indexStart < 0) {
				break;
			}
			text = text.substring(indexStart + 1);
			int indexGleich = text.indexOf("=");
			if (indexGleich < 0) {
				break;
			}
			String sAnzahl = text.substring(0, indexGleich);
			text = text.substring(indexGleich + 1);
			int indexEnde = text.indexOf("$");
			if (indexEnde < 0) {
				break;
			}
			String sLogik = text.substring(0, indexEnde);
			text = text.substring(indexEnde + 1);

			// Wenn eine Logik rausgeschmissen wurde in einer neuen
			// Programmversion kann LogikID.valueOf() keine ID liefern
			// und meldet dies per Exception!
			try {
				Logik_ID logik = Logik_ID.valueOf(sLogik);
				int anzahl = Integer.valueOf(sAnzahl);
				logikAnzahlen.add(logik, anzahl);
			} catch (IllegalArgumentException e) {
			}
		}
		return logikAnzahlen;
	}

	static private LogikAnzahlen gibSumme(LogikAnzahlen basis, LogikAnzahlen neue) {
		final int maxAnzahl = 1000000000;
		Logik_ID[] neueArray = neue.gibLogiken();

		for (int iLogik = 0; iLogik < neueArray.length; iLogik++) {
			Logik_ID logik = neueArray[iLogik];
			int neueAnzahl = neue.gibAnzahl(logik);

			int basisAnzahl = basis.gibAnzahl(logik);
			if (basisAnzahl < maxAnzahl) {
				basis.add(logik, neueAnzahl);
			}
		}

		return basis;
	}

	static void logLogikAnzahlen(LogikAnzahlen logikAnzahlen) {
		final String dateiName = "Logiken.log";
		final String dateiNameBak = "Logiken.bak";

		// Pfad zum Speichern
		// File f0 = new File(".");
		// String p0 = f0.getAbsolutePath();
		// String pfadName = p0.substring(0, p0.length() - 1);
		String pfadName = Verzeichnis.gibAktuellesVerzeichnis();

		// Bak-Datei l�schen
		String fileNameBak = pfadName + dateiNameBak;
		File fileBak = new File(fileNameBak);
		fileBak.delete();

		// Alte Datei laden und umbenennen
		LogikAnzahlen alteLogikAnzahlen = new LogikAnzahlen();
		String fileName = pfadName + dateiName;
		File logFile = new File(fileName);

		if (logFile.exists()) {
			String sAlteLogikAnzahlen = "";
			try {
				sAlteLogikAnzahlen = TextDatei.lese1String(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}

			alteLogikAnzahlen.add(gibLogiken(sAlteLogikAnzahlen));
			logFile.renameTo(fileBak);
		}

		LogikAnzahlen summeLogikAnzahlen = gibSumme(alteLogikAnzahlen, logikAnzahlen);
		String logText = gibLogText(summeLogikAnzahlen);
		try {
			// System.out.println("logErfolgreicheLogiken in " + fileName);
			TextDatei.erstelle(fileName, logText);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
