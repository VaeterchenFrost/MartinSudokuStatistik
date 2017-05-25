package sudoku.bild;

import java.util.Iterator;
import java.util.List;

/**
 * @author heroe Iterator einer Liste von Abst�nden, der die Abst�nde vom
 *         letzten zum ersten (auf Index 0) Abstand auflistet
 */
class AbstandIterator implements Iterator<Abstand> {
	private List<Abstand> abstaende;
	private boolean istRichtungIndex0;
	private int currentIndex;

	/**
	 * @param abstaende
	 * @param istRichtungIndex0
	 *            true: Der Iterator bewegt sich vom Ende zu Index 0 false: Der
	 *            Iterator bewegt sich vom Index 0 zum Ende
	 */
	AbstandIterator(List<Abstand> abstaende, boolean istRichtungIndex0) {
		this.abstaende = abstaende;
		this.istRichtungIndex0 = istRichtungIndex0;
		currentIndex = abstaende.size();
	}

	/**
	 * @param abstaende
	 * @param istRichtungIndex0
	 *            true: Der Iterator bewegt sich Richtung Index 0 false: Der
	 *            Iterator bewegt sich Richtung Ende
	 * @param startIndex
	 */
	AbstandIterator(List<Abstand> abstaende, boolean istRichtungIndex0, int startIndex) {
		this.abstaende = abstaende;
		this.istRichtungIndex0 = istRichtungIndex0;
		currentIndex = startIndex;
	}

	// /**
	// * @param iZu0
	// * @return Differenz der Indizees der beiden Iteratoren:
	// * Zwischen den beiden Abstaenden, auf die die Iteratoren zeigen liegen
	// Differenz-1 Abstaende.
	// * Die Anzahl der Abstaende inklusive der beiden, auf die die Iteratorn
	// zeigen ist Differenz+1.
	// */
	// public int gibDifferenz(IteratorAbstand iZu0){
	// int i0 = iZu0.gibIndex();
	// return currentIndex - i0;
	// }

	@Override
	public boolean hasNext() {
		if (istRichtungIndex0) {
			return currentIndex > 0;
		} else {
			return currentIndex < abstaende.size() - 1;
		}
	}

	@Override
	public Abstand next() {
		if (istRichtungIndex0) {
			currentIndex--;
		} else {
			currentIndex++;
		}
		return abstaende.get(currentIndex);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
