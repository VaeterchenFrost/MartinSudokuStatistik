package sudoku.bild;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author heroe Die Arrays mit der Weiss-Pixel-Anzahl: Wie oft tritt ein
 *         Weiss-Pixel auf Spalte[i] bzw. Zeile[i] auf?
 */
class LinienWeiss {
	static public void systemOutSchwarz(int[] linienWeiss, int schwarzUnter) {
		System.out.print("Schwar: ");

		for (int i = 0; i < linienWeiss.length; i++) {
			int weiss = linienWeiss[i];
			String s = null;
			if (weiss < schwarzUnter) {
				s = " ###";
			} else {
				s = " ___";
			}
			System.out.print(s);
		}
		System.out.println();
	}

	final int zeilenWeiss[];

	final int spaltenWeiss[];

	public LinienWeiss(BufferedImage image, Rectangle r) {
		if (r == null) {
			r = new Rectangle(0, 0, image.getWidth(), image.getHeight());
		}

		zeilenWeiss = new int[r.height];
		spaltenWeiss = new int[r.width];

		// Ein Weiss-Pixelwert erstellen
		int hell = 255;
		Color weissColor = new Color(hell, hell, hell, 255);
		int weissInt = weissColor.getRGB();

		// Die Arrays mit der Weiss-Pixel-Anzahl erstellen: Wie oft tritt ein
		// Weiss-Pixel auf Spalte[i] bzw. Zeile[i] auf?
		for (int iZeile = r.y; iZeile < r.y + r.height /*-1*/; iZeile++) {
			for (int iSpalte = r.x; iSpalte < r.x + r.width /*-1*/; iSpalte++) {

				int intRGB = image.getRGB(iSpalte, iZeile);

				if (intRGB == weissInt) {
					zeilenWeiss[iZeile - r.y]++;
					spaltenWeiss[iSpalte - r.x]++;
				}
			}
		} // for (int iZeile
	}

	/**
	 * @return Anteil der schwarzen Pixel in Prozent bezogen auf das
	 *         Pixel-Rechteck
	 */
	public float gibSchwarzAnteil() {
		int nWeissPixel = gibWeissPixelAnzahl();
		int nPixel = spaltenWeiss.length * zeilenWeiss.length;
		int nSchwarzPixel = nPixel - nWeissPixel;
		float schwarzAnteil = (nSchwarzPixel * 100.0f) / nPixel;
		return schwarzAnteil;
	}

	/**
	 * @return Anteil der weissen Pixel in Prozent bezogen auf das
	 *         Pixel-Rechteck
	 */
	public int gibWeissAnteil() {
		int nWeissPixel = gibWeissPixelAnzahl();
		int nPixel = spaltenWeiss.length * zeilenWeiss.length;
		int weissAnteil = (nWeissPixel * 100) / nPixel;
		return weissAnteil;
	}

	private int gibWeissPixelAnzahl() {
		int nWeissPixel = 0;
		for (int i = 0; i < zeilenWeiss.length; i++) {
			nWeissPixel += zeilenWeiss[i];
		}
		return nWeissPixel;
	}

	public void systemOut() {
		System.out.println(
				String.format("LinienWeiss mit %d Spalten und %d Zeilen", spaltenWeiss.length, zeilenWeiss.length));

		int maxIndex = Math.max(spaltenWeiss.length, zeilenWeiss.length) - 1;
		System.out.print("Indexe: ");
		for (int i = 0; i <= maxIndex; i++) {
			System.out.print(String.format("%4d", i));
		}
		System.out.println();

		System.out.print("Spalte: ");
		for (int i = 0; i < spaltenWeiss.length; i++) {
			System.out.print(String.format("%4d", spaltenWeiss[i]));
		}
		System.out.println();

		System.out.print("Zeilen: ");
		for (int i = 0; i < zeilenWeiss.length; i++) {
			System.out.print(String.format("%4d", zeilenWeiss[i]));
		}
		System.out.println();
	}

}
