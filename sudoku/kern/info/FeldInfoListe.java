package sudoku.kern.info;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.FeldListe;

@SuppressWarnings("serial")
public class FeldInfoListe extends ArrayList<FeldInfo> {

	public FeldInfoListe() {
	}

	public FeldInfoListe(FeldListe feldListe) {
		for (int i = 0; i < feldListe.size(); i++) {
			FeldInfo feldInfo = new FeldInfo(feldListe.get(i));
			this.add(feldInfo);
		}
	}

}
