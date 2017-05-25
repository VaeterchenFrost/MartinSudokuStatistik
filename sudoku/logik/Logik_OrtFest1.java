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

class Logik_OrtFest1 implements Logik__Interface {

	// =========================================================
	private class TipInfoOrtFest1 extends TipInfo0 {
		private FeldNummerMitZahl klareZahl;
		private Gruppe gruppe;

		private TipInfoOrtFest1(FeldNummerMitZahl klareZahl, Gruppe gruppe) {
			super(Logik_ID.ORTFEST1, new FeldNummerListe(gruppe));
			this.klareZahl = klareZahl;
			this.gruppe = gruppe;
		}

		@Override
		public FeldNummerListe gibAktiveFelder() {
			FeldNummerListe feldNummerListe = new FeldNummerListe();
			feldNummerListe.add(klareZahl.gibFeldNummer());
			return feldNummerListe;
		}

		@Override
		public ZahlenListe gibLoeschZahlen() {
			return null;
		}

		public EinTipText[] gibTip() {
			String s1 = String.format("%s ist die Zahl %d", gruppe.gibInText(true), klareZahl.gibZahl());
			String s2 = String.format(" nur in dem einen Feld %s m�glich.", klareZahl.gibFeldNummer());

			EinTipText[] sArray = new EinTipText[] { new EinTipText(s1, s2) };
			return sArray;
		}

		@Override
		public FeldNummerMitZahl gibZahlFeld() {
			return klareZahl;
		}

		@Override
		public boolean istZahl() {
			return true;
		}
	}

	// =========================================================
	private ArrayList<Gruppe> gruppen;

	public Logik_OrtFest1(ArrayList<Gruppe> gruppen) {
		this.gruppen = gruppen;
	}

	@Override
	public String gibErgebnis() {
		return "Diese Zahl wird ein Eintrag.";
	}

	@Override
	public double gibKontrollZeit1() {
		return 4.4;
	}

	@Override
	public String gibKurzName() {
		return "O1";
	}

	@Override
	public Logik_ID gibLogikID() {
		return Logik_ID.ORTFEST1;
	}

	@Override
	public String gibName() {
		return "Ort ist fest f�r 1 Zahl";
	}

	@Override
	public String[] gibSituation() {
		return new String[] { "Es ist 1 Zahl nur in 1 Feld m�glich." };
	}

	@Override
	public String[] gibSituationAbstrakt() {
		return new String[] { "Der Ort f�r 1 Zahl ist festgelegt." };
	}

	@Override
	public String[] gibWo() {
		return new String[] { "In einer Gruppe (Zeile, Spalte bzw. Kasten)" };
	}

	@Override
	public LogikErgebnis laufen(boolean istTip, List<TipInfo> ignorierTips) throws Exc {
		ArrayList<Gruppe> freieGruppen = Gruppe.gibFreieGruppen(gruppen, 1);
		if (!freieGruppen.isEmpty()) {
			GruppenLaeufeListe gruppenLaeufeListe = new GruppenLaeufeListe(freieGruppen.get(0).gibTyp());

			for (int i = 0; i < freieGruppen.size(); i++) {
				Gruppe gruppe = freieGruppen.get(i);
				gruppenLaeufeListe.add(gruppe.gibTyp());

				FeldNummerMitZahl klareZahl = null;
				for (int zahl = 1; zahl < 10; zahl++) {
					FeldListe zahlenFelder = gruppe.gibFelderDerMoeglichenZahl(zahl);

					if (1 == zahlenFelder.size()) {
						Feld feld = zahlenFelder.get(0);
						klareZahl = new FeldNummerMitZahl(feld.gibFeldNummer(), zahl);
						// Soll-Eintrag ist erkannt!
						break;
					}
				}

				if (klareZahl != null) {
					TipInfo tipInfo = null;
					if (istTip) {
						tipInfo = new TipInfoOrtFest1(klareZahl, gruppe);
					}

					LogikErgebnis ergebnis = new LogikErgebnis(gruppenLaeufeListe, klareZahl, null, tipInfo);
					return ergebnis;
				}
			} // for (int i = 0; i < freieGruppen

			return new LogikErgebnis(gruppenLaeufeListe);
		} // if ( ! freieGruppen.isEmpty()){

		return null;
	}

}
