package sudoku.bild;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sudoku.Paar;
import sudoku.kern.exception.UnerwarteterInhalt;
import sudoku.kern.info.InfoSudoku;
//import sudoku.langerprozess.FortschrittZeiger;
//import sudoku.langerprozess.LangerProzess;

/**
 * @author heroe
 * 
 */
public class SudokuSucher{ //  implements LangerProzess{
	private static boolean istSystemOut =false;

//	static boolean istSystemOut() {
//		return istSystemOut;
//	}
	
	private static void systemOut(String text) {
		if (istSystemOut) {
			System.out.println(text);
		}
	}

//	/**
//	 * Gibt das Array als Balkendiagramm aus
//	 * @param intWeiss
//	 * @param titel
//	 */
//	static protected void systemOut(int[] intWeiss, String titel){
//		int iMax = intWeiss.length / 3;
//		System.out.println(String.format("%s-Weiss (/10) bis Index %d (maxIndex=%d)", titel, iMax, intWeiss.length));
//		if (istSystemOut) {
//			char zeichen = '#';
//			for (int i = 0; i < iMax; i++) {
//				int wert = intWeiss[i] / 10;
//				String sWert = new String();
//				
//				for (int j = 0; j < wert; j++) {
//					sWert += zeichen;
//				}
//				System.out.println(String.format("%d %s", i, sWert));
//			}
//		}
//	}
	
//	/**
//	 * Es wird vorausgesetzt, dass nur eines von beiden OK ist: Spalten oder Zeilen
//	 * 
//	 * @param image
//	 * @param spaltenStriche
//	 * @param zeilenStriche
//	 * @param okSpalten
//	 *            Bei true wird das Bild auf den durch spaltenStriche
//	 *            bezeichneten Bereich verkleinert
//	 * @param okZeilen
//	 *            Bei true wird das Bild auf den durch zeilenStriche
//	 *            bezeichneten Bereich verkleinert
//	 * @return Bild mit in einer Richtung bereits gefangenem Sudokus
//	 */
//	static private Rectangle gibBesserenBildAusschnitt(BufferedImage image, StricheKreuz stricheKreuz){
//		Rectangle neuesRechteck = null;
//		if (stricheKreuz.istSpaltenOk()) {
//			// die Spaltenstriche sind nutzbar:
//			// In der Horizontalen nur der Ausschnitt
//			Pair<Integer, Integer> ausschnitt = stricheKreuz.gibBereichSpalten();
//			int x = ausschnitt.getKey();
//			int letzterIndex = ausschnitt.getValue();
//			int breite = letzterIndex - x + 1;
//
//			// Vertikaler Bereich bleibt
//			int y = 0;
//			int hoehe = image.getHeight();
//			neuesRechteck = new Rectangle(x, y, breite, hoehe);
//		}
//		if (stricheKreuz.istZeilenOk()) {
//			// die Zeilenstriche sind nutzbar:
//			// In der Vertikalen nur der Ausschnitt
//			Pair<Integer, Integer> ausschnitt = stricheKreuz.gibBereichZeilen();
//			int y = ausschnitt.getKey();
//			int letzterIndex = ausschnitt.getValue();
//			int hoehe = letzterIndex - y + 1;
//
//			// Horizontaler Bereich bleibt
//			int x = 0;
//			int breite = image.getWidth();
//			neuesRechteck = new Rectangle(x, y, breite, hoehe);
//		}
//		return neuesRechteck;
//	}

	/**
	 * @param image
	 * @param spaltenStriche
	 * @param zeilenStriche
	 * @return Bildausschnitt aus image, den das Kreuz der Striche bezeichnet.
	 */
	static private Rectangle gibBildAusschnitt(BufferedImage image,
			StricheKreuz stricheKreuz) {
		Rectangle neuesRechteck = null;

		// Horizontaler Bereich
		Paar<Integer, Integer> ausschnittX = stricheKreuz.gibBereichSpalten();
		int x = ausschnittX.getKey();
		int letzterIndexX = ausschnittX.getValue();
		int breite = letzterIndexX - x + 1;

		// Vertikaler Bereich bleibt
		Paar<Integer, Integer> ausschnittY = stricheKreuz.gibBereichZeilen();
		int y = ausschnittY.getKey();
		int letzterIndexY = ausschnittY.getValue();
		int hoehe = letzterIndexY - y + 1;
		neuesRechteck = new Rectangle(x, y, breite, hoehe);

		return neuesRechteck;
//		BufferedImage returnImage = Bild.gibBildAusschnitt(image, neuesRechteck);
//		return returnImage;
	}

	/**
	 * Ermittelt die Veränderung der weissAnzahlen von Index zu Index+1.
	 * @param weissAnzahlen
	 * @param minStrichLaenge 
	 * @param linienName
	 * @return Alle Sprünge, deren abs(Höhe) größer/gleich minStrichLaenge sind.
	 */
	private static List<Sprung> gibSprunghoehen(int[] weissAnzahlen,
			int minStrichLaenge, String linienName) {
		ArrayList<Sprung> spruenge = new ArrayList<Sprung>();

		for (int i = 0; i < weissAnzahlen.length - 1; i++) {
			int sprung = weissAnzahlen[i + 1] - weissAnzahlen[i];

			if (Math.abs(sprung) >= minStrichLaenge) {
				Sprung sprungSpeicher = new Sprung(i, sprung);
				spruenge.add(sprungSpeicher);
			}
		}

		// systemOut(String.format("%s: %s Länge=%d",
		// SudokuSucher.class.getName(), linienName, linienLaenge));
		// Iterator<Sprung> iterator = spruenge.iterator();
		// while(iterator.hasNext()){
		// Sprung sprung = iterator.next();
		// int weiss = weissAnzahlen[sprung.gibVonIndex()];
		// systemOut(String.format("i%s=%4d Sprung=%3d von weiss=%4d",
		// linienName, sprung.gibVonIndex(), sprung.gibSprungHoehe(), weiss));
		// }

		return spruenge;
	}

	/**
	 * @param spruenge
	 *            Nur Sprünge, deren Absolutwert der Sprunghöhe > 0 ist.
	 * @return Striche, die aus den Sprüngen erkennbar sind. Also ev. auch eine
	 *         leere Liste.
	 */
	private static StrichListe gibLinienStriche(List<Sprung> spruenge,
			String titel,
			String linienName) {
		Collections.sort(spruenge, new SprungComparator(
				SprungComparator.Art.INDEX));

		StrichListe striche = new StrichListe();

		// Die beiden letzten als relevant erkannten Sprünge
		Sprung weissSchwarzSprung = null; // Sprunghöhe < 0
		Sprung schwarzWeissSprung = null; // Sprunghöhe > 0
		for (Sprung sprung : spruenge) {
			int sprungHoehe = sprung.gibSprungHoehe();
			if (sprungHoehe < 0) {
				// Es handelt sich um einen Sprung von weiss nach schwarz.
				// Eventuell ist es ein Strichbeginn.
				weissSchwarzSprung = sprung;
				schwarzWeissSprung = null;
			} else {
				// Es handelt sich um einen Sprung von schwarz nach weiss.
				// Eventuell ist Strich-Ende.
				schwarzWeissSprung = sprung;
			} // if (sprungHoehe

			// Ist ein vermerkter Strich zu speichern?
			boolean istStrichZuSpeichern = ((weissSchwarzSprung != null) & (schwarzWeissSprung != null));
			if (istStrichZuSpeichern) {
				Strich strich = new Strich(weissSchwarzSprung,
						schwarzWeissSprung);
				striche.add(strich);
				weissSchwarzSprung = null;
				schwarzWeissSprung = null;
			}
		} // for(Sprung sprung: spruenge){

		striche.systemOut(istSystemOut, titel, linienName);
		return striche;
	}

//	private static StricheKreuz gibStriche(BufferedImage image, StricheKreuz stricheKreuz,
//			int minStrichLaengeSenkrechtProzent,
//			int minStrichLaengeWaagerechtProzent,
//			String titel) 
//	{
//		LinienWeiss linienWeiss = new LinienWeiss(image, null);
//		int minStrichLaengeSenkrecht = (minStrichLaengeSenkrechtProzent * image.getHeight()) / 100;
//		int minStrichLaengeWaagerecht = (minStrichLaengeWaagerechtProzent * image.getWidth()) / 100;
//		
//		StricheKreuz returnStricheKreuz = gibStriche(linienWeiss, stricheKreuz,
//				minStrichLaengeSenkrecht,	minStrichLaengeWaagerecht, titel); 
//		return returnStricheKreuz;
//	}
	
	/**
	 * @param linienWeiss
	 *            Das Array mit der Weiss-Pixel-Anzahl: Wie oft tritt ein
	 *            Weiss-Pixel auf Spalte[i] bzw. Zeile[i] auf?
	 * @param linienLaenge
	 *            Länge von Spalte bzw. Zeile
	 * @param linienName
	 *            "Spalte" bzw. "Zeile"
	 * @return Die Striche, die als Sudoku-Striche erkannt wurden. 
	 */
	static private StrichListe[] gibSudokuStriche(
			int[] linienWeiss,
			int minStrichLaenge, 
			String titel,
			String linienName) {
		List<Sprung> spruenge = gibSprunghoehen(linienWeiss, minStrichLaenge,
				linienName);
		StrichListe striche = gibLinienStriche(spruenge, titel, linienName);

		StrichListe sudokuStriche[] = SudokuFinder.gibSudokuStriche(striche, linienName);

		return sudokuStriche;
	}

	/**
	 * @param linienWeiss
	 * @param stricheKreuz
	 * 			null falls noch garkeine Striche erkannt wurden oder
	 * 			stricheKreuz mit nur senkrechten oder waagerechten Strichen (die wurden schon erfolgreich erkannt).
	 * 			Die bereits erkannten Striche der einen Richtung werden übernommen. 
	 * 			Sie werden nicht neu erstellt!  
	 * @param minStrichLaengeSenkrecht
	 * 			Die kleinste erlaubte Länge eines senkrechten Striches 
	 * @param minStrichLaengeWaagerecht
	 * 			Die kleinste erlaubte Länge eines waagerechten Striches 
	 * @param titel 
	 * @return Die Sudoku-Striche, die im Schwarz-Weiss-Bild erkannt wurden
	 */
	private static StricheKreuz gibStriche(LinienWeiss linienWeiss, StricheKreuz stricheKreuz,
			int minStrichLaengeSenkrecht,
			int minStrichLaengeWaagerecht,
			String titel) 
	{
		boolean spaltenErstellen = true;
		boolean zeilenErstellen = true;
		if (stricheKreuz != null){
			if (stricheKreuz.istSpaltenOk()){
				spaltenErstellen = false;
			}
			if (stricheKreuz.istZeilenOk()){
				zeilenErstellen = false;
			}
		}
		if ( ! spaltenErstellen & ! zeilenErstellen){
			throw new UnerwarteterInhalt("Im StrichKreuz sind beide Richtungen bereits erstellt: Es darf nur eine sein!");
		}
		
		// Senkrechte Sudoku-Striche
		StrichListe[] spaltenSudokuStriche = null;
		if (spaltenErstellen){
			systemOut(String.format("%s Spalten-Strich-Erstellung mit Min-Strich-Länge=%d Pixel", 
					titel, minStrichLaengeSenkrecht));
			systemOut(" ------------------------------------------------------------------------------------------------------");
			spaltenSudokuStriche = gibSudokuStriche(linienWeiss.spaltenWeiss,
					minStrichLaengeSenkrecht, titel, "Spalte");
		}
		else
		{
			spaltenSudokuStriche = stricheKreuz.spaltenStriche;
		}
		
		// Waagerechte Sudoku-Striche
		StrichListe[] zeilenSudokuStriche = null;
		if (zeilenErstellen){
			systemOut(String.format("%s Zeilen-Strich-Erstellung mit Min-Strich-Länge=%d Pixel", 
					titel, minStrichLaengeWaagerecht));
			systemOut(" ------------------------------------------------------------------------------------------------------");
			zeilenSudokuStriche = gibSudokuStriche(linienWeiss.zeilenWeiss,
					minStrichLaengeWaagerecht, titel, "Zeile");
		}
		else{
			zeilenSudokuStriche = stricheKreuz.zeilenStriche;
		}
		StricheKreuz sudokuStriche = new StricheKreuz(
				spaltenSudokuStriche, zeilenSudokuStriche);
		return sudokuStriche;
	}

	
	// ===============================================================
	BufferedImage image; 
	BufferedImage returnImage; 
	InfoSudoku[] sudokus;
	
	public SudokuSucher (BufferedImage image){
		this.image = image;
	}

	public BufferedImage gibImage(){
		return returnImage;
	}

	public InfoSudoku[] gibSudokus(){
		return sudokus;
	}
	
	public void prozess(){ 
		image = Bild.schwarzWeiss(image); 

		LinienWeiss linienWeiss = new LinienWeiss(image, null);
		
		// Mehrere Suchläufe mit immer kürzeren erlaubten Strichen
		// Das hilft bei Bildern mit schiefen Sudokus oder auch bei miesen Bildern  
		for (int nSuchLauf = 1; nSuchLauf < 3; nSuchLauf++) {
			boolean b = suchen(linienWeiss, nSuchLauf); //, fortschrittZeiger);
			if (b){
				break;
			}
		}	
	}

	/**
	 * Sucht im Bild nach Sudokus
	 * @param linienWeiss Info zu den Weiss-Anteilen der Spalten bzw. Zeilen
	 * @param nSuchLauf Eine größere Zahl verkleinert die geforderte Strichlänge 
	 * @param fortschrittZeiger
	 * @return true wenn Sudokus gefunden wurden
	 */
	private boolean suchen(LinienWeiss linienWeiss, int nSuchLauf){ 
		systemOut(String.format(
				"%s.suchen(): Untersuchung Gesamtbild mit Breite=%d und Höhe=%d, Lauf %d",
				SudokuSucher.class.getName(), image.getWidth(),
				image.getHeight(), nSuchLauf));
		systemOut(" ==========================================================================");
		returnImage = image;

		// Erstellung der minimal geforderten Strichlängen:
		// Es wird davon ausgegangen, dass auf der schmalen Bildseite max 3 Sudokus liegen,
		// auf der langen Seite max 6 Sudokus.
		// Die Zwischenräume zwischen den Sudokus berücksichtigen heisst: jeweils + 1.
		int minStrichLaengeProzentKurz = 25 / nSuchLauf;
		int minStrichLaengeProzentLang = 13 / nSuchLauf;
		
		// Bild ist breiter als hoch (Querformat)
		int minStrichLaengeSenkrechtProzent = minStrichLaengeProzentKurz;
		int minStrichLaengeWaagerechtProzent = minStrichLaengeProzentLang;
		if (image.getHeight() > image.getWidth()){
			// Bild im Hochformat
			minStrichLaengeSenkrechtProzent = minStrichLaengeProzentLang;
			minStrichLaengeWaagerechtProzent = minStrichLaengeProzentKurz;
		}
		int minStrichLaengeSenkrecht = (minStrichLaengeSenkrechtProzent * image.getHeight()) / 100;
		int minStrichLaengeWaagerecht = (minStrichLaengeWaagerechtProzent * image.getWidth()) / 100;
	
		StricheKreuz stricheKreuz = gibStriche(linienWeiss, null,
				minStrichLaengeSenkrecht,
				minStrichLaengeWaagerecht,
				"Gesamtbild");

		if (stricheKreuz.istOK()) {
			// schon so gut wie fertsch
			
			// Bild auf StricheKreuz verkleinern
			Rectangle stricheKreuzRechteck = gibBildAusschnitt(image, stricheKreuz);
			image = Bild.gibBildAusschnitt(image, stricheKreuzRechteck); 
			stricheKreuz.transformiereIndizees(stricheKreuzRechteck.getLocation());
					
			// Es wird vorausgesetzt, dass sämtliche Striche richtige Sudoku-Striche
			// BildFeldGruppen (je 1 für 1 Sudoku) erstellen
			ArrayList<BildFeldGruppe> bildFeldGruppeListe = new ArrayList<>();
			
			for (int iZeilenStrich = 0; iZeilenStrich < stricheKreuz.zeilenStriche.length; iZeilenStrich++) {
				StrichListe  zeilenStriche = stricheKreuz.zeilenStriche[iZeilenStrich];
				for (int iSpaltenStrich = 0; iSpaltenStrich < stricheKreuz.spaltenStriche.length; iSpaltenStrich++) {
					StrichListe spaltenStriche = stricheKreuz.spaltenStriche[iSpaltenStrich];
					BildFeldGruppe bildFeldGruppe = new BildFeldGruppe(spaltenStriche, zeilenStriche);
					bildFeldGruppeListe.add(bildFeldGruppe);
				}
			}
			
			if ( ! bildFeldGruppeListe.isEmpty()){
				BildFeldGruppe bildFeldGruppe = bildFeldGruppeListe.get(0);
				InfoSudoku sudoku = bildFeldGruppe.gibSudoku(image);
				if (sudoku != null){
					sudokus = new InfoSudoku[1];
					sudokus[0] = sudoku;
					returnImage = bildFeldGruppe.gibImage();
					systemOut("Erfolgreich ferttschsch");
				}
			}
		} else {
//			Das Sudoku muss schon ordentlich groß auf dem Monitor sein, damit die Zahlen sicher erkannt werden.
//			Deswegen ist der Versuch mit dem Bildausschnitt nicht nötig.
			
//			if (stricheKreuz.istEineRichtungBekannt()) {
//				// kleineren Bildteil nehmen und nochmal...
//				Rectangle bessererBildAusschnitt = gibBesserenBildAusschnitt(
//						image, stricheKreuz);
//				systemOut(String.format(
//						"%s.suchen(): Untersuchung des Bildausschnitts %s",
//						SudokuSucher.class.getName(), bessererBildAusschnitt));
//				systemOut(" ====================================================");
//
//				BufferedImage besseresBild = Bild.gibBildAusschnitt(image,
//						bessererBildAusschnitt);
//				returnImage = besseresBild;
//
//				// Im Bildausschnitt ist der Kick, dass die zu erkennenden Striche alle sehr lang sind:
//				int minStrichLaenge = 50;
//				//stricheKreuz: die bekannten Striche müssen jetzt auf den Ausschnitt transformiert werden!
//				stricheKreuz.transformiereIndizees(bessererBildAusschnitt.getLocation());
//
//				
//				StricheKreuz stricheKreuz2 = gibStriche(besseresBild, stricheKreuz, minStrichLaenge, minStrichLaenge, "Bildausschnitt");
//
//				if (stricheKreuz2.istOK()) {
//					sudokus = gibSudokus(besseresBild, stricheKreuz2);
//					returnImage = gibBildAusschnitt(besseresBild, stricheKreuz2);
//					systemOut("Ferttschsch per Ausschnitt");
//				} else {
//					systemOut("Kein Glück gehabt mit Ausschnitt !?");
//				}
//			} else {
				// da läßt sich nichts machen
				systemOut("Kein Glück gehabt !?");
//			}
		} // if (stricheKreuz.istOK()) {
		return sudokus != null;
	}

}
