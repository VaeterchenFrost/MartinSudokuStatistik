package sudoku.bild.leser;

abstract class KriteriumZahl_BildInfo extends KriteriumUnd {
	final int zahl;

	/**
	 * @param zahl
	 * @param kriterien
	 */
	KriteriumZahl_BildInfo(int zahl) {
		super("Kriterium Zahl " + zahl);
		this.zahl = zahl;
		super.setzeKriterien(gibKriterien());
	}

	abstract protected KriteriumBildInfo[] gibKriterien();

	// private void systemOut(float erfuellungsGradZahl, Set<KriteriumErgebnis>
	// erfuellungsGradJeKriterium){
	// if (ZahlLeser.istSystemOut()){
	// System.out.println(String.format("%s Die Zahl %d ist zu %1.1f%%
	// erf�llt.", getClass().getName(), zahl, erfuellungsGradZahl));
	//// for(KriteriumErgebnis kriteriumErgebnis: erfuellungsGradJeKriterium){
	//// System.out.println(String.format("%1.1f%% %s",
	// kriteriumErgebnis.erfuellungsGrad, kriteriumErgebnis.kriterium));
	//// }
	// }
	// }
	// public int gibZahl(){
	// return zahl;
	// }
}
