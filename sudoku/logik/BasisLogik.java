package sudoku.logik;

import java.util.HashMap;
import java.util.Map;

import sudoku.kern.feldmatrix.Feld;
import sudoku.kern.feldmatrix.FeldListe;

/**
 * @author heroe
 * Die BasisLogik (je Feld) wird hier realisiert in setzeMoegliche.  
 */
public class BasisLogik {
	protected class FeldMitGruppen{
		final Feld feld;
		// Alle Felder, die als MIX-Gruppe für dies Feld zu beachten sind: Kasten + Zeile + Spalte 
		final Gruppe gruppe;
		
		private FeldMitGruppen(Feld feld, FeldListe alleFelder){
			this.feld = feld;
			gruppe = new Gruppe(Gruppe.Typ.MIX, feld.gibFeldNummer(), false,  alleFelder);
		}
	}
	// ===============================================================
	
	final FeldMitGruppen[] felderMitGruppen;
	private Map<Feld, FeldMitGruppen> map;
	
	BasisLogik(FeldListe alleFelder) {
		felderMitGruppen = new FeldMitGruppen[alleFelder.size()];
		map = new HashMap<Feld, FeldMitGruppen>();
		for (int iFeld = 0; iFeld < alleFelder.size(); iFeld++) {
			Feld feld = alleFelder.get(iFeld);
			FeldMitGruppen feldMitGruppen = new FeldMitGruppen(feld, alleFelder);
			felderMitGruppen[iFeld] = feldMitGruppen;
			map.put(feld, feldMitGruppen);
		}
	}

	/**
	 * Wenn weder Vorgabe noch Eintrag existiert werden die Möglichen neu ermittelt:
 	 * Alle Zahlen, die in den Feldern der Gruppen, in denen ich stecke (Zeile + Spalte + Kasten), 
 	 * noch nicht gesetzt sind, werden als Mögliche eingetragen.
 	 */
	void setzeMoegliche()
	{
		for (FeldMitGruppen feldMitGruppen: felderMitGruppen){
			Feld feld = feldMitGruppen.feld;
			if (feld.istFrei())
			{
				// moegliche auffrischen
				feld.loescheMoegliche();
				Gruppe gruppe = feldMitGruppen.gruppe;
				for(int i=1; i<10; i++){
					if (gruppe.istMoeglich(i)){
						feld.setzeMoeglicheUnbedingt(i);
					}
				}
			}
		}
	}

}
