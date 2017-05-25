package sudoku.bild.leser;

class KriteriumOder implements KriteriumBildInfo {

	// ========================================================
	private String name;
	private KriteriumBildInfo kriterium1;
	private KriteriumBildInfo kriterium2;

	/**
	 * @param name
	 * @param vonFeld
	 * @param bisFeld
	 * @param sollSchwarz
	 * @param erfuellung0Min
	 * @param erfuellung0Max
	 */
	KriteriumOder(String name, KriteriumBildInfo kriterium1, KriteriumBildInfo kriterium2) {
		super();
		this.name = name;
		this.kriterium1 = kriterium1;
		this.kriterium2 = kriterium2;
	}

	public float gibErfuellungsGrad(ZahlBildInfo zahlBildInfo, boolean istHierSystemOut) {
		float grad1 = kriterium1.gibErfuellungsGrad(zahlBildInfo, istHierSystemOut);
		float grad2 = kriterium2.gibErfuellungsGrad(zahlBildInfo, istHierSystemOut);
		float grad = grad1;

		if (grad2 > grad1) {
			grad = grad2;
		}

		if (istHierSystemOut) {
			System.out.println(String.format("%s Erf�llt=%1.1f%% %s Kriterium1=%1.1f%% Kriterium2=%1.1f%%",
					getClass().getSimpleName(), grad, this.name, grad1, grad2));
		}

		return grad;
	}

	@Override
	public String toString() {
		return "KriteriumOder [" + name + ": Kriterium1=" + kriterium1.gibName() + " Kriterium2=" + kriterium2.gibName()
				+ "]";
	}

	public String gibName() {
		return this.toString();
	}
}
