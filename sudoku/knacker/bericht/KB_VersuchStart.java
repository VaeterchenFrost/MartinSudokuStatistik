package sudoku.knacker.bericht;

import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.logik.KnackerPartner;

public class KB_VersuchStart extends KB_Eintrag1Zeile{
	ZahlenListe eintraege;
	
	/**
	 * @param partner
	 * @param istEintragGesetzt oder sonst wurden die Alternativen gesetzt
	 */
	public KB_VersuchStart(KnackerPartner partner, boolean istEintragGesetzt) {
		if (istEintragGesetzt){
			eintraege = new ZahlenListe();
			eintraege.add(partner.gibBasis());
		}
		else {
			eintraege = partner.gibAlternativen();
		}
	}

	@Override
	public String toString() {
		return "VersuchePaareEintrag [eintraege=" + eintraege + "]";
	}

}
