package sudoku.bild;

import java.util.ArrayList;

import sudoku.Paar;

@SuppressWarnings("serial")
public class StrichListe extends ArrayList<Strich> {
	/**
	 * Transformiert alle Indizees so, dass sie sich auf den neuen
	 * Indize-Ursprung beziehen.
	 * 
	 * @param neuerUrsprung
	 */
	public void transformiereIndizees(int neuerUrsprung) {
		for (Strich strich : this) {
			strich.transformiereIndizees(neuerUrsprung);
		}
	}

	public StrichListe() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Index-Bereich, den die Striche insgesamt benennen: Pair.Key den
	 *         1. Index (linken bzw. oberen Index) Pair.Value den letzten Index
	 *         (rechten bzw. unteren Index)
	 */
	public Paar<Integer, Integer> gibBereich() {
		int ersterIndex = this.get(0).von.gibVonIndex() + 1;
		Strich letzterStrich = this.get(this.size() - 1);
		int letzterIndex = letzterStrich.nach.gibVonIndex();
		Paar<Integer, Integer> pair = new Paar<Integer, Integer>(ersterIndex, letzterIndex);

		return pair;
	}

	/**
	 * Es werden die Striche mit titel und unter dem Liniennamen ausgegeben.
	 * 
	 * @param titel
	 * @param linienName
	 * @param striche
	 */
	public void systemOut(boolean istSystemOut, String titel, String linienName) {
		if (istSystemOut) {
			System.out.println(String.format("%s: %s-Striche Anzahl=%d", titel, linienName, this.size()));
			Strich letzterStrich = null;
			for (Strich strich : this) {
				int abstand = letzterStrich == null ? 0 : Strich.gibAbstand(letzterStrich, strich);
				letzterStrich = strich;
				System.out.println(String.format("Abstand zum Vorg�nger=%3d %s", abstand, strich));
			}
		}
	}

	public void loescheAlleAb(int index) {
		while (size() > index) {
			remove(size() - 1);
		}
	}

}
