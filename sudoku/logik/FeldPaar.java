package sudoku.logik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldListe;

/**
 * @author heroe
 * Es handelt sich um zwei Felder einer Gruppe.
 * In der Gruppe (Zeile, Spalte, Kasten) existiert die eine mögliche Zahl "nur" in genau diesen beiden Feldern.
 */
class FeldPaar {
	/**
	 * Für die Logiken.
	 * @param gruppen
	 * @return Je Zahl, für die es Feldpaare in den Gruppen gibt, ein Map-Eintrag.
	 * 				Der Map-Eintrag benennt all die FeldPaare, die über die mögliche "Zahl" verbunden sind.
	 * 				Oder anders gesagt: Er benennt alle Gruppen, die FeldPaare besitzen, die über die mögliche "Zahl" verbunden sind.
	 */
	static Map<Integer, ArrayList<FeldPaar>> gibZahlenPartner(ArrayList<Gruppe> gruppen) {
		Map<Integer, ArrayList<FeldPaar>> zahlenPartner = new HashMap<Integer, ArrayList<FeldPaar>>();
		ArrayList<FeldPaar> feldPaare = gibFeldPaare(gruppen);

		for (FeldPaar feldPaar : feldPaare) {
			ArrayList<FeldPaar> zahlFeldPaare = zahlenPartner.get(feldPaar.zahl);

			if (zahlFeldPaare == null) {
				zahlFeldPaare = new ArrayList<>();
				zahlenPartner.put(feldPaar.zahl, zahlFeldPaare);
			}
			zahlFeldPaare.add(feldPaar);
		}

		return zahlenPartner;
	}

	/**
	 * @param gruppen
	 * @param feldPaarGeber Der bestimmt, welche Felder als FeldPaare gelten
	 * @return Alle FeldPaare der gruppen
	 */
	static ArrayList<FeldPaar> gibFeldPaare(ArrayList<Gruppe> gruppen, FeldPaarGeber feldPaarGeber) {
		ArrayList<FeldPaar> feldPaare = new ArrayList<>();
		Map<Gruppe, Map<Integer, FeldListe>> gruppenMap = gibMoeglicheZahlFelder(gruppen);

		for (int iGruppe = 0; iGruppe < gruppen.size(); iGruppe++) {
			Gruppe gruppe = gruppen.get(iGruppe);
			Map<Integer, FeldListe> zahlFelderMap = gruppenMap.get(gruppe);

			if (zahlFelderMap != null) {
				Set<Integer> moeglicheZahlen = zahlFelderMap.keySet();
				for (Integer zahl : moeglicheZahlen) {
					FeldListe felder = zahlFelderMap.get(zahl);
					ArrayList<FeldPaar> dieseFeldPaare = feldPaarGeber.gibFeldPaare(gruppe, zahl, felder);
					if (dieseFeldPaare != null){
						feldPaare.addAll(dieseFeldPaare);
					}
				}
			}
		}
		return feldPaare;
	}

	/**
	 * @author heroe
	 * Die übergebenen Felder sind ein FeldPaar, wenn es genau 2 Felder gibt (in dieser Gruppe der Felder)
	 */
	class EchterFeldPaarGeber extends FeldPaarGeber {
		@Override
		ArrayList<FeldPaar> gibFeldPaare(Gruppe gruppe, int zahl, FeldListe felder) {
			if (felder.size() == 2) {
				// Es handelt sich bei 2 Feldern um ein FeldPaar in der Gruppe
				FeldPaar feldPaar = new FeldPaar(gruppe, zahl, felder.get(0), felder.get(1));
				ArrayList<FeldPaar> feldPaare = new ArrayList<>();
				feldPaare.add(feldPaar);
				return feldPaare;
			}
			return null;
		}
	}

	/**
	 * @param gruppen
	 * @return Alle FeldPaare der gruppen
	 */
	static ArrayList<FeldPaar> gibFeldPaare(ArrayList<Gruppe> gruppen) {
		FeldPaar dummy = new FeldPaar(null, 0, null, null);
		FeldPaarGeber feldPaarGeber = dummy.new EchterFeldPaarGeber();

		ArrayList<FeldPaar> feldPaare = gibFeldPaare(gruppen, feldPaarGeber);
		return feldPaare;
	}

	/**
	 * @param gruppen
	 * @return Je Gruppe: Je mögliche Zahl die Felder, die diese mögliche Zahl enthalten. 
	 */
	static private Map<Gruppe, Map<Integer, FeldListe>> gibMoeglicheZahlFelder(ArrayList<Gruppe> gruppen) {
		Map<Gruppe, Map<Integer, FeldListe>> gruppenMap = new HashMap<Gruppe, Map<Integer, FeldListe>>();

		for (int iGruppe = 0; iGruppe < gruppen.size(); iGruppe++) {
			Gruppe gruppe = gruppen.get(iGruppe);

			for (int zahl = 1; zahl < 10; zahl++) {
				for (int iFeld = 0; iFeld < gruppe.size(); iFeld++) {
					Feld feld = gruppe.get(iFeld);
					if (feld.istMoeglich(zahl)) {
						Map<Integer, FeldListe> zahlFelder = gruppenMap.get(gruppe);
						if (zahlFelder == null) {
							zahlFelder = new HashMap<Integer, FeldListe>();
							gruppenMap.put(gruppe, zahlFelder);
						}

						FeldListe feldListe = zahlFelder.get(zahl);
						if (feldListe == null) {
							feldListe = new FeldListe();
							zahlFelder.put(zahl, feldListe);
						}
						feldListe.add(feld);
					}
				}
			} // for zahl
		}
		return gruppenMap;
	}

	// =================================================================
	final Gruppe gruppe;
	final int zahl;
	final Feld feld1;
	final Feld feld2;

	/**
	 * @param gruppe
	 * @param zahl
	 * @param feld1
	 * @param feld2
	 */
	FeldPaar(Gruppe gruppe, int zahl, Feld feld1, Feld feld2) {
		super();
		this.gruppe = gruppe;
		this.zahl = zahl;
		this.feld1 = feld1;
		this.feld2 = feld2;
	}

}
