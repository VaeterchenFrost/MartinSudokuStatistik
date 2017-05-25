package sudoku.neu;

import java.util.ArrayList;

import sudoku.logik.KastenIndex;

@SuppressWarnings("serial")
public class UnabhaengigeKasten extends ArrayList<ArrayList<KastenIndex>> {
	private static KastenIndex gibNeuenKastenIndex(int iSpalte, int iZeile) {
		return new KastenIndex(iSpalte, iZeile);
	}

	private static ArrayList<KastenIndex> gib3(KastenIndex k0, KastenIndex k1, KastenIndex k2) {
		ArrayList<KastenIndex> fList = new ArrayList<KastenIndex>();
		fList.add(k0);
		fList.add(k1);
		fList.add(k2);
		return fList;
	}

	public UnabhaengigeKasten() {
		ArrayList<KastenIndex> diagonale1 = gib3(gibNeuenKastenIndex(0, 0), gibNeuenKastenIndex(1, 1),
				gibNeuenKastenIndex(2, 2));
		ArrayList<KastenIndex> diagonale2 = gib3(gibNeuenKastenIndex(0, 2), gibNeuenKastenIndex(1, 1),
				gibNeuenKastenIndex(2, 0));
		ArrayList<KastenIndex> weitere1 = gib3(gibNeuenKastenIndex(0, 0), gibNeuenKastenIndex(1, 2),
				gibNeuenKastenIndex(2, 1));
		ArrayList<KastenIndex> weitere2 = gib3(gibNeuenKastenIndex(0, 1), gibNeuenKastenIndex(1, 0),
				gibNeuenKastenIndex(2, 2));
		ArrayList<KastenIndex> weitere3 = gib3(gibNeuenKastenIndex(0, 2), gibNeuenKastenIndex(1, 0),
				gibNeuenKastenIndex(2, 1));
		ArrayList<KastenIndex> weitere4 = gib3(gibNeuenKastenIndex(0, 1), gibNeuenKastenIndex(1, 2),
				gibNeuenKastenIndex(2, 0));

		this.add(diagonale1);
		this.add(diagonale2);
		this.add(weitere1);
		this.add(weitere2);
		this.add(weitere3);
		this.add(weitere4);
	}

}
