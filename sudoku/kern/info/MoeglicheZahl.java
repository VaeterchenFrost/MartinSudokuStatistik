package sudoku.kern.info;

public class MoeglicheZahl {

	private int zahl;

	private boolean istMarkiert;

	public MoeglicheZahl(int zahl) {
		this.zahl = zahl;
		istMarkiert = false;
	}

	public int gibZahl() {
		return zahl;
	}

	public boolean istMarkiert() {
		return istMarkiert;
	}

	public void setzeMarkiert() {
		this.istMarkiert = true;
	}
}
