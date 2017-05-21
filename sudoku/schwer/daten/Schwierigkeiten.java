package sudoku.schwer.daten;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import sudoku.Paar;
import sudoku.kern.exception.Exc;
import sudoku.kern.info.InfoSudoku;
import sudoku.langerprozess.FortschrittAnzeige;
import sudoku.langerprozess.FortschrittZeiger;
import sudoku.langerprozess.LangerProzess;
import sudoku.langerprozess.ProcessOrganisator;
import sudoku.schwer.Analysator;
import sudoku.schwer.AnzeigeInfo;
import sudoku.schwer.SudokuSchwierigkeit;
import sudoku.tools.Starter;
import sudoku.tools.TextDatei;
import sudoku.tools.Verzeichnis;

/**
 * @author heroe
 * Dient einzig und allein dem Auflisten aller Sudokus eines Verzeichnisses mit ihrer Schwierigkeit.
 * In der Form von System.out.
 */
public class Schwierigkeiten implements LangerProzess {
	/**
	 * @return Name der Datei, die geladen wird, um diese Logik zu starten
	 * Er muss als erstes eine Zahl beinhalten für die Dateinamen-Sortierung nach der Zeit!
	 */
	public static String gibStartDateiName() {
		return "99999999.txt"; // SchwierigkeitAllerSudokusImVerzeichnis.txt";
	}

	/**
	 * @param dateiName vollständiger Dateiname
	 * @return Schwierigkeit oder null
	 */
	private static int gibVorgabenAnzahl(String dateiName) {
		try {
			InfoSudoku vorgaben = InfoSudoku.lade(dateiName);
			return vorgaben.gibAnzahlVorgaben();
		} catch (IOException | Exc e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @param dateiName vollständiger Dateiname
	 * @return Schwierigkeit oder null
	 */
	private static SudokuSchwierigkeit gibSchwierigkeit(String dateiName) {
		try {
			InfoSudoku vorgaben = InfoSudoku.lade(dateiName);
			return Analysator.gibSchwierigkeit(vorgaben);
		} catch (IOException | Exc e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param nameDatei
	 * @param schwierigkeit wird angezeigt
	 * @return Schwierigkeiten-Info-Text zur Datei
	 */
	private static String zeige(String nameDatei, int anzahlVorgaben, SudokuSchwierigkeit schwierigkeit,
			boolean mitDetails) {
		AnzeigeInfo versucheSumme = schwierigkeit.gibAnzahlOKVersuche();
		InfoKlareZeit klareZeit = schwierigkeit.gibKlareZeit();
		ArrayList<InfoKlareDetail> klareDetails = schwierigkeit.gibDetails();
		LogikAnzahlen logikAnzahlen = schwierigkeit.gibKlareErfolgreicheLogiken();
		String infoText = null;
		{ // Zusammenfassung
			String infoVorgaben = String.format("%2d Vorgaben", anzahlVorgaben);
//			String infoZeit = String.format("%2d", Verzeichnis.gibSudokuLoesungsZeit(nameDatei));
			String infoSumme = "";
			if (versucheSumme != null) {
				infoSumme += versucheSumme.gibAnzeigeText() + ":  ";
			} else {
				infoSumme += klareZeit.gibAnzeigeText() + ": ";
			}
			infoText = infoVorgaben + "; WieSchwer=" + infoSumme + " " + logikAnzahlen + " Dateiname=" + nameDatei;
//			System.out.println(infoText);
		}

		if (mitDetails) {
			for (int i = 0; i < klareDetails.size(); i++) {
				InfoKlareDetail info1 = klareDetails.get(i);
				double detailZeit = info1.gibZeit();

				System.out.println(String.format("%.1f %s", detailZeit, info1.gibAnzeigeText()));
			}
		}
		return infoText;
	}

	/**
	 * @param dateiNamen
	 * @param mitDetails
	 */
	private static void schreibeZeiten(String nameVerzeichnis, String[] dateiNamen, boolean mitDetails,
			FortschrittZeiger fortschrittZeiger) {
		ArrayList<String> infoTexte = new ArrayList<>();
		for (int i = 0; i < dateiNamen.length; i++) {
			String nameDatei = dateiNamen[i];
			boolean istStartDatei = nameDatei.indexOf(Schwierigkeiten.gibStartDateiName()) >= 0;
			if (!istStartDatei) {
				// System.out.println(nameDatei);
				int anzahlVorgaben = gibVorgabenAnzahl(nameVerzeichnis + nameDatei);
				SudokuSchwierigkeit schwierigkeit = gibSchwierigkeit(nameVerzeichnis + nameDatei);
				if (schwierigkeit != null) {
					String infoText = zeige(nameDatei, anzahlVorgaben, schwierigkeit, mitDetails);
					infoTexte.add(infoText);
				}
			}
			fortschrittZeiger.zeigeFortschritt(i);
			String info = String.format("%d  %d", dateiNamen.length, i);
			fortschrittZeiger.zeigeInfo(info);
			if (fortschrittZeiger.istAbbruchGefordert()) {
				return;
			}
			if (Thread.currentThread().isInterrupted()) {
				return;
			}
		} // for

		if ((!mitDetails) & (!infoTexte.isEmpty())) {
			// Schwierigkeiten-Übersicht in der Datei ablegen
			String speicherDateiName = nameVerzeichnis + gibStartDateiName();
			try {
				TextDatei.erstelle(speicherDateiName, infoTexte);
				// String filePath = speicherDateiName.replaceAll("\\\\", "/");
				Starter.execProgramUnterWindows(speicherDateiName);
			} catch (Exception e) {
				e.printStackTrace();
				String msg = String.format("Die Datei %s konnte nicht ordungsgemäß erstellt werden", speicherDateiName);
				JOptionPane.showMessageDialog(null, msg, "Schwierigkeiten-Erstellung", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
	 * Listet alle Dateien des Verzeichnisses auf. Als langer Prozess unter einer Fortschrittsanzeige
	 * @param nameVerzeichnis
	 */
	public static void schreibe(String nameVerzeichnis, FortschrittAnzeige fortschrittAnzeige) {
		Schwierigkeiten prozess = new Schwierigkeiten(nameVerzeichnis);
		ProcessOrganisator.laufenLassen(prozess, fortschrittAnzeige);
	}

	// ===============================================================
	private String verzeichnisName;

	public Schwierigkeiten(String verzeichnisName) {
		this.verzeichnisName = verzeichnisName;
	}

	@Override
	public String gibTitel() {
		String unterVerzeichnisName = Verzeichnis.gibLetztesUnterverzeichnis(verzeichnisName);
		String titel = String.format("%s-Schwierigkeiten schreiben", unterVerzeichnisName);
		return titel;
	}

	@Override
	public Paar<Integer, Integer> gibFortschrittBereich() {
		File dir = new File(verzeichnisName);
		String[] dateiNamen = dir.list();
		return new Paar<Integer, Integer>(0, dateiNamen.length);
	}

	@Override
	public void prozess(FortschrittZeiger fortschrittZeiger) {
//		System.out.println(Schwierigkeiten.class.getName());
		String[] dateiNamen = Verzeichnis.gibSudokuNamen(this.verzeichnisName);
		// System.out.println("list(): " + Arrays.toString(files));
		Verzeichnis.sortiereNachLoesungsZeit(dateiNamen);
		schreibeZeiten(this.verzeichnisName, dateiNamen, false, fortschrittZeiger); // true); Wirklich nur wenn es unbedingt gewollt ist.
	}

}
