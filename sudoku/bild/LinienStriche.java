package sudoku.bild;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import sudoku.bild.WerteGruppeComparator.VergleichsArt;

class LinienStriche {

	/**
	 * @author heroe Beinhaltet die Striche-Start-Indizees der Striche einer
	 *         bestimmten L�nge
	 */
	class Strich implements Comparable<Strich> {
		final int startIndex;
		final int laenge;

		public Strich(int startIndex, int laenge) {
			this.startIndex = startIndex;
			this.laenge = laenge;
		}

		public int gibEndeIndex() {
			int ende = startIndex + laenge - 1;
			return ende;
		}

		@Override
		public int compareTo(Strich other) {
			if (this == other) {
				return 0;
			}
			if (other == null) {
				return 1;
			}
			if (getClass() != other.getClass()) {
				return 1;
			}
			if (this.laenge > other.laenge) {
				return -1;
			}
			if (this.laenge < other.laenge) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * @author heroe Beinhaltet die Striche-Starts verschiedener L�ngen (aller
	 *         L�ngen einer Spalte bzw. Zeile)
	 */
	@SuppressWarnings("serial")
	class StricheEinerLinie extends ArrayList<Strich> {
		final String linienName;
		final int iLinie;

		public StricheEinerLinie(String linienName, int iLinie) {
			this.linienName = linienName;
			this.iLinie = iLinie;
		}

		public StricheEinerLinie gibLangeStriche(int minStrichLaenge) {
			StricheEinerLinie neueStriche = new StricheEinerLinie(linienName, iLinie);
			for (Strich strich : this) {
				if (strich.laenge >= minStrichLaenge) {
					neueStriche.add(strich);
				}
			}
			return neueStriche;
		}

		public void systemOut() {
			// Collections.sort(this);
			// String s = "";
			// for(LaengenStriche eineLaenge: this){
			// String s1 = String.format(" %d=%dx", eineLaenge.laenge,
			// eineLaenge.size());
			// s += s1;
			// }
			String s = "";
			for (Strich strich : this) {
				String s1 = String.format(" %d:%d-%d", strich.laenge, strich.startIndex, strich.gibEndeIndex());
				s += s1;
			}
			String sOut = String.format("%s %s %d: %s", getClass().getSimpleName(), linienName, iLinie, s);
			System.out.println(sOut);
		}
	}

	// ===================================================
	/**
	 * @author heroe iteriert in einer Linie (Spalte bzw. Zeile) des Bildes
	 */
	private class IteratorLinie1 implements Iterator<Boolean> {
		final BufferedImage image;
		final boolean istSpalte;
		final int iLinie;
		final int weissInt;
		final int indexMax;
		private int currentIndex;

		IteratorLinie1(BufferedImage image, boolean istSpalte, int iLinie, int index0, int laenge, int weissInt) {
			this.image = image;
			this.istSpalte = istSpalte;
			this.iLinie = iLinie;
			this.weissInt = weissInt;
			this.currentIndex = index0 - 1;
			// this.indexMax = istSpalte ? image.getHeight()-1 :
			// image.getWidth()-1;
			this.indexMax = index0 + laenge - 1;
		}

		String gibLinienName() {
			String s = istSpalte ? "Spalte" : "Zeile";
			return s;
		}

		@Override
		public boolean hasNext() {
			return currentIndex <= indexMax;
		}

		@Override
		public Boolean next() {
			currentIndex++;
			int intRGB = 0;
			if (istSpalte) {
				intRGB = image.getRGB(iLinie, currentIndex);
			} else {
				intRGB = image.getRGB(currentIndex, iLinie);
			}
			return intRGB != weissInt;
		}

		/**
		 * @return Den Index beim letzten Ruf auf next()
		 */
		public int gibIndex() {
			return currentIndex - 1;
		}

		@Override
		public void remove() {
		}

		@Override
		public void forEachRemaining(Consumer<? super Boolean> action) {
		}
	}

	// ===================================================
	final ArrayList<StricheEinerLinie> spaltenStriche;
	final ArrayList<StricheEinerLinie> zeilenStriche;

	/**
	 * @param iterator
	 * @return Alle Striche einer Linie (Spalte bzw. Zeile)
	 */
	private StricheEinerLinie gibLinienStriche(IteratorLinie1 iterator) {
		StricheEinerLinie stricheDieserLinie = new StricheEinerLinie(iterator.gibLinienName(), iterator.iLinie);

		int iSchwarzStart = -1;
		boolean istLetzterSchwarz = false;

		while (iterator.hasNext()) {
			Boolean istSchwarz = iterator.next();
			if (istSchwarz) {
				if (!istLetzterSchwarz) {
					istLetzterSchwarz = true;
					iSchwarzStart = iterator.gibIndex();
				}
			} else {
				// Weiss gefunden
				if (istLetzterSchwarz & iSchwarzStart >= 0) {
					int iAktuell = iterator.gibIndex();
					int laenge = iAktuell - iSchwarzStart;
					if (laenge > 1) {
						Strich neuerStrich = new Strich(iSchwarzStart, laenge);
						stricheDieserLinie.add(neuerStrich);
					}
				}

				iSchwarzStart = -1;
				istLetzterSchwarz = false;
			} // if (istSchwarz){
		} // while
		return stricheDieserLinie;
	}

	public LinienStriche(BufferedImage image, Rectangle r) {
		if (r == null) {
			r = new Rectangle(0, 0, image.getWidth(), image.getHeight());
		}

		zeilenStriche = new ArrayList<StricheEinerLinie>();
		spaltenStriche = new ArrayList<StricheEinerLinie>();

		// Ein Weiss-Pixelwert erstellen
		int hell = 255;
		Color weissColor = new Color(hell, hell, hell);
		int weissInt = weissColor.getRGB();

		for (int iZeile = r.y; iZeile < r.y + r.height; iZeile++) {
			IteratorLinie1 iteratorZeile = new IteratorLinie1(image, false, iZeile, r.x, r.width, weissInt);
			StricheEinerLinie stricheEinerLinie = gibLinienStriche(iteratorZeile);
			if (!stricheEinerLinie.isEmpty()) {
				zeilenStriche.add(stricheEinerLinie);
			}
		} // for

		for (int iSpalte = r.x; iSpalte < r.x + r.width; iSpalte++) {
			IteratorLinie1 iteratorSpalte = new IteratorLinie1(image, true, iSpalte, r.y, r.height, weissInt);
			StricheEinerLinie stricheEinerLinie = gibLinienStriche(iteratorSpalte);
			if (!stricheEinerLinie.isEmpty()) {
				spaltenStriche.add(stricheEinerLinie);
			}
		} // for
	}

	public LinienStriche(ArrayList<StricheEinerLinie> spaltenStriche, ArrayList<StricheEinerLinie> zeilenStriche) {
		this.spaltenStriche = spaltenStriche;
		this.zeilenStriche = zeilenStriche;
	}

	public void systemOut() {
		// int indexMax = zeilenStriche.length; // 400;
		// for (int iZeile = 0; iZeile < indexMax; iZeile++) {
		// StricheEinerLinie stricheEinerLinie = zeilenStriche[iZeile];
		// if ( ! stricheEinerLinie.isEmpty()){
		// stricheEinerLinie.systemOut("Zeile", iZeile);
		// }
		// } // for
		int nMax = 800;
		for (int iSpalte = 0; iSpalte < Math.min(nMax, spaltenStriche.size()); iSpalte++) {
			spaltenStriche.get(iSpalte).systemOut();
		} // for
	}

	private ArrayList<StricheEinerLinie> gibLangeStricheAb(ArrayList<StricheEinerLinie> striche, int minStrichLaenge) {
		ArrayList<StricheEinerLinie> neueStriche = new ArrayList<StricheEinerLinie>();

		for (int i = 0; i < striche.size(); i++) {
			StricheEinerLinie stricheEinerLinie = striche.get(i);
			StricheEinerLinie langeStriche = stricheEinerLinie.gibLangeStriche(minStrichLaenge);
			if (!langeStriche.isEmpty()) {
				neueStriche.add(langeStriche);
			}
		} // for
		return neueStriche;
	}

	public LinienStriche gibLangeStricheAb(int minStrichLaenge) {
		ArrayList<StricheEinerLinie> neueSpaltenStriche = gibLangeStricheAb(spaltenStriche, minStrichLaenge);
		ArrayList<StricheEinerLinie> neueZeilenStriche = gibLangeStricheAb(zeilenStriche, minStrichLaenge);
		LinienStriche neueStriche = new LinienStriche(neueSpaltenStriche, neueZeilenStriche);
		return neueStriche;
	}

	/**
	 * Gibt die zu Gruppen zusammengefassten L�ngen aus, nach L�nge sortiert.
	 * 
	 * @param striche
	 * @param dimension
	 *            des Rechteckes, in dem sich die Striche befinden
	 */
	private void systemOutLaengen(ArrayList<StricheEinerLinie> striche, Dimension dimension, boolean istSpalten) {
		if (striche.isEmpty()) {
			String s = String.format("%s.systemOutLaengen() ohne Striche", getClass().getName());
			System.out.println(s);
			return;
		}

		// Alle L�ngen bereitstellen
		int anzahl = 0;
		for (StricheEinerLinie stricheEinerLinie : striche) {
			anzahl += stricheEinerLinie.size();
		}

		int[] laengen = new int[anzahl];
		int i = 0;
		for (StricheEinerLinie stricheEinerLinie : striche) {
			for (Strich strich : stricheEinerLinie) {
				laengen[i] = strich.laenge;
				i++;
			}
		}

		// L�ngen-Gruppen bilden
		boolean istInvers = true;
		WerteGruppeComparator comparator = new WerteGruppeComparator(VergleichsArt.DURCHSCHNITT, istInvers);
		List<WerteGruppe> laengenGruppen = WerteGruppe.gibGruppen(laengen, 20, comparator);
		String sLinie = striche.get(0).linienName;

		// L�ngen-Text bilden
		float bezugsLaenge = dimension.height;
		if (!istSpalten) {
			bezugsLaenge = dimension.width;
		}
		String sLaengen = "";
		for (WerteGruppe werteGruppe : laengenGruppen) {
			int durchschnitt = werteGruppe.gibDurchschnitt();
			float laengeProzent = (durchschnitt * 100) / bezugsLaenge;
			sLaengen += String.format(" %1.1f%%=%d:%dx", laengeProzent, durchschnitt, werteGruppe.gibWerteAnzahl());
		}

		String sOut = String.format("%s: %s", sLinie, sLaengen);
		System.out.println(sOut);
	}

	public void systemOutLaengen(Dimension dimension) {
		systemOutLaengen(spaltenStriche, dimension, true);
		systemOutLaengen(zeilenStriche, dimension, false);
	}

	public void sortierenNachLaenge() {
		for (StricheEinerLinie stricheEinerLinie : spaltenStriche) {
			Collections.sort(stricheEinerLinie);
		}
		for (StricheEinerLinie stricheEinerLinie : zeilenStriche) {
			Collections.sort(stricheEinerLinie);
		}
	}
}
