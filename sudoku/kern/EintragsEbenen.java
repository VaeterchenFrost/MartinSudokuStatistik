package sudoku.kern;

import java.util.ArrayList;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Feld;

/**
 * @author Hendrick
 * Die Klasse verwaltet Eintragsebenen.
 * Jeder Eintrag befindet sich auf einer Eintragsebene.
 * Das ist nötig für das Zeigen und Rückgängigmachen von Versuchen.
 * Ein Versuch startet (eine neue Ebene) wenn ein Feld mit mehreren Möglichen einen Eintrag erhält.
 * Der erste Eintrag überhaupt startet unbedingt eine Ebene, auch wenn dies Feld eines mit nur 1 möglichen Zahl ist.
 * Eine Ebene verschwindet mit dem Löschen des letzten Eintrags auf dieser Ebene 
 * oder wenn sie ausdrücklich als Gesamtheit gelöscht wird.
 *
 * Die Ebene Nr. 1 ist den Einträgen in freien Feldern mit nur 1 möglichen Zahl vorbehalten.
 * Versuche laufen auf den Ebenen ab Nr. 2.
 */
public class EintragsEbenen {

	private int aktuelleEbene;
	
	public EintragsEbenen() {
		reset();
	}

	/**
	 * @return Die Ebene Nr. 1 ist den Einträgen in freien Feldern mit 1 möglichen Zahl vorbehalten.
	 * (Versuche laufen auf den Ebenen ab Nr. 2.)
	 */
	static public int gibStandardEbene1(){
		return 1;
	}
	
	public boolean laeuftEine(){
		return aktuelleEbene>0;
	}
	
	public int gibNummer(){
		return aktuelleEbene;
	}
	
	public void reset(){
		aktuelleEbene = 0;
	}

	
	public void loesche(){
		aktuelleEbene--;

		if (aktuelleEbene<0){
			aktuelleEbene = 0;
		}
	}

	/**
	 * Start des Eintrag-Setzens auf einem Feld mit nur einer möglichen Zahl
	 */
	private void starteEbene(){
		aktuelleEbene++;
	}
	
	/**
	 * Ein Feld mit mehreren Möglichen erhält einen Eintrag
	 */
	private void starteVersuch(){
		if (aktuelleEbene == 0){
			// Ebene 1 überspringen
			aktuelleEbene++;
		}
		aktuelleEbene++;
	}

	/**
	 * Führt die Eintrags-Ebene mit je Anzahl der Möglichen im Feld:
	 * 	Anzahl=0: Exc; 
	 *  Anzahl=1: a) keine Einträge=>1 @see gibStandardEbene1() b)Einträge sind=>return; 
	 *  Anzahl>1 Starte Versuch (++)
	 * @param feld
	 * @param zahl Sie soll dann gesetzt werden
	 * @return true wenn eine neue Ebene für diesen Eintrag erstellt wurde
	 * @throws Exc Falls das Setzen der zahl als Eintrag anhand der Möglichen des Feldes gar nicht zulässig ist
	 */
	public boolean setzeEintragsEbene(Feld feld,  int zahl) throws Exc{
		ArrayList<Integer> moegliche = feld.gibMoegliche();
		
		if (0 == moegliche.size()) {
			throw Exc.setzeEintragNichtOhneMoegliche(feld, zahl);
		}
		// Ist dieser Eintrag möglich?
		if (! moegliche.contains(zahl)){
			throw Exc.setzeEintragNichtOhneDieseMoegliche(feld, zahl);
		}
		
		boolean istNeueEbene = false;
		// Eventuell ist Ebenen-Start
		if (1 == moegliche.size()) {
			if (!laeuftEine()) {
				starteEbene();
				istNeueEbene = true;
			}
		}
		else{
			starteVersuch();
			istNeueEbene = true;
		}
		return istNeueEbene;
	}

	public void setzeEintragsEbeneUnbedingt(int zahl) {
		this.aktuelleEbene = zahl;
	}
}
