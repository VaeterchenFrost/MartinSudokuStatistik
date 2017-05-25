package sudoku.bild;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heroe Dies ist die "heisse" Logik, die aus dem verr�ckten Haufen von
 *         Strichen versucht, die Sudoku-Striche herauszufinden.
 * 
 */
public class SudokuFinder {
	static private boolean istSystemOut = false;

	/**
	 * Die Anzahl der Sudoku-Spalten bzw. Zeilen
	 */
	static final public int anzahlSudokuLinien = 9;

	// Bei 7 Abst�nden k�nnte man annehmen dass 2 nicht gesehen wurden und diese
	// erg�nzbar sind.
	static final private int anzahlAbstaendeMin = anzahlSudokuLinien - 2;

	static private void systemOut(List<Abstand> abstaende, String titel) {
		if (istSystemOut) {
			System.out.print(titel + " " + abstaende.size() + " Abst�nde:");
			for (Abstand abstand : abstaende) {
				System.out.print(" " + abstand.gibAbstand());
			}
			System.out.println("");
		}
	}

	static private void systemOut(String text) {
		if (istSystemOut) {
			System.out.println(text);
		}
	}

	/**
	 * @param striche
	 *            Es wird versucht, aus der Gesamtheit dieser Striche echte
	 *            Sudoku-Striche zu erkennen. Es wird vorausgesetzt, dass die
	 *            Striche nach aufsteigendem Index aufgelistet sind.
	 * @param linienName
	 * @return Die Striche, die als Sudoku-Striche erkannt wurden. F�r jedes
	 *         Sudoku extra. null: bei zuwenigen Strichen Leeres Array falls
	 *         keine Sudoku-Striche erkannt werden konnten ansonsten stets eine
	 *         Gruppe von den 10 erkannten Sudoku-Strichen.
	 */
	static public StrichListe[] gibSudokuStriche(StrichListe striche, String linienName) {
		systemOut(String.format("%s.%s %s)", SudokuFinder.class.getName(), " gibSudokuStriche()", linienName));
		systemOut(" ----------------------------------------------------------------------");

		if (striche.isEmpty()) {
			systemOut(String.format("Kein Ergebnis. Grund: Habe keine Striche bekommen"));
			return null;
		}

		int minStrichAnzahl = anzahlAbstaendeMin + 1;
		if (striche.size() < minStrichAnzahl) {
			systemOut(String.format("Kein Ergebnis. Grund: Striche-Anzahl=%d ist zu klein (%d sind gefordert)",
					striche.size(), minStrichAnzahl));
			return null;
		}

		ArrayList<StrichListe> strichGruppen = new ArrayList<>();

		// Erkenne maximal 3 Sudokus neben- bzw. untereinander
		for (int nLauf = 1; nLauf <= 3; nLauf++) {
			systemOut(String.format("gibSudokuStriche() Lauf %d", nLauf));
			StrichListe eineStrichGruppe = gib1StrichGruppe(striche, linienName);

			if (eineStrichGruppe == null) {
				// Es werden keine Sudoku-Striche mehr erkannt!
				systemOut(String.format("gibSudokuStriche() Lauf %d ohne Erfolg", nLauf));

				break;
			} else {
				strichGruppen.add(0, eineStrichGruppe);
				systemOut(String.format("gibSudokuStriche() Lauf %d brachte ein Sudoku", nLauf));

				// Die ausgewerteten Striche von der weiteren Sucherei
				// ausschliessen
				int linkerStrichIndex = striche.indexOf(eineStrichGruppe.get(0));
				striche.loescheAlleAb(linkerStrichIndex);
			}
		}

		StrichListe[] arrayDerSudokuStriche = null;
		if (!strichGruppen.isEmpty()) {
			arrayDerSudokuStriche = new StrichListe[strichGruppen.size()];
			strichGruppen.toArray(arrayDerSudokuStriche);
			systemOut(String.format("gibSudokuStriche() %s: Es wurden %d Sudoku-Striche-Gruppen gefunden:", linienName,
					arrayDerSudokuStriche.length));
			for (int i = 0; i < arrayDerSudokuStriche.length; i++) {
				StrichListe strichListe = arrayDerSudokuStriche[i];
				strichListe.systemOut(istSystemOut, String.format("gib1StrichGruppe() Sudoku[%d] ", i), linienName);
			}
		} else {
			systemOut(String.format("gibSudokuStriche() %s: Es wurden Keine Sudoku-Striche-Gruppen gefunden",
					linienName));
		}

		if ((arrayDerSudokuStriche == null)) {
			systemOut(String.format("%s %s: Tut uns leid. Hatte nur %d Striche zur Verf�gung", "findeSudokuStriche() ",
					linienName, striche.size()));
		}

		return arrayDerSudokuStriche;
	}

	static private boolean istImBereich(int wert, int min, int max) {
		if (wert < min) {
			return false;
		}
		if (wert > max) {
			return false;
		}
		return true;
	}

	/**
	 * 0. Klappt es mit dem n�chsten Abstand bei doppelter Toleranz? 1.
	 * Vielleicht klappt es mit der Summe noch des n�chsten Abstandes weil ein
	 * Strich sich eingeschmuggelt hat, 2. Oder dieser Abstand besitzt die
	 * doppelte Breite weil ein Strich fehlt.
	 * 
	 * @param guteAbstaende
	 * @param alleAbstaende
	 * @param iteratorZu0
	 *            Der aktuell zu haltende Iterator in alleAbstaende Richtung
	 *            Index0
	 * @param abstandToleranzProzent
	 *            Toleranz der Abst�nde in Prozent des Durchschnitts der guten
	 *            Abst�nde.
	 * @return erkannte Sudoku-Strich-Gruppe oder null
	 * 
	 */
	static private StrichListe gibAusWenigenAbstaenden(List<Abstand> guteAbstaende, List<Abstand> alleAbstaende,
			int abstandToleranzProzent) {
		int guteSummeDurchschnitt = 0;
		for (Abstand abstand : guteAbstaende) {
			guteSummeDurchschnitt += abstand.gibAbstand();
		}
		guteSummeDurchschnitt /= guteAbstaende.size();
		int toleranzAbsolut = (guteSummeDurchschnitt * abstandToleranzProzent) / 100;
		int abstandMin = guteSummeDurchschnitt - toleranzAbsolut;
		int abstandMax = guteSummeDurchschnitt + toleranzAbsolut;

		systemOut(String.format("gibAusWenigenAbstaenden() mit %d Start-Abst�nden, Durchschnitt=%d (%d bis %d):",
				guteAbstaende.size(), guteSummeDurchschnitt, abstandMin, abstandMax));
		systemOut("----------------------------------------------------------------------------");

		// Der Iterator Richtunf Index 0
		int iteratorIndex0 = alleAbstaende.indexOf(guteAbstaende.get(0));
		AbstandIterator iZu0 = new AbstandIterator(alleAbstaende, true, iteratorIndex0);

		// Vorliegenden Abstand bzw. Abstaende Absuchen
		while (iZu0.hasNext()) {
			if (guteAbstaende.size() == anzahlSudokuLinien) {
				break;
			}
			Abstand testAbstand = iZu0.next();
			if (istImBereich(testAbstand.gibAbstand(), abstandMin, abstandMax)) {
				guteAbstaende.add(0, testAbstand);
			} else {
				// Ist doppelter Abstand?
				if (istImBereich(testAbstand.gibAbstand(), 2 * abstandMin, 2 * abstandMax)) {
					// Den fehlenden Abstand reinsetzen
					List<Abstand> ersatzAbstaende = Abstand.gibErsatzAbstaende(testAbstand);
					guteAbstaende.add(0, ersatzAbstaende.get(1));
					guteAbstaende.add(0, ersatzAbstaende.get(0));
				} else {
					// Vielleicht klappt es mit der Summe der n�chsten Abst�nde
					// weil Striche sich eingeschmuggelt haben
					int testIndex2 = testAbstand.strich1.von.gibVonIndex();
					while (iZu0.hasNext()) {
						Abstand vorigerAbstand = iZu0.next();
						int testIndex1 = vorigerAbstand.strich2.nach.gibVonIndex() + 1;
						int testAbstandBreite = testIndex2 - testIndex1 + 1;
						if (istImBereich(testAbstandBreite, abstandMin, abstandMax)) {
							Abstand ersatzAbstand = Abstand.gibErsatzAbstand(vorigerAbstand, testAbstand);
							guteAbstaende.add(0, ersatzAbstand);
							break;
						}
					}
				}
			}
		} // while (iZu0.hasNext())

		// Der Iterator Richtung Ende
		int iteratorIndexEnde = alleAbstaende.indexOf(guteAbstaende.get(guteAbstaende.size() - 1));
		AbstandIterator iZumEnde = new AbstandIterator(alleAbstaende, false, iteratorIndexEnde);

		// Nachfolgenden Abstand bzw. Abstaende Absuchen
		while (iZumEnde.hasNext()) {
			if (guteAbstaende.size() == anzahlSudokuLinien) {
				break;
			}
			Abstand testAbstand = iZumEnde.next();
			if (istImBereich(testAbstand.gibAbstand(), abstandMin, abstandMax)) {
				guteAbstaende.add(testAbstand);
			} else {
				// Ist doppelter Abstand?
				if (istImBereich(testAbstand.gibAbstand(), abstandMin, abstandMax)) {
					// Den fehlenden Abstand reinsetzen
					List<Abstand> ersatzAbstaende = Abstand.gibErsatzAbstaende(testAbstand);
					guteAbstaende.add(ersatzAbstaende.get(0));
					guteAbstaende.add(ersatzAbstaende.get(1));
				} else {
					// Vielleicht klappt es mit der Summe der n�chsten Abst�nde
					// weil Striche sich eingeschmuggelt haben
					int testIndex1 = testAbstand.strich2.nach.gibVonIndex() + 1;
					while (iZumEnde.hasNext()) {
						Abstand naechsterAbstand = iZumEnde.next();
						int testIndex2 = naechsterAbstand.strich2.von.gibVonIndex();
						int testAbstandBreite = testIndex2 - testIndex1 + 1;
						if (istImBereich(testAbstandBreite, abstandMin, abstandMax)) {
							Abstand ersatzAbstand = Abstand.gibErsatzAbstand(naechsterAbstand, testAbstand);
							guteAbstaende.add(ersatzAbstand);
							break;
						}
					}
				}
			}
		} // while (iZumEnde.hasNext())

		if (guteAbstaende.size() == anzahlSudokuLinien) {
			StrichListe strichListe = Abstand.gibStrichListe(guteAbstaende);
			return strichListe;
		} else {
			return null;
		}
	}

	static private List<Abstand> gibGuteAbstaende(List<Abstand> abstaende, int nGuteMin, int abstandMin,
			int abstandMax) {
		// Der Iterator Richtung Anfang: Sitzt jetzt auf dem Ende
		AbstandIterator iZu0 = new AbstandIterator(abstaende, true);

		// Mehrere gute Abst�nde direkt nebeneinander suchen ab iZu0.
		List<Abstand> guteAbstaende = new ArrayList<Abstand>();
		while (iZu0.hasNext()) {
			Abstand naechster = iZu0.next();
			int abstandsWert = naechster.gibAbstand();
			boolean istGuterAbstand = istImBereich(abstandsWert, abstandMin, abstandMax);
			if (istGuterAbstand) {
				// Guter Abstand ist gefunden: Weiter so.
				guteAbstaende.add(0, naechster);
			} else {
				// Es geht ab hier also nicht mehr weiter mit guten Abst�nden
				if (guteAbstaende.size() < nGuteMin) {
					// Erneut weiterversuchen
					guteAbstaende.clear();
				} else {
					// Dann ist der Auftrag auch erf�llt
					break;
				}
			} // if(istGuterAbstand)
		} // while()
		return guteAbstaende;
	}

	/**
	 * @param striche
	 *            Die erkannten Striche
	 * @return Die 10 Striche eines Sudoku (vom Ende der Strichliste an gesucht)
	 *         oder null wenn keine Sudoku-Striche erkannt wurden
	 */
	static private StrichListe gib1StrichGruppe(StrichListe striche, String linienName) {
		// Abst�nde zwischen je zwei benachbarten Strichen ermitteln
		List<Abstand> abstaende = Abstand.gibAbstaende(striche);

		if (abstaende == null) {
			systemOut(String.format(
					"gib1StrichGruppe() %s: ------------------------------------ Keine Abst�nde: %d Striche sind zuwenig (%d sind n�tig)",
					linienName, striche.size(), anzahlAbstaendeMin));
			return null;
		}

		// Solange ausreichend unbewertete Abst�nde verf�gbar sind
		if (abstaende.size() < anzahlAbstaendeMin) {
			systemOut(String.format(
					"gib1StrichGruppe() %s: ------------------------------------ %d Striche sind zuwenig (%d sind n�tig)",
					linienName, striche.size(), anzahlAbstaendeMin));
			return null;
		}
		// Toleranz der Abst�nde in Prozent des Durchschnitts der guten
		// Abst�nde.
		int startToleranzProzent = 5;

		for (int nToleranzLauf = 1; nToleranzLauf < 5; nToleranzLauf++) {
			// Toleranz von Lauf zu Lauf aufweichen bis 20 %
			int toleranzProzent = startToleranzProzent * nToleranzLauf;
			systemOut(String.format("gib1StrichGruppe() %s: ------------------------------------ Starte Lauf %d",
					linienName, nToleranzLauf));

			// Den am h�ufigsten auftretenden Abstand ermitteln
			int[] abstandsWerte = Abstand.gibAbstaende(abstaende);
			WerteGruppe haeufigsterAbstand = WerteGruppe.gibHaeufigstenWert(abstandsWerte, toleranzProzent);

			// Erlaubten Absolut-Bereich des Abstandes der Sudoku-Striche
			// ermitteln
			int abstandMin = haeufigsterAbstand.gibMinErlaubt();
			int abstandMax = haeufigsterAbstand.gibMaxErlaubt();
			systemOut(String.format("gib1StrichGruppe() %s mit %d%% Toleranz (von %d bis %d) aus %d Abst�nden",
					linienName, toleranzProzent, abstandMin, abstandMax, abstaende.size()));
			systemOut(abstaende, "");
			systemOut("----------------------------------------------------------------------------");

			// Gute Abst�nde suchen
			int nGuteMin = 2; // *toleranzProzent;
			List<Abstand> guteAbstaende = gibGuteAbstaende(abstaende, nGuteMin, abstandMin, abstandMax);

			// Die guten Abst�nde bewerten
			int nGuteAbstaende = guteAbstaende.size();
			systemOut(guteAbstaende, "Gute Abst�nde");
			StrichListe sudokuStrichListe = null;
			if (nGuteAbstaende == anzahlSudokuLinien) {
				// gefundene Sudoku-Striche-Gruppe vermerken
				sudokuStrichListe = Abstand.gibStrichListe(guteAbstaende);
				systemOut(guteAbstaende, "Sudoku-StrichListe direkt aus Guten Abst�nden erstellt:");
			} else {
				if (nGuteAbstaende >= nGuteMin) {
					// In jede Richtung weiterw�hlen bis zum Erfolg:
					// Mit den n�chsten Abst�nden und doppelter Toleranz:
					systemOut(guteAbstaende, "Lasse aus den wenigen Guten Abst�nden weiterw�hlen:");
					sudokuStrichListe = gibAusWenigenAbstaenden(guteAbstaende, abstaende, toleranzProzent);
				}
			}

			if (sudokuStrichListe != null) {
				systemOut("gib1StrichGruppe(): Erfolg!!!");
				return sudokuStrichListe;
			}
		} // for (int nToleranzLauf

		systemOut("gib1StrichGruppe(): Nichts gefunden b��hhh!!!");
		return null;
	}

}
