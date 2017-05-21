package sudoku.bild;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author heroe
 * Iterator über den Teil eines Bildes. 
 * Er gibt die Anzahl Weiss-Pixel eines angeforderten Bereichs einer Bildlinie in Prozent zurück.  
 */
/**
 * @author heroe
 *
 */
class IteratorRandY  extends IteratorRand {
	/**
	 * @param image Auf dies bezieht sich r
	 * @param r 
	 * 			In Y-Richtung wird iteriert. 
	 * 			X: Die Lage der zu kontrollierenden Zeilen-Linien sind durch r.x und r.width benannt.
	 * @param istRichtungIndex0
	 * @param testRand max. Ausmass des Y-Randes in Pixeln
	 */
	IteratorRandY(BufferedImage image, Rectangle r, boolean istRichtungIndex0, int testRand){
		super(image, r, istRichtungIndex0, testRand);
	}

	@Override
	void init(Rectangle r, boolean istRichtungIndex0, int testRand) {
		this.linienIndexMin = r.x;
		this.linienLaenge = r.width;
		this.istRichtungIndex0 = istRichtungIndex0;
		if (istRichtungIndex0){
			this.letzterIndex = r.y + r.height - testRand;
			currentIndex = r.y + r.height;
			this.ersterIndex = currentIndex -1; 
		}
		else{
			// Richtung Ende
			this.letzterIndex = r.y + testRand;
			currentIndex = r.y -1;
			this.ersterIndex = currentIndex +1;
		}
	}

	@Override
	Rectangle gibLinienRechteck() {
		Rectangle rLinie = new Rectangle(linienIndexMin, currentIndex, linienLaenge, 1);
		return rLinie;
	}

}
