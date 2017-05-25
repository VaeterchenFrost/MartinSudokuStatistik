package sudoku.knacker.bericht;

import java.util.ArrayList;

import sudoku.logik.Klugheit;
import sudoku.logik.KnackerPartner;
import sudoku.logik.LogikBerichtKurier;
import sudoku.logik.SudokuLogik;
import sudoku.knacker.Ergebnis;

public class Schreiber implements LogikBerichtKurier {
	private BerichtKnacker bericht;

	public Schreiber() {
		this.bericht = new BerichtKnacker();
	}

	public BerichtKnacker gibKnackerBericht() {
		return bericht;
	}

	public void systemOut() {
		bericht.systemOut();
	}

	public void setzeSudokuBeschreibung(Klugheit klugheit, SudokuLogik feldMatrix, String name) {
		this.bericht = new BerichtKnacker();
		KB_SudokuBeschreibung berichtEintrag = new KB_SudokuBeschreibung(klugheit.gibTextKurz(), feldMatrix, name);
		bericht.add(berichtEintrag);
	}

	public void treibeKlareErgebnis(Ergebnis ergebnis) {
		KB_KlareErgebnis berichtEintrag = new KB_KlareErgebnis(ergebnis);
		bericht.add(berichtEintrag);
	}

	public void treibeKlareKlare1(int nKlare) {
		KB_KlareSetzeKlare1 berichtEintrag = new KB_KlareSetzeKlare1(nKlare);
		bericht.add(berichtEintrag);
	}

	public void loeseIntern(int durchlauf) {
		KB_LoeseIntern loeseIntern = new KB_LoeseIntern(durchlauf);
		bericht.add(loeseIntern);
	}

	public void loeseInternEnde(Ergebnis ergebnis, int ebeneNummer) {
		KB_LoeseInternEnde loeseInternEnde = new KB_LoeseInternEnde(ergebnis, ebeneNummer);
		bericht.add(loeseInternEnde);

	}

	public void versuchePaare(ArrayList<KnackerPartner> partnerListe, boolean istKontrollVersuchErlaubt) {
		KB_VersuchePaare versuchePaare = new KB_VersuchePaare(partnerListe, istKontrollVersuchErlaubt);
		bericht.add(versuchePaare);
	}

	public void versuchStart(KnackerPartner partner, boolean istEintragGesetzt) {
		KB_VersuchStart eintrag = new KB_VersuchStart(partner, istEintragGesetzt);
		bericht.add(eintrag);
	}

	@Override
	public void nimmLogikBericht(sudoku.logik.bericht.BerichtLogik berichtDerLogik) {
		KB_KlareSetzeMoegliche berichtEintrag = new KB_KlareSetzeMoegliche(berichtDerLogik);
		bericht.add(berichtEintrag);
	}
}
