package sudoku.schwer.daten;

import java.util.ArrayList;

import sudoku.kern.EintragsEbenen;
import sudoku.knacker.bericht.BerichtKnacker;
import sudoku.knacker.bericht.KB_LoeseInternEnde;
import sudoku.knacker.bericht.KB_VersuchePaare;
import sudoku.knacker.bericht.KB_VersuchStart;

/**
 * @author Hendrick
 * Analysiert den sudoku.knacker.bericht.BerichtKnacker auf vorhandene Versuche
 */
public class AnalysatorVersuch {


	/**
	 * @param bericht
	 * @return Nummer der maximalen Ebene () 
	 * 	oder 0 wenn kein gesuchter Eintrag existiert im Bericht
	 */
	private static int gibMaxEbene(BerichtKnacker bericht){
		for (int i = bericht.size()-1; i >= 0; i--){
			Object o = bericht.get(i);
			if (o instanceof KB_LoeseInternEnde){
				KB_LoeseInternEnde bericht1 = (KB_LoeseInternEnde) o;
				return bericht1.gibEbeneNummer();
			}
		}
		return 0;
	}
	
	private static int gibAnzahlVersuche(int maxEbene){
		return maxEbene - EintragsEbenen.gibStandardEbene1();
	}

	/**
	 * @param bericht
	 * @return null wenn keine Versuche stattgefunden haben
	 */
	public static InfoVersucheOK gibVersucheOK(sudoku.knacker.bericht.BerichtKnacker bericht) {
		int maxEbene = gibMaxEbene(bericht);
		int nVersucheOK = gibAnzahlVersuche(maxEbene);

		if (nVersucheOK <= 0){
			return null;
		}
		else{
			InfoVersucheOK info = new InfoVersucheOK(nVersucheOK);
			return info;
		}
	}

	/**
	 * @param bericht
	 * @return null wenn keine Versuche stattgefunden haben
	 */
	public static ArrayList<InfoVersuche> gibVersuchsStarts(sudoku.knacker.bericht.BerichtKnacker bericht) {
		int maxEbene = gibMaxEbene(bericht);
		int nVersucheOK = gibAnzahlVersuche(maxEbene);

		if (nVersucheOK == 0){
			return null;
		}
		else{
			ArrayList<InfoVersuche> infos = new ArrayList<>();
			int nStartsFeld1 = 0;
			int nStartsFelderPaare = 0;
			boolean istStartFeldPaar = false;
				
			if (maxEbene > EintragsEbenen.gibStandardEbene1()){
				for (int i = 0; i <bericht.size(); i++){
					Object o = bericht.get(i);
					if (o instanceof KB_VersuchePaare){
						KB_VersuchePaare bericht1 = (KB_VersuchePaare) o;
						 istStartFeldPaar = bericht1.benutzteFeldPaare();
					}
					if (o instanceof KB_VersuchStart){
						if (istStartFeldPaar){
							nStartsFelderPaare++;
						}
						else {
							nStartsFeld1++;
						}
					}
				}
			}

			if (nStartsFeld1 > 0){
				InfoVersuche info = new InfoVersuche(nStartsFeld1, true);
				infos.add(info);
			}
			if (nStartsFelderPaare > 0){
				InfoVersuche info = new InfoVersuche(nStartsFelderPaare, false);
				infos.add(info);
			}
			return infos;
		} // if (nVersucheOK == 0){
	}

}
