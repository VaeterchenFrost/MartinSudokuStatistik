package sudoku.bild;

import java.util.ArrayList;
import java.util.Iterator;

import sudoku.bild.LinienStriche.StricheEinerLinie;

// ===================================================
/**
 * @author heroe iteriert �ber jeden Eintrag der Liste von StricheEinerLinie
 */
public class IteratorLinien implements Iterator<StricheEinerLinie> {
	final ArrayList<StricheEinerLinie> linien;
	final int indexEnde;
	final boolean istRueckwaerts;
	private int currentIndex;

	IteratorLinien(ArrayList<StricheEinerLinie> linien, int indexStart, int indexEnde) {
		this.linien = linien;
		this.indexEnde = indexEnde;
		this.istRueckwaerts = indexStart > indexEnde;
		this.currentIndex = istRueckwaerts ? indexStart + 1 : indexStart - 1;
	}

	/**
	 * @return Der aktuelle Index (nach next())
	 */
	int gibIndex() {
		return currentIndex;
	}

	@Override
	public boolean hasNext() {
		if (linien.size() == 0) {
			return false;
		}
		if (istRueckwaerts) {
			return currentIndex > indexEnde;
		} else {
			return currentIndex < indexEnde;
		}
	}

	@Override
	public StricheEinerLinie next() {
		if (istRueckwaerts) {
			currentIndex--;
		} else {
			currentIndex++;
		}
		return linien.get(currentIndex);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
