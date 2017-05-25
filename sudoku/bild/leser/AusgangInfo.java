package sudoku.bild.leser;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

class AusgangInfo {
	// ================================================================
	public enum Ausgang {
		LINKS, KEINER, RECHTS
	}

	// ================================================================
	static private float startX = 0.5f;
	static private float startYoben = 1.5f / 6;
	static private float startYunten = 4.5f / 6;
	static private float yDifferenzMax = 0.5f / 6;
	static private int weissInt = new Color(255, 255, 255).getRGB();

	// static private boolean istSystemOut = true;
	// static private void systemOut(String s){
	// if (istSystemOut){
	// System.out.println("AusgangInfo: " + s);
	// }
	// }

	// static private int gibWeissPixelwert(){
	// // Ein Weiss-Pixelwert erstellen
	// int hell = 255;
	// Color weissColor = new Color(hell, hell, hell);
	// int weissInt = weissColor.getRGB();
	//
	// return weissInt;
	// }

	/**
	 * @param image
	 * @param rechteck
	 * @param istOben
	 * @param start
	 * @return null oder die Info zu dem einen Ausgang
	 */
	static private Ausgang gibAusgang(BufferedImage image, Rectangle rechteck, boolean istOben, Point start,
			boolean istSystemOut) {
		int xMin = rechteck.x + 1;
		int xMax = rechteck.x + rechteck.width - 1;
		int yAusweich = istOben ? 1 : -1;

		if (istSystemOut) {
			String s = String.format("gibAusgang: oben=%d xStart=%d yStart=%d xMin=%d xMax=%d yAusweich=%d",
					istOben ? 1 : 0, start.x, start.y, xMin, xMax, yAusweich);
			System.out.println(s);
		}

		boolean istAusgangLinks = istAusgang(image, start, xMin, yAusweich, istSystemOut);
		if (istAusgangLinks) {
			return Ausgang.LINKS;
		}

		boolean istAusgangRechts = istAusgang(image, start, xMax, yAusweich, istSystemOut);
		if (istAusgangRechts) {
			return Ausgang.RECHTS;
		}

		return Ausgang.KEINER;
	}

	/**
	 * @param image
	 * @param rechteck
	 *            Das Zahlenrechteck im image
	 * @return AusgangInfo zum Rechteck oder null falls keine solche ermittelbar
	 *         ist
	 */
	static public AusgangInfo gibAusgangInfo(BufferedImage image, Rectangle rechteck, boolean istSystemOut) {
		Point startOben = gibStart(image, rechteck, true, istSystemOut);
		if (startOben == null) {
			if (istSystemOut) {
				System.out.println("Kein Startpunkt oben gefunden");
			}
			return null;
		}
		Ausgang oben = gibAusgang(image, rechteck, true, startOben, istSystemOut);

		Point startUnten = gibStart(image, rechteck, false, istSystemOut);
		if (startUnten == null) {
			if (istSystemOut) {
				System.out.println("Kein Startpunkt unten gefunden");
			}
			return null;
		}
		Ausgang unten = gibAusgang(image, rechteck, false, startUnten, istSystemOut);

		AusgangInfo ausgangInfo = new AusgangInfo(oben, unten);
		if (istSystemOut) {
			System.out.println(ausgangInfo.toString());
		}
		return ausgangInfo;
	}

	/**
	 * @param image
	 * @param x
	 *            die noch gute X-Koordinate
	 * @param y
	 *            die noch gute Y-Koordinate (auf ihr geht es zur�ck von x zu
	 *            xEnde)
	 * @param xEnde
	 *            Wenn die Suche auf dieser X-Koordinate landet, ist sie
	 *            fehlgeschlagen
	 * @param yAusweich
	 *            Y-Koordinate, auf die auswegichen werden kann. (Ist stets
	 *            Nachbar zu y.)
	 * @return null falls es keinen Ausgang aus der Sackgasse gibt oder der
	 *         (weisse) Punkt ab dem weiter gesucht werden kann
	 */
	static private Point gibSackgasseAusgang(BufferedImage image, int x, int y, int xEnde, int yAusweich,
			boolean istSystemOut) {
		int xRichtung = 1;
		if (xEnde < x) {
			xRichtung = -1;
		}

		if (istSystemOut) {
			String s = String.format("gibSackgasseAusgang-Start: x=%d y=%d xEnde=%d yAusweich=%d xRichtung=%d", x, y,
					xEnde, yAusweich, xRichtung);
			System.out.println(s);
		}

		while (x != xEnde) {
			x += xRichtung;
			if (x == xEnde) {
				// Weiter kann nicht gesucht werden
				if (istSystemOut) {
					System.out.println("Ende durch x==xEnde");
				}
				return null;
			}

			// Suche auf der Y-Linie
			if (!istWeiss(image, x, y, istSystemOut)) {
				// Kein Rauskommen auf y
				if (istSystemOut) {
					String s = String.format("Ende durch Schwarz auf der Y-Linie: x=%d y=%d", x, y);
					System.out.println(s);
				}
				return null;
			}

			// Y-Linie zeigt weiss: Auch die yAusweich-Linie?
			if (istWeiss(image, x, yAusweich, istSystemOut)) {
				// Hier geht es (vielleicht) raus
				Point p = new Point(x, yAusweich);
				if (istSystemOut) {
					String s = String.format("Ende mit Vorschlag zur Weitersuche ab: x=%d y=%d", p.x, p.y);
					System.out.println(s);
				}
				return p;
			}
		} // while

		if (istSystemOut) {
			System.out.println("Illegaler Ausgang au�erhalb der While-Schleife");
		}
		return null;
	}

	/**
	 * @param image
	 * @param rechteck
	 * @param istOben
	 * @return null oder Startpunkt (etwa Mittelpunkt) in einem wei�en Feld mit
	 *         schwarzer (teilweiser) Umrandung
	 */
	static private Point gibStart(BufferedImage image, Rectangle rechteck, boolean istOben, boolean istSystemOut) {
		int x = rechteck.x + Math.round(rechteck.width * startX);

		float yFaktor = istOben ? startYoben : startYunten;
		int y = rechteck.y + Math.round(rechteck.height * yFaktor);

		if (istSystemOut) {
			String s = String.format("gibStart: oben=%d xStart=%d yStart=%d Rechteck=%s", istOben ? 1 : 0, x, y,
					rechteck);
			System.out.println(s);
		}

		if (istWeiss(image, x, y, istSystemOut)) {
			if (istSystemOut) {
				String s = String.format("gibStart: Gefunden x=%d y=%d", x, y);
				System.out.println(s);
			}
			return new Point(x, y);
		}

		float yDifferenz = istOben ? -1 * yDifferenzMax : yDifferenzMax;
		int yRichtung = istOben ? -1 : 1;
		int yZiel = rechteck.y + Math.round(rechteck.height * yDifferenz);

		while (y != yZiel) {
			y += yRichtung;
			if (istWeiss(image, x, y, istSystemOut)) {
				if (istSystemOut) {
					String s = String.format("gibStart: Gefunden x=%d y=%d", x, y);
					System.out.println(s);
				}
				return new Point(x, y);
			}
		}

		if (istSystemOut) {
			System.out.println("gibStart: Kein Start gefunden ");
		}
		return null;
	}

	/**
	 * @param image
	 * @param rechteck
	 * @param start
	 *            Startpunkt f�r die Suche
	 * @param xZiel
	 *            start.x und xZiel bestimmen die Suchrichtung
	 * @param yAusweich
	 *            Ausweich-Richtung
	 * @return true falls ein Ausgang in die X-Richtung existiert
	 */
	static private boolean istAusgang(BufferedImage image, Point start, int xZiel, int yAusweich,
			boolean istSystemOut) {
		int x = start.x;
		int y = start.y;
		int xRichtung = 1;
		if (xZiel < start.x) {
			xRichtung = -1;
		}

		if (istSystemOut) {
			String s = String.format("istAusgang: xStart=%d yStart=%d xZiel=%d xRichtung=%d yAusweich=%d", start.x,
					start.y, xZiel, xRichtung, yAusweich);
			System.out.println(s);
		}

		while (x != xZiel) {
			x += xRichtung;

			if (istWeiss(image, x, y, istSystemOut)) {
				if (x == xZiel) {
					// Wir sind am Ziel gelandet!!!
					if (istSystemOut) {
						System.out.println("Ausgang erkannt");
					}
					return true;
				}
			} else { // Schwarz: Gibt es eine Ausweichm�glichkeit (in
						// y-Richtung) ?
				y += yAusweich;
				x -= xRichtung;
				if (istWeiss(image, x, y, istSystemOut)) {
					// Dies ist die Ausweichm�glichkeit
				} else {
					// unter bzw. �ber letztem weiss ist schwarz: Ende oder
					// Sackgasse?
					int yAusweichSackgasse = y;
					y -= yAusweich;
					int xEnde = start.x;
					Point p = gibSackgasseAusgang(image, x, y, xEnde, yAusweichSackgasse, istSystemOut);
					if (p == null) {
						// Es gibt keinen Ausgang aus der Sackgasse
						if (istSystemOut) {
							System.out.println("Kein Ausgang erkannt: Sackgasse");
						}
						return false;
					}
					// Weiter nach der Sackgasse
					x = p.x;
					y = p.y;
				}
			} // if (intRGB1 == weissInt){
		} // while

		if (istSystemOut) {
			System.out.println("Illegaler Ausgang au�erhalb der While-Schleife");
		}
		return false;
	}

	static private boolean istWeiss(BufferedImage image, int x, int y, boolean istSystemOut) {
		int intRGB = image.getRGB(x, y);
		boolean istWeissPixel = intRGB == weissInt;
		if (istSystemOut) {
			String s = String.format("[x=%d, y=%d] : %s", x, y, istWeissPixel ? "weiss" : "schwarz");
			System.out.println(s);
		}
		return istWeissPixel;
	}

	// ================================================================
	public final Ausgang oben;
	public final Ausgang unten;

	public AusgangInfo(Ausgang oben, Ausgang unten) {
		this.oben = oben;
		this.unten = unten;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AusgangInfo other = (AusgangInfo) obj;
		if (oben != other.oben) {
			return false;
		}
		if (unten != other.unten) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oben == null) ? 0 : oben.hashCode());
		result = prime * result + ((unten == null) ? 0 : unten.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "AusgangInfo [oben=" + oben + ", unten=" + unten + "]";
	}

}
