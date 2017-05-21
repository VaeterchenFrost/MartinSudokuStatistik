package sudoku.schwer.daten;

import sudoku.schwer.AnzeigeInfo;

public class InfoVersucheOK implements AnzeigeInfo{
	// Anzahl geglückte Versuche
	private int nVersucheOK;
	
	public InfoVersucheOK(int nVersucheOK) {
		super();
		this.nVersucheOK = nVersucheOK;
	}

	private String gibVersuchOKAnzahl(){
		if (nVersucheOK == 1){
			return "1 Versuch";
		}
		else{
			return String.format("%d Versuche", nVersucheOK);
		}
	}
	
	@Override
	public String gibAnzeigeText() {
		return gibVersuchOKAnzahl();
	}

	@Override
	public String gibToolTip() {
		String sEnde = "</html>";
		String s = String.format("<html>Es fanden statt: %s", gibVersuchOKAnzahl());
//		s += gibToolTipAnzeigeTextBeschreibung();
		s += sEnde;
		return s;
	}
	

}
