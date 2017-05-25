package sudoku.kern.info;

public class MoeglicheZahl {

	public int gibZahl() {
		return zahl;
	}

	public boolean istMarkiert() {
		return istMarkiert;
	}

	public void setzeMarkiert() {
		this.istMarkiert = true;
	}

	private int zahl;
	private boolean istMarkiert;

	public MoeglicheZahl(int zahl) {
		this.zahl = zahl;
		istMarkiert = false;
	}
}
