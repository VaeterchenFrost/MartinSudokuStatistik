package sudoku.bild.leser;

import sudoku.kern.feldmatrix.FeldNummer;

/**
 * @author heroe
 * Gibt die SchwarzAnteile der Raster-Rechtecke im Zahl-Bild-Rechteck heraus
 */
public interface ZahlBildInfo {
	/**
	 * Anzahl der Linien (Spalten und Zeilen), in die das Zahl-Bild-Rechteck jeweils horizontal und vertikal aufgeteilt wird 
	 */
	static final int nLinien = 6;

	/**
	 * @param feldNummer Spalte und Zeile je von 1 bis nLinien
	 * @return SchwarzAnteil des Ausschnitt-Rechtecks im Zahl-Bild-Rechteck
	 */
	float gibSchwarzAnteil(FeldNummer feldNummer);
}
