package sudoku.bild;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sudoku.bild.leser.ZahlLeser;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.info.FeldInfo;
import sudoku.kern.info.FeldInfoListe;
import sudoku.kern.info.InfoSudoku;
import sudoku.logik.SudokuLogik;

/**
 * @author heroe Die BildFelder f�r ein Sudoku
 */
@SuppressWarnings("serial")
class BildFeldGruppe extends ArrayList<BildFeld> {
	private BufferedImage image;

	/**
	 * Erstellt die BildFelder mit ihren Rechtecken
	 * 
	 * @param spaltenStriche
	 * @param zeilenStriche
	 */
	BildFeldGruppe(StrichListe spaltenStriche, StrichListe zeilenStriche) {
		image = null;
		for (int iSpaltenStrich = 1; iSpaltenStrich < spaltenStriche.size(); iSpaltenStrich++) {
			for (int iZeilenStrich = 1; iZeilenStrich < zeilenStriche.size(); iZeilenStrich++) {
				FeldNummer feldNummer = new FeldNummer(iSpaltenStrich, iZeilenStrich);
				BildFeld bildFeld = new BildFeld(feldNummer, spaltenStriche.get(iSpaltenStrich - 1),
						spaltenStriche.get(iSpaltenStrich), zeilenStriche.get(iZeilenStrich - 1),
						zeilenStriche.get(iZeilenStrich));
				this.add(bildFeld);
			}
		}
	}

	BufferedImage gibImage() {
		return image;
	}

	/**
	 * @param image
	 *            Diese Methode entfernt alle leeren Felder aus dieser Liste der
	 *            BildFelder
	 */
	private void entferneLeereFelder(BufferedImage image) {
		Map<BildFeld, Integer> felderWeiss = new HashMap<BildFeld, Integer>();

		// Array der Weiss-Anteile erstellen
		int[] weissAnteile = new int[this.size()];
		int index = 0;

		// Weiss-Anteile erstellen in jedem BildFeld
		// if (BildFeld.istTestAnzeige()){
		// System.out.println(String.format("%s.gibWeissAnteilLeeresFeld",
		// getClass().getName()));
		// }
		for (BildFeld bildFeld : this) {
			int weissAnteilProzent = bildFeld.gibZentrumWeissAnteil(image);
			felderWeiss.put(bildFeld, weissAnteilProzent);
			weissAnteile[index] = weissAnteilProzent;
			index++;
			// if (BildFeld.istTestAnzeige()){
			// System.out.println(String.format("%s Weiss-Anteil=%d",
			// feldNummer, weissAnteilProzent));
			// }
		}

		// Gro�en Weiss-Anteil (der leeren Felder) erkennen
		int toleranz = 4;
		WerteGruppe weissAnteilLeer = WerteGruppe.gibHaeufigstenWert(weissAnteile, toleranz);
		int kleinsterWeissAnteilLeer = weissAnteilLeer.gibMinimum();

		// Die leeren Felder anhand des gro�en Weiss-Anteils erkennen und
		// entfernen
		for (Map.Entry<BildFeld, Integer> entry : felderWeiss.entrySet()) {
			BildFeld bildFeld = entry.getKey();
			if (entry.getValue() >= kleinsterWeissAnteilLeer) {
				this.remove(bildFeld);
			}
		}

		// if (BildFeld.istTestAnzeige()){
		// System.out.println(String.format("%s.gibWeissAnteilLeeresFeld
		// Weiss-Anteil leerer (?) Felder=%s", getClass().getName(),
		// weissAnteilLeer));
		// System.out.println(String.format("%d Felder eventuell mit einer
		// Zahl", this.size()));
		// for(Entry<FeldNummer, BildFeld> entry: this.entrySet()){
		// System.out.println(String.format("%s %s", entry.getKey(),
		// entry.getValue().gibFeldRechteck()));
		// }
		// }
	}

	/**
	 * Erstellt in den BildFeldern die Zahlen-Rechtecke, die innerhalb der
	 * Feld-Rechtecke liegen
	 * 
	 * @param image
	 *            Auf das Bild bezieht sich das BildFeld.feldRechteck
	 */
	private void setzeZahlenRechtecke(BufferedImage image) {
		if (BildFeld.istTestAnzeige()) {
			System.out.println(String.format("BildFelder.setzeZahlenRechtecke() in %d Feldern", this.size()));
		}

		// Jedes Feld sein Zahlenrechteck erstellen lassen
		ArrayList<BildFeld> leereFelder = new ArrayList<BildFeld>();
		for (int i = 0; i < this.size(); i++) {
			BildFeld bildFeld = this.get(i);
			Rectangle zahlRechteck = bildFeld.setzeZahlenRechteck(image);
			if (zahlRechteck == null) {
				leereFelder.add(bildFeld);
			}
		}

		// Leere Felder von der weiteren Bearbeitung ausschliessen
		for (BildFeld bildFeld : leereFelder) {
			this.remove(bildFeld);
		}
	}

	/**
	 * Falls die Zahlenrechtecke im Querformat vorliegen, werden die
	 * Bildfelder-Feldnummern gedreht um 90� im Uhrzeigersinn. Dabei werden auch
	 * die ZahlBildInfos transformiert. Bildfelder mit einem besonderen
	 * Zahlenrechteck, z.B. die Gewinnzahl-Kennzeichnungen von Prisma-Sudokus,
	 * werden von der weiteren Bearbeitung ausgeschlossen
	 * 
	 * @return true wenn in das Hochformat gesetzt werden musste
	 */
	private boolean setzeZahlenRechteckeInHochformat(Dimension bildDimension) {

		// Ist Hochformat?
		int[] breiten = new int[this.size()];
		int[] hoehen = new int[this.size()];
		int zahlHoch = 0;
		int zahlQuer = 0;

		for (int iFeld = 0; iFeld < hoehen.length; iFeld++) {
			BildFeld bildFeld = this.get(iFeld);
			Rectangle r = bildFeld.gibZahlRechteck();
			breiten[iFeld] = r.width;
			hoehen[iFeld] = r.height;
			if (r.height > r.width) {
				zahlHoch++;
			} else {
				zahlQuer++;
			}
		}

		boolean istHochFormat = zahlHoch > zahlQuer;

		// 90� Rechtsdrehen
		if (!istHochFormat) {
			hoehen = breiten;

			for (BildFeld bildFeld : this) {
				bildFeld.drehe90GradRechts(bildDimension);
			}
		}

		// Nicht-Zahlen-Bildfelder entfernen
		int toleranzProzent = 10;
		WerteGruppe haeufigstehoehe = WerteGruppe.gibHaeufigstenWert(hoehen, toleranzProzent);
		int hoeheDurchschnitt = haeufigstehoehe.gibDurchschnitt();
		int hoeheMin = hoeheDurchschnitt - Math.round((hoeheDurchschnitt * toleranzProzent) / 100.0f);
		ArrayList<BildFeld> leereFelder = new ArrayList<BildFeld>();

		for (int iFeld = 0; iFeld < hoehen.length; iFeld++) {
			BildFeld bildFeld = this.get(iFeld);
			int feldHoehe = hoehen[iFeld];
			if (feldHoehe < hoeheMin) {
				leereFelder.add(bildFeld);
			}
		}

		// Leere Felder von der weiteren Bearbeitung ausschliessen
		for (BildFeld bildFeld : leereFelder) {
			this.remove(bildFeld);
		}

		return !istHochFormat;
	}

	/**
	 * Dreht die Bildfelder um 180 Grad
	 * 
	 * @param bildDimension
	 */
	private void dreheUm180Grad(Dimension bildDimension) {
		for (int i = 0; i < this.size(); i++) {
			BildFeld bildFeld = this.get(i);
			bildFeld.drehe90GradRechts(bildDimension);
		}

		Dimension bildDimension2 = new Dimension(bildDimension.height, bildDimension.width);
		for (int i = 0; i < this.size(); i++) {
			BildFeld bildFeld = this.get(i);
			bildFeld.drehe90GradRechts(bildDimension2);
		}
	}

	/**
	 * Setzt die Zahlen anhand der BildInfo
	 * 
	 * @return Anzahl der gesetzten Zahlen
	 */
	private int setzeZahlen() {
		int anzahl = 0;
		// Jedes Feld seine Zahl erstellen lassen
		for (int i = 0; i < this.size(); i++) {
			BildFeld bildFeld = this.get(i);
			Integer zahl = bildFeld.setzeZahl();
			if (zahl != null) {
				anzahl++;
			}
		}

		if (ZahlLeser.istSystemOut()) {
			System.out.println(
					String.format("%s.setzeZahlen() in: ----------------------------------", getClass().getName()));
			for (int i = 0; i < this.size(); i++) {
				BildFeld bildFeld = this.get(i);
				Integer zahl = bildFeld.gibZahl();
				if (zahl != null) {
					System.out.println(String.format("%d in %s", zahl, bildFeld.gibFeldNummer()));
				}
			}
		}
		return anzahl;
	}

	/**
	 * Setzt die Zahl anhand der Ausg�nge in den Zahlenbildern
	 * 
	 * @param image
	 */
	private void setzeZahlen(BufferedImage image) {
		// Jedes Feld seine Zahl erstellen lassen
		for (int i = 0; i < this.size(); i++) {
			BildFeld bildFeld = this.get(i);
			bildFeld.setzeZahl(image);
		}

		if (ZahlLeser.istSystemOut()) {
			System.out.println(
					String.format("%s.setzeZahl() in: ----------------------------------", getClass().getName()));
			for (int i = 0; i < this.size(); i++) {
				BildFeld bildFeld = this.get(i);
				System.out.println(String.format("%s in %s", bildFeld.gibZahl(), bildFeld.gibFeldNummer()));
			}
		}
	}

	private void systemOutErgebnis(BufferedImage image) {
		if (ZahlLeser.istSystemOut()) {
			System.out.println(String.format("%s Ergebnisse: ---------------------------------", getClass().getName()));
			for (int i = 0; i < this.size(); i++) {
				BildFeld bildFeld = this.get(i);
				bildFeld.systemOutErgebnis(image);
			}
		}
	}

	/**
	 * @return null falls nicht ausreichend Vorgaben gelesen wurden oder das
	 *         Sudoku
	 */
	private InfoSudoku gibSudoku() {
		FeldInfoListe feldInfoListe = new FeldInfoListe();
		for (int i = 0; i < this.size(); i++) {
			BildFeld bildFeld = this.get(i);
			Integer zahl = bildFeld.gibZahl();
			if (zahl != null) {
				FeldInfo feldInfo = FeldInfo.gibVorgabeInstanz(bildFeld.gibFeldNummer(), zahl);
				feldInfoListe.add(feldInfo);
			}
		}

		InfoSudoku sudoku = null;
		if (feldInfoListe.size() >= SudokuLogik.gibAnzahlVorgabenMin()) {
			sudoku = new InfoSudoku(feldInfoListe);
		}
		return sudoku;
	}

	private InfoSudoku gibSudokuIntern() {
		// Alle leeren Felder von der weiteren Betrachtung ausschlie�en
		entferneLeereFelder(image);

		// Die Zahlen-Rechtecke (und ZahlBild-Info) je BildFeld erstellen.
		// Das ist separat n�tig f�r die Ermittlung des Seitenformates der
		// Zahlen-Rechtecke
		// Hier als leer erkannte Felder werden von der weiteren Betrachtung
		// ausgeschlossen
		setzeZahlenRechtecke(image);

		// Nach dem Drehen des Bildes sitzen die Zahlenrechtecke nicht mehr
		// 100%ig.
		boolean istBildGedreht = false;

		// Hochformat erzwingen
		Dimension bildDimension = new Dimension(image.getWidth(), image.getHeight());
		boolean warQuerFormat = setzeZahlenRechteckeInHochformat(bildDimension);
		if (warQuerFormat) {
			istBildGedreht = true;
			image = Bild.rotiere90Grad(image);
		}

		// Zahlen setzen anhand der BildInfo
		final int anzahlZahlenMin = 3;
		int anzahlZahlen = setzeZahlen();
		if (anzahlZahlen < anzahlZahlenMin) {
			// 180 Drehen
			istBildGedreht = true;
			Dimension bildDimension2 = new Dimension(image.getWidth(), image.getHeight());
			dreheUm180Grad(bildDimension2);
			BufferedImage neuesBild90 = Bild.rotiere90Grad(image);
			image = Bild.rotiere90Grad(neuesBild90);
		}

		if (istBildGedreht) {
			// Zahlrechteck erneut erstellen
			setzeZahlenRechtecke(image);
			// Zahlen setzen anhand der BildInfo
			anzahlZahlen = setzeZahlen();
		}

		if (anzahlZahlen >= anzahlZahlenMin) {
			// Zahlen setzen anhand der Ausg�nge der Zahlbilder
			setzeZahlen(image);
		}

		systemOutErgebnis(image);
		InfoSudoku sudoku = gibSudoku();
		return sudoku;
	}

	InfoSudoku gibSudoku(BufferedImage image) {
		this.image = image;
		return gibSudokuIntern();
	}
}
