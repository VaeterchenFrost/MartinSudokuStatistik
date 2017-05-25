package sudoku.bild;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

class IteratorRandX extends IteratorRand {
	/**
	 * @param image
	 *            Auf dies bezieht sich r
	 * @param r
	 *            In X-Richtung wird iteriert. Y: Die Lage der zu
	 *            kontrollierenden Spalten-Linien sind durch r.y und r.hight
	 *            benannt.
	 * @param istRichtungIndex0
	 * @param testRand
	 *            max. Ausmass des X-Randes in Pixeln
	 */
	IteratorRandX(BufferedImage image, Rectangle r, boolean istRichtungIndex0, int testRand) {
		super(image, r, istRichtungIndex0, testRand);
	}

	@Override
	Rectangle gibLinienRechteck() {
		Rectangle rLinie = new Rectangle(currentIndex, linienIndexMin, 1, linienLaenge);
		return rLinie;
	}

	@Override
	void init(Rectangle r, boolean istRichtungIndex0, int testRand) {
		this.linienIndexMin = r.y;
		this.linienLaenge = r.height;
		this.istRichtungIndex0 = istRichtungIndex0;
		if (istRichtungIndex0) {
			this.letzterIndex = r.x + r.width - testRand;
			currentIndex = r.x + r.width;
			this.ersterIndex = currentIndex - 1;
		} else {
			// Richtung Ende
			this.letzterIndex = r.x + testRand;
			currentIndex = r.x - 1;
			this.ersterIndex = currentIndex + 1;
		}
	}

}
