package sudoku.schwer.daten;

import java.util.ArrayList;
import java.util.List;

import sudoku.knacker.bericht.KB_KlareSetzeMoegliche;
import sudoku.logik.Logik_ID;
import sudoku.logik.SudokuLogik;
import sudoku.logik.bericht.BE_Durchlauf;
import sudoku.logik.bericht.BE_Ende;
import sudoku.logik.bericht.BE_Logik;
import sudoku.logik.bericht.BE_Start;

/**
 * @author Hendrick
 * Analysiert den sudoku.knacker.bericht.BerichtKnacker:
 * dessen Einträge KB_KlareSetzeMoegliche, die die Logik-Berichte beinhalten.
 */
/**
 * @author heroe
 *
 */
public class AnalysatorKlare {
	static private boolean istSystemOut = false;
	static private boolean istSystemOutZeit = false; //true;

	public static ArrayList<InfoKlareDetail> wandelUm(sudoku.knacker.bericht.BerichtKnacker bericht){
		
		ArrayList<InfoKlareDetail> infos = wandelUmIntern(bericht);
		if (istSystemOut){
			systemOut("wandleUmIntern", infos);
		}

		ArrayList<InfoKlareDetail> laufListe = gibKomprimiert(infos); 
		if (istSystemOut){
			systemOut("gibKomprimiert", laufListe);
		}

		return laufListe;
	}

	private static void systemOut(String methode, ArrayList<InfoKlareDetail> infos){
		System.out.println(AnalysatorKlare.class.getName() + " " + methode); 
		for (int i = 0; i <infos.size(); i++){
			InfoKlareDetail info = infos.get(i);
			info.systemOut(i);
		}		
	}

	/**
	 * @param knackerBericht 	 
	 * @see SudokuLogik.setzeMoegliche()
	 * @see SudokuLogik.setzeEintrag()
	 * @return ArrayList<InfoKlareGruppen>: 
	 * 		Zu jedem Berichteintrag KB_KlareSetzeMoegliche ein Eintrag InfoKlareGruppen.
	 */
	private static ArrayList<InfoKlareDetail> wandelUmIntern(sudoku.knacker.bericht.BerichtKnacker knackerBericht) {
		ArrayList<InfoKlareDetail> laufListe = new ArrayList<>(); 
		
		for (int i = 0; i <knackerBericht.size(); i++){
			Object o = knackerBericht.get(i);
			if (o instanceof KB_KlareSetzeMoegliche){
				KB_KlareSetzeMoegliche infoMoegliche = (KB_KlareSetzeMoegliche) o;
//				if (istSystemOut){
//					infoMoegliche.systemOut();
//				}
				fuelleLaufListe(laufListe, infoMoegliche);
			}
		}
		return laufListe;
	}

	/**
	 * Füllt die laufListe weiter auf entsprechend des übergebenen Logik-Berichtes:
	 * Die Anzahl der Logik-Durchläufe bestimmt die Anzahl der neuen Einträge in laufListe:
	 * - Ein Durchlauf (BE_Durchlauf) mit Nummer>1 erzeugt einen neuen Eintrag in laufListe, 
	 * 		denn im vorigen Durchlauf konnte die Logik noch keinen Eintrag ermitteln.
	 * - Die Ende-Meldung (BE_Ende) erzeugt unbedingt einen Eintrag in laufListe.
	 * 
	 * @param laufListe Erhält neue Einträge InfoKlareGruppen 
	 * @param infoMoegliche Beinhaltet einen Logiklauf. Dieser kann mehrere Durchläfe beinhalten!
	 */
	private static void fuelleLaufListe(
			ArrayList<InfoKlareDetail> laufListe, 
			KB_KlareSetzeMoegliche infoMoegliche){
		sudoku.logik.bericht.BerichtLogik bericht = infoMoegliche.gibBericht();

		BE_Start berichtEintragStart = null;
		ArrayList<BE_Logik> logikEintraege = new ArrayList<>();
		int durchlauf = 0;
		
		for (int i = 0; i <bericht.size(); i++){
			Object o = bericht.get(i);
			if (o instanceof BE_Start){
				berichtEintragStart = (BE_Start) o;
			} // if (o instanceof BE_Start){
			else if (o instanceof BE_Logik){
				BE_Logik bericht1 = (BE_Logik) o;
				logikEintraege.add(bericht1);
			} // if (o instanceof Detail){
			else if (o instanceof BE_Durchlauf){
				// Durchlauf  > 1 triggert: In dem Logik-Lauf konnte noch kein Eintrag erkannt werden.
				BE_Durchlauf bericht1 = (BE_Durchlauf) o;
				durchlauf = bericht1.gibDurchLauf();
				if (durchlauf > 1){
					InfoKlareDetail lLKG = 
							new InfoKlareDetail(durchlauf-1, 	logikEintraege,
									berichtEintragStart.gibAnzahlFreieFelder(), 
									berichtEintragStart.gibAnzahlFreieZeilen(),
									berichtEintragStart.gibAnzahlFreieSpalten(),
									berichtEintragStart.gibAnzahlFreieKaesten());
					laufListe.add(lLKG);
					logikEintraege = new ArrayList<>();
				}
			} // if (o instanceof Durchlauf){
			else if (o instanceof BE_Ende){
//				BE_Ende bericht1 = (BE_Ende) o;
//				FeldNummerMitZahl eintrag = bericht1.gibEintrag();

				InfoKlareDetail lLKG = new InfoKlareDetail(
						durchlauf,  logikEintraege,
						berichtEintragStart.gibAnzahlFreieFelder(), 
						berichtEintragStart.gibAnzahlFreieZeilen(),
						berichtEintragStart.gibAnzahlFreieSpalten(),
						berichtEintragStart.gibAnzahlFreieKaesten());
				laufListe.add(lLKG);
				break;
			} // if (o instanceof Ende){
		}  // for (int i = 0; i <bericht.size(); i++){
	}

//	/**
//	 * @param liste Soll komrimiert werden
//	 * @return liste so komprimiert, dass alle aufeinanderfolgenden OrtFest1-Logiken (mit identischem GruppenTyp enfällt) 
//	 * zusammengefasst zu einem Eintrag sind.
//	 */
//	private static ArrayList<InfoKlareDetail> gibKomprimiert(ArrayList<InfoKlareDetail> liste){
//		ArrayList<InfoKlareDetail> listeKomprimiert = new ArrayList<>();
//
//		if (liste.isEmpty()){
//			return liste;
//		}
//		// Der erste Eintrag wird nie komprimiert:
//		listeKomprimiert.add(liste.get(0));
//		
//		for (int iInfo = 1; iInfo < liste.size(); iInfo++){
//			InfoKlareDetail info = liste.get(iInfo);
//			
//			boolean eintragen = true;
//			if (info.istNurLogikOrtFest1()){
//				InfoKlareDetail letzteInfo = listeKomprimiert.get(listeKomprimiert.size()-1);
//				if (letzteInfo.istNurLogikOrtFest1() & (letzteInfo.gibDurchLauf() == 1) ){
//						eintragen = false;
//						letzteInfo.addLogiken(info);
//				}
//			}
//			if (eintragen){
//				listeKomprimiert.add(info);
//			}
//		}	
//		return listeKomprimiert;
//	}
	
	/**
	 * @param liste Soll komrimiert werden
	 * @return liste so komprimiert, dass alle aufeinanderfolgenden OrtFest1-Logiken (mit identischem GruppenTyp enfällt) 
	 * zusammengefasst zu einem Eintrag sind.
	 */
	private static ArrayList<InfoKlareDetail> gibKomprimiert(ArrayList<InfoKlareDetail> liste){
		ArrayList<InfoKlareDetail> listeKomprimiert = new ArrayList<>();

		if (liste.isEmpty()){
			return liste;
		}
		// Der erste Eintrag wird nie komprimiert:
		listeKomprimiert.add(liste.get(0));
		
		for (int iInfo = 1; iInfo < liste.size(); iInfo++){
			InfoKlareDetail info = liste.get(iInfo);
			boolean eintragen = true;

			LogikAnzahlen logikAnzahlen = info.gibErfolgreicheLogiken();
			Logik_ID[] logikIDArray = logikAnzahlen.gibLogiken();
			if (logikIDArray.length == 1){
				Logik_ID logikID = logikIDArray[0];
				
				InfoKlareDetail letzteInfo = listeKomprimiert.get(listeKomprimiert.size()-1);
				LogikAnzahlen logikAnzahlenLetzte = letzteInfo.gibErfolgreicheLogiken();
				Logik_ID[] logikIDArrayLetzte = logikAnzahlenLetzte.gibLogiken();
				if (logikIDArrayLetzte.length == 1){
					Logik_ID logikIDLetze = logikIDArrayLetzte[0];

					if (logikID == logikIDLetze){
						eintragen = false;
						letzteInfo.addLogiken(info);
					}
				}
			}
			if (eintragen){
				listeKomprimiert.add(info);
			}
		}	
		return listeKomprimiert;
	}
	
	/**
	 * @return Die Anzahl gelaufener Logiken (je Logik) 
	 */
	private static LogikAnzahlen gibGelaufeneLogiken(List<InfoKlareDetail> liste){
		LogikAnzahlen logikAnzahlen = new LogikAnzahlen();
		for(InfoKlareDetail detail: liste){
			LogikAnzahlen detailLogikAnzahlen = detail.gibGelaufeneLogiken();
			logikAnzahlen.add(detailLogikAnzahlen);
		}
		return logikAnzahlen;
	}

	// Zeit
	// =================================================================
	// Minimale Anzeigezeit in Minuten
	private static int minimum = 10; 
	// Anzeigezeit-Raster in Minuten
	private static int raster = 5; 
	// Halbe Anzeigezeit-Raster in Minuten
	private static double rasterDiv2 = ( (double) raster ) / 2.5 ; 

	/**
	 * @param zeit in Sekunden
	 * @param gerastert Bei true wird die Zeit auf 5 Minuten gerastert
	 * @return Anzeigezeit in Minuten
	 */
	public static int gibAnzeigeZeit(double zeit, boolean gerastert){
		zeit /= 60;
		if ( ! gerastert){
			int i = (int) (zeit);
			return i;
		}
		
		zeit += rasterDiv2;
		int i = (int) (zeit / raster) * raster;
		if (i < minimum){
			i = minimum;
		}
		return i;
	}

	/**
	 * @return Die Zeit eines Menschen für die Realisierung des Ablaufes der Logiken in Sekunden
	 */
	static double gibZeit(List<InfoKlareDetail> liste){
		LogikAnzahlen logikAnzahlen = gibGelaufeneLogiken(liste);
		Logik_ID[] gelaufeneLogiken = logikAnzahlen.gibLogiken();
		double zeit = 0;
		
		if (istSystemOutZeit){
			System.out.println(AnalysatorKlare.class.getName() + ".gibZeit(): " + logikAnzahlen); 
		}
		
		for (int iLogik = 0; iLogik < gelaufeneLogiken.length; iLogik++) {
			Logik_ID logik = gelaufeneLogiken[iLogik];
			int n = logikAnzahlen.gibAnzahl(logik);
			double logikZeit1 = SudokuLogik.gibKontrollZeit1(logik);
			double logikZeit = n * logikZeit1;

			if (istSystemOutZeit){
				String s = String.format("%s=%02dmin (%.0fs)", 
						SudokuLogik.gibNameKurz(logik), Math.round(logikZeit / 60), logikZeit);
				System.out.println(s); 
			}
			
			zeit += logikZeit;
		}
		
		if (logikAnzahlen.istNurLogikOrtFest1()){
			// Ist nicht ganz korrekt: Gemeint ist hier die Schwierigkeit "Leicht":
			// Die Löser von Leicht-Sudokus benötigen typisch eine andere Zeitachse.
			zeit *= 2;
		}
		
		if (istSystemOutZeit){
			String s = String.format("Summe=%dmin", Math.round(zeit / 60));
			System.out.println(s); 
		}

		return zeit;
	}


}
