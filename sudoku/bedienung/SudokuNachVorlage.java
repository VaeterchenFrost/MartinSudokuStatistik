package sudoku.bedienung;

import sudoku.Paar;
import sudoku.kern.EintragsEbenen;
import sudoku.kern.exception.Exc;
import sudoku.kern.info.InfoSudoku;
import sudoku.kern.protokoll.Protokoll;
import sudoku.langerprozess.FortschrittAnzeige;
import sudoku.langerprozess.FortschrittZeiger;
import sudoku.langerprozess.LangerProzess;
import sudoku.langerprozess.ProcessOrganisator;
import sudoku.logik.SudokuLogik;
import sudoku.neu.NeuTyp;
import sudoku.neu.SudokuPool;
import sudoku.neu.pool.NeuTypOption;
import sudoku.varianz.Loesungen;
import sudoku.varianz.Varianz;

/**
 * @author heroe Neues Sudoku nach einer Vorlage erstellen. Das kann nat�rlich
 *         eventuell dauern... Deshalb l�uft die Sudoku-Erstellung im Thread mit
 *         FortschrittAnzeige
 */
class SudokuNachVorlage implements LangerProzess {
	private static boolean istSystemOut = false;
	private static int ersterLauf = 1;
	private static int letzterLauf = 100;

	/**
	 * @param vorlage
	 *            != null und beinhaltet seinen namen als titel1
	 * @param sudokuPool
	 *            != null
	 * @param fortschrittAnzeige
	 *            != null
	 * @return auch null, ansonsten das Sudoku mit dem Namen der Vorlage im
	 *         titel1
	 */
	static InfoSudoku gibNeues(InfoSudoku vorlage, SudokuPool sudokuPool, FortschrittAnzeige fortschrittAnzeige) {
		SudokuNachVorlage prozess = new SudokuNachVorlage(vorlage, sudokuPool);
		ProcessOrganisator.laufenLassen(prozess, fortschrittAnzeige);

		InfoSudoku sudoku = prozess.gibSudoku();
		return sudoku;
	}

	private static void systemOut(String text) {
		if (istSystemOut) {
			System.out.println(text);
		}
	}

	// =============================================================
	private InfoSudoku vorlage;
	private SudokuPool sudokuPool;

	private InfoSudoku bestesSudoku = null;
	private int besteVarianz = 999;

	SudokuNachVorlage(InfoSudoku vorlage, SudokuPool sudokuPool) {
		this.vorlage = vorlage;
		this.sudokuPool = sudokuPool;
	}

	@Override
	public Paar<Integer, Integer> gibFortschrittBereich() {
		return new Paar<Integer, Integer>(ersterLauf, letzterLauf);
	}

	InfoSudoku gibSudoku() {
		return bestesSudoku;
	}

	/**
	 * @param vorlage
	 *            != null und beinhaltet seinen namen als titel1
	 * @param sudokuPool
	 *            != null
	 * @param fortschrittAnzeige
	 *            != null
	 */
	@Override
	public String gibTitel() {
		return "Neues Sudoku auf Basis der Vorlage " + vorlage.gibTitel1() + " erstellen";
	}

	private boolean istAbbruch(FortschrittZeiger fortschrittZeiger) {
		if (Thread.currentThread().isInterrupted()) {
			return true;
		}
		if (fortschrittZeiger.istAbbruchGefordert()) {
			return true;
		}
		return false;
	}

	@Override
	public void prozess(FortschrittZeiger fortschrittZeiger) {
		// Hier kann ev. Mist entstehen: Deshalb eventuell mehrmals Erstellen
		bestesSudoku = null;
		besteVarianz = 999;

		int nLauf;
		for (nLauf = ersterLauf; nLauf <= letzterLauf; nLauf++) {
			// if (nLauf == 50){
			// Thread.currentThread().interrupt();
			// }
			if (Thread.currentThread().isInterrupted()) {
				systemOut("Abbruch durch Thread-Interrupt zu Lauf-Anfang: " + nLauf);
				break;
			}
			if (fortschrittZeiger.istAbbruchGefordert()) {
				systemOut("Abbruch durch Fortschritt-Zeiger zu Lauf-Anfang: " + nLauf);
				break;
			}

			InfoSudoku neuesSudoku = null;

			systemOut(String.format("SudokuNachVorlage: Lauf %d", nLauf));
			fortschrittZeiger.zeigeFortschritt(nLauf);
			// Hierin fehlen noch Fortschrittanzeige und der Abbruch
			while (true) {
				if (Thread.currentThread().isInterrupted()) {
					systemOut("Abbruch durch Thread-Interrupt w�hrend des Wartens auf das volle Sudoku");
					break;
				}
				if (fortschrittZeiger.istAbbruchGefordert()) {
					systemOut("Abbruch durch Fortschritt-Zeiger w�hrend des Wartens auf das volle Sudoku");
					break;
				}
				NeuTyp erzeugeTyp = new NeuTyp(NeuTyp.Typ.VOLL);
				NeuTypOption option = NeuTypOption.MIX;
				neuesSudoku = sudokuPool.gibSudoku(erzeugeTyp, option);
				if (neuesSudoku != null) {
					// Endlich ist ein volles Sudoku da!
					break;
				} else {
					systemOut(String.format("Lauf %d: Pool gab null", nLauf));
					// Dem generatorThread Zeit geben zur Neuerstellung durch
					// kurze Blockierung dieses Thread).
					try {
						long sleepTime = 100;
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} // try {
				} // if (neuesSudoku != null){
			} // while (true){

			if ((neuesSudoku != null) & !istAbbruch(fortschrittZeiger)) {
				try {
					SudokuLogik sudokuLogik = new SudokuLogik(new EintragsEbenen());
					sudokuLogik.reset(neuesSudoku);
					sudokuLogik.setzeMuster(vorlage);
					neuesSudoku = sudokuLogik.gibVorgaben();
					// Schwierigkeit schwierigkeit =
					// Analysator.gibSchwierigkeit(neuesSudoku);

					// Ist ein gutes Sudoku entstanden?
					Protokoll protokoll = new Protokoll(sudokuLogik);
					Loesungen loesungen = Varianz.gibLoesungen(sudokuLogik, protokoll, 2);
					if (loesungen.gibAnzahl() == 1) {
						bestesSudoku = neuesSudoku;
						besteVarianz = 1;
						systemOut(String.format("Lauf %d: Fertig", nLauf));
						break; // besser gehts nicht
					} else {
						if (loesungen.gibAnzahl() < besteVarianz) {
							besteVarianz = loesungen.gibAnzahl();
							bestesSudoku = neuesSudoku;
							String mitteilung = String.format("Lauf %d: Bestes Sudoku mit Varianz = %d", nLauf,
									besteVarianz);
							fortschrittZeiger.zeigeInfo(mitteilung);
							systemOut(mitteilung);
						} else {
							systemOut(String.format("Lauf %d: Kein besseres Sudoku (Varianz = %d)", nLauf,
									loesungen.gibAnzahl()));
						}
					}
				} catch (Exc e) {
					e.printStackTrace();
				}
			} // if (neuesSudoku != null){
			neuesSudoku = null;
		} // for (nLauf
			// systemOut(String.format("SudokuBedienung.gibNeuesNachVorlage():
			// %d
			// L�ufe", nLauf));
		if (bestesSudoku != null) {
			bestesSudoku.setzeTitel(vorlage.gibTitel1());
		}
	}

}
