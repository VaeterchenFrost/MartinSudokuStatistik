package sudoku.logik;

/**
 * @author heroe Die L�sungs-Strategien. Sie werden in genau der hier
 *         definierten Reihenfolge abgearbeitet. Diese Reihenfolge hier sollte
 *         genau der Anzahl der erfolgreichen Logiken entsprechen (siehe Datei
 *         "Logiken.txt" im Sudoku-Verzeichnis). Bei �nderungen sind (eventuell)
 *         erforderliche Arbeitsschritte: - Erstellung einer neuen Logik-Klasse
 *         - SudokuLogik: die zwei Logiken-Listen anpassen -
 *         Schwierigkeit.gibSchwierigkeitKlugheiten()
 */
public enum Logik_ID { // gruppen zahlen
	ORTFEST1, // 1 1
	ORTFEST2, // 1 2
	KASTEN1, // 2 1
	TEILMENGEFEST2, // 1 2
	KASTEN2, // 3 1
	// Die Logik KastenLinie hat, wie sich zeigte die identische Wirkung wie die
	// Logik Kasten2.
	// Anders gesagt: Sie kommt nie zur Wirkung wenn Logik Kasten2 eingeschalten
	// ist.
	// Das kam mir zwar nicht im Gedanken, aber sehr sch�n beim Ansehen von
	// entsprechenden Tips.
	// Deshalb wird diese Logik nicht eingeordnet!
	// KASTENLINIE,
	FELD, // Feld 1
	KREUZFLUEGEL, // 4 1
	// Die beiden Logiken mit 4 Feldern haben nie zugeschlagen. Deshalb sind sie
	// rausgenommen.
	// ORTFEST4, // 1 4
	// TEILMENGEFEST4 // 1 4
	AUSWIRKUNGSKETTE, // viele paarweise
	SCHWERTFISCH, // 6 1
	// Die beiden Logiken mit 3 Feldern schlagen nur ganz selten mal zu, O3
	// eigentlich garnicht.
	ORTFEST3, // 1 3
	TEILMENGEFEST3, // 1 3
	;

	/**
	 * @return Die leichteste Logik
	 */
	public static Logik_ID gibMinimum() {
		Logik_ID[] logikArray = Logik_ID.values();
		Logik_ID minimum = logikArray[0];

		return minimum;
	}

	// /**
	// * @return Die leichteste Logik
	// */
	// public static Logik gibMaximum(){
	// Logik[] logikArray = Logik.values();
	// Logik logik = logikArray[logikArray.length -1];
	//
	// return logik;
	// }
}
