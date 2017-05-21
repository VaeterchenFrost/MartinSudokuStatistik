package sudoku.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TextDatei {

	/**
	 * @param dateiname Es wird eine neue Datei erstellt. Eine bestehende wird zuvor gelöscht.
	 * @param text
	 * @throws IOException
	 */
	public static void erstelle(String dateiname, String text) throws IOException{
		 ArrayList<String> texte = new ArrayList<>();
		 texte.add(text);
		 erstelle(dateiname, texte);
	}

	
	/**
	 * @param dateiname Es wird eine neue Datei erstellt. Eine bestehende wird zuvor gelöscht.
	 * @param texte
	 * @throws IOException
	 */
	public static void erstelle(String dateiname, ArrayList<String> texte) throws IOException{
		File f = new File(dateiname);
		if (f.exists()){
			f.delete();
		}
		f.createNewFile();
		FileWriter fw = new FileWriter(f, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.flush();
		for (int iText = 0; iText < texte.size(); iText++) {
		    bw.write(texte.get(iText));
		    bw.newLine();
		}
	    bw.close();
	}

	/**
	 * @param dateiname
	 * @return Alle Zeilen der Datei in einem String (ohne CR + Zeilenvorschübe)
	 * @throws IOException
	 */
	public static String lese1String (String dateiname) throws IOException{
		ArrayList<String> texte = lese (dateiname);
		String returnText = "";
		for (int iText = 0; iText < texte.size(); iText++) {
			returnText += texte.get(iText);
		}
		return returnText;
	}
	
	/**
	 * @param dateiname
	 * @return Alle Zeilen der Datei (ohne CR + Zeilenvorschübe)
	 * @throws IOException
	 */
	public static ArrayList<String> lese (String dateiname) throws IOException{
		FileInputStream fstream = new FileInputStream(dateiname);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(in));
		ArrayList<String> texte = new ArrayList<>();

		while (true){
			String sZeile = br.readLine();
			if (null == sZeile){
				break;
			} 
			else {
				texte.add(sZeile);
			}
		}
		in.close();
		return texte;
	} 

	
}
