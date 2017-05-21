package sudoku.logik;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.logik.bericht.GruppenLaeufeListe;
import sudoku.logik.tipinfo.EinTipText;
import sudoku.logik.tipinfo.TipInfo;
import sudoku.logik.tipinfo.TipInfo0;

abstract class LogikFeldPaareRing implements Logik__Interface {
	/**
	 * Wenn diese Zahl == anzahlFeldPaare ist, erfolgt systemOut()
	 */
	static private int istSystemOut = 0;

	static private FeldNummerListe gibMitspieler(Gruppe[] gruppen) {
		FeldNummerListe mitspieler = new FeldNummerListe();
		for (int i = 0; i < gruppen.length; i++) {
			FeldNummerListe fNL = new FeldNummerListe(gruppen[i]);
			mitspieler.addAll(fNL);
		}
		return mitspieler;
	}

	/**
	 * Diese Logik wird nur im TipInfo benötigt. Sie steht deshalb hier, weil die interne Klasse keine statischen Methoden besitzen kann. 
	 * @param linien
	 * @param senkrechte
	 * @return Alle FeldNummern der an der Logik beteiligten Gruppen
	 */
	static private FeldNummerListe gibMitspieler(Gruppe[] linien, Gruppe[] senkrechte) {
		FeldNummerListe mitspieler = new FeldNummerListe();
		FeldNummerListe fNLinien = gibMitspieler(linien);
		FeldNummerListe fNSenkrechte = gibMitspieler(senkrechte);

		mitspieler.addAll(fNLinien);
		mitspieler.addAll(fNSenkrechte);
		return mitspieler;
	}

	/**
	 * @param paarFeldNummern
	 * @param typZu0
	 * @return FeldNummern, deren Zeile bzw. Spalte entsprechend typZu0 0 ist.
	 */
	static private FeldNummerListe gibMit0(FeldNummerListe paarFeldNummern, Gruppe.Typ typZu0) {
		FeldNummerListe feldNummern0 = new FeldNummerListe();

		for (int i = 0; i < paarFeldNummern.size(); i++) {
			FeldNummer paarFeldNummer = paarFeldNummern.get(i);
			FeldNummer feldNummer0 = Gruppe.gibFeldNummerMit0(paarFeldNummer, typZu0);

			feldNummern0.add(feldNummer0);
		}
		return feldNummern0;
	}

	/**
	 * @param feldPaare
	 * @param linien
	 * @return Die Linien, die durch die FeldPaare benannt sind.
	 */
	static private Gruppe[] gibRingGruppen(FeldPaar[] feldPaare, ArrayList<Gruppe> linien) {
		ArrayList<Gruppe> feldPaareLinien = new ArrayList<>();
		Gruppe linie0 = linien.get(0);
		// Gruppe.Typ linienTyp = linie0.gibTyp();
		Gruppe.Typ senkrechteTyp = linie0.gibTypDerSenkrechten();
		FeldNummerListe paarFeldNummern = gibFeldNummern(feldPaare);
		FeldNummerListe testFeldNummern = gibMit0(paarFeldNummern, senkrechteTyp);

		for (int iTestNummer = 0; iTestNummer < testFeldNummern.size(); iTestNummer++) {
			FeldNummer testFeldNummer = testFeldNummern.get(iTestNummer);
			for (int iLinie = 0; iLinie < linien.size(); iLinie++) {
				Gruppe linie = linien.get(iLinie);
				FeldNummer linieFeldNummer = linie.get(0).gibFeldNummer();
				if (linieFeldNummer.equals(testFeldNummer)) {
					if (!feldPaareLinien.contains(linie)) {
						feldPaareLinien.add(linie);
					}
				}
			}
		}

		Gruppe[] feldPaareLinienArray = new Gruppe[feldPaareLinien.size()];
		return feldPaareLinien.toArray(feldPaareLinienArray);
	}

	/**
	 * @param zahl Die Zahl des FeldPaareRinges
	 * @param senkrechte Die senkrechten Gruppen: Spalten bzw. Zeilen
	 * @param feldNummern Die FeldNummern des FeldPaareRinges
	 * @return Die zum Löschen vorgeschlagenen Zahlen oder null wenn es keine solche gibt.
	 */
	static private ZahlenListe gibLoeschZahlen(int zahl, Gruppe[] senkrechte, FeldNummerListe feldNummern) {
		FeldListe zahlFelder = new FeldListe();
		for (int iSenkrecht = 0; iSenkrecht < senkrechte.length; iSenkrecht++) {
			Gruppe gruppe = senkrechte[iSenkrecht];
			FeldListe felderDerZahl = gruppe.gibFelderDerMoeglichenZahl(zahl);
			zahlFelder.addAll(felderDerZahl);
		}

		ZahlenListe loeschZahlen = new ZahlenListe();
		for (int iZahlFeld = 0; iZahlFeld < zahlFelder.size(); iZahlFeld++) {
			Feld feld = zahlFelder.get(iZahlFeld);
			if (!feldNummern.contains(feld.gibFeldNummer())) {
				loeschZahlen.add(new FeldNummerMitZahl(feld.gibFeldNummer(), zahl));
			}
		}

		if (loeschZahlen.isEmpty()) {
			loeschZahlen = null;
		}
		return loeschZahlen;
	}

	static private boolean istErgebnisIgnorieren(List<TipInfo> ignorierTips, TipInfoRingFeldPaare tipInfoLogik) {
		if (ignorierTips == null) {
			return false;
		}
		if (ignorierTips.isEmpty()) {
			return false;
		}

		for (TipInfo tipInfo : ignorierTips) {
			TipInfoRingFeldPaare ignorierTip = (TipInfoRingFeldPaare) tipInfo;
			if (ignorierTip.zahl == tipInfoLogik.zahl) {
				boolean gleicheAktive = ignorierTip.gibAktiveFelder().istGleicherInhalt(tipInfoLogik.gibAktiveFelder());
				if (gleicheAktive) {
					return true;
				}
			}
		}
		return false;
	}

	static private FeldNummerListe gibFeldNummern(FeldPaar feldPaar) {
		FeldNummerListe feldNummernDerZahl = new FeldNummerListe();
		feldNummernDerZahl.add(feldPaar.feld1.gibFeldNummer());
		feldNummernDerZahl.add(feldPaar.feld2.gibFeldNummer());
		return feldNummernDerZahl;
	}

	/**
	 * @param feldPaare
	 * @return Alle FeldNummern der FeldPaare. Jede FeldNummer nur einmal.
	 */
	static private FeldNummerListe gibFeldNummern(FeldPaar[] feldPaare) {
		FeldNummerListe feldNummern = new FeldNummerListe();
		for (int iFeldPaar = 0; iFeldPaar < feldPaare.length; iFeldPaar++) {
			FeldNummerListe paarFeldNummern = gibFeldNummern(feldPaare[iFeldPaar]);
			for (int iFeldNummer = 0; iFeldNummer < paarFeldNummern.size(); iFeldNummer++) {
				FeldNummer feldNummer = paarFeldNummern.get(iFeldNummer);
				if (!feldNummern.contains(feldNummer)) {
					feldNummern.add(feldNummer);
				}
			}
		}
		return feldNummern;
	}

	/**
	 * @param ringInfo Alle nötigen Infos zum bereits erkannten noch unvollständigen Ring.
	 * @return true wenn es sich um einen Ring handelt.
	 */
	static private boolean istRingKomplett(ArrayList<FeldPaar> ringFeldPaare, FeldNummer anfang, FeldNummer ende,
			ArrayList<FeldPaar> suchFeldPaare) {
		while (true) {
			if (suchFeldPaare.size() == 1) {
				// Es fehlt noch der Ring-Abschluss
				FeldPaar feldPaar = suchFeldPaare.get(0);
				FeldNummer feldNummer1 = feldPaar.feld1.gibFeldNummer();
				FeldNummer feldNummer2 = feldPaar.feld2.gibFeldNummer();
				boolean istEndeFeldPaar = (feldNummer1.equals(anfang) & feldNummer2.equals(ende))
						| (feldNummer1.equals(ende) & feldNummer2.equals(anfang));
				return istEndeFeldPaar;
			} else {
				// Es fehlen noch mehr als ein Feldpaar: Fortsetzung versuchen zu finden
				for (FeldPaar feldPaar : suchFeldPaare) {
					Gruppe.Typ linienTyp = feldPaar.gruppe.gibTyp();
					FeldNummer feldNummer1 = feldPaar.feld1.gibFeldNummer();
					FeldNummer feldNummer2 = feldPaar.feld2.gibFeldNummer();

					boolean istFortsetzung = feldNummer1.equals(anfang);
					// Auf deutsch: Auf der Senkrechten des Anfang befindet sich FeldNummer1.
					// Also ist der neu Anfang die Senkrechte der FeldNummer2.
					if (istFortsetzung) {
						// Fortsetzung am Anfang mit feldNummer2
						anfang = Gruppe.gibFeldNummerMit0(feldNummer2, linienTyp);
						ringFeldPaare.add(feldPaar);
						suchFeldPaare.remove(feldPaar);
						break; // for
					}

					istFortsetzung = feldNummer1.equals(ende);
					if (istFortsetzung) {
						// Fortsetzung am Ende mit feldNummer2
						ende = Gruppe.gibFeldNummerMit0(feldNummer2, linienTyp);
						ringFeldPaare.add(feldPaar);
						suchFeldPaare.remove(feldPaar);
						break; // for
					}

					istFortsetzung = feldNummer2.equals(anfang);
					if (istFortsetzung) {
						// Fortsetzung am Anfang mit feldNummer1
						anfang = Gruppe.gibFeldNummerMit0(feldNummer1, linienTyp);
						ringFeldPaare.add(feldPaar);
						suchFeldPaare.remove(feldPaar);
						break; // for
					}

					istFortsetzung = feldNummer2.equals(ende);
					if (istFortsetzung) {
						// Fortsetzung am Ende mit feldNummer1
						ende = Gruppe.gibFeldNummerMit0(feldNummer1, linienTyp);
						ringFeldPaare.add(feldPaar);
						suchFeldPaare.remove(feldPaar);
						break; // for
					}

					// Dieses FeldPaar bietet keine Fortsetzung
					return false;
				} // for (FeldPaar feldPaar : suchFeldPaare) {
			} // else
		} // while true
	}

	/**
	 * @param feldPaare genau die richtige Anzahl FeldPaare 
	 * @return true wenn es sich um einen FeldPaare-Ring handelt.
	 */
	static private boolean istRing(FeldPaar[] feldPaare) {
		FeldPaar basisFeldPaar = feldPaare[0];
		Gruppe.Typ linienTyp = feldPaare[0].gruppe.gibTyp();
		FeldNummer anfang = basisFeldPaar.feld1.gibFeldNummer();
		anfang = Gruppe.gibFeldNummerMit0(anfang, linienTyp);

		FeldNummer ende = basisFeldPaar.feld2.gibFeldNummer();
		ende = Gruppe.gibFeldNummerMit0(ende, linienTyp);

		ArrayList<FeldPaar> ringFeldPaare = new ArrayList<>();
		ringFeldPaare.add(basisFeldPaar);

		ArrayList<FeldPaar> suchFeldPaare = new ArrayList<>();
		for (int i = 1; i < feldPaare.length; i++) {
			suchFeldPaare.add(feldPaare[i]);
		}

		// Der Ring besteht jetzt zunächst nur aus dem Basis-FeldPaar
		boolean istRingKomplett = istRingKomplett(ringFeldPaare, anfang, ende, suchFeldPaare);
		return istRingKomplett;
	}

	/**
	 * @param feldPaare
	 * @param anzahlRingFeldPaare
	 * @return Alle möglichen Kombinationen der FeldPaare, die einen Ring bilden könnten.
	 * 				Jede Kombination besitzt genau anzahlFeldPaare FeldPaare. 
	 */
	static private ArrayList<FeldPaar[]> gibMoeglicheRinge(ArrayList<FeldPaar> feldPaare, int anzahlRingFeldPaare) {
		int[] teilnehmer = new int[feldPaare.size()];
		for (int i = 0; i < teilnehmer.length; i++) {
			teilnehmer[i] = i;
		}

		ArrayList<int[]> kombinationen = Kombinationen.gibAlleKombinationen(teilnehmer, anzahlRingFeldPaare);
		ArrayList<FeldPaar[]> moeglicheRinge = new ArrayList<>();

		for (int iKombination = 0; iKombination < kombinationen.size(); iKombination++) {
			int[] kombination = kombinationen.get(iKombination);
			FeldPaar[] moeglicherRing = new FeldPaar[kombination.length];
			for (int i = 0; i < kombination.length; i++) {
				int feldPaarIndex = kombination[i];
				moeglicherRing[i] = feldPaare.get(feldPaarIndex);
			}
			moeglicheRinge.add(moeglicherRing);
		}
		return moeglicheRinge;
	}

	// =========================================================
	private class TipInfoRingFeldPaare extends TipInfo0 {

		final int zahl;
		final FeldNummerListe ringFeldNummern;
		final Gruppe[] linien;
		final Gruppe[] senkrechte;
		final ZahlenListe loeschZahlen;

		private TipInfoRingFeldPaare(Logik_ID logik, int zahl, FeldNummerListe ringFeldNummern, Gruppe[] linien,
				Gruppe[] senkrechte, ZahlenListe loeschZahlen) {
			super(logik, LogikFeldPaareRing.gibMitspieler(linien, senkrechte));
			this.zahl = zahl;
			this.ringFeldNummern = ringFeldNummern;
			this.linien = linien;
			this.senkrechte = senkrechte;
			this.loeschZahlen = loeschZahlen;
		}

		public EinTipText[] gibTip() {
			ArrayList<EinTipText> texte = new ArrayList<>();
			{
				String s1 = String.format("Die mögliche Zahl %d muss %s", zahl, Gruppe.gibInText(linien, false));
				String s2 = "in einem der beiden jeweils möglichen Felder stehen.";
				EinTipText tipText1 = new EinTipText(s1, s2);
				texte.add(tipText1);
				s1 = "Die genannten Linien sind über ihre Senkrechten miteinander verbunden.";
				tipText1 = new EinTipText(s1, null);
				texte.add(tipText1);
				s1 = String.format("Daher muss die mögliche Zahl %d auch in den Senkrechten ", zahl);
				s2 = "in genau einem von beiden Feldern stehen.";
				tipText1 = new EinTipText(s1, s2);
				texte.add(tipText1);
			}
			{
				String s1 = String.format("Deshalb wird die Zahl %d ansonsten ", zahl);
				String s2 = String.format("%s gelöscht.", Gruppe.gibInText(senkrechte, false));
				EinTipText tipText1 = new EinTipText(s1, s2);
				texte.add(tipText1);
			}

			EinTipText[] texteArr = texte.toArray(new EinTipText[texte.size()]);
			return texteArr;
		}

		@Override
		public FeldNummerListe gibAktiveFelder() {
			return ringFeldNummern;
		}

		@Override
		public ZahlenListe gibLoeschZahlen() {
			return loeschZahlen;
		}

		@Override
		public boolean istZahl() {
			return false;
		}

		@Override
		public FeldNummerMitZahl gibZahlFeld() {
			return null;
		}

	}

	// =========================================================
	private final int anzahlRingFeldPaare;
	private final ArrayList<Gruppe> zeilen;
	private final ArrayList<Gruppe> spalten;

	public LogikFeldPaareRing(int anzahlRingFeldPaare, ArrayList<Gruppe> zeilen, ArrayList<Gruppe> spalten) {
		this.anzahlRingFeldPaare = anzahlRingFeldPaare;
		this.zeilen = zeilen;
		this.spalten = spalten;
	}

	@Override
	public String[] gibWo() {
		String s = String
				.format("Auf %d parallelen Zeilen. (Alles gesagte gilt auch für %d parallele Spalten und Zeilen als Senkrechte.)",
						anzahlRingFeldPaare, anzahlRingFeldPaare);
		return new String[]{s};
	}

	@Override
	public String[] gibSituationAbstrakt() {
		return new String[]{"1 Zahl ist nur an 2 Orten je Linie möglich.", 
				"Die Linien sind über gleiche Orte der Zahlen über ihre Senkrechten verbunden."};
	}

	@Override
	public String[] gibSituation() {
		return new String[]{"1 Zahl ist nur auf 2 Feldern je Zeile möglich.",
				"Je zwei der Felder der relevanten Zeilen befinden sich auf den gleichen Spalten."};
	}

	@Override
	public String gibErgebnis() {
		return "Außer in den betrachteten Feldern kann die mögliche Zahl in den betreffenden Spalten gelöscht werden.";
	}

	private void systemOut(String s) {
		if (istSystemOut == this.anzahlRingFeldPaare) {
			System.out.println(s);
		}
	}

	/**
	 * @param gruppenLaeufeListe Die ist mitzuschreiben (für die Zeitermittlung)
	 * @param linien Die Zeilen bzw. Spalten, die auf FeldPaare (Genau 2 Felder mit 1 möglichen Zahl in der Gruppe) zu testen sind
	 * @param senkrechte Die Spalten bzw. Zeilen, die die Senkrechten zu den Linien sind.
	 * @param ignorierTips falls != null soll die TipInfo eventuell ignoriert werden.
	 * @return null oder TipInfo falls die Logik erfolgreich war
	 */
	private TipInfo gibInfo(GruppenLaeufeListe gruppenLaeufeListe, ArrayList<Gruppe> linien,
			ArrayList<Gruppe> senkrechte, List<TipInfo> ignorierTips) {

		// FeldPaare-Map holen
		Map<Integer, ArrayList<FeldPaar>> zahlenPartner = FeldPaar.gibZahlenPartner(linien);
		// Zahlen, die FeldPaare besitzen, bereitstellen
		Set<Integer> moeglicheZahlen = zahlenPartner.keySet();

		for (Integer zahl : moeglicheZahlen) {
			// gruppenLaeufeListe mitschreiben
			gruppenLaeufeListe.add(linien.get(0).gibTyp());

			// Ring-FeldPaare bereitstellen
			ArrayList<FeldPaar> zahlFeldPaare = zahlenPartner.get(zahl);
			if (zahlFeldPaare.size() >= anzahlRingFeldPaare) {

				ArrayList<FeldPaar[]> moeglicheRinge = gibMoeglicheRinge(zahlFeldPaare, anzahlRingFeldPaare);
				systemOut(String
						.format("Für die Zahl %d: Notwendige Anzahl FeldPaare = %d. Anzahl FeldPaare = %d. Anzahl möglicher Ringe = %d",
								zahl, anzahlRingFeldPaare, zahlFeldPaare.size(), moeglicheRinge.size()));

				for (FeldPaar[] moeglicherRing : moeglicheRinge) {
					if (istRing(moeglicherRing)) {
						Gruppe[] linienArray = gibRingGruppen(moeglicherRing, linien);
						Gruppe[] senkrechteArray = gibRingGruppen(moeglicherRing, senkrechte);
						FeldNummerListe ringFeldNummern = gibFeldNummern(moeglicherRing);

						systemOut(String.format("Die Zahl %d insgesamt in den Feldern %s", zahl, ringFeldNummern));

						ZahlenListe loeschZahlen = gibLoeschZahlen(zahl, senkrechteArray, ringFeldNummern);
						if (loeschZahlen != null) {
							// Logik war erfolgreich!
							systemOut(String.format("Die Zahl %d wird gelöscht: %s", zahl, loeschZahlen));
							TipInfoRingFeldPaare tipInfo = new TipInfoRingFeldPaare(this.gibLogikID(), zahl,
									ringFeldNummern, linienArray, senkrechteArray, loeschZahlen);
							boolean ergebnisIgnorieren = istErgebnisIgnorieren(ignorierTips, tipInfo);

							if (!ergebnisIgnorieren) {
								return tipInfo;
							}
						}
					}
				}
			} // for iZahl
		}
		return null;
	}

	@Override
	public LogikErgebnis laufen(boolean istTip, List<TipInfo> ignorierTips) throws Exc {
		ArrayList<Gruppe> freieZeilen = Gruppe.gibFreieGruppen(zeilen, 2);
		ArrayList<Gruppe> freieSpalten = Gruppe.gibFreieGruppen(spalten, 2);

		if ((!freieZeilen.isEmpty()) & (!freieSpalten.isEmpty())) {
			systemOut(String.format("%s%d %d relevante Zeilen, %d relevante Spalten", getClass().getName(),
					anzahlRingFeldPaare, freieZeilen.size(), freieSpalten.size()));
			GruppenLaeufeListe gruppenLaeufeListe = new GruppenLaeufeListe(freieZeilen.get(0).gibTyp());

			TipInfo tipInfo = gibInfo(gruppenLaeufeListe, freieZeilen, freieSpalten, ignorierTips);
			if (tipInfo == null) {
				tipInfo = gibInfo(gruppenLaeufeListe, freieSpalten, freieZeilen, ignorierTips);
			}

			if (tipInfo != null) {
				LogikErgebnis logikErgebnis = new LogikErgebnis(gruppenLaeufeListe, null, tipInfo.gibLoeschZahlen(),
						tipInfo);
				return logikErgebnis;
			}
			return new LogikErgebnis(gruppenLaeufeListe);
		} // if ((!freieZeilen.isEmpty()) & (!freieSpalten.isEmpty())) {
		return null;
	}

}
