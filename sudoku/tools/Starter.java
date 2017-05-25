package sudoku.tools;

public class Starter {

	/**
	 * Startet unter Windows eine Datei, indem das f�r den Dateityp in Windows
	 * vereinbarte Standardprogramm gerufen wird.
	 * 
	 * @param dateiPfad
	 *            Vollst�ndige Pfadangabe
	 * @throws Exception
	 */
	static public void execProgramUnterWindows(String dateiPfad) throws Exception { // if
																					// (isWindowsSystem())
		{
			String verzeichnis = Verzeichnis.gibVerzeichnis(dateiPfad);
			// verzeichnis = verzeichnis.replace('\\', '/');
			String datei = Verzeichnis.gibDateiname(dateiPfad);
			Runtime.getRuntime().exec("cmd /c start /D " + verzeichnis + " " + datei);

			//// String command = " start /D " + verzeichnis+ " " + datei;
			//// String[] command = new String[]{"cmd.exe", "/c", "start", "/D "
			//// + verzeichnis, datei};
			//
			// String[] command = new String[]{"cmd.exe", "/c", "cd", "/D " +
			//// verzeichnis};
			// ProcessBuilder pb = new ProcessBuilder(command);
			//// Process p =
			// pb.start();
			// command = new String[]{"cmd.exe", "/c", "start", datei};
			// pb = new ProcessBuilder(command);
			//// Process p =
			// pb.start();
		}
	}
}
