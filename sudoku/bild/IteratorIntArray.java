package sudoku.bild;

import java.util.Iterator;

/**
 * @author heroe
 * Iterator eines int-Arrays.  
 */
class IteratorIntArray  implements Iterator<Integer> {
	private int[] intArray;
	private boolean istRichtungIndex0;
	private int currentIndex;

	/**
	 * @param linienWeiss
	 * @param istRichtungIndex0
	 * 			true: Der Iterator bewegt sich Richtung Index 0
	 * 			false: Der Iterator bewegt sich Richtung Ende
	 * @param startIndex Ab diesem Index wird iteriert
	 */
	IteratorIntArray(int[] intArray, boolean istRichtungIndex0, int startIndex){
		this.intArray = intArray;
		this.istRichtungIndex0 = istRichtungIndex0;
		currentIndex = startIndex;
	}

	public int gibVorigenIndex() {
		if (istRichtungIndex0){
			return currentIndex+1;
		}
		else{
			return currentIndex-1;
		}
	}

	
	@Override
	public boolean hasNext() {
		if (istRichtungIndex0){
			return currentIndex > 0;
		}
		else{
			return currentIndex < intArray.length - 1;
		}
	}

	@Override
	public Integer next() {
		if (istRichtungIndex0){
			currentIndex--;
		}
		else{
			currentIndex++;
		}
		return intArray[currentIndex];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
