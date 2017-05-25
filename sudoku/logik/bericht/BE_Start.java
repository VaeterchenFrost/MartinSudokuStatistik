package sudoku.logik.bericht;

import sudoku.logik.Klugheit;

public class BE_Start {
	private Klugheit klugheit;
	private int nFreieFelder;
	private int nFreieZeilen;
	private int nFreieSpalten;
	private int nFreieKaesten;

	public BE_Start(Klugheit klugheit, int nFreieFelder, int nFreieZeilen, int nFreieSpalten, int nFreieKaesten) {
		super();
		this.klugheit = klugheit;
		this.nFreieFelder = nFreieFelder;
		this.nFreieZeilen = nFreieZeilen;
		this.nFreieSpalten = nFreieSpalten;
		this.nFreieKaesten = nFreieKaesten;
	}

	/**
	 * @return Anzahl freier Felder
	 */
	public int gibAnzahlFreieFelder() {
		return nFreieFelder;
	}

	/**
	 * @return Anzahl freier K�sten
	 */
	public int gibAnzahlFreieKaesten() {
		return nFreieKaesten;
	}

	/**
	 * @return Anzahl freier Spalten
	 */
	public int gibAnzahlFreieSpalten() {
		return nFreieSpalten;
	}

	/**
	 * @return Anzahl freier Zeilen
	 */
	public int gibAnzahlFreieZeilen() {
		return nFreieZeilen;
	}

	/**
	 * @return klugheit mit all den von ihr beherrschten Logiken
	 */
	public Klugheit gibKlugheit() {
		return klugheit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BE_Start [" + " klugheit=" + klugheit + " freie Felder=" + nFreieFelder + " freie Zeilen="
				+ nFreieZeilen + " freie Spalten=" + nFreieSpalten + " freie K�sten=" + nFreieKaesten + "]";
	}
}
