package sudoku.logik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenFeldNummern;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.logik.bericht.GruppenLaeufeListe;
import sudoku.logik.tipinfo.EinTipText;
import sudoku.logik.tipinfo.TipInfo;
import sudoku.logik.tipinfo.TipInfo0;

/**
 * @author heroe
 * Die Logik hat (fast) gar nichts mit FeldPaaren zu tun:
 *  1. Es geht einzig und allein um Felder, die 2 mögliche Zahlen besitzen.
 *  2. Diese Felder sind über eine gemeinsame Zahl miteinander  innerhalb einer Gruppe verbunden.
 * Die Forderung der Feldpaare, dass eine mögliche Zahl nur zweimal je Gruppe auftreten darf, gibt es hier jedenfalls nicht!
 * 
 * Diese Logik sucht für einen Tip die kürzeste Kette. Im Falle, dass die Logik nicht für einen Tip läuft,
 * nutzt sie die erste beste gefundene Kette. Dadurch kann es (meist) sein, dass in beiden Fällen 
 * verschiedene mögliche Zahlen (bzw. vorgeschlagene Einträge) als Ergebnis erscheinen, wass ein bischen irretieren könnte. 
 * Aber der Lösung des Sudoku schadet das garnichts!
 */
class Logik_Auswirkungskette implements Logik__Interface {
	static private boolean istSystemOut = false;

	static private void systemOut(String s) {
		if (istSystemOut) {
			System.out.println(s);
		}
	}

	static private void systemOut(ArrayList<FeldPaar> feldPaare) {
		if (!istSystemOut) {
			return;
		}

		System.out.println(Logik_Auswirkungskette.class.getSimpleName() + ": FeldPaare");
		for (FeldPaar feldPaar : feldPaare) {
			String sMoegliche1 = feldPaar.feld1.gibMoegliche().toString();
			String sMoegliche2 = feldPaar.feld2.gibMoegliche().toString();
			systemOut(String.format("%s: Die Zahl %d verbindet Feld%s(%s) mit Feld%s(%s)",
					feldPaar.gruppe.gibInText(true), feldPaar.zahl, feldPaar.feld1.gibFeldNummer(), sMoegliche1,
					feldPaar.feld2.gibFeldNummer(), sMoegliche2));

		}
	}

	static private void systemOut(Map<FeldNummer, ZahlenFeldNummern> feldPartner) {
		if (!istSystemOut) {
			return;
		}

		System.out.println(Logik_Auswirkungskette.class.getSimpleName() + ": FeldPartner");
		Set<FeldNummer> feldNummernSet = feldPartner.keySet();
		FeldNummer[] feldNummernArray = new FeldNummer[feldNummernSet.size()];
		feldNummernSet.toArray(feldNummernArray);
		Arrays.sort(feldNummernArray);

		for (int iFeldNummer = 0; iFeldNummer < feldNummernArray.length; iFeldNummer++) {
			FeldNummer feldNummer = feldNummernArray[iFeldNummer];
			ZahlenFeldNummern partner = feldPartner.get(feldNummer);
			System.out.println(String.format("Feld%s mit den Partnern: %s", feldNummer, partner));
		}
	}

	static private String gibKettenVorspann(ZahlenListe kette) {
		String vorspann = "";
		for (int iKettenGlied = 1; iKettenGlied < kette.size(); iKettenGlied++) {
			vorspann += "  ";
		}
		vorspann = String.format("%sTiefe %d:: ", vorspann, kette.size());
		return vorspann;
	}

	/**
	 * @param kette Wird ausgegeben
	 * @param titel
	 */
	static private void systemOut(ZahlenListe kette, boolean mitVorspann, String titel) {
		if (!istSystemOut) {
			return;
		}

		if (mitVorspann) {
			System.out.print(gibKettenVorspann(kette));
		}
		for (int iKettenGlied = 0; iKettenGlied < kette.size(); iKettenGlied++) {
			FeldNummerMitZahl kettenGlied = kette.get(iKettenGlied);
			System.out.print(String.format("%d->%s ", kettenGlied.gibZahl(), kettenGlied.gibFeldNummer()));
		}
		System.out.print(Logik_Auswirkungskette.class.getSimpleName() + ": " + titel);
		System.out.println();
	}

	static private Map<FeldNummer, ZahlenFeldNummern> gibFeldPartner(ArrayList<FeldPaar> feldPaare) {
		Map<FeldNummer, ZahlenFeldNummern> feldPartner = new HashMap<FeldNummer, ZahlenFeldNummern>();

		for (FeldPaar feldPaar : feldPaare) {
			FeldNummer feldNummer1 = feldPaar.feld1.gibFeldNummer();
			FeldNummer feldNummer2 = feldPaar.feld2.gibFeldNummer();
			ZahlenFeldNummern partnerDesFeld1 = feldPartner.get(feldNummer1);
			if (partnerDesFeld1 == null) {
				partnerDesFeld1 = new ZahlenFeldNummern();
				feldPartner.put(feldNummer1, partnerDesFeld1);
			}
			partnerDesFeld1.addNurNeue(feldPaar.zahl, feldNummer2);

			ZahlenFeldNummern partnerDesFeld2 = feldPartner.get(feldNummer2);
			if (partnerDesFeld2 == null) {
				partnerDesFeld2 = new ZahlenFeldNummern();
				feldPartner.put(feldNummer2, partnerDesFeld2);
			}
			partnerDesFeld2.addNurNeue(feldPaar.zahl, feldNummer1);
		}
		return feldPartner;
	}

	/**
	 * @param vorhandeneKette Die bereits zusammengestellten Kettenglieder: 
	 * 				Jedes Kettenglied benennt die Nummer des Feldes und die Zahl, die hierher geführt hat.
					Das erste Kettenglied (auf Index 0) besitzt die Zahl, die dann der Eintrag werden müsste.
	 * @param feldPartner Alle Partner von Feldern, die als ein Kettenglied infrage kommen könnten
	 * @param sudoku Gibt Felder für FeldNummern
	 * @return Eine Kette oder null, falls keine gefunden werden konnte.
	 * @throws Exc 
	 */
	static private ZahlenListe gibKette(ZahlenListe vorhandeneKette, Map<FeldNummer, ZahlenFeldNummern> feldPartner)
			throws Exc {
		if (vorhandeneKette.size() > 80) {
			throw Exc.endlosSchleife();
		}
		FeldNummerMitZahl kettenStart = vorhandeneKette.get(0);
		FeldNummerMitZahl aktuell = vorhandeneKette.get(vorhandeneKette.size() - 1);
		FeldNummer aktuelleFeldNummer = aktuell.gibFeldNummer();
		int herkunftsZahl = aktuell.gibZahl();

		// > 3: Eine Auswirkungskette besitzt stets mindestens 4 Felder,
		// denn die beiden Nachbar-Felder des Ende-Feldes müssen dieselbe Zahl als Eintrag erhalten.
		// Damit können die beiden Nachbar-Felder nicht innerhalb eines Kastens liegen.
		if ((vorhandeneKette.size() > 3) & aktuelleFeldNummer.equals(kettenStart.gibFeldNummer())) {
			// Der Kreis ist komplett
			if (herkunftsZahl != kettenStart.gibZahl()) {
				// Auswirkungskette ! (?)
				systemOut(vorhandeneKette, true, "Auswirkungskette komplett");
				return vorhandeneKette;
			} else {
				systemOut(vorhandeneKette, true, "Der Kreis ist komplett, aber keine Auswirkungskette");
				return null;
			}
		}

		ZahlenFeldNummern aktuellePartner = feldPartner.get(aktuelleFeldNummer);
		// Wir können hier über ein FeldPaar auf einem Feld gelandet sein, dass nicht genau zwei mögliche Zahlen besitzt!
		// ArrayList<Integer> moegliche = aktuellesFeld.gibMoegliche();
		// if (moegliche.size() != 2) {
		// systemOut(vorhandeneKette, String.format(
		// "Vorhandene Kette führt auf eine Feld, das nicht genau 2 mögliche Zahlen besitzt, sondern %d",
		// moegliche.size()));
		// return null;
		// }

		// Die Zahl von den genau 2 möglichen Zahlen, die auf dem Weg weiterführen könnte, ermitteln.
		int[] zahlen = aktuellePartner.gibZahlen();
		// Es gibt hier für jedes Feld Zahlen, mindestens eine.
		// Mit allen Zahlen, die verbinden
		for (int iZahl = 0; iZahl < zahlen.length; iZahl++) {
			int wegZahl = zahlen[iZahl];
			if (wegZahl != herkunftsZahl) {
				// Hier kann es weitergehen
				FeldNummerListe zielFeldNummern = aktuellePartner.gibFeldNummern(wegZahl);
				if (istSystemOut) {
					String sZiele = "";
					if (zielFeldNummern != null) {
						sZiele = zielFeldNummern.gibKette("+");
					}
					String s = String.format(" ==> Ziele mit WegZahl %d: %s", wegZahl, sZiele);
					systemOut(vorhandeneKette, true, s);
				}

				if (zielFeldNummern != null) {
					for (FeldNummer zielFeldNummer : zielFeldNummern) {
						ZahlenListe kette = new ZahlenListe(vorhandeneKette);
						FeldNummerMitZahl testZiel = new FeldNummerMitZahl(zielFeldNummer, wegZahl);
						// systemOut(String.format("   Idee für TestZiel %s", testZiel));
						if (kette.contains(testZiel)) {
							if (istSystemOut) {
								systemOut(String.format("%sTestZiel %s ist schon Ketten-Mitglied",
										gibKettenVorspann(kette), testZiel));
							}
						} else {
							systemOut(String.format("%sVersuch mit TestZiel %s", gibKettenVorspann(kette), testZiel));
							kette.add(testZiel);
							kette = gibKette(kette, feldPartner);
							if (kette != null) {
								return kette;
							}
						}
					}
				}
			}
		}

		systemOut(vorhandeneKette, true, "Vorhandene Kette führt mit nicht weiter");
		return null;
	}

	/**
	 * @param feldPartner Alle Felder mit ihren Partnern, die jeweils in einer Kette sein könnten.
	 * @param istTip bei true wird die kürzestes Kette gesucht
	 * @param sudoku Gibt Felder für FeldNummern
	 * @return Eine Kette oder null, falls keine gefunden werden konnte.
	 * 				Die Kette startet und endet auf demselben Feld mit derselben Zahl. 
	 * 				Die andere mögliche Zahl des Star- bzw. Ende-Feldes ist dann schon der Vorschlag für einen Eintrag:
	 * 				Egal wie die Ketten-Felder, die weder Start noch Ende sind, belegt sind: 
	 * 				Die andere mögliche Zahl muss im Start- bzw. Ende-Feld gesetzt sein, wenn das Sudoku lösbar ist. 
	 * @throws Exc 
	 */
	static private ZahlenListe gibKette(Map<FeldNummer, ZahlenFeldNummern> feldPartner, boolean istTip,
			SudokuLogik sudoku) throws Exc {
		// Auf Index 0 steht die kürzeste Kette
		ArrayList<ZahlenListe> ketten = new ArrayList<>();

		// Alle Felder durchklappern
		Set<FeldNummer> feldNummernSet = feldPartner.keySet();
		for (FeldNummer feldNummer : feldNummernSet) {
			ZahlenFeldNummern partner = feldPartner.get(feldNummer);

			int[] zahlen = partner.gibZahlen();
			// Es gibt hier für jedes Feld Zahlen, mindestens eine.
			// Mit allen Zahlen, die verbinden
			for (int iZahl = 0; iZahl < zahlen.length; iZahl++) {
				int zahl = zahlen[iZahl];
				if (partner.gibFeldNummernAnzahl(zahl) >= 2) {
					// Dies Feld ist möglicherweise ein Ketten-Ende-Feld

					// Die StartZahl (die andere als Zahl) ermitteln
					Feld startFeld = sudoku.gibLogikFeld(feldNummer);
					ArrayList<Integer> moegliche = startFeld.gibMoegliche();
					int zahl1 = moegliche.get(0);
					int zahl2 = moegliche.get(1);
					int startZahl = zahl1;
					if (startZahl == zahl) {
						startZahl = zahl2;
					}

					FeldNummerMitZahl start = new FeldNummerMitZahl(feldNummer, startZahl);
					ZahlenListe testKette = new ZahlenListe(start);
					systemOut(testKette, true, " ==================> Ketten-Start");
					ZahlenListe kette = gibKette(testKette, feldPartner); // sudoku);
					if (kette != null) {
						if (istTip) {
							if (ketten.isEmpty()) {
								ketten.add(kette);
							} else {
								if (kette.size() < ketten.get(0).size()) {
									ketten.add(0, kette);
								} else {
									ketten.add(kette);
								}
							}
						} else {
							return kette;
						}
					}
					systemOut(testKette, true,
							" ==================> Es gibt keine Auswirkungs-Kette auf diesem Startfeld");
				} // if (partner.gibFeldNummernAnzahl(zahl) >= 2) {
			} // for (int iZahl = 0; iZahl < zahlen.length; iZahl++) {
		} // for (FeldNummer feldNummer : feldNummernSet) {

		if (ketten.isEmpty()) {
			return null;
		}

		if (istSystemOut) {
			systemOut(Logik_Auswirkungskette.class.getSimpleName() + ": Tip-Ketten");
			for (int iKette = 0; iKette < ketten.size(); iKette++) {
				systemOut(ketten.get(iKette), false, "");
			}
		}
		// kürzeste Kette
		return ketten.get(0);
	}

	// ===========================================================
	/**
	 * @author heroe
	 * Die übergebenen Felder sind alles Felder der einen Gruppe, die alle die eine mögliche Zahl besitzen.
	 * Die übergebenen Felder werden zu FeldPaaren kombiniert, wenn sie jeweils genau 2 mögliche Zahlen enthalten.
	 */
	class KettenFeldPaarGeber extends FeldPaarGeber {
		@Override
		ArrayList<FeldPaar> gibFeldPaare(Gruppe gruppe, int zahl, FeldListe felder) {
			FeldListe zweier = new FeldListe();
			for (Feld feld : felder) {
				if (feld.gibMoeglicheAnzahl() == 2) {
					zweier.add(feld);
				}
			}
			if (zweier.size() < 2) {
				// Wenn es nicht wenigstens zwei Felder der Zahl in der einen Gruppe gibt, gibt es hierin keine FeldPaare.
				return null;
			}

			int[] teilnehmer = new int[zweier.size()];
			for (int i = 0; i < teilnehmer.length; i++) {
				teilnehmer[i] = i;
			}

			// 2: Immer 2 Felder bilden ein FeldPaar
			ArrayList<int[]> kombinationen = Kombinationen.gibAlleKombinationen(teilnehmer, 2);

			ArrayList<FeldPaar> feldPaare = new ArrayList<>();
			for (int iKombination = 0; iKombination < kombinationen.size(); iKombination++) {
				int[] kombination = kombinationen.get(iKombination);
				// Also besitzt jede Kombination zwei Feld-Indizees
				Feld feld1 = zweier.get(kombination[0]);
				Feld feld2 = zweier.get(kombination[1]);
				FeldPaar feldPaar = new FeldPaar(gruppe, zahl, feld1, feld2);
				feldPaare.add(feldPaar);
			}

			if (!feldPaare.isEmpty()) {
				return feldPaare;
			}
			return null;
		}
	}

	private class TipInfoKette extends TipInfo0 {
		private FeldNummerMitZahl sollEintrag;
		private ZahlenListe kette;

		private TipInfoKette(FeldNummerMitZahl sollEintrag, ZahlenListe kette) {
			super(Logik_ID.AUSWIRKUNGSKETTE, new FeldNummerListe(kette));
			this.sollEintrag = sollEintrag;
			this.kette = kette;
		}

		public EinTipText[] gibTip() {
			String s1 = String.format("Im Feld%s ist einzig die Zahl %d möglich.", this.sollEintrag.gibFeldNummer(),
					sollEintrag.gibZahl());
			String s2 = String.format("Das bestimmt die auf diesem Feld%s startende und endende Kette:",
					this.sollEintrag.gibFeldNummer());
			String s3 = "Die Felder sind jeweils durch die zwischen ihnen genannte mögliche Zahl verbunden.";
			int verschlussZahl = kette.get(kette.size() - 1).gibZahl();
			FeldNummerMitZahl nachbarStart = kette.get(1);
			FeldNummerMitZahl nachbarEnde = kette.get(kette.size() - 2);
			String s4 = String.format("Das Ketten-Verschluß-Feld%s ist über die EINE Zahl %d ",
					this.sollEintrag.gibFeldNummer(), verschlussZahl);
			String s5 = String.format("mit seinen BEIDEN Nachbarn Feld%s und Feld%s verbunden.",
					nachbarStart.gibFeldNummer(), nachbarEnde.gibFeldNummer());
			String s6 = String.format(
					"Unabhängig von der Belegung der Kette ist im Feld%s einzig die Zahl %d möglich.",
					this.sollEintrag.gibFeldNummer(), sollEintrag.gibZahl());
			EinTipText[] sArray = new EinTipText[] { new EinTipText(s1, null), new EinTipText(s2, kette.toString()),
					new EinTipText(s3, null), new EinTipText(s4, s5), new EinTipText(s6, null) };
			return sArray;
		}

		@Override
		public FeldNummerListe gibAktiveFelder() {
			FeldNummerListe aktiveFelder = new FeldNummerListe();
			FeldNummerMitZahl f = gibZahlFeld();
			if (f != null) {
				aktiveFelder.add(f.gibFeldNummer());
			}
			return aktiveFelder;
		}

		@Override
		public ZahlenListe gibLoeschZahlen() {
			return null;
		}

		@Override
		public boolean istZahl() {
			return true;
		}

		@Override
		public FeldNummerMitZahl gibZahlFeld() {
			return this.sollEintrag;
		}

	}

	// ===========================================================
	private final ArrayList<Gruppe> gruppen;
	private final SudokuLogik sudoku;

	public Logik_Auswirkungskette(ArrayList<Gruppe> gruppen, SudokuLogik sudoku) {
		this.gruppen = gruppen;
		this.sudoku = sudoku;
	}

	@Override
	public Logik_ID gibLogikID() {
		return Logik_ID.AUSWIRKUNGSKETTE;
	}

	@Override
	public String gibKurzName() {
		return "AK";
	}

	@Override
	public String gibName() {
		return "Auswirkungskette";
	}

	@Override
	public String[] gibWo() {
		return new String[] { "Es handelt sich nur um Felder mit genau 2 möglichen Zahlen.",
				"Es handelt sich nur um Feldpaare:",
				"Es gibt in einer der 9-Felder-Gruppen eines Feldes mind. ein weiteres Feld mit einer der möglichen Zahlen des Feldes." };
	}

	@Override
	public String[] gibSituationAbstrakt() {
		return new String[] { "In einem Feld einer Kette von Feldern ist eine Zahl festgelegt",
				"unabhängig davon welche Alternative in einem anderen Feld der Kette gewählt wird." };
	}

	@Override
	public String[] gibSituation() {
		return new String[] { "Die Feldpaare bilden eine Kette. die geschlossen ist:",
				"- 'Normale' Kettenfelder liegen mit beiden möglichen Zahlen in der Kette.",
				"- Das Verschlußfeld liegt nur mit einer möglichen Zahl in der Kette.",
				"Über diese eine mögliche Zahl ist das Verschlussfeld Feldpaar zu seinen beiden Ketten-Nachbar-Feldern." };
	}

	@Override
	public String gibErgebnis() {
		return "Die nicht verkette mögliche Zahl des Verschlußfeldes ist ein Eintrag.";
	}

	@Override
	public double gibKontrollZeit1() {
		return 120;
	}

	@Override
	public LogikErgebnis laufen(boolean istTip, List<TipInfo> ignorierTips) throws Exc {
		KettenFeldPaarGeber feldPaarGeber = new KettenFeldPaarGeber();
		// Die Ketten-FeldPaare, die paareweise über eine Zahl (in einer Gruppe) verbunden sind.
		// Jedes der Felder besitzt genau zwei mögliche Zahlen.
		ArrayList<FeldPaar> feldPaare = FeldPaar.gibFeldPaare(gruppen, feldPaarGeber);
		systemOut(feldPaare);

		Map<FeldNummer, ZahlenFeldNummern> feldPartner = gibFeldPartner(feldPaare);
		systemOut(feldPartner);
		// ZahlenFeldNummern moeglicheKettenEnden = gibMoeglicheKettenEnden(feldPartner);
		// systemOut(moeglicheKettenEnden, "Mögliche Ketten-End-Felder");
		//
		ZahlenListe kette = gibKette(feldPartner, istTip, sudoku);

		if (kette != null) {
			systemOut(kette, false, "Auswirkungskette ist gefunden HURRAAAA");

			FeldNummerMitZahl sollEintrag = kette.get(0);
			TipInfo tipInfo = null;

			if (istTip) {
				tipInfo = new TipInfoKette(sollEintrag, kette);
			}

			// Hier kann man wohl nur eine einzige pauschale Zeit für diese Logik annehmen.
			// Die könnte höchstens von der Anzahl der freien Felder abhängen.
			GruppenLaeufeListe gruppenLaeufeListe = new GruppenLaeufeListe(null);
			LogikErgebnis ergebnis = new LogikErgebnis(gruppenLaeufeListe, sollEintrag, null, tipInfo);
			return ergebnis;
		}
		return null;
	}
}
