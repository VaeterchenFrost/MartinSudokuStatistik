package sudoku.bild;

import java.awt.Rectangle;

/**
 * @author heroe
 * Die 4 Kanten eines Rechtecks
 */
class Kanten {
	private int links;
	private int oben;
	private int rechts;
	private int unten;

	/**
	 * @param links
	 * @param oben
	 * @param rechts
	 * @param unten
	 */
	Kanten(int links, int oben, int rechts, int unten) {
		super();
		this.links = links;
		this.oben = oben;
		this.rechts = rechts;
		this.unten = unten;
	}


	@Override
	public String toString() {
		return "Kanten [links=" + links + ", oben=" + oben + ", rechts=" + rechts + ", unten=" + unten + "]";
	}

	/**
	 * @param links
	 * @param oben
	 * @param rechts
	 * @param unten
	 */
	void erweitere(int links, int oben, int rechts, int unten) {
		if (links < this.links) this.links = links;
		if (oben < this.oben) this.oben = oben;
		if (rechts > this.rechts) this.rechts = rechts;
		if (unten > this.unten) this.unten = unten;
	}
	
	Rectangle gibRechteck() {
		int breite = rechts - links +1;
		int hoehe = unten - oben +1;
		Rectangle r = new Rectangle(links, oben, breite, hoehe);
		
		return r;
	}

	/**
	 * Dreht das Rechteck 90° nach rechts, also wird z.B. rechts zu oben, unten zu rechts...
	 */
	void rechtsDrehen(){
		int altesLinks = links;
		links = oben;
		oben = altesLinks;
		int altesUnten = unten;
		unten = rechts;
		rechts = altesUnten;
	}

	/**
	 * @param kanten2
	 * @return Das Maximum der beiden Rechtecke
	 */
	Kanten gibMaximum(Kanten kanten2){
		if (kanten2 == null){
			return this;
		}
		
		erweitere(kanten2.links, kanten2.oben, kanten2.rechts, kanten2.unten);
		return this;
	}

}
