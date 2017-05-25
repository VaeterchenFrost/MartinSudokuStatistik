package sudoku.bild;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import sudoku.bild.LinienStriche.StricheEinerLinie;
import sudoku.bild.leser.ZahlLeser;
import sudoku.kern.animator.Animator_DrehenRechts;
import sudoku.kern.feldmatrix.FeldMatrix;
import sudoku.kern.feldmatrix.FeldNummer;

/**
 * @author heroe
 *
 */
class BildFeld {
	static private boolean istTestAnzeige = false; // true;

	/**
	 * @param linien
	 *            Jede Linie sortiert nach Strichl�nge: Auf Index0 der l�ngste
	 *            Strich
	 * @param linienLaenge
	 *            L�nge einer jeden Linie
	 * @param linienIndexStart
	 *            Ab der Linie mit diesem Index (in ihr) beginnt das
	 *            FeldRechteck
	 * @param linienIndexEnde
	 *            Bis zu der Linie mit diesem Index (in ihr) endet das
	 *            FeldRechteck
	 * @return Index-Bereich der Zahl im Bild oder null: Oben ist die Linie mit
	 *         Array-Index=0 Links ist der Index des Strich-Starts
	 */
	static private Kanten gibZahlBereich(ArrayList<StricheEinerLinie> linien, int linienLaenge) {
		final int langerStrichLaengeMinProzent = 20;

		// if (istTestAnzeige){
		// String name = "";
		// if ( ! linien.isEmpty()){
		// name = linien.get(0).linienName;
		// }
		// System.out.println(String.format("gibZahlBereich %s: nLinien=%d
		// Linien-L�nge=%d",
		// name, linien.size(), linienLaenge));
		// }
		IteratorLinien iterator = new IteratorLinien(linien, 0, linien.size() - 1);
		final int langerStrichLaengeMin = Math.round((linienLaenge * langerStrichLaengeMinProzent) / 100.0f);
		Kanten kanten = null;

		while (iterator.hasNext()) {
			StricheEinerLinie stricheEinerLinie = iterator.next();
			if (!stricheEinerLinie.isEmpty()) {
				LinienStriche.Strich strich0 = stricheEinerLinie.get(0);
				if (strich0.laenge >= langerStrichLaengeMin) {
					int links = strich0.startIndex;
					int oben = stricheEinerLinie.iLinie;
					int rechts = strich0.gibEndeIndex();
					int unten = stricheEinerLinie.iLinie;
					if (kanten == null) {
						kanten = new Kanten(links, oben, rechts, unten);
					} else {
						kanten.erweitere(links, oben, rechts, unten);
					}
				} // if (strich0.laenge
			} // if ( ! stricheEinerLinie.isEmpty()
		} // while

		if (kanten == null) {
			if (istTestAnzeige) {
				System.out.println("gibZahlBereich: Kanten = null");
			}
			return null;
		}

		// if (istTestAnzeige){
		// System.out.println(String.format("gibZahlBereich: Linien-L�nge=%d
		// %s", linienLaenge, kanten));
		// }
		return kanten;
	}

	/**
	 * @return Das Zentrum, dass sicher weit genug vom schwarzen Feldrand
	 *         eintfernt liegt.
	 */
	static private Rectangle gibZentrum(Rectangle r) {
		int randX = r.width / 4;
		int randY = r.height / 4;
		int rand = -1 * Math.min(randX, randY);
		r = vergroessern(r, rand);
		return r;
	}

	/**
	 * @param image
	 * @return Anteil der weissen Pixel im Zentrum des feldRechteck in Prozent
	 *         Dieses Zentrum ist der Bereich des feldRechtecks, in dem eine
	 *         Zahl stehen k�nnte.
	 */
	static private int gibZentrumWeissAnteil(BufferedImage image, Rectangle rBasis) {
		Rectangle r = gibZentrum(rBasis);
		// if (istTestAnzeige){
		// maleRechteck(image, r);
		// }

		LinienWeiss linienWeiss = new LinienWeiss(image, r);
		int weissAnteil = linienWeiss.gibWeissAnteil();
		return weissAnteil;
	}

	static public boolean istTestAnzeige() {
		return istTestAnzeige;
	}

	static private void maleRechteck(BufferedImage image, Rectangle r, int vergroesserung) {
		Graphics2D g = image.createGraphics();

		r = vergroessern(r, vergroesserung);

		// g.setXORMode(Color.WHITE);
		g.setColor(Color.BLACK);
		g.drawRect(r.x, r.y, r.width, r.height);

		g.setColor(Color.WHITE);
		int x2 = r.x + r.width;
		int y2 = r.y + r.height;
		g.drawLine(r.x, r.y, x2, y2);
		g.drawLine(x2, r.y, r.x, y2);
	}

	/**
	 * @param rechteck
	 * @param vergroesserung
	 *            jeden Randes. Ein negativer Wert verkleinert.
	 * @return Das neue zu Rechteck zentrisch liegende gr��ere bzw. kleinere
	 *         Rechteck Wenn das gewollte Rechteck eine negative L�nge bek�me,
	 *         so wird null zur�ckgegeben.
	 */
	static private Rectangle vergroessern(Rectangle rechteck, int vergroesserung) {
		int neueBreite = rechteck.width + 2 * vergroesserung;
		int neueHoehe = rechteck.height + 2 * vergroesserung;

		if ((neueBreite < 0) | (neueHoehe < 0)) {
			return null;
		}
		neueHoehe = Math.max(1, neueHoehe);
		Rectangle r = new Rectangle(rechteck.x - vergroesserung, rechteck.y - vergroesserung, neueBreite, neueHoehe);
		return r;
	}

	/**
	 * @param rechteck
	 * @param vergroesserungProzent
	 *            jeden Randes. Ein negativer Wert verkleinert.
	 * @return Das neue zu Rechteck zentrisch liegende gr��ere bzw. kleinere
	 *         Rechteck
	 */
	static private Rectangle vergroessernProzent(Rectangle rechteck, int vergroesserungProzent) {
		int vergroesserungX = Math.round((rechteck.width * vergroesserungProzent) / 100.0f);
		int vergroesserungY = Math.round((rechteck.height * vergroesserungProzent) / 100.0f);
		Rectangle r = new Rectangle(rechteck.x - vergroesserungX, rechteck.y - vergroesserungY,
				rechteck.width + 2 * vergroesserungX, rechteck.height + 2 * vergroesserungY);
		return r;
	}

	// =============================================================
	private FeldNummer feldNummer;
	private Rectangle feldRechteck;
	private Rectangle zahlRechteck;
	private SchwarzAnteile schwarzAnteile;
	/**
	 * Die Bereiche, in die das ZahlRechteck f�r die Decodierung aufgeteilt ist.
	 */
	private ZahlBereiche zahlBereiche;
	/**
	 * Die Wahrscheinlichkeit f�r jede der Zahlen 1 bis 9 in Prozent. Auf Index
	 * 0 steht die Zahl mit der gr��ten Wahrscheinlichkeit, auch null.
	 */
	private ArrayList<ZahlLeser.ZahlErgebnis> zahlWahrscheinlichkeiten;
	private Integer zahl;

	public BildFeld(FeldNummer feldNummer, Strich spaltenStrich0, Strich spaltenStrich1, Strich zeilenStrich0,
			Strich zeilenStrich1) {
		this.feldNummer = feldNummer;
		int x = spaltenStrich0.nach.gibVonIndex() + 1;
		int xLast = spaltenStrich1.von.gibVonIndex();
		int breite = xLast - x + 1;

		int y = zeilenStrich0.nach.gibVonIndex() + 1;
		int yLast = zeilenStrich1.von.gibVonIndex();
		int hoehe = yLast - y + 1;

		this.feldRechteck = new Rectangle(x, y, breite, hoehe);
		this.zahlRechteck = null;
		this.schwarzAnteile = null;
		this.zahl = null;
	}

	/**
	 * Dreht die FeldNummer, die Schwarzanteile, feldRechteck, zahlRechteck,
	 * zahlBereiche
	 */
	public void drehe90GradRechts(Dimension bildDimension) {
		Animator_DrehenRechts animator = new Animator_DrehenRechts();
		FeldNummer neueFeldNummer = animator.gibFeldNummer(feldNummer, FeldMatrix.feldNummerMax);
		feldNummer = neueFeldNummer;
		if (schwarzAnteile != null) {
			schwarzAnteile.drehen(animator);
		}
		feldRechteck = Bild.rotiere90Grad(bildDimension, feldRechteck);
		zahlRechteck = Bild.rotiere90Grad(bildDimension, zahlRechteck);
		if (zahlBereiche != null) {
			zahlBereiche.rotiere90Grad(bildDimension);
		}
	}

	public FeldNummer gibFeldNummer() {
		return new FeldNummer(feldNummer);
	}

	public Rectangle gibFeldRechteck() {
		return new Rectangle(feldRechteck);
	}

	public Integer gibZahl() {
		if (zahl == null) {
			return null;
		}
		return new Integer(zahl.intValue());
	}

	public Rectangle gibZahlRechteck() {
		return new Rectangle(zahlRechteck);
	}

	/**
	 * @param image
	 * @return Anteil der weissen Pixel im Zentrum des feldRechteck in Prozent
	 *         Dieses Zentrum ist der Bereich des feldRechtecks, in dem eine
	 *         Zahl stehen k�nnte.
	 */
	public int gibZentrumWeissAnteil(BufferedImage image) {
		int weissAnteil = gibZentrumWeissAnteil(image, feldRechteck);
		return weissAnteil;
	}

	/**
	 * Setzt die Zahl auf Basis der SchwarzAnteile
	 */
	public Integer setzeZahl() {
		this.zahl = null;
		// if (BildFeld.istTestAnzeige()){
		// System.out.println(String.format("BildFeld %s setzeZahl() auf der
		// Basis der Schwarzanteile", feldNummer));
		// }
		if (schwarzAnteile != null) {
			// Zahl-Erkennung per Bild-Info
			this.zahlWahrscheinlichkeiten = ZahlLeser.gibZahlWahrscheinlichkeiten(schwarzAnteile);
			this.zahl = zahlWahrscheinlichkeiten.get(0).zahl;

			if (zahl != null) {
				if (ZahlLeser.istSystemOutZahl(zahl)) {
					schwarzAnteile.systemOut(feldNummer);
				}
			}

		}
		return this.zahl;
	}

	/**
	 * Setzt die Zahl auf Basis der Ausg�nge
	 */
	public void setzeZahl(BufferedImage image) {
		if (this.zahl == null) {
			boolean istHierSystemOut = false;
			// istHierSystemOut = feldNummer.equals(new FeldNummer(4, 5));

			if (istHierSystemOut) {
				System.out.println(String.format("BildFeld %s setzeZahl() auf der Basis der Ausg�nge", feldNummer));
			}
			this.zahl = ZahlLeser.gibZahl(image, zahlRechteck, istHierSystemOut);
		}
	}

	/**
	 * Setzt auf der Basis des Feldrechtecks das Zahlenrechteck. Au�erdem die
	 * ZahlBereiche und SchwarzAnteile.
	 * 
	 * @param image
	 * @return null oder das Zahlenrechteck
	 */
	public Rectangle setzeZahlenRechteck(BufferedImage image) {

		// FeldRechteck um den Rand verkleinern => Test-Rechteck
		final int randLaengeProzent = 7;
		Rectangle testRechteck = vergroessernProzent(feldRechteck, -1 * randLaengeProzent);
		if (istTestAnzeige) {
			System.out.println(String.format("%s.setzeZahlenRechteck() %s", getClass().getSimpleName(), feldNummer));
			System.out.println(String.format("Feld-Rechteck %s", feldRechteck));
			System.out.println(String.format("Test-Rechteck %s", testRechteck));
		}

		LinienStriche linienStriche = new LinienStriche(image, testRechteck);

		// if (istTestAnzeige){
		// linienStriche.systemOutLaengen(feldRechteck.getSize());
		// }

		linienStriche.sortierenNachLaenge();
		Kanten kantenX = gibZahlBereich(linienStriche.zeilenStriche, testRechteck.width);
		Kanten kantenY = gibZahlBereich(linienStriche.spaltenStriche, testRechteck.height);

		// if (istTestAnzeige){
		// System.out.println(String.format("setzeZahlenRechteck KantenX %s",
		// kantenX));
		// System.out.println(String.format("setzeZahlenRechteck KantenY %s",
		// kantenY));
		// }

		if (kantenY != null) {
			kantenY.rechtsDrehen();
			// if (istTestAnzeige){
			// System.out.println(String.format("setzeZahlenRechteck KantenY %s
			// gedreht", kantenY));
			// }
		}

		Kanten kanten = kantenX;
		if (kanten == null) {
			kanten = kantenY;
		} else {
			kanten = kanten.gibMaximum(kantenY);
			// if (istTestAnzeige){
			// System.out.println(String.format("setzeZahlenRechteck Kanten_
			// %s", kanten));
			// }
		}

		if (kanten != null) {
			zahlRechteck = kanten.gibRechteck();
		}

		if (istTestAnzeige) {
			if (zahlRechteck == null) {
				System.out.println("Zahl-Rechteck == null");
			} else {
				System.out.println(String.format("Zahl-Rechteck %s", zahlRechteck));
			}
		}

		if (zahlRechteck != null) {
			zahlBereiche = new ZahlBereiche(zahlRechteck);
			schwarzAnteile = new SchwarzAnteile(image, zahlBereiche);
		}

		return zahlRechteck;
	}

	public void systemOutErgebnis(BufferedImage image) {
		String sWahrscheinlichkeiten = ZahlLeser.gibErgebnisString(zahlWahrscheinlichkeiten);
		float w1 = zahlWahrscheinlichkeiten.get(1).erfuellungsGrad;
		float w0 = zahlWahrscheinlichkeiten.get(0).erfuellungsGrad;
		float vorsprung = w0 - w1;
		String sVorsprung = "";
		if (vorsprung > 0) {
			sVorsprung = String.format("Vorsprung=%5.1f%%", vorsprung);
		}
		System.out.println(String.format("%s %s=%s %s (%s)", getClass().getSimpleName(), feldNummer, zahl, sVorsprung,
				sWahrscheinlichkeiten));

		// if (zahlBereiche != null){
		// if (istTestAnzeige | ZahlLeser.istSystemOut()){
		// zahlBereiche.male(image);
		// }
		// }
		//
		// if (istTestAnzeige & (zahlRechteck != null)){
		// maleRechteck(image, zahlRechteck, 2);
		// }

		zahlBereiche.male(image);
		maleRechteck(image, zahlRechteck, 2);
	}

}
