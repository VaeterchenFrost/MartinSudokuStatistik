package sudoku.bild;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heroe
 * Zwei Striche: Sie haben einen Abstand. 
 * Die Indizees der Striche laufen von strich1 zu strich2 (hoch).
 */
class Abstand {
	/**
	 * @param abstaende
	 * @return Die reinen Abstandswerte im int-Array
	 */
	static public int[] gibAbstaende(List<Abstand> abstaende){
		int[] werte = new int[abstaende.size()];
		for (int i = 0; i < abstaende.size(); i++) {
			Abstand abstand = abstaende.get(i);
			werte[i] = abstand.gibAbstand();
		}
		return werte;
	}

	/**
	 * @param striche
	 * @return Die Striche in Form einer Abstandsliste oder
	 * 			null wenn es nur weniger als zwei Striche gibt 
	 */
	static public List<Abstand> gibAbstaende (StrichListe striche){
		if (striche.size() < 2){
			return null;
		}
		
		List<Abstand> abstaende = new ArrayList<Abstand>();
		for (int i = 0; i < striche.size()-1; i ++) {
			Strich strich1 = striche.get(i);
			Strich strich2 = striche.get(i+1);
			Abstand abstand = new Abstand(strich1, strich2);
			abstaende.add(abstand);
		}
		return abstaende;
	}

	/**
	 * @param abstaende
	 * @return In der Reihenfolge der Abstände die auftretenden Striche
	 */
	static public StrichListe gibStrichListe(List<Abstand> abstaende){
		StrichListe strichListe = new StrichListe();
		
		for (int i = 0; i < abstaende.size(); i++) {
			Abstand abstand = abstaende.get(i);
			if(i == 0){
				// Auch den strich1 eintragen
				strichListe.add(abstand.strich1);
			}
			strichListe.add(abstand.strich2);
		}
		return strichListe;
	}

	/**
	 * @param abstand Abstand mit dem doppelten des Soll-Abstandes
	 * @return Die beiden Abstände jeweils mit dem Soll-Abstand, die den abstand ersetzen
	 */
	static public List<Abstand> gibErsatzAbstaende(Abstand abstand){
		// mittleren Strich erstellen
		int strichBreite = Math.min(abstand.strich1.gibBreite(), abstand.strich1.gibBreite());
		int sprungHoehe1 = Math.max(abstand.strich1.von.gibSprungHoehe(), abstand.strich1.nach.gibSprungHoehe());
		int sprungHoehe2 = Math.max(abstand.strich2.von.gibSprungHoehe(), abstand.strich2.nach.gibSprungHoehe());
		int sprungHoehe = Math.max(sprungHoehe1, sprungHoehe2);
		int abstandWert = (abstand.gibAbstand() - strichBreite) / 2;
//		int abstand2 = abstand.gibAbstand()  - abstand1 - strichBreite;
		int index0 = abstand.strich1.nach.gibVonIndex()+1;
		Sprung sprungVon = new Sprung(index0+abstandWert, -sprungHoehe);
		Sprung sprungNach = new Sprung(index0+abstandWert+strichBreite, sprungHoehe);
		Strich strich = new Strich(sprungVon, sprungNach);
		
		Abstand abstand0 = new Abstand(abstand.strich1, strich);
		Abstand abstand1 = new Abstand(strich, abstand.strich2);
		List<Abstand> returnList = new ArrayList<Abstand>();
		
		returnList.add(abstand0);
		returnList.add(abstand1);
		return returnList;
	}
	
	/**
	 * @param abstand1
	 * @param abstand2
	 * @return Abstandsobjekt, dass von abstand1.strich2 bis abstand2.strich1 geht.
	 */
	static public Abstand gibErsatzAbstand(Abstand abstand1, Abstand abstand2){
		Abstand abstand = new Abstand(abstand1.strich2, abstand2.strich1);
		return abstand;
	}

	// ===========================================================
	Strich strich1;
	Strich strich2;
	
	public Abstand(Strich strich1, Strich strich2 ) {
		this.strich1 = strich1;
		this.strich2 = strich2;
	}

	/**
	 * @return Zwischenraum zwischen den beiden Strichen
	 */
	public int gibAbstand(){
		int abstand = strich2.von.gibVonIndex() - strich1.nach.gibVonIndex() +1;
		return abstand;
	}

	@Override
	public String toString() {
		return String.valueOf(gibAbstand());
	}
	
}
