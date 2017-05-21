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

/**
 * @author heroe
 * Die Logik KastenLinie hat, wie sich zeigte die identische Wirkung wie die Logik Kasten2.
 * Anders gesagt: Sie kommt nie zur Wirkung wenn Logik Kasten2 eingeschalten ist.
 * Das kam mir zwar nicht im Gedanken, aber sehr schön beim Ansehen von entsprechenden Tips.
 * Deshalb ist diese Logik nicht eingeordnet!
 */
class Logik_KastenLinie implements Logik__Interface{
    
	// =========================================================
	static private FeldNummerListe gibMitspieler(Gruppe linie, Kasten kasten){
		FeldNummerListe mitspieler = new FeldNummerListe(linie);
		FeldNummerListe kastenMitspieler = new FeldNummerListe(kasten);
		mitspieler.addAll(kastenMitspieler);
		return mitspieler;
	}

	private class TipInfoKastenLinie extends TipInfo0 {
		
		final int zahl;
		final FeldNummerListe zahlFeldNummern;
		final Gruppe linie;
		final Kasten kasten;
		final ZahlenListe loeschZahlen;

		private TipInfoKastenLinie(Logik_ID logik, int zahl, FeldNummerListe zahlFeldNummern, 
				Gruppe linie, Kasten kasten, ZahlenListe loeschZahlen) {
			super(logik, Logik_KastenLinie.gibMitspieler(linie, kasten));
			this.zahl = zahl;
			this.zahlFeldNummern = zahlFeldNummern;
			this.linie = linie;
			this.kasten = kasten;
			this.loeschZahlen = loeschZahlen;
		}

		public EinTipText[] gibTip() {
			ArrayList<EinTipText> texte = new ArrayList<>();
			String sFeldNummern = zahlFeldNummern.gibKette("+");
			{
				String s1 = String.format("%s muss die Zahl %d in einem der Felder", linie.gibInText(true), zahl);
				String s2 = String.format("%s stehen.", sFeldNummern);
				EinTipText tipText1 = new EinTipText(s1, s2);
				texte.add(tipText1);
			}
			{
				String s1 = String.format("Alle diese Felder befinden sich %s", kasten.gibInText(false));
				EinTipText tipText1 = new EinTipText(s1, null);
				texte.add(tipText1);
			}
			{
				String s1 = String.format("Deshalb wird die Zahl %d in den anderen Feldern des Kastens gelöscht.", zahl);
				EinTipText tipText1 = new EinTipText(s1, null);
				texte.add(tipText1);
			}

			EinTipText[] texteArr = texte.toArray(new EinTipText[texte.size()]);
			return texteArr;
		}

		@Override
		public FeldNummerListe gibAktiveFelder() {
			return zahlFeldNummern;
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
	private ArrayList<Kasten> kaesten;
	private ArrayList<Gruppe> linien;

	public Logik_KastenLinie(ArrayList<Kasten> kaesten, ArrayList<Gruppe> zeilen, ArrayList<Gruppe> spalten) {
		this.kaesten = kaesten;
		this.linien = null;
		if (zeilen != null){
			this.linien = new ArrayList<>();
			this.linien.addAll(zeilen);
			this.linien.addAll(spalten);
		}
	}

	@Override
	public Logik_ID gibLogikID() {
		return null; //LogikID.KASTENLINIE;
	}

	@Override
	public String gibKurzName() {
		return "KL";
	}

	@Override
	public String gibName() {
		return "Kastenlinie";
	}

	@Override
	public String[] gibWo(){
		return new String[]{"Auf einer Linie eines Kastens"};
	}
	
	@Override
	public String[] gibSituationAbstrakt(){
		return new String[]{"1 Zahl ist in einer Zeile bzw. Spalte nur auf Feldern innerhald eines Kastens möglich."};
	}

	@Override
	public String[] gibSituation() {
		return new String[]{"1 Zahl ist in einer Zeile bzw. Spalte nur auf Feldern innerhald eines Kastens möglich."};
	}

	@Override
	public String gibErgebnis() {
		return "Diese 1 Zahl ist innerhalb des Kastens nirgendwoanders möglich. Auf den anderen Feldern wird die Zahl also gelöscht.";
	}

	@Override
	public double gibKontrollZeit1() {
		return 6;
	}

	/**
	 * @param felder
	 * @return null wenn die felder nicht in einem Kasten liegen
	 */
	private KastenIndex gibKastenIndex(FeldListe felder){
		KastenIndex kastenIndex = Kasten.gibKastenIndex(felder.get(0).gibFeldNummer());
		for(Feld feld: felder){
			KastenIndex kastenIndex2 = Kasten.gibKastenIndex(feld.gibFeldNummer());
			if ( ! kastenIndex.equals(kastenIndex2)){
				return null;
			}
		}
		return kastenIndex;
	}

	static private boolean istErgebnisIgnorieren(List<TipInfo> ignorierTips, TipInfoKastenLinie tipInfoLogik) {
		if (ignorierTips == null) {
			return false;
		}

		for (TipInfo tipInfo : ignorierTips) {
			TipInfoKastenLinie ignorierTip = (TipInfoKastenLinie) tipInfo;
			boolean gleicheZahlen = ignorierTip.zahl == tipInfoLogik.zahl;
			if (gleicheZahlen){
				gleicheZahlen = ignorierTip.zahlFeldNummern.istGleicherInhalt(tipInfoLogik.zahlFeldNummern);
			}
			
			if (gleicheZahlen) {
				return true;
			}
		}
		return false;
	}

	@Override
	public LogikErgebnis laufen(boolean istTip, List<TipInfo> ignorierTips)  throws Exc
	{
		ArrayList<Gruppe> freieLinien = Gruppe.gibFreieGruppen(linien, 2);
		if (!freieLinien.isEmpty()) {
			GruppenLaeufeListe gruppenLaeufeListe = new GruppenLaeufeListe(freieLinien.get(0).gibTyp());

			for (int iLinie = 0; iLinie < freieLinien.size(); iLinie++) {
				Gruppe linie = freieLinien.get(iLinie);
				gruppenLaeufeListe.add(linie.gibTyp());
				for (int zahl = 1; zahl < 10; zahl++) {
					FeldListe felderDerMoeglichenLinienZahl = linie.gibFelderDerMoeglichenZahl(zahl);
					int anzahlMoeglicherFelderDerLinienZahl = felderDerMoeglichenLinienZahl.size();
					if ( (anzahlMoeglicherFelderDerLinienZahl == 2) | (anzahlMoeglicherFelderDerLinienZahl == 3)){
						KastenIndex kastenIndex = gibKastenIndex(felderDerMoeglichenLinienZahl);
	
						if (kastenIndex != null){
							// Alle Felder liegen in einem Kasten: Gibt es zu löschende Zahlen im Kasten
							Kasten kasten = Kasten.gibKasten(kastenIndex, kaesten);
							FeldListe felderDerMoeglichenZahlImKasten = kasten.gibFelderDerMoeglichenZahl(zahl);
	
							if (anzahlMoeglicherFelderDerLinienZahl < felderDerMoeglichenZahlImKasten.size()){
								// Es gibt zu löschende Zahlen im Kasten
								FeldNummerListe loeschFeldNummern = new FeldNummerListe();
								for (int iFeldImKasten = 0; iFeldImKasten < felderDerMoeglichenZahlImKasten.size(); iFeldImKasten++) {
									Feld feld = felderDerMoeglichenZahlImKasten.get(iFeldImKasten);
									if ( ! felderDerMoeglichenLinienZahl.contains(feld)){
										loeschFeldNummern.add(feld.gibFeldNummer());
									}
								}
								
								ZahlenListe loeschZahlen = new ZahlenListe(loeschFeldNummern, zahl);
								boolean ergebnisIgnorieren = false;
								TipInfoKastenLinie tipInfo = null;
								if (istTip) {
									FeldNummerListe feldNummernDerMoeglichenLinienZahl = new FeldNummerListe(felderDerMoeglichenLinienZahl);
									tipInfo = new TipInfoKastenLinie(this.gibLogikID(), zahl, feldNummernDerMoeglichenLinienZahl,
											linie, kasten, loeschZahlen);
									ergebnisIgnorieren = istErgebnisIgnorieren(ignorierTips, tipInfo);
								}

								if (!ergebnisIgnorieren) {
									LogikErgebnis ergebnis = new LogikErgebnis(gruppenLaeufeListe, null, loeschZahlen, tipInfo);
									return ergebnis;
								}
							}
						}
					} // if ( (anzahlMoeglicherFelder 
				} // for (int zahl
			} // for (int iLinie
			return new LogikErgebnis(gruppenLaeufeListe);
		} // if (!freieLinien.isEmpty())
		return null;
	}

}
