package sudoku.kern.protokoll;

import java.io.IOException;
import java.util.ArrayList;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.Eintrag;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.tools.TextDatei;

public class Protokoll_IO {
	static private final String strIDProtokoll = "Protokoll";
	static private final String strIDKursor = "Kursor";
	static private final String strIDEintrag = "Eintrag";
	static private final String strIDAlt = "Alt";
	static private final String strIDNeu = "Neu";
	
	static private final String strIDIndex = "i";
	static private final String strIDZeile = "Z";
	static private final String strIDSpalte  = "S";
	static private final String strIDZahl = "z";
	static private final String strIDEbene = "e";
	static private final String strIDIstEbenenStart = "s";
	static private final String strIDIstTipZahl = "t";

	// ===========================================
	private int kursor;
	private ArrayList<ProtokollEintrag> eintraege;

	/**
	 * Initialisierung aus dem Protokoll
	 * @param protokoll
	 */
	public Protokoll_IO(int kursor, ArrayList<ProtokollEintrag> eintraege) {
		init();
		this.kursor = kursor;
		this.eintraege = eintraege;
	}

	private void init() {
		kursor = -1;
		eintraege = new ArrayList<ProtokollEintrag>();
	}

	/**
	 * Initialisierung aus der Textdatei
	 * @param dateiName
	 */
	public Protokoll_IO(String dateiName) {
		init();
		ArrayList<String> texte = null;
		try {
			texte = TextDatei.lese(dateiName);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		int kursorGelesen = kursor;
		ArrayList<ProtokollEintrag> eintraegeGelesen = new ArrayList<>();
		try {
			// Kursor lesen
			int kursorTexteIndex = -1;
			String sIDKursor = strIDProtokoll + strIDKursor;
			for (int iText = 0; iText < texte.size(); iText++) {
				String text = texte.get(iText);
				if (text.startsWith(sIDKursor)) {
					String inhalt = gibInhalt(text, sIDKursor);

					kursorGelesen = gibWert(inhalt, strIDKursor);
					kursorTexteIndex = iText;
					break;
				}
			}

			if (kursorTexteIndex < 0) {
				return;
			}

			// Einträge lesen
			String sIDEintrag = strIDProtokoll + strIDEintrag;
			for (int iText = kursorTexteIndex + 1; iText < texte.size(); iText++) {
				String text = texte.get(iText);
				if (text.startsWith(sIDEintrag)) {
					String inhalt = gibInhalt(text, sIDEintrag);
					ProtokollEintrag protokollEintrag = gibProtokollEintrag(inhalt);
					eintraegeGelesen.add(protokollEintrag);
				}
			}
		} catch (Exc e) {
			e.printStackTrace();
			return;
		}

		kursor = kursorGelesen;
		eintraege = eintraegeGelesen;
	}

	public ArrayList<ProtokollEintrag> gibEintraege() {
		return eintraege;
	}

	public int gibKursor() {
		return kursor;
	}

	private int gibWert(String text, String sID) throws Exc {
		int indexOfID = text.indexOf(sID);
		if (indexOfID < 0){
			throw Exc.protokoll_IOWertFehlt(sID);
		}
		String inhalt = text.substring(indexOfID + sID.length(), text.length());
		int indexOfLeerZeichen = inhalt.indexOf(' ');
		inhalt = inhalt.substring(1, indexOfLeerZeichen);
		int wert = Integer.valueOf(inhalt);
		return wert;
	}

	/**
	 * @param text 
	 * @param sID dieser Text steht direkt am Anfang von text als Identifikator
	 * @return text ohne Identifikator und Leerzeichen am Rand
	 */
	private String gibInhalt(String text, String sID) {
		String inhalt = text.substring(sID.length(), text.length());
//		inhalt = inhalt.trim();
		return inhalt;
	}

	ProtokollEintrag gibProtokollEintrag(String text) throws Exc{
		int zeile = gibWert(text, strIDZeile);
		int spalte = gibWert(text, strIDSpalte);
		Eintrag eintragAlt = gibEintrag(text, strIDAlt);
		Eintrag eintragNeu = gibEintrag(text, strIDNeu);
		ProtokollEintrag protokollEintrag = new ProtokollEintrag(new FeldNummer(spalte, zeile), eintragAlt, eintragNeu);
		return protokollEintrag;
	}

	Eintrag gibEintrag(String text, String sID) throws Exc{
		int indexOfID = text.indexOf(sID);
		if (indexOfID < 0){
			throw Exc.protokoll_IOWertFehlt(sID);
		}
		String inhalt = text.substring(indexOfID + sID.length(), text.length());
		indexOfID = inhalt.indexOf(sID);
		if (indexOfID < 0){
			throw Exc.protokoll_IOWertFehlt(sID);
		}
		inhalt = inhalt.substring(0, indexOfID);

		indexOfID = inhalt.indexOf(strIDZahl);
		if (indexOfID < 0){
			return null;
		}
		
		int zahl = gibWert(inhalt, strIDZahl);
		int ebene = gibWert(inhalt, strIDEbene);
		int ebenenStart = gibWert(inhalt, strIDIstEbenenStart);
		int zahlTip = gibWert(inhalt, strIDIstTipZahl);
		Eintrag eintrag = new Eintrag(zahl, ebene, ebenenStart != 0, zahlTip != 0);
		return eintrag;
	}
	
	/**
	 * @return Die Protokoll-Infos in Textform zum Ablegen
	 */
	public ArrayList<String> gibSpeicherTexte() {
		 ArrayList<String> texte = new ArrayList<>();
		 String zeileKursor = String.format("%s %s=%d ", strIDProtokoll+strIDKursor, strIDKursor, kursor);
		 texte.add(zeileKursor);
		 
		 for (int iEintrag = 0; iEintrag < eintraege.size(); iEintrag++) {
			 ProtokollEintrag protokollEintrag = eintraege.get(iEintrag);
			 
			 String sFeldNummer = String.format(" %s=%d %s=%d ", strIDZeile, protokollEintrag.gibFeldNummer().gibZeile(),
					 strIDSpalte, protokollEintrag.gibFeldNummer().gibSpalte());
			 String sAlt = gibSpeicherText(protokollEintrag.eintragAlt, strIDAlt);
			 String sNeu = gibSpeicherText(protokollEintrag.eintragNeu, strIDNeu);
			 String zeile = String.format("%s %s=%d %s %s %s", strIDProtokoll+strIDEintrag, strIDIndex, iEintrag, sFeldNummer, sAlt, sNeu);
			 texte.add(zeile);
		 }
		 return texte;
	}

	 private String gibSpeicherText(Eintrag eintrag, String strID){
		 String sEintrag = " ";
		 if (eintrag != null){
			 sEintrag = String.format(" %s=%d %s=%d %s=%d %s=%d ", 	
				strIDZahl, eintrag.gibZahl(),
				strIDEbene, eintrag.gibEbene(),
				strIDIstEbenenStart, eintrag.istEbenenStart() ? 1 : 0,
				strIDIstTipZahl, eintrag.istTipZahl() ? 1: 0);
		 }
		 String text = String.format("%s %s %s", strID, sEintrag, strID);
		 return text;
	 }
	
}
