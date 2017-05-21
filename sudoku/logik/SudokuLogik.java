package sudoku.logik;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import sudoku.kern.EintragsEbenen;
import sudoku.kern.exception.Exc;
import sudoku.kern.exception.UnerwarteteLogik;
import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldMatrix;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.Problem;
import sudoku.kern.feldmatrix.ZahlenFeldNummern;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.kern.info.InfoSudoku;
import sudoku.logik.bericht.Schreiber;
import sudoku.logik.tipinfo.EinTipText;
import sudoku.logik.tipinfo.TipInfo;
import sudoku.logik.tipinfo.TipInfo0;
import sudoku.logik.tipinfo.TipKurier;

public class SudokuLogik extends FeldMatrix {
	// ab dieser Anzahl an Vorgaben könnte das Sudoku ausreichend festgelegt sein
	static private int nVorgabenLogik = 17;
	static private EnumMap<Logik_ID, Logik__Infos> logikenInfos = gibLogiken();

	static private EnumMap<Logik_ID, Logik__Infos> gibLogiken() {
		EnumMap<Logik_ID, Logik__Infos> logikMap = new EnumMap<Logik_ID, Logik__Infos>(Logik_ID.class);
		Logik_ID[] logikArray = Logik_ID.values();

		for (int iLogik = 0; iLogik < logikArray.length; iLogik++) {
			switch (logikArray[iLogik]) {
			case ORTFEST1:
				logikMap.put(logikArray[iLogik], new Logik_OrtFest1(null));
				break;
			case ORTFEST2:
				logikMap.put(logikArray[iLogik], new Logik_OrtFest2(null));
				break;
			case KASTEN1:
				logikMap.put(logikArray[iLogik], new Logik_Kasten1(null));
				break;
			case KASTEN2:
				logikMap.put(logikArray[iLogik], new Logik_Kasten2(null));
				break;
			// case KASTENLINIE:
			// logikMap.put(logikArray[iLogik], new Logik_KastenLinie(null, null, null));
			// break;
			case FELD:
				logikMap.put(logikArray[iLogik], new Logik_Feld(null));
				break;
			case KREUZFLUEGEL:
				logikMap.put(logikArray[iLogik], new Logik_KreuzFluegel(null, null));
				break;
			case SCHWERTFISCH:
				logikMap.put(logikArray[iLogik], new Logik_SchwertFisch(null, null));
				break;
			case ORTFEST3:
				logikMap.put(logikArray[iLogik], new Logik_OrtFest3(null));
				break;
//			case ORTFEST4:
//				logikMap.put(logikArray[iLogik], new Logik_OrtFest4(null));
//				break;
			case TEILMENGEFEST2:
				logikMap.put(logikArray[iLogik], new Logik_TeilMengeFest2(null));
				break;
			case TEILMENGEFEST3:
				logikMap.put(logikArray[iLogik], new Logik_TeilMengeFest3(null));
				break;
//			case TEILMENGEFEST4:
//				logikMap.put(logikArray[iLogik], new Logik_TeilMengeFest4(null));
//				break;
			case AUSWIRKUNGSKETTE:
				logikMap.put(logikArray[iLogik], new Logik_Auswirkungskette(null, null));
				break;
			default:
				// Für eine Exception ist es zu dieser (static) Stunde noch ein bischen früh. Deshanlb dies unten.
				// throw new UnerwarteteLogik(logikArray[iLogik].toString());
			}
		}
		return logikMap;
	}

	private void initLogiken() {
		basisLogik = new BasisLogik(super.felder);
		Logik_ID[] logikArray = Logik_ID.values();
		logiken = new Logik__Interface[logikArray.length];

		for (int iLogik = 0; iLogik < logikArray.length; iLogik++) {
			switch (logikArray[iLogik]) {
			case ORTFEST1:
				logiken[iLogik] = new Logik_OrtFest1(gruppen);
				break;
			case ORTFEST2:
				logiken[iLogik] = new Logik_OrtFest2(gruppen);
				break;
			case KASTEN1:
				logiken[iLogik] = new Logik_Kasten1(kaesten);
				break;
			case KASTEN2:
				logiken[iLogik] = new Logik_Kasten2(kaesten);
				break;
			// case KASTENLINIE:
			// logiken[iLogik] = new Logik_KastenLinie(kaesten, zeilen, spalten);
			// break;
			case FELD:
				logiken[iLogik] = new Logik_Feld(super.felder);
				break;
			case KREUZFLUEGEL:
				logiken[iLogik] = new Logik_KreuzFluegel(zeilen, spalten);
				break;
			case SCHWERTFISCH:
				logiken[iLogik] = new Logik_SchwertFisch(zeilen, spalten);
				break;
			case ORTFEST3:
				logiken[iLogik] = new Logik_OrtFest3(gruppen);
				break;
//			case ORTFEST4:
//				logiken[iLogik] = new Logik_OrtFest4(gruppen);
//				break;
			case TEILMENGEFEST2:
				logiken[iLogik] = new Logik_TeilMengeFest2(gruppen);
				break;
			case TEILMENGEFEST3:
				logiken[iLogik] = new Logik_TeilMengeFest3(gruppen);
				break;
//			case TEILMENGEFEST4:
//				logiken[iLogik] = new Logik_TeilMengeFest4(gruppen);
//				break;
			case AUSWIRKUNGSKETTE:
				logiken[iLogik] = new Logik_Auswirkungskette(gruppen, this);
				break;
			default:
				throw new UnerwarteteLogik(logikArray[iLogik].toString());
			}
		}
	}

	/**
	 * @return true wenn die Logik zur Erstellung von möglichen Zahlen (schon) läuft
	 */
	static public int gibAnzahlVorgabenMin() {
		return nVorgabenLogik;
	}

	static public Logik__Infos gibLogikInfos(Logik_ID logik) {
		Logik__Infos texte = logikenInfos.get(logik);
		return texte;
	}

	static public String gibNameKurz(Logik_ID logik) {
		Logik__Infos texte = logikenInfos.get(logik);

		if (texte == null) {
			throw new UnerwarteteLogik(logik.toString());
		}
		String s = texte.gibKurzName();
		return s;
	}

	static public double gibKontrollZeit1(Logik_ID logik) {
		Logik__Infos infos = logikenInfos.get(logik);

		if (infos == null) {
			throw new UnerwarteteLogik(logik.toString());
		}
		double zeit = infos.gibKontrollZeit1();
		return zeit;
	}

	static public String gibNameExtrem(boolean istMaximum) {
		return istMaximum ? "Maximum" : "Minimum";
	}

	// /**
	// * @param gruppenTyp null oder Typ der Felder-Gruppe
	// * @return Startnummer (ab 1) der Gruppe des Gruppentyps innerhalb aller Gruppen(-Typen)
	// */
	// static public int gibStartNummer(Gruppe.Typ gruppenTyp) {
	// if (gruppenTyp == null) {
	// return 1;
	// }
	//
	// int startNummer = gruppenTyp.ordinal() + 1;
	// return startNummer;
	// }

	// static
	// ============================================================
	// interne Klassen
	/**
	 * @author heroe
	 * Enthält das Ergebnis von setzeMoegliche(): Entweder ein erkanntes Problem 
	 * oder ein zu setzender Eintrag oder leer.
	 */
	public class SetzeMoeglicheErgebnis {
		final Problem problem;
		final FeldNummerMitZahl eintrag;

		public Problem gibProblem() {
			return problem;
		}

		public FeldNummerMitZahl gibEintrag() {
			return eintrag;
		}

		SetzeMoeglicheErgebnis() {
			this.problem = null;
			this.eintrag = null;
		}

		SetzeMoeglicheErgebnis(FeldNummerMitZahl eintrag) {
			this.problem = null;
			this.eintrag = eintrag;
		}

		SetzeMoeglicheErgebnis(Problem problem) {
			this.eintrag = null;
			this.problem = problem;
		}
	}

	/**
	 * @author heroe
	 * Beinhaltet die Infos zum Ergebnis des Tips
	 */
	private class TipInfoEnde extends TipInfo0 {
		private Problem problem;
		private FeldNummerMitZahl eintrag;

		// public Problem gibProblem() {
		// return problem;
		// }

		public TipInfoEnde(Problem problem, FeldNummerMitZahl eintrag, InfoSudoku infoSudoku) {
			super(null, null);
			setzeSudoku(infoSudoku);
			this.problem = problem;
			this.eintrag = eintrag;
		}

		@Override
		public EinTipText[] gibTip() {
			if (this.eintrag != null) {
				String s1 = String.format("Im Feld %s ist einzig die Zahl %d möglich.", this.eintrag.gibFeldNummer(),
						this.eintrag.gibZahl());

				EinTipText[] sArray = new EinTipText[] { new EinTipText(s1, null) };
				return sArray;
			} else {
				if (problem != null) {
					EinTipText[] sArray = new EinTipText[] { new EinTipText(problem.gibProblem(), null) };
					return sArray;
				} else {
					EinTipText[] erfolglosTip = {
							new EinTipText("Entweder muß die Klugheit (rechts oben) vergrößert werden", null),
							new EinTipText("oder das Sudoku ist nicht ohne Versuche lösbar:", null),
							new EinTipText("   - 'Gib Zahl' benutzen", null),
							new EinTipText("   - oder die Lösung durch 'Knacke' erstellen.", null) };
					return erfolglosTip;
				}
			}
		}

		public String gibUeberschrift(int tipNummer) {
			String s = null;
			if (this.eintrag != null) {
				s = String.format("%d. Tip: Jetzt steht ein Eintrag fest:", tipNummer);
			} else {
				s = String.format("%d. Tip: Es ist kein Tip möglich:", tipNummer);
			}
			return s;
		}

		@Override
		public FeldNummerListe gibAktiveFelder() {
			FeldNummerListe feldNummerListe = new FeldNummerListe();
			if (eintrag != null) {
				feldNummerListe.add(eintrag.gibFeldNummer());
			}
			return feldNummerListe;
		}

		@Override
		public ZahlenListe gibLoeschZahlen() {
			return null;
		}

		@Override
		public boolean istZahl() {
			return eintrag != null;
		}

		@Override
		public FeldNummerMitZahl gibZahlFeld() {
			return eintrag;
		}
	}

	// Ende interne Klassen
	// ============================================================

	private LogikBerichtKurier berichtKurier;
	private TipKurier tipKurier;
	private ArrayList<Gruppe> gruppen;
	private ArrayList<Gruppe> zeilen;
	private ArrayList<Gruppe> spalten;
	private ArrayList<Kasten> kaesten;
	private BasisLogik basisLogik;
	private Logik__Interface[] logiken;

	public SudokuLogik(EintragsEbenen eintragsEbenen) {
		super(eintragsEbenen);
		berichtKurier = null;
		tipKurier = null;
		gruppen = null;
		zeilen = null;
		spalten = null;

		// Zeilen-Gruppen erschaffen
		zeilen = new ArrayList<Gruppe>();
		for (int zeile = 1; zeile < 10; zeile++) {
			Gruppe g = new Gruppe(Gruppe.Typ.ZEILE, new FeldNummer(0, zeile), true, felder);
			zeilen.add(g);
		}

		// Spalten-Gruppen erschaffen
		spalten = new ArrayList<Gruppe>();
		for (int spalte = 1; spalte < 10; spalte++) {
			Gruppe g = new Gruppe(Gruppe.Typ.SPALTE, new FeldNummer(spalte, 0), true, felder);
			spalten.add(g);
		}

		// Kasten-Gruppen erschaffen
		kaesten = new ArrayList<Kasten>();
		for (int iSpalte = 0; iSpalte < 3; iSpalte++) {
			for (int iZeile = 0; iZeile < 3; iZeile++) {
				Kasten kasten = new Kasten(new KastenIndex(iSpalte, iZeile), felder);
				kaesten.add(kasten);
			}
		}
		// Kasten-Nachbarn setzen
		for (int i = 0; i < kaesten.size(); i++) {
			Kasten kasten = kaesten.get(i);
			kasten.setzeNachbarn(kaesten);
		}

		// gruppen erstellen: 1. Kästen, 2. Zeile, 3. Spalten
		// Diese Reihenfolge soll der Reihenfolge in Gruppe.Typ entsprechen: Für die Lösungs-Zeit-Errechnung!
		// Muss also korrespondieren mit gibStartNummer().
		gruppen = new ArrayList<Gruppe>();
		gruppen.addAll(kaesten);
		gruppen.addAll(zeilen);
		gruppen.addAll(spalten);

		initLogiken();
	}

	public void registriereBerichtKurier(LogikBerichtKurier berichtKurier) {
		this.berichtKurier = berichtKurier;
	}

	public void registriereTipKurier(TipKurier tipKurier) {
		this.tipKurier = tipKurier;
	}

	/**
	 * Diese package-interne Methode bietet allen Logiken den Zugang zum Feld per FeldNummer.
	 * @param feldNummer
	 * @return Das Feld zur FeldNummer.
	 * @throws Exc
	 */
	Feld gibLogikFeld (FeldNummer feldNummer) throws Exc{
		return super.gibFeld(feldNummer);
	}

	/**
	 * @param gruppe
	 * @return Problem innerhalb der gruppe oder (besser) null
	 */
	private Problem sucheGruppenProblem(Gruppe gruppe) {
		ZahlenFeldNummern alleVorhandeneZahlen = gruppe.gibVorhandeneZahlen();

		if (alleVorhandeneZahlen.gibAnzahlVorhandene() < 9) {
			return Problem.nichtAlleZahlenInDerGruppe(gruppe.gibInText(true), alleVorhandeneZahlen);
		}
		// Das ist Laufzeit und die obige generelle Logik sollte reichen!

		// FeldListe0Bis9 moeglicheFelderJeZahl = gruppe.gibMoeglicheFelderJeZahl();
		//
		// // Wenn eine Zahl n mögliche Felder hat, müssen in diesen n Feldern auch mindestens n Zahlen möglich sein
		// // Mit jedem Kulgheitsgrad bzw. Anzahl Felder je Zahl
		// for (int anzahlFelder = 2; anzahlFelder<9; anzahlFelder++){
		//
		// // Jede Zahl wird kontrolliert
		// for (int basisZahl=1; basisZahl<10; basisZahl++){
		//
		// FeldListe basisZahlFelder = moeglicheFelderJeZahl.get(basisZahl);
		// int basisZahlFeldAnzahl = basisZahlFelder.size();
		//
		// // Wenn die basisZahl genau anzahlFelder besitzt:
		// if (basisZahlFeldAnzahl == anzahlFelder) {
		// VorhandeneZahlen moeglicheZahlen = basisZahlFelder.gibMoeglicheZahlen();
		// if (anzahlFelder > moeglicheZahlen.gibAnzahlVorhandene()){
		// return Problem.zuwenigMoeglicheInFreienFeldern(
		// gruppe, basisZahl, anzahlFelder, moeglicheZahlen.gibAnzahlVorhandene());
		// }
		// }
		// }
		// } // for

		return null;
	}

	private Problem sucheProblem() {
		// Felder suchen ein Problem
		Problem problem = super.sucheFeldProblem();

		// Gruppen suchen ein Problem
		if ((problem == null)) {
			for (int i = 0; i < gruppen.size(); i++) {
				Gruppe gruppe = gruppen.get(i);
				problem = sucheGruppenProblem(gruppe);
				if (problem != null) {
					break;
				}
			}
		}
		return problem;
	}

	/**
	 * @return Anzahl der Gruppen mit einem oder mehr freien Feldern
	 */
	private int gibAnzahlFreieGruppen(ArrayList<Gruppe> gruppen) {
		int n = 0;
		for (int i = 0; i < gruppen.size(); i++) {
			Gruppe gruppe = gruppen.get(i);
			if (gruppe.existiertFreiesFeld()) {
				n++;
			}
		}
		return n;
	}

	/**
	 * @return Anzahl der Kästen mit einem oder mehr freien Feldern
	 */
	private int gibAnzahlFreieKaesten(ArrayList<Kasten> kaesten) {
		int n = 0;
		for (int i = 0; i < kaesten.size(); i++) {
			Gruppe gruppe = kaesten.get(i);
			if (gruppe.existiertFreiesFeld()) {
				n++;
			}
		}
		return n;
	}

	/**
	 * Solange freie Felder mit nur einer möglichen Zahl erkannt werden, erhalten diese ihren Eintrag.
	 * Das Sudoku muss danach nicht gelöst sein: Wenn es nämlich kein eindeutiges ist!
	 * @throws Exc
	 * @return Problem falls das Sudoku zuvor einen falschen Eintrag erhielt, sonst null.
	 */
	public Problem setzeEintraegeAufKlare(Klugheit klugheit, boolean alle, boolean feldPaareSetzen) throws Exc {
		do {
			SetzeMoeglicheErgebnis ergebnis = this.setzeMoegliche(klugheit, feldPaareSetzen);
			if (ergebnis.problem != null) {
				return ergebnis.problem; // =============>
			}

			if (ergebnis.eintrag == null) {
				break; // =============>
			}

			this.setzeEintrag(ergebnis.eintrag);
		} while (alle);

		return null;
	}

	/**
	 * Vermerkt in den Feldern wenn es in einer Gruppe genau 2 Felder gibt mit ein und derselben Zahl.
	 * @throws Exc 
	 * @return Alle Felder, die Teil eines FeldPaares sind.
	  */
	FeldListe setzeFeldPaare() throws Exc {
		ArrayList<FeldPaar> feldPaare = FeldPaar.gibFeldPaare(gruppen);
		FeldListe feldListe = new FeldListe();
		
		for (FeldPaar feldPaar : feldPaare) {
			feldPaar.feld1.setzeFeldPaar(feldPaar.zahl, feldPaar.feld2.gibFeldNummer());
			if(!feldListe.contains(feldPaar.feld1)){
				feldListe.add(feldPaar.feld1);
			}
			feldPaar.feld2.setzeFeldPaar(feldPaar.zahl, feldPaar.feld1.gibFeldNummer());
			if(!feldListe.contains(feldPaar.feld2)){
				feldListe.add(feldPaar.feld2);
			}
		}
		
		return feldListe;
	}

	/**
	 * Die Ansiedelung dieser Funktionalität an dieser Stelle hat den bedeutenden Effekt,
	 * dass die Liste der Felder private bleibt!
	 * @return Die Liste von FeldNummerMitZahl, die Felder benennt, die jeweils genau zwei mögliche Zahlen beinhalten. 
	 * @throws Exc
	 */
	public ArrayList<KnackerPartner> gibFelderMit2Moeglichen() throws Exc {
		FeldListe zweier = felder.gibFreieFelderMit2Moeglichen();
		
		ArrayList<KnackerPartner> partnerListe = new ArrayList<KnackerPartner>(zweier.size()) ;
		for (Feld feld: zweier) {
			partnerListe.add(new KnackerPartner(feld));
		}
		return partnerListe; 
	}

	/**
	 * @return Die Liste von Partnern, die FelderPaare benennt. 
	 * @throws Exc
	 */
	public ArrayList<KnackerPartner> gibKnackerPartnerFeldPaare() throws Exc {
		ArrayList<KnackerPartner> knackerPartnerListe = new ArrayList<KnackerPartner>();
		ArrayList<FeldPaar> feldPaare = FeldPaar.gibFeldPaare(gruppen);
		
		for (FeldPaar feldPaar: feldPaare){
			FeldNummer feldNummer = new FeldNummer(feldPaar.feld2.gibFeldNummer());
			FeldNummerListe feldNummerListe = new FeldNummerListe();
			feldNummerListe.add(feldNummer);
			KnackerPartner knackerPartner = new KnackerPartner(feldPaar.zahl, feldPaar.feld1, feldNummerListe); 
			knackerPartnerListe.add(knackerPartner);
		}
		
		return knackerPartnerListe;
	}

	/**
	 * Ziel ist das Erkennen bzw. Erreichen eines freien Feldes mit nur einer möglichen Zahl: Dass kann dann ein Eintrag werden. 
	 * Für freie Felder werden die möglichen Zahlen neu ermittelt:
	 *   1. Jedes Feld setzt seine möglichen Zahlen
	 *   2. Soweit es die Logik hergibt werden von diesen Zahlen gelöscht, 
	 *   		um Felder mit nur einer möglichen Zahl zu erreichen. 
	 *   3. Es werden Probleme gesucht.
	 * @param feldPaareSetzen Mit true werden die in jedem Feld gesetzen FeldPaare angefordert
	 * @return SetzeMoeglicheErgebnis: Mit einem von 4 denkbaren Zuständen:
	 * 					1. Mit dem zu setzenden Eintrag oder
	 * 					2. Mit einem erkannten Problem wenn das Sudoku eine falsche Zahl beinhaltet oder
	 * 					- Leeres Ergebnis 
	 * 						3. wenn die Lösung nicht weiter vorangetrieben werden kann oder
	 * 						4. das Sudoku bereits vollständig gelöst war. 
	 * @throws Exc
	 */
	public SetzeMoeglicheErgebnis setzeMoegliche(Klugheit klugheit, boolean feldPaareSetzen) throws Exc {
		// Der Schreiber läuft nur wenn er gebraucht wird:
		Schreiber berichtSchreiber = null;
		if (Schreiber.istSystemOut() || (berichtKurier != null)) {
			berichtSchreiber = new Schreiber();
		}

		int nFreieFelder = felder.gibAnzahlFreieFelder();
		int nFreieZeilen = gibAnzahlFreieGruppen(zeilen);
		int nFreieSpalten = gibAnzahlFreieGruppen(spalten);
		int nFreieKaesten = gibAnzahlFreieKaesten(kaesten);
		if (berichtSchreiber != null) {
			berichtSchreiber.addStart(new Klugheit(klugheit), nFreieFelder, nFreieZeilen, nFreieSpalten, nFreieKaesten);
		}

		// Jedes freie Feld ermittelt die Möglichen anhand seiner Gruppe (Zeile+Spalte+Kasten) neu
		basisLogik.setzeMoegliche();

		int durchLauf = 0;
		int anzahlGeloeschterZahlen = 1;
		FeldNummerMitZahl eintrag = null;

		while (true) {
			if (!super.istUnFertig()) { // Fertig
				break;
			}
			if (null != eintrag) {
				// Das Erkennen eines freien klaren Feldes (mit nur einer möglichen Zahl) war das Ziel
				break;
			}
			if (anzahlGeloeschterZahlen == 0) {
				// Es gibt keine Erfolge mehr beim Löschen von (Un-) Möglichen
				break;
			}

			anzahlGeloeschterZahlen = 0;
			durchLauf++;

			if (durchLauf > 500) {
				throw Exc.endlosSchleife();
			}

			if (berichtSchreiber != null) {
				berichtSchreiber.addDurchlauf(durchLauf);
			}

			for (int iLogik = 0; iLogik < logiken.length; iLogik++) {
				Logik__Interface logik = logiken[iLogik];

				if (klugheit.istSoKlug(logik.gibLogikID())) {

					List<TipInfo> ignorierTips = null;
					if (tipKurier != null) {
						ignorierTips = tipKurier.gibIgnorierTips(logik.gibLogikID());
					}

					// Die Logik ablaufen lassen
					// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
					LogikErgebnis logikErgebnis = logik.laufen(tipKurier != null, ignorierTips);
					// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

					if (logikErgebnis != null) {
						// Tip setzen
						if (tipKurier != null) {
							// Es gibt Logikläufe, die gar keine entsprechende Logik-Situation vorfinden!
							if (logikErgebnis.tipInfo != null) {
								logikErgebnis.tipInfo.setzeSudoku(this.gibFeldInfos());
								tipKurier.add(logikErgebnis.tipInfo);
							}
						}

						// Bericht schreiben
						if (berichtSchreiber != null) {
							berichtSchreiber.addLogik(logik.gibLogikID(), logikErgebnis.gruppenLaeufeListe,
									logikErgebnis.eintrag, logikErgebnis.loeschZahlen);
						}

						// Das Löschen der vorgeschlagenen möglichen Zahlen realisieren
						if (logikErgebnis.loeschZahlen != null) {
							for (FeldNummerMitZahl loeschZahl : logikErgebnis.loeschZahlen) {
								Feld loeschFeld = this.gibFeld(loeschZahl.gibFeldNummer());
								loeschFeld.loescheUnmoeglicheZahl(loeschZahl.gibZahl());
							}
						}

						// Bewertung des Eintrags
						if (logikErgebnis.eintrag != null) {
							eintrag = logikErgebnis.eintrag;
							// Ein für einen Eintrag vorgesehenes Feld besitzt im Allgemeinen mehr als nur eine mögliche Zahl:
							// Z.B. sieht Logik O1 in einer Gruppe die 8 einzig im einem Feld. 
							// Da können in diesem Feld noch andere mögliche Zahlen drin sein.
							// Es muss noch dafür gesorgt werden, dass dieses Feld nur einzig die Eintrags-Zahl
							// als mögliche Zahl besitzt:
							Feld feld = this.gibFeld(eintrag.gibFeldNummer());
							feld.setzeMoeglichEinzig(eintrag.gibZahl());

							// Ein gefundene Zahl, die als Eintrag im folgenden gesetzt werden kann:
							// Genau das war gewollt.
							break; // for
						}

						// Bewertung der gelöschten Zahlen
						if (logikErgebnis.loeschZahlen != null) {
							anzahlGeloeschterZahlen = logikErgebnis.loeschZahlen.size();
							if (anzahlGeloeschterZahlen > 0) {
								// Nach gelöschten Zahlen soll wieder ab der einfachsten Logik begonnen werden
								break; // for
							}
						}
					} // if (logikErgebnis != null) {
				} // if (klugheit.istSoKlug

			} // for (int iLogik

		} // while (true)

		Problem problem = sucheProblem();

		if (berichtSchreiber != null) {
			berichtSchreiber.addEnde(problem, eintrag);
			berichtSchreiber.systemOut();
			if (berichtKurier != null) {
				berichtKurier.nimmLogikBericht(berichtSchreiber.gibBericht());
			}
		}
		if (tipKurier != null) {
			this.tipKurier.add(new TipInfoEnde(problem, eintrag, this.gibFeldInfos()));
		}

		berichtKurier = null;
		tipKurier = null;

		// Falls es kein Problem gibt:
		if (problem == null) {
			if (feldPaareSetzen) {
				setzeFeldPaare();
			}
			return new SetzeMoeglicheErgebnis(eintrag);
		}

		return new SetzeMoeglicheErgebnis(problem);
	}

}
