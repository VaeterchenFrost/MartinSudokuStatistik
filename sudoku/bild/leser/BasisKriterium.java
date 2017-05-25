package sudoku.bild.leser;

import sudoku.kern.feldmatrix.FeldNummer;

class BasisKriterium {

	private static SollSchwarz schwarz = new SollSchwarz(50, 100, null);
	private static SollSchwarz schwarzAb70 = new SollSchwarz(0, 70, null);
	private static SollSchwarz schwarzAb80 = new SollSchwarz(0, 80, null);

	// ganz weiss: halb schwarz entspricht 0%
	private static SollSchwarz weiss = new SollSchwarz(null, 0, 40);
	// weiss ab 90 % weiss aufw�rts
	private static SollSchwarz weissAb90 = new SollSchwarz(null, 10, 50);
	// weiss ab 80 % weiss aufw�rts
	private static SollSchwarz weissAb80 = new SollSchwarz(null, 20, 50);
	// weiss ab 70 % weiss aufw�rts
	private static SollSchwarz weissAb70 = new SollSchwarz(null, 30, 100);

	private static SollSchwarz weissPunkt50 = new SollSchwarz(0, 50, 100);

	// -----------------------------------------------------------------------------
	static public KriteriumBildInfo senkrechterStrichRechts14() {
		return new KriteriumFelder("Schwarz Spalte 5", schwarz, new FeldNummer(5, 1), new FeldNummer(5, 6));
	}

	// -----------------------------------------------------------------------------
	static public KriteriumBildInfo waagerechterStrichUnten2() {
		return new KriteriumFelder("Schwarz Zeile 6", schwarzAb70, new FeldNummer(1, 6), new FeldNummer(6, 6));
	}

	static public KriteriumBildInfo waagerechterStrich4() {
		KriteriumFelder kriterium1 = new KriteriumFelder("Schwarz Zeile 4", schwarzAb70, new FeldNummer(1, 4),
				new FeldNummer(6, 4));
		KriteriumFelder kriterium2 = new KriteriumFelder("Schwarz Zeile 5", schwarzAb70, new FeldNummer(1, 5),
				new FeldNummer(6, 5));

		return new KriteriumOder("Waagerechter Strich 4", kriterium1, kriterium2);
	}

	static public KriteriumBildInfo waagerechterStrichOben5() {
		return new KriteriumFelder("Waagerechter Strich 5", schwarzAb70, new FeldNummer(2, 1), new FeldNummer(5, 1));
	}

	static public KriteriumBildInfo waagerechterStrichOben7() {
		return new KriteriumFelder("Waagerechter Strich 7", schwarzAb70, new FeldNummer(1, 1), new FeldNummer(6, 1));
	}

	// -----------------------------------------------------------------------------
	static public KriteriumBildInfo grosserKreisOben9() {
		return new KriteriumFelder("Gro�er Kreis oben", schwarzAb80,
				new FeldNummer[] { new FeldNummer(2, 1), new FeldNummer(3, 1), new FeldNummer(4, 1),
						new FeldNummer(5, 1), new FeldNummer(1, 2), new FeldNummer(6, 2), new FeldNummer(1, 3),
						new FeldNummer(6, 3), new FeldNummer(2, 4), new FeldNummer(3, 4), new FeldNummer(4, 4),
						new FeldNummer(5, 4), });
	}

	static public KriteriumBildInfo grosserKreisUnten68() {
		return new KriteriumFelder("Gro�er Kreis unten", schwarzAb80,
				new FeldNummer[] { new FeldNummer(2, 3), new FeldNummer(3, 3), new FeldNummer(4, 3),
						new FeldNummer(5, 3), new FeldNummer(1, 4), new FeldNummer(6, 4), new FeldNummer(1, 5),
						new FeldNummer(6, 5), new FeldNummer(2, 6), new FeldNummer(3, 6), new FeldNummer(4, 6),
						new FeldNummer(5, 6), });
	}

	static public KriteriumBildInfo grosserKreisUntenOffen35() {
		return new KriteriumFelder("Gro�er Kreis unten offen", schwarzAb80,
				new FeldNummer[] { new FeldNummer(4, 3), new FeldNummer(5, 3), new FeldNummer(6, 4),
						new FeldNummer(1, 5), new FeldNummer(6, 5), new FeldNummer(2, 6), new FeldNummer(3, 6),
						new FeldNummer(4, 6), new FeldNummer(5, 6), });
	}

	static public KriteriumBildInfo grosserHalbkreisOben26() {
		return new KriteriumFelder("Gro�er Halbkreis oben", schwarzAb80,
				new FeldNummer[] { new FeldNummer(2, 1), new FeldNummer(3, 1), new FeldNummer(4, 1),
						new FeldNummer(5, 1), new FeldNummer(1, 2), new FeldNummer(6, 2) });
	}

	static public KriteriumBildInfo kleinerKreisOben3() {
		return new KriteriumFelder("Kleiner Kreis oben", schwarzAb80,
				new FeldNummer[] { new FeldNummer(2, 1), new FeldNummer(3, 1), new FeldNummer(4, 1),
						new FeldNummer(1, 2), new FeldNummer(5, 2), new FeldNummer(5, 3) });
	}

	static public KriteriumBildInfo kleinerKreisOben8() {
		return new KriteriumFelder("Kleiner Kreis oben", schwarzAb80,
				new FeldNummer[] { new FeldNummer(2, 1), new FeldNummer(3, 1), new FeldNummer(4, 1),
						new FeldNummer(5, 1), new FeldNummer(2, 2), new FeldNummer(5, 2), new FeldNummer(2, 3),
						new FeldNummer(5, 3) });
	}

	// -----------------------------------------------------------------------------
	static public KriteriumBildInfo bogenSenkrecht6() {
		return new KriteriumFelder("Bogen Links", schwarz, new FeldNummer(1, 3));// ,
																					// new
																					// FeldNummer(1,
																					// 4));
	}

	static public KriteriumBildInfo bogenSenkrecht9() {
		return new KriteriumFelder("Bogen Rechts", schwarz, new FeldNummer(6, 3)); // ,
																					// new
																					// FeldNummer(6,
																					// 4));
	}

	// -----------------------------------------------------------------------------
	static public KriteriumBildInfo lochGrossOben9() {
		return new KriteriumFelder("Wei� Mitte oben", weiss, new FeldNummer(3, 2), new FeldNummer(4, 3));
	}

	static public KriteriumBildInfo lochGrossUnten3568() {
		return new KriteriumFelder("Wei� Mitte unten", weissAb80, new FeldNummer(3, 4), new FeldNummer(4, 5));
	}

	static public KriteriumBildInfo lochHalbGrossOben2() {
		return new KriteriumFelder("Wei� Mitte oben", weiss, new FeldNummer(3, 2), new FeldNummer(4, 2));
	}

	static public KriteriumBildInfo lochKleinOben68() {
		return new KriteriumFelder("Wei� Mitte oben", weiss, new FeldNummer(3, 2), new FeldNummer(4, 2));
	}

	// -----------------------------------------------------------------------------
	static public KriteriumBildInfo ausgangLinksOben14() {
		return new KriteriumFelder("Wei� Links oben", weiss, new FeldNummer(1, 1), new FeldNummer(2, 1));
	}

	static public KriteriumBildInfo ausgangLinksUnten1() {
		return new KriteriumFelder("Wei� Links unten", weiss, new FeldNummer(1, 3), new FeldNummer(3, 6));
	}

	static public KriteriumBildInfo ausgangLinks2() {
		return new KriteriumFelder("Wei� Links oben", weiss,
				new FeldNummer[] { new FeldNummer(1, 3), new FeldNummer(2, 3), new FeldNummer(1, 4) });
	}

	static public KriteriumBildInfo ausgangLinks3() {
		return new KriteriumFelder("Wei� Links Mitte", weissAb90, new FeldNummer(1, 3), new FeldNummer(2, 4));
	}

	static public KriteriumBildInfo ausgangLinksUnten4() {
		return new KriteriumFelder("Wei� Links Unten", weiss, new FeldNummer(1, 6), new FeldNummer(3, 6));
	}

	static public KriteriumBildInfo ausgangLinks5() {
		return new KriteriumFelder("Wei� Links", weissAb70, new FeldNummer(1, 4), new FeldNummer(2, 4));
	}

	static public KriteriumBildInfo ausgangLinks7() {
		return new KriteriumFelder("Wei� Links", weiss,
				new FeldNummer[] { new FeldNummer(1, 2), new FeldNummer(2, 2), new FeldNummer(3, 2),
						new FeldNummer(1, 3), new FeldNummer(1, 4), new FeldNummer(1, 5), new FeldNummer(1, 6) });
	}

	static public KriteriumBildInfo ausgangLinks8() {
		return new KriteriumFelder("Wei� Links", weissAb70, new FeldNummer[] { new FeldNummer(1, 3) });
	}

	static public KriteriumBildInfo ausgangLinks9() {
		return new KriteriumFelder("Wei� Links", weissPunkt50, new FeldNummer(1, 4), new FeldNummer(1, 5));
	}

	static public KriteriumBildInfo ausgangRechts7() {
		return new KriteriumFelder("Wei� Rechts", weiss, new FeldNummer(5, 4), new FeldNummer(6, 6));
	}

	static public KriteriumBildInfo ausgangRechtsOben4() {
		return new KriteriumFelder("Wei� Rechts Oben", weissAb80, new FeldNummer(6, 1), new FeldNummer(6, 3));
	}

	static public KriteriumBildInfo ausgangRechtsOben5() {
		return new KriteriumFelder("Wei� Rechts Oben", weissAb70, new FeldNummer(5, 2), new FeldNummer(6, 2));
	}

	static public KriteriumBildInfo ausgangRechtsOben6() {
		return new KriteriumFelder("Wei� Rechts Oben", weissPunkt50, new FeldNummer(5, 2), new FeldNummer(6, 2));
	}

	static public KriteriumBildInfo ausgangRechtsUnten4() {
		return new KriteriumFelder("Wei� Rechts Unten", weissAb80, new FeldNummer[] { new FeldNummer(6, 6) });
	}

	static public KriteriumBildInfo ausgangRechtsMitte38() {
		return new KriteriumFelder("Wei� Rechts Mitte", weissAb70, new FeldNummer(6, 3), new FeldNummer(6, 3));
	}

	static public KriteriumBildInfo ausgangRechtsUnten2() {
		return new KriteriumFelder("Wei� Rechts unten", weiss,
				new FeldNummer[] { new FeldNummer(6, 4), new FeldNummer(5, 5), new FeldNummer(6, 5) });
	}

	// -----------------------------------------------------------------------------
	static public KriteriumBildInfo zahl5gegen3links() {
		return new KriteriumFelder("5 gegen 3 links Mitte", schwarz, new FeldNummer(1, 3));
	}

	static public KriteriumBildInfo zahl8gegen3links() {
		return new KriteriumFelder("8 gegen 3 links Mitte", schwarz, new FeldNummer(1, 3), new FeldNummer(2, 3));
	}

	static public KriteriumBildInfo zahl8gegen6rechts() {
		return new KriteriumFelder("8 gegen 6 rechts", schwarzAb80, new FeldNummer(5, 2), new FeldNummer(6, 2));
	}

}
