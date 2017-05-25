package sudoku.knacker;

import java.time.LocalDateTime;
import java.util.ArrayList;

import sudoku.kern.exception.Exc;
import sudoku.kern.exception.SudokuFertig;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.Problem;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.kern.protokoll.ProtokollMarkierer;
import sudoku.knacker.bericht.BerichtKnacker;
import sudoku.knacker.bericht.Schreiber;
import sudoku.logik.Klugheit;
import sudoku.logik.KnackerPartner;
import sudoku.logik.SudokuLogik;
import sudoku.logik.SudokuLogik.SetzeMoeglicheErgebnis;
import sudoku.varianz.Varianz;

public class Knacker {
	/**
	 * @author Hendrick Diese Klasse f�r den Ausstieg aus der kompletten Logik,
	 *         denn es ist nichts mehr zu tun.
	 */
	@SuppressWarnings("serial")
	private class MeldeTip extends Exception {
		private FeldNummerMitZahl tip;

		MeldeTip(FeldNummerMitZahl tip) {
			super();
			this.tip = tip;
		}

		public FeldNummerMitZahl gibTip() {
			return tip;
		}
	}

	/**
	 * @author Hendrick KEINE Es sollen gar keine Versuche (Eintrag auf Felder
	 *         mit mehreren m�glichen Zahlen) gestartet werden. EINE Es darf
	 *         eine VersuchsEbene gestartet werden UNBEGRENZT Es d�rfen beliebig
	 *         viele Versuche gestartet werden
	 */
	public enum VersuchsEbenen {
		KEINE, EINE, UNBEGRENZT
	}

	public class ZahlTipErgebnis {
		public final FeldNummerMitZahl zahl;
		public final Problem problem;

		/**
		 * @param tipZahl
		 * @param problem
		 */
		ZahlTipErgebnis(FeldNummerMitZahl tipZahl, Problem problem) {
			super();
			this.zahl = tipZahl;
			this.problem = problem;
		}
	}

	static private boolean istSystemOut = false;

	public static String gibText(Ergebnis ergebnis) {
		switch (ergebnis.gibArt()) {
		case FERTIG:
			return null; // "Das Sudoku ist fertig";
		case UNGELOEST:
			return "Das Sudoku scheint nicht l�sbar";
		case PROBLEM:
			return "Das Sudoku beinhaltet eine falsche Zahl";
		default:
			break;
		}
		return null;
	}

	static private void systemOut(String text) {
		if (istSystemOut) {
			System.out.println(text);
		}
	}

	// =========================================
	private Klugheit klugheit;

	private SudokuLogik sudoku;;

	private ProtokollMarkierer protokollMarkierer;

	private Optionen optionen;

	// ===========================================================

	private Schreiber schreiber;

	public Knacker(Klugheit klugheit, SudokuLogik aSudoku, ProtokollMarkierer protokollMarkierer) {
		this.klugheit = klugheit;
		sudoku = aSudoku;
		this.protokollMarkierer = protokollMarkierer;
		optionen = new Optionen();
		schreiber = new Schreiber();
	}

	public BerichtKnacker gibBericht() {
		BerichtKnacker berichtKnacker = schreiber.gibKnackerBericht();
		if (berichtKnacker == null) {
			return new BerichtKnacker();
		}
		return berichtKnacker;
	}

	/**
	 * @return null wenn ich keinen Tip geben kann, ansonsten gern
	 * @throws Exc
	 */
	public ZahlTipErgebnis gibTipZahl(String sudokuName) throws Exc {
		// Ist das Sudoku Fertig?
		if (!sudoku.istUnFertig()) {
			return null;
		}

		schreiber.setzeSudokuBeschreibung(klugheit, sudoku, sudokuName);

		// Den Ursprungszustand unbedingt wiederherstellen
		int markierungsID = setzeMarkierung();

		FeldNummerMitZahl tip = null;
		optionen.setzeOptionenGibTip();
		Ergebnis ergebnis = null;

		try {
			ergebnis = loeseIntern();
			if (ergebnis.gibArt() == Ergebnis.Art.FERTIG) {
				tip = protokollMarkierer.markierungGibZahlTip(markierungsID);
			}
		} catch (MeldeTip e) {
			tip = e.gibTip();
		}

		Problem problem = null;
		if (ergebnis != null) { // wegen MeldeTip!
			ergebnis.gibProblem();
		}

		ursprungWiederHerstellen(markierungsID, problem);
		schreiber.systemOut();
		return new ZahlTipErgebnis(tip, problem);
	}

	/**
	 * @return
	 * @throws Exc
	 */
	public Ergebnis knacke(String sudokuName) throws Exc {
		Ergebnis ergebnis = loese(VersuchsEbenen.UNBEGRENZT, sudokuName);
		return ergebnis;
	}

	/**
	 * Es wurde zuvor ein Versuch gestartet: Es wurde ein Eintrag auf ein Feld
	 * mit mehreren m�glichen Zahlen gesetzt. Hier wird kontrolliert, ob dieser
	 * Eintrag ein Problem im Sudoku erzeugt. Ein Problem kann feststellt
	 * werden: - entweder durch treibeKlare() - oder wenn ein weiterer Versuch
	 * sowohl mit Eintrag als auch den Alternativen ein Problem erzeugt.
	 * 
	 * @param istVersuchErlaubt
	 *            - false: Kontrolle mit treibeKlare() - true: Kontrolle mit
	 *            einem weiteren Versuch
	 * @return Problem oder null
	 * @throws Exc
	 * @throws SudokuFertig
	 */
	private Problem kontrolliereEintrag(boolean istKontrollVersuchErlaubt) throws Exc, SudokuFertig {
		{
			Problem problem = treibeKlare(false);
			if (problem != null) {
				return problem;
			}
		}

		if (istKontrollVersuchErlaubt) {
			ArrayList<KnackerPartner> felderMit2Moeglichen = sudoku.gibFelderMit2Moeglichen();
			Problem problem = kontrolliereEintragVersuch(felderMit2Moeglichen);
			if (problem != null) {
				return problem;
			}

			ArrayList<KnackerPartner> feldPaare = sudoku.gibKnackerPartnerFeldPaare();
			problem = kontrolliereEintragVersuch(feldPaare);
			if (problem != null) {
				return problem;
			}
		}
		return null;
	}

	private Problem kontrolliereEintragVersuch(ArrayList<KnackerPartner> partnerListe) throws Exc, SudokuFertig {

		if (BerichtKnacker.istSystemOut()) {
			System.out.println("Knacke.kontrolliereEintragVersuch(): " + partnerListe);
		}

		// Partner-Liste durcharbeiten
		for (int iPartner = 0; iPartner < partnerListe.size(); iPartner++) {
			// Partner versuchen mit Kontrolle per treibeKlare();
			KnackerPartner aPartner = partnerListe.get(iPartner);
			Problem problemEintrag = versuchePaarEintrag(aPartner, false);
			Problem problemAlternativen = versuchePaarAlternativen(aPartner, false);
			if ((problemEintrag != null) && (problemAlternativen != null)) {
				// Wenn beide Versuche ein Problem bringen war der vorige
				// Eintrag falsch:
				return problemEintrag;
			}
		} // for iPartner
		return null;
	}

	/**
	 * @return
	 * @throws Exc
	 */
	public Ergebnis loese(VersuchsEbenen versuchsEbenen, String sudokuName) throws Exc {
		schreiber.setzeSudokuBeschreibung(klugheit, sudoku, sudokuName);
		optionen.setzeOptionenLoese(versuchsEbenen);
		Ergebnis ergebnis;
		try {
			ergebnis = loeseIntern();
		} catch (MeldeTip e) {
			e.printStackTrace();
			throw Exc.unerwarteterTip(e.gibTip());
		}
		schreiber.systemOut();
		return ergebnis;
	}

	/**
	 * @return
	 * @throws Exc
	 * @throws MeldeTip
	 */
	private Ergebnis loeseIntern() throws Exc, MeldeTip {
		// Falls das L�sen des Sudoku nicht gelingt: Den Ursprungszustand
		// wiederherstellen
		int markierungsID = setzeMarkierung();
		Ergebnis ergebnis = Ergebnis.ungeloest();
		Problem startProblem = null;
		try {

			boolean wurdeEintragGesetzt;
			int durchlauf = 0;
			do {
				systemOutZeit("Start");
				wurdeEintragGesetzt = false;
				durchlauf++;
				schreiber.loeseIntern(durchlauf);

				// Erstmal alle Klaren setzen
				{
					startProblem = treibeKlare(true); // ======> eventuell
														// Fertig
					if (startProblem != null) {
						ergebnis = Ergebnis.problem(startProblem);
						break; // Problem
					}
				}
				// Ungel�st
				if (optionen.gibMaxErlaubteVersuchsEbene().ordinal() >= VersuchsEbenen.EINE.ordinal()) {
					// Versuchen mit einer Versuchs-Ebene

					// Partner-Liste bereitstellen
					ArrayList<KnackerPartner> felderMit2Moeglichen = sudoku.gibFelderMit2Moeglichen();
					ArrayList<KnackerPartner> feldPaare = null;

					// Partner versuchen mit Kontrolle per treibeKlare();
					wurdeEintragGesetzt = versuchePaare(felderMit2Moeglichen, false);
					systemOutZeit("War Versuche 1 Ebene mit 2 m�glichen im Feld");
					if (!wurdeEintragGesetzt) {
						feldPaare = sudoku.gibKnackerPartnerFeldPaare();
						wurdeEintragGesetzt = versuchePaare(feldPaare, false);
						systemOutZeit("War Versuche 1 Ebene mit FeldPaaren");
					}

					// if ((!wurdeEintragGesetzt)
					// && (optionen.gibMaxErlaubteVersuchsEbene() ==
					// VersuchsEbenen.VERSUCHINVERSUCH)) {
					// // Versuchen mit zwei Versuchs-Ebenen
					//
					// // Partner versuchen mit Kontrolle per Versuch();
					// wurdeEintragGesetzt = versuchePaare(felderMit2Moeglichen,
					// true);
					// systemOutZeit("War Versuche n Ebenen mit 2 m�glichen im
					// Feld");
					// if (!wurdeEintragGesetzt) {
					// if (feldPaare == null) {
					// feldPaare = sudoku.gibPartnerFeldPaare();
					// }
					// wurdeEintragGesetzt = versuchePaare(feldPaare, true);
					// systemOutZeit("War Versuche n Ebenen mit FeldPaaren");
					// }
					// } // if VERSUCHINVERSUCH

					if ((!wurdeEintragGesetzt)
							&& (optionen.gibMaxErlaubteVersuchsEbene() == VersuchsEbenen.UNBEGRENZT)) {
						// Varianz bem�hen
						boolean istFertig = Varianz.loese(sudoku, protokollMarkierer);
						if (istFertig) {
							ergebnis = Ergebnis.fertig();
						} else {
							ergebnis = Ergebnis.problem(new Problem("Das Sudoku besitzt keine L�sung"));
						}
						systemOutZeit("War unbegrenzte Versuche-Anzahl mit Varianz");
						break;
					} // if UNBEGRENZT

				} // if (optionen... >= VersuchsEbenen.EINE)
			} while (wurdeEintragGesetzt);
		} catch (SudokuFertig e) {
			ergebnis = Ergebnis.fertig();
		}

		schreiber.loeseInternEnde(ergebnis, sudoku.ebeneGibNummer());

		if (!optionen.istGibTip()) {
			// ursprungWiederHerstellen
			switch (ergebnis.gibArt()) {
			case FERTIG: // Das wollten wir eigentlich
				break;
			case UNGELOEST: // Falls nicht fertig: Den Ursprungszustand
							// herstellen:
				ursprungWiederHerstellen(markierungsID);
				break;
			case PROBLEM: // Falls nicht fertig: Den Ursprungszustand
							// herstellen:
				ursprungWiederHerstellen(markierungsID, startProblem);
			default:
				break;
			}
		}
		return ergebnis;
	}

	/**
	 * Setzt auf den aktuellen SudokuStand im Protokoll eine Markierung, auf die
	 * im weiteren der Stand des Sudoku zur�ckgedreht werden kann
	 * 
	 * @return id
	 */
	private int setzeMarkierung() {
		int markierungsID = protokollMarkierer.markierungSetzen();
		return markierungsID;
	}

	private void setzePaarAlternativen(KnackerPartner aPartner) throws Exc {
		// Hier werden unter Umst�nden mehrere Felder gesetzt.
		// Diese k�nnten also auch mehrere Ebenen erzeugen.
		// Deshalb wird hier vorgebeugt mit sudoku.setzeEintragOhneVersuch().
		// Diese m�ssen in der richtigen Reihenfolge wieder abgebaut werden.
		ZahlenListe sollList = aPartner.gibAlternativen();

		for (int iPartner = 0; iPartner < sollList.size(); iPartner++) {
			FeldNummerMitZahl sollEintrag = sollList.get(iPartner);
			// Das erzeugt eine neue Ebene:
			FeldNummer feldNummer = sollEintrag.gibFeldNummer();
			if (iPartner == 0) {
				if (BerichtKnacker.istSystemOut()) {
					System.out.println(
							"   Knacke.setzePaarAlternativen(): " + sollEintrag.gibZahl() + " in " + feldNummer);
				}
				sudoku.setzeEintrag(new FeldNummerMitZahl(feldNummer, sollEintrag.gibZahl()));
			} else {
				if (BerichtKnacker.istSystemOut()) {
					System.out.println("   Knacke.setzePaarAlternativen() ohne Versuch: " + sollEintrag.gibZahl()
							+ " in " + feldNummer);
				}
				sudoku.setzeEintragOhneVersuch(feldNummer, sollEintrag.gibZahl());
			}
		}
	}

	private void setzePaarEintrag(KnackerPartner aPartner) throws Exc {
		FeldNummerMitZahl eintrag = aPartner.gibBasis();

		// Das erzeugt eine neue Ebene:
		FeldNummer feldNummer = eintrag.gibFeldNummer();
		if (BerichtKnacker.istSystemOut()) {
			System.out.println("   Knacke.setzePaarEintrag(): " + eintrag.gibZahl() + " in " + feldNummer);
		}
		sudoku.setzeEintrag(new FeldNummerMitZahl(feldNummer, eintrag.gibZahl()));

	}

	private void systemOutZeit(String text) {
		if (istSystemOut) {
			LocalDateTime now = LocalDateTime.now();
			String s = String.format("%s %s %s", this.getClass().getSimpleName(), now, text);
			systemOut(s);
		}
	}

	/**
	 * Solange freie Felder mit nur einer m�glichen Zahl vorhanden sind,
	 * erhalten diese ihren Eintrag.
	 * 
	 * @return Es gibt 3 m�gliche Zust�nde nach treibeKlare(): - Problem: Wenn
	 *         zuvor eine falsche Zahl gesetzt wurde, - null: Ungel�st: Wenn es
	 *         keine weiteren Solisten mehr gibt. - Fertig: Dies wird per throw
	 *         SudokuFertig() gemeldet.
	 * @throws Exc
	 * @throws SudokuFertig
	 *             Falls das Sudoku Fertig gel�st ist
	 */
	private Problem treibeKlare(boolean schreibeBericht) throws Exc, SudokuFertig {
		boolean istFertig = false;
		Problem problem = null;
		Ergebnis ergebnis = Ergebnis.ungeloest();

		while (true) {
			if (schreibeBericht) {
				sudoku.registriereBerichtKurier(schreiber);
			}
			SetzeMoeglicheErgebnis moeglicheErgebnis = sudoku.setzeMoegliche(klugheit, false);
			if (moeglicheErgebnis.gibProblem() != null) {
				problem = moeglicheErgebnis.gibProblem();
				ergebnis = Ergebnis.problem(moeglicheErgebnis.gibProblem());
				break; // =======================>
			}
			FeldNummerMitZahl eintrag = moeglicheErgebnis.gibEintrag();
			if (eintrag != null) {
				sudoku.setzeEintrag(eintrag);
				if (schreibeBericht) {
					schreiber.treibeKlareKlare1(1);
				}
			}
			if (!sudoku.istUnFertig()) {
				istFertig = true;
				ergebnis = Ergebnis.fertig();
				break; // =======================>
			}
			if (eintrag == null) {
				// F�r diesen Zustand gibt es keine logisch eindeutige L�sung
				// (mit der Klugheit)
				break; // =======================>
			}

		}

		if (schreibeBericht) {
			schreiber.treibeKlareErgebnis(ergebnis);
		}

		if (istFertig) {
			throw new SudokuFertig();
		}
		return problem;
	}

	/**
	 * Den Weitermachenden den Ursprungszustand herstellen
	 * 
	 * @param markierungsID
	 * @throws Exc
	 */
	private void ursprungWiederHerstellen(int markierungsID) throws Exc {
		ursprungWiederHerstellen(markierungsID, null);
	}

	/**
	 * Den Weitermachenden den Ursprungszustand herstellen
	 * 
	 * @param markierungsID
	 * @param startProblem
	 *            wenn dies schon bei treibeKlare aufgetreten ist, wird es auch
	 *            hier notwendigerweise toleriert.
	 * @throws Exc
	 */
	private void ursprungWiederHerstellen(int markierungsID, Problem startProblem) throws Exc {
		protokollMarkierer.markierungAnsteuern(markierungsID);
		SetzeMoeglicheErgebnis ergebnis = sudoku.setzeMoegliche(klugheit, false);
		Problem problem = ergebnis.gibProblem();
		if (problem != null) {
			if (!problem.equals(startProblem)) {
				System.out.println("Knacker.ursprungWiederHerstellen() unerwartetesProblem:" + problem);
				throw Exc.unerwartetesProblem(problem);
			}
		}
	}

	/**
	 * Versucht mit diesen Alternativen (eines Paares) per Solisten zur L�sung
	 * zu kommen
	 * 
	 * @return @see treibeSolisten()
	 * @throws Problem
	 *             maximal wenn das L�schen der Eintr�ge am Ende nicht
	 *             funktioniert
	 * @throws Exc
	 * @throws SudokuFertig
	 *             Falls Sudoku fertig gel�st ist
	 */
	private Problem versuchePaarAlternativen(KnackerPartner aPartner, boolean istVersuchErlaubt)
			throws Exc, SudokuFertig {
		int markierungsID = setzeMarkierung();

		// Hier werden unter Umst�nden mehrere Felder gesetzt.
		// Diese k�nnen also auch mehrere Ebenen erzeugen.
		// Diese m�ssen in der richtigen Reihenfolge wieder abgebaut werden.
		setzePaarAlternativen(aPartner);
		Problem problem = kontrolliereEintrag(istVersuchErlaubt);

		ursprungWiederHerstellen(markierungsID);

		return problem;
	}

	/**
	 * Es werden Versuche durchgef�hrt in der Hoffnung einen Eintrag eines
	 * Partners setzen zu k�nnen, der kein Problem verursacht.
	 * 
	 * @param partnerListe
	 *            Diese Partner werden f�r Versuche genutzt.
	 * @param istKontrollVersuchErlaubt
	 *            Bei true sind Versuche zur Kontrolle eines Eintrags erlaubt.
	 * @return true wenn ein Eintrag gesetzt wurde
	 * @throws Exc
	 * @throws MeldeTip
	 * @throws SudokuFertig
	 */
	private boolean versuchePaare(ArrayList<KnackerPartner> partnerListe, boolean istKontrollVersuchErlaubt)
			throws Exc, MeldeTip, SudokuFertig {
		schreiber.versuchePaare(partnerListe, istKontrollVersuchErlaubt);

		if (partnerListe.isEmpty()) {
			return false;
		}

		KnackerPartner aPartner = null;
		boolean istEintragRichtig = false;
		boolean istAlternativeRichtig = false;

		// Partner-Liste durcharbeiten
		for (int iPartner = 0; iPartner < partnerListe.size(); iPartner++) {
			aPartner = partnerListe.get(iPartner);
			istEintragRichtig = false;
			istAlternativeRichtig = false;

			schreiber.versuchStart(aPartner, true);
			Problem problemEintrag = versuchePaarEintrag(aPartner, istKontrollVersuchErlaubt);

			if (problemEintrag != null) {
				// Es m�ssen die Alternativen richtig sein!
				istAlternativeRichtig = true;
				break;
			}

			schreiber.versuchStart(aPartner, false);
			Problem problemAlternativen = versuchePaarAlternativen(aPartner, istKontrollVersuchErlaubt);
			if (problemAlternativen != null) {
				// Es muss der Eintrag richtig sein!
				istEintragRichtig = true;
				break;
			}
		} // for iPartner

		if (istAlternativeRichtig) {
			// Alternativen setzen
			setzePaarAlternativen(aPartner);

			if (optionen.istGibTip()) {
				FeldNummerMitZahl tip = aPartner.gibAlternativen().get(0);
				throw new MeldeTip(tip);
			}
			return true;
		}

		if (istEintragRichtig) {
			// Eintrag setzen
			setzePaarEintrag(aPartner);
			if (optionen.istGibTip()) {
				FeldNummerMitZahl tip = aPartner.gibBasis();
				throw new MeldeTip(tip);
			}
			return true;
		}

		return false;
	}

	/**
	 * Versucht mit diesem Eintrag (eines Paares) per Solisten zur L�sung zu
	 * kommen
	 * 
	 * @param eintrag
	 * @return @see treibeSolisten()
	 * @throws Problem
	 *             maximal wenn das L�schen der Eintr�ge am Ende nicht
	 *             funktioniert
	 * @throws Exc
	 * @throws SudokuFertig
	 *             Falls Sudoku fertig gel�st ist
	 */
	private Problem versuchePaarEintrag(KnackerPartner aPartner, boolean istVersuchErlaubt) throws Exc, SudokuFertig {
		int markierungsID = setzeMarkierung();

		// Das erzeugt eine neue Ebene:
		setzePaarEintrag(aPartner);

		if (istVersuchErlaubt) {
			markierungsID++;
			markierungsID--;
		}
		Problem problem = kontrolliereEintrag(istVersuchErlaubt);

		ursprungWiederHerstellen(markierungsID);
		return problem;
	}

}
