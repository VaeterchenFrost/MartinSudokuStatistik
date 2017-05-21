package sudoku.logik;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.FeldListe;

abstract class FeldPaarGeber {
	/**
	 * @param gruppe In dieser Gruppe befinden sich alle genannten Felder.
	 * @param zahl Die Zahl, die in allen genannten Feldern (der einen Gruppe) als mögliche Zahl vorhanden ist.
	 * @param felder Alle Felder befinden sich in einer Gruppe und besitzen alle die eine mögliche Zahl; 
	 * @return Alle Felder, die über die Zahl paarweise verbunden sind oder null wenn es keine solche gibt.
	 */
	abstract ArrayList<FeldPaar> gibFeldPaare(Gruppe gruppe, int zahl, FeldListe felder);
}
