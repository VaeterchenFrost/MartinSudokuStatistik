package sudoku.logik;

import java.util.ArrayList;

class Kombinationen {

	/**
	 * @param teilnehmer
	 * @param gruppenGroesse
	 * @return Alle Kombinationen von Teilnehmern in Gruppen
	 */
	static ArrayList<int[]> gibAlleKombinationen(int[] teilnehmer, int gruppenGroesse) {
		ArrayList<int[]> gruppenListe = new ArrayList<>();

		for (int iErster = 0; iErster <= teilnehmer.length - gruppenGroesse; iErster++) {
			// Der Erste
			int erster = teilnehmer[iErster];

			// Alle Gruppen mit diesem ersten bilden
			for (int iZweiter = iErster + 1; iZweiter <= teilnehmer.length - gruppenGroesse + 1; iZweiter++) {
				// Eine Gruppe bilden
				int[] gruppe = new int[gruppenGroesse];
				int indexInGruppe = 0;
				gruppe[indexInGruppe++] = erster;
				for (int iGruppenTeilnehmer = 0; iGruppenTeilnehmer < gruppe.length - 1; iGruppenTeilnehmer++) {
					gruppe[indexInGruppe++] = teilnehmer[iZweiter + iGruppenTeilnehmer];
				}
				gruppenListe.add(gruppe);
			}
		}
		return gruppenListe;
	}

}
