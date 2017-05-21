package sudoku.bild;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * @author heroe
 * Iterator über den Teil eines Bildes. 
 * Er gibt die Anzahl Weiss-Pixel eines angeforderten Bereichs einer Bildlinie in Prozent zurück.  
 */
/**
 * @author heroe
 *
 */
abstract class IteratorRand  implements Iterator<Integer> {
	protected BufferedImage image;
	protected boolean istRichtungIndex0;
	protected int linienIndexMin;
	protected int linienLaenge;
	
	protected int ersterIndex;
	protected int letzterIndex;
	protected int currentIndex;

	/**
	 * @param image Auf dies bezieht sich r
	 * @param r 
	 * 			In X-Richtung wird iteriert. 
	 * 			Y: Die Lage der zu kontrollierenden Spalten-Linien sind durch r.y und r.hight benannt.
	 * @param istRichtungIndex0
	 * @param testRand max. Ausmass des X-Randes in Pixeln
	 */
	IteratorRand(BufferedImage image, Rectangle r, boolean istRichtungIndex0, int testRand){
		this.image = image;
		init(r, istRichtungIndex0, testRand);
	}

	abstract void init(Rectangle r, boolean istRichtungIndex0, int testRand);
	
	int gibVorigenIndex() {
		if (istRichtungIndex0){
			return Math.min(ersterIndex, currentIndex+1);
		}
		else{
			return Math.max(ersterIndex, currentIndex-1);
		}
	}

	
	@Override
	public boolean hasNext() {
		if (istRichtungIndex0){
			return currentIndex > letzterIndex;
		}
		else{
			return currentIndex < letzterIndex;
		}
	}

	abstract Rectangle gibLinienRechteck();
	
	@Override
	public Integer next() {
		if (istRichtungIndex0){
			currentIndex--;
		}
		else{
			currentIndex++;
		}
		
		Rectangle rLinie = gibLinienRechteck();
		LinienWeiss linienWeiss = new LinienWeiss(image, rLinie);
		
		return linienWeiss.gibWeissAnteil();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
