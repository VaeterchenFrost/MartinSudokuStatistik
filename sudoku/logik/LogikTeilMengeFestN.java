package sudoku.logik;

import java.util.ArrayList;
import java.util.List;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.logik.bericht.GruppenLaeufeListe;
import sudoku.logik.tipinfo.EinTipText;
import sudoku.logik.tipinfo.TipInfo;
import sudoku.logik.tipinfo.TipInfo0;

class LogikTeilMengeFestN implements Logik__Interface {
	static private boolean istSystemOut = false;

	static private void systemOut(String s) {
		if (istSystemOut) {
			System.out.println(s);
		}
	}

	private void systemOut2(String titel, ArrayList<Gruppe> gruppen) {
		systemOut(String.format("%s %s %d %s", titel, this.gibKurzName(), anzahlZahlen, this.getClass().getName()));
		if (gruppen == null) {
			return;
		}
		if (anzahlZahlen > 2) {
			return;
		}
		for (int i = 0; i < gruppen.size(); i++) {
			Gruppe gruppe = gruppen.get(i);
			systemOut(String.format("%d: Gruppe=%s", i, gruppe.gibTyp()));
		}
	}

	/**
	 * @param anzahlZahlen
	 * @return Die id f�r diese Logik. Diese Logiken m�ssen im enum Logik nicht
	 *         hintereinander stehen!
	 */
	static private Logik_ID gibLogikID(int anzahlZahlen) {
		String name2 = Logik_ID.TEILMENGEFEST2.name();

		String name = name2.substring(0, name2.length() - 1);
		name += anzahlZahlen;
		Logik_ID id = Logik_ID.valueOf(name);
		return id;
	}

	// =========================================================
	private class TipInfoTeilMengeFestN extends TipInfo0 {
		int anzahlZahlen;
		final Geschwister geschwister;
		final String textInGruppe;

		/**
		 * @param mitSpieler
		 *            Sind diejenigen der Geschwisterlogik - ohne die des
		 *            solistErgebnis
		 * @param geschwister
		 * @param textInGruppe
		 *            beschreibt die Gruppe in der die Geschwister gefunden
		 *            wurden
		 * @param infoSudoku
		 */
		private TipInfoTeilMengeFestN(Logik_ID logik, int anzahlZahlen, FeldNummerListe mitSpieler,
				Geschwister geschwister, String textInGruppe) {
			super(logik, mitSpieler);
			this.anzahlZahlen = anzahlZahlen;
			this.geschwister = geschwister;
			this.textInGruppe = textInGruppe;
		}

		public EinTipText[] gibTip() {
			ArrayList<EinTipText> texte = new ArrayList<>();
			Geschwister g1 = this.geschwister;
			ArrayList<Integer> zahlen = g1.gibZahlen();
			FeldNummerListe feldNummern = g1.gibFelder();

			String sZahlen = "";
			for (Integer zahl : zahlen) {
				if (sZahlen.isEmpty()) {
					sZahlen += zahl;
				} else {
					sZahlen += "+" + zahl;
				}
			}
			String sFeldNummern = feldNummern.gibKette("+");
			String s1 = String.format("%s besitzen die %d Felder %s", textInGruppe, anzahlZahlen, sFeldNummern);
			String s2 = String.format("unbedingt die %d Zahlen %s.", anzahlZahlen, sZahlen);
			EinTipText tipText = new EinTipText(s1, s2);
			texte.add(tipText);

			String s3 = "In den anderen Feldern der Gruppe werden diese Zahlen gel�scht.";
			EinTipText tipText2 = new EinTipText(s3, "");
			texte.add(tipText2);

			EinTipText[] texteArr = texte.toArray(new EinTipText[texte.size()]);
			return texteArr;
		}

		@Override
		public FeldNummerListe gibAktiveFelder() {
			return geschwister.gibFelder();
		}

		@Override
		public ZahlenListe gibLoeschZahlen() {
			return geschwister.gibLoeschZahlen();
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
	private final Logik_ID idLogik;
	private final int anzahlZahlen;
	private final ArrayList<Gruppe> gruppen;

	public LogikTeilMengeFestN(int anzahlZahlen, ArrayList<Gruppe> gruppen) {
		this.idLogik = gibLogikID(anzahlZahlen);
		this.anzahlZahlen = anzahlZahlen;
		this.gruppen = gruppen;
		systemOut2("Konstruktor", gruppen);
	}

	@Override
	public Logik_ID gibLogikID() {
		return idLogik;
	}

	@Override
	public String gibKurzName() {
		return "T" + anzahlZahlen;
	}

	@Override
	public String gibName() {
		String s = String.format("Teilmenge ist fest f�r %d Zahlen", anzahlZahlen);
		return s;
	}

	@Override
	public String[] gibWo() {
		return new String[] { "In einer Gruppe (Zeile, Spalte bzw. Kasten)" };
	}

	@Override
	public String[] gibSituationAbstrakt() {
		String s = String.format("Die Teilmenge f�r %d Zahlen ist festgelegt.", anzahlZahlen);
		return new String[] { s };
	}

	@Override
	public String[] gibSituation() {
		String s = String.format("Es gibt %d freie Felder mit denselben %d m�glichen Zahlen.", anzahlZahlen,
				anzahlZahlen);
		return new String[] { s };
	}

	@Override
	public String gibErgebnis() {
		String s = "In allen anderen Feldern (der Gruppe) werden diese Zahlen aus den m�glichen gel�scht.";
		return s;
	}

	@Override
	public double gibKontrollZeit1() {
		return 3 * anzahlZahlen;
	}

	private boolean istErgebnisIgnorieren(List<TipInfo> ignorierTips, TipInfoTeilMengeFestN tipInfoLogik) {
		if (ignorierTips == null) {
			return false;
		}

		for (TipInfo tipInfo : ignorierTips) {
			TipInfoTeilMengeFestN ignorierTip = (TipInfoTeilMengeFestN) tipInfo;
			boolean gleicheAktive = ignorierTip.gibAktiveFelder().istGleicherInhalt(tipInfoLogik.gibAktiveFelder());

			if (gleicheAktive) {
				return true;
			}
		}
		return false;
	}

	@Override
	public LogikErgebnis laufen(boolean istTip, List<TipInfo> ignorierTips) throws Exc {
		systemOut2("Laufen gruppen", gruppen);
		ArrayList<Gruppe> freieGruppen = Gruppe.gibFreieGruppen(gruppen, anzahlZahlen + 1);
		systemOut2("Laufen freie Gruppen", freieGruppen);
		if (!freieGruppen.isEmpty()) {
			GruppenLaeufeListe gruppenLaeufeListe = new GruppenLaeufeListe(freieGruppen.get(0).gibTyp());

			for (int i = 0; i < freieGruppen.size(); i++) {
				Gruppe gruppe = freieGruppen.get(i);

				gruppenLaeufeListe.add(gruppe.gibTyp());

				// Gibt es Felder in dieser Gruppe mit genau n m�glichen Zahlen?
				FeldListe felderMitNMoeglichen = new FeldListe();
				for (int iFeld = 0; iFeld < gruppe.size(); iFeld++) {
					Feld feld = gruppe.get(iFeld);
					if (feld.gibMoeglicheAnzahl() == anzahlZahlen) {
						felderMitNMoeglichen.add(feld);
					}
				}

				// systemOut(String.format("%s: n=%d Gruppe=%s",
				// this.getClass().getSimpleName(), anzahlZahlen,
				// gruppe.gibTyp()));
				// if ( (anzahlZahlen == 2) & (felderMitNMoeglichen.size() == 2)
				// & (gruppe.gibTyp() == Gruppe.Typ.ZEILE) ){
				// int j = 0;
				// int g = j;
				// }

				// Gibt es in diesen Feldern mit n m�glichen Zahlen Felder mit
				// genau denselben n Zahlen?
				for (int iBasisFeld = 0; iBasisFeld < felderMitNMoeglichen.size(); iBasisFeld++) {
					Feld basisFeld = felderMitNMoeglichen.get(iBasisFeld);
					ArrayList<Integer> basisMoegliche = basisFeld.gibMoegliche();
					FeldListe geschwister = new FeldListe();

					for (int iFeld = iBasisFeld + 1; iFeld < felderMitNMoeglichen.size(); iFeld++) {
						Feld feld = felderMitNMoeglichen.get(iFeld);
						ArrayList<Integer> feldMoegliche = feld.gibMoegliche();

						if (basisMoegliche.containsAll(feldMoegliche)) {
							geschwister.add(feld);
						}
					} // for (int iFeld
						// Das BasisFeld geh�rt sowieso zu den Geschwistern:
					geschwister.add(0, basisFeld);

					if (geschwister.size() == anzahlZahlen) {

						// Jetzt sind n Felder mit n gleichen Zahlen gefunden.
						// Jetzt kommt noch die Frage, ob es denn in den anderen
						// Feldern der Gruppe
						// in ihren m�glichen Zahlen eine der basisM�glichen
						// gibt.
						ZahlenListe loeschZahlen = new ZahlenListe();
						for (Feld feld : gruppe) {
							if (feld.istFrei() & (!geschwister.contains(feld))) {
								ArrayList<Integer> feldMoegliche = feld.gibMoegliche();

								for (Integer basisMoeglich : basisMoegliche) {
									if (feldMoegliche.contains(basisMoeglich)) {
										FeldNummerMitZahl loeschZahl = new FeldNummerMitZahl(feld.gibFeldNummer(),
												basisMoeglich);
										loeschZahlen.add(loeschZahl);
									}
								} // for(Integer basisMoeglich: basisMoegliche){
							}
						} // for (Feld feld: gruppe) {

						if (!loeschZahlen.isEmpty()) {
							// Die Logik-Situation liegt vor.

							// Methoden-Ende mit dem ersten Ergebnis!
							FeldNummerListe geschwisterFeldNummern = new FeldNummerListe(geschwister);
							Geschwister aktiveGeschwister = new Geschwister(basisMoegliche, geschwisterFeldNummern,
									loeschZahlen);

							boolean ergebnisIgnorieren = false;
							TipInfoTeilMengeFestN tipInfo = null;
							if (istTip) {
								FeldNummerListe mitSpieler = new FeldNummerListe(gruppe);
								tipInfo = new TipInfoTeilMengeFestN(this.idLogik, this.anzahlZahlen, mitSpieler,
										aktiveGeschwister, gruppe.gibInText(true));
								ergebnisIgnorieren = istErgebnisIgnorieren(ignorierTips, tipInfo);
							}

							if (!ergebnisIgnorieren) {
								LogikErgebnis ergebnis = new LogikErgebnis(gruppenLaeufeListe, null, loeschZahlen,
										tipInfo);
								return ergebnis;
							}
						} // if ( ! loeschZahlen.isEmpty()){
					} // if (geschwister.size() == anzahlZahlen){
				} // for (int iBasisFeld
			} // for (int i = 0; i < gruppen.size(); i++) {

			return new LogikErgebnis(gruppenLaeufeListe);
		} // if ( ! freieGruppen.isEmpty()){

		return null;
	}

}
