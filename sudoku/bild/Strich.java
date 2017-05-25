package sudoku.bild;

/**
 * @author heroe Schwarzer Strich: Die zwei Kanten des Striches in der Form der
 *         zwei Spr�nge weiss/schwarz und schwarz/weiss. Der Sprung von weiss
 *         auf dem kleineren Index nach Schwarz. Und der Sprung von schwarz auf
 *         dem gr��eren Index zur�ck nach weiss.
 */
class Strich {

	/**
	 * @param strich1
	 *            Strich mit kleinerem Index
	 * @param strich2
	 *            Strich mit gr��erem Index
	 * @return Zwischenraum zwischen den beiden Strichen
	 */
	static public int gibAbstand(Strich strich1, Strich strich2) {
		int abstand = strich2.von.gibVonIndex() - strich1.nach.gibVonIndex() + 1;
		return abstand;
	}

	// =========================================================
	Sprung von;
	Sprung nach;

	public Strich(Sprung von, Sprung nach) {
		this.von = von;
		this.nach = nach;
	}

	/**
	 * @return Breite des (schwarzen) Striches
	 */
	public int gibBreite() {
		int breite = nach.gibVonIndex() - von.gibVonIndex();
		return breite;
	}

	@Override
	public String toString() {
		return "Strich [Breite=" + gibBreite() + ", von=" + von + ", nach=" + nach + "]";
	}

	public void transformiereIndizees(int neuerUrsprung) {
		von.transformiereIndex(neuerUrsprung);
		nach.transformiereIndex(neuerUrsprung);
	}

}
