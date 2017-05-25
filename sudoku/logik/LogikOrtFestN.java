package sudoku.logik;

import java.util.ArrayList;
import java.util.List;

import sudoku.ArrayListInt;
import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenFeldNummern;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.logik.bericht.GruppenLaeufeListe;
import sudoku.logik.tipinfo.EinTipText;
import sudoku.logik.tipinfo.TipInfo;
import sudoku.logik.tipinfo.TipInfo0;

class LogikOrtFestN implements Logik__Interface {

	// =========================================================
	private class TipInfoOrtFestN extends TipInfo0 {
		final int anzahlZahlen;
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
		 * @param solistErgebnis
		 *            null oder ein �ber die Geschwister-Felder gefundener
		 *            Solist mit dessen Gruppe
		 * @param infoSudoku
		 */
		private TipInfoOrtFestN(Logik_ID logik, int anzahlZahlen, FeldNummerListe mitSpieler, Geschwister geschwister,
				String textInGruppe) {
			super(logik, mitSpieler);
			this.anzahlZahlen = anzahlZahlen;
			this.geschwister = geschwister;
			this.textInGruppe = textInGruppe;
		}

		@Override
		public FeldNummerListe gibAktiveFelder() {
			FeldNummerListe aktiveFelder = new FeldNummerListe();
			FeldNummerListe felder = geschwister.gibFelder();
			aktiveFelder.add(felder);
			return aktiveFelder;
		}

		@Override
		public ZahlenListe gibLoeschZahlen() {
			return geschwister.gibLoeschZahlen();
		}

		public EinTipText[] gibTip() {
			ArrayList<EinTipText> texte = new ArrayList<>();
			ArrayList<Integer> zahlen = geschwister.gibZahlen();
			String sZahlen = new ArrayListInt(zahlen).gibKette("+");
			FeldNummerListe felder = geschwister.gibFelder();
			String sFeldNummern = felder.gibKette("+");

			String s1 = String.format("%s besetzen die %d Zahlen (%s)", textInGruppe, anzahlZahlen, sZahlen);
			String s2 = String.format("die %d Felder %s.", anzahlZahlen, sFeldNummern);
			EinTipText tipText1 = new EinTipText(s1, s2);
			texte.add(tipText1);

			String s3 = "Deshalb werden alle anderen Zahlen hier gel�scht.";
			EinTipText tipText2 = new EinTipText(s3, "");
			texte.add(tipText2);

			EinTipText[] texteArr = texte.toArray(new EinTipText[texte.size()]);
			return texteArr;
		}

		@Override
		public FeldNummerMitZahl gibZahlFeld() {
			return null;
		}

		@Override
		public boolean istZahl() {
			return false;
		}

	}

	/**
	 * @param anzahlZahlen
	 * @return Die id f�r diese Logik. Diese Logiken m�ssen im enum Logik nicht
	 *         hintereinander stehen!
	 */
	static private Logik_ID gibLogikID(int anzahlZahlen) {
		String name1 = Logik_ID.ORTFEST1.name();

		String name = name1.substring(0, name1.length() - 1);
		name += anzahlZahlen;
		Logik_ID id = Logik_ID.valueOf(name);
		return id;
	}

	/**
	 * Erkennt und setzt M�glichGeschwister, n�mlich n Zahlen in n Feldern in
	 * einer Gruppe: Eine Zahl ist in ZWEI Feldern der Gruppe m�glich: Wenn es
	 * EINE weitere Zahl mit genau diesen Feldern gibt: => Die M�glichen dieser
	 * Felder erhalten nur diese ZWEI Zahlen. Eine Zahl ist in DREI Feldern der
	 * Gruppe m�glich: Wenn es ZWEI weitere Zahlen mit genau diesen Feldern
	 * gibt: => Die M�glichen dieser Felder erhalten nur diese DREI Zahlen.
	 * u.s.w. M�glichGeschwister ist allerdings nur, wer mindestens 1 m�gliche
	 * Zahl l�scht.
	 * 
	 * @param gruppe
	 * @param anzahlFelder
	 *            oder 3 (Felder in der Gruppe mit genau bzw. 3 gleichen
	 *            m�glichen Zahlen
	 * @return Geschwister oder null
	 * @throws Exc
	 */
	static private Geschwister gibMoeglichGeschwister(Gruppe gruppe, int anzahlFelder) throws Exc {

		// Jede Zahl (basisZahl = 1 bis 9) wird kontrolliert in der Gruppe:
		// 1. Ob sie in genau anzahlFelder m�glich ist und
		// . ob es eine weitere Zahl (bzw. weitere bei anzahlFelder=3) gibt, die
		// in genau denselben Feldern m�glich ist wie die basisZahl.
		for (int basisZahl = 1; basisZahl < 10; basisZahl++) {

			// Sicht auf die Gruppe erschaffen
			// Der Parameter '1':
			// Nach dem Lauf der Logik OrtFest1 k�nnen weitere m�gliche Zahlen
			// so gel�scht worden sein,
			// dass eine m�gliche Zahl als einzige im Feld steht. Auch diese ist
			// hier relevant!
			ZahlenFeldNummern moeglicheFelderJeZahl = gruppe.gibFelderJeMoeglicheZahl(1);

			// Die freien Felder dieser m�glichen basisZahl
			FeldNummerListe basisZahlFelder = moeglicheFelderJeZahl.gibFeldNummern(basisZahl);
			if (basisZahlFelder != null) {
				// Die Anzahl der freien Felder dieser m�glichen basisZahl
				int basisZahlFeldAnzahl = basisZahlFelder.size();

				// Wenn Anzahl der freien Felder dieser m�glichen basisZahl
				// genau anzahlFelder ist:
				// K�nnte (!) es sich um gesuchte Geschwister handeln
				if (basisZahlFeldAnzahl == anzahlFelder) {

					// Auflisten aller Zahlen, die genau dieselben Felder
					// besitzen wie diese basisZahl
					ArrayList<Integer> geschwisterZahlen = new ArrayList<Integer>();

					// Alle (! also auch mehr als anzahlFelder) Geschwister
					// zusammensammeln:
					// Also die Zahlen, die in genau denselben Feldern und nur
					// dort m�glich sind wie die basisZahl.
					// Die kleineren Zahlen wurden in dieser for-Schleife
					// bereits abgehandelt.
					for (int zahl = basisZahl + 1; zahl < 10; zahl++) {
						FeldNummerListe zahlFelder = moeglicheFelderJeZahl.gibFeldNummern(zahl);
						if (zahlFelder != null) {
							int zahlFeldAnzahl = zahlFelder.size();

							// Wenn die Feldanzahl gleich ist
							if (zahlFeldAnzahl == basisZahlFeldAnzahl) {
								if (basisZahlFelder.equals(zahlFelder)) {
									geschwisterZahlen.add(new Integer(zahl));
								}
							}
						}
					}

					// Geschwister-Zahlen auswerten:
					// Wenn anzahlFelder-1 == Geschwister-Anzahl ist: Es handelt
					// sich um M�glich-Geschwister.
					// "-1" weil die basisZahl selbst ja eine der endg�ltigen
					// Geschwisterzahlen ist.
					int braucheGeschwisterAnzahl = anzahlFelder - 1;
					if (braucheGeschwisterAnzahl == geschwisterZahlen.size()) {
						// Geschwister sind gefunden:
						// Wieviele m�gliche Zahlen besitzen die Geschwister
						// (basisZahlFelder)?
						FeldListe basisZahlFelderListe = new FeldListe();
						for (int iBasisZahlFeld = 0; iBasisZahlFeld < basisZahlFelder.size(); iBasisZahlFeld++) {
							Feld feld = gruppe.gibFeld_SehrLahm(basisZahlFelder.get(iBasisZahlFeld));
							basisZahlFelderListe.add(feld);
						}
						ZahlenFeldNummern moegliche = basisZahlFelderListe.gibMoeglicheZahlen();

						if (moegliche.gibAnzahlVorhandene() > anzahlFelder) {
							// Wenn mehr als zwei M�gliche existieren wird
							// gehandelt
							// Geschwister als M�gliche um die basisZahl
							// erg�nzen
							geschwisterZahlen.add(0, new Integer(basisZahl));

							// Die zu l�schenden Zahlen vermerken
							ZahlenListe loeschZahlen = new ZahlenListe();
							for (int iBasisZahlFeld = 0; iBasisZahlFeld < basisZahlFelderListe
									.size(); iBasisZahlFeld++) {
								Feld feld = basisZahlFelderListe.get(iBasisZahlFeld);
								ArrayList<Integer> moeglicheDesFeldes = feld.gibMoegliche();
								for (Integer moeglicheZahlDesFeldes : moeglicheDesFeldes) {
									if (!geschwisterZahlen.contains(moeglicheZahlDesFeldes)) {
										FeldNummerMitZahl fNrZ = new FeldNummerMitZahl(feld.gibFeldNummer(),
												moeglicheZahlDesFeldes);
										loeschZahlen.add(fNrZ);
									}
								}
							}

							if (!loeschZahlen.isEmpty()) {
								Geschwister geschwister = new Geschwister(geschwisterZahlen,
										new FeldNummerListe(basisZahlFelder), loeschZahlen);
								// // Diese Geschwisterzahlen einzig als
								// M�gliche stehen lassen in ihren Feldern
								// basisZahlFelder.setzeMoegliche(geschwisterZahlen);
								return geschwister;
							}
						}
					} // if ( braucheGeschwisterAnzahl == geschwister.size())
				} // if (basisZahlFelder != null){
			} // if (basisZahlFeldAnzahl == anzahlFelder)

		} // for (int basisZahl
		return null;
	}

	static private boolean istErgebnisIgnorieren(List<TipInfo> ignorierTips, TipInfoOrtFestN tipInfoLogik) {
		if (ignorierTips == null) {
			return false;
		}

		for (TipInfo tipInfo : ignorierTips) {
			TipInfoOrtFestN ignorierTip = (TipInfoOrtFestN) tipInfo;
			boolean gleicheAktive = ignorierTip.gibAktiveFelder().istGleicherInhalt(tipInfoLogik.gibAktiveFelder());

			if (gleicheAktive) {
				return true;
			}
		}
		return false;
	}

	// =========================================================
	private final Logik_ID idLogik;
	private final int anzahlZahlen;

	private final ArrayList<Gruppe> gruppen;

	public LogikOrtFestN(int anzahlZahlen, ArrayList<Gruppe> gruppen) {
		this.idLogik = gibLogikID(anzahlZahlen);
		this.anzahlZahlen = anzahlZahlen;
		this.gruppen = gruppen;
	}

	@Override
	public String gibErgebnis() {
		String s = String.format("Alle au�er diesen %d Zahlen werden aus den m�glichen dieser %d Felder gel�scht.",
				anzahlZahlen, anzahlZahlen);
		return s;
	}

	@Override
	public double gibKontrollZeit1() {
		return 3 * anzahlZahlen;
	}

	@Override
	public String gibKurzName() {
		return "O" + anzahlZahlen;
	}

	@Override
	public Logik_ID gibLogikID() {
		return idLogik;
	}

	@Override
	public String gibName() {
		String s = String.format("Ort ist fest f�r %d Zahlen", anzahlZahlen);
		return s;
	}

	@Override
	public String[] gibSituation() {
		String s = String.format("%d Zahlen sind nur in %d Feldern m�glich.", anzahlZahlen, anzahlZahlen);
		return new String[] { s };
	}

	@Override
	public String[] gibSituationAbstrakt() {
		String s = String.format("Der Ort f�r %d Zahlen ist festgelegt.", anzahlZahlen);
		return new String[] { s };
	}

	@Override
	public String[] gibWo() {
		return new String[] { "In einer Gruppe (Zeile, Spalte bzw. Kasten)" };
	}

	@Override
	public LogikErgebnis laufen(boolean istTip, List<TipInfo> ignorierTips) throws Exc {
		ArrayList<Gruppe> freieGruppen = Gruppe.gibFreieGruppen(gruppen, anzahlZahlen + 1);
		if (!freieGruppen.isEmpty()) {
			GruppenLaeufeListe gruppenLaeufeListe = new GruppenLaeufeListe(freieGruppen.get(0).gibTyp());

			for (int i = 0; i < freieGruppen.size(); i++) {
				Gruppe gruppe = freieGruppen.get(i);
				gruppenLaeufeListe.add(gruppe.gibTyp());

				// Gibt es n Zahlen, die in genau n Feldern m�glich sind?
				Geschwister geschwister = gibMoeglichGeschwister(gruppe, anzahlZahlen);
				if (geschwister != null) {

					boolean ergebnisIgnorieren = false;
					TipInfoOrtFestN tipInfo = null;
					if (istTip) {
						FeldNummerListe mitSpieler = new FeldNummerListe(gruppe);
						tipInfo = new TipInfoOrtFestN(idLogik, anzahlZahlen, mitSpieler, geschwister,
								gruppe.gibInText(true));
						ergebnisIgnorieren = istErgebnisIgnorieren(ignorierTips, tipInfo);
					}

					if (!ergebnisIgnorieren) {
						LogikErgebnis ergebnis = new LogikErgebnis(gruppenLaeufeListe, null,
								geschwister.gibLoeschZahlen(), tipInfo);
						return ergebnis;
					}
				} // if (geschwister != null) {
			} // for (int i=0; i<freieGruppen.size();

			return new LogikErgebnis(gruppenLaeufeListe);
		} // if ( ! freieGruppen.isEmpty()){

		return null;
	}

}
