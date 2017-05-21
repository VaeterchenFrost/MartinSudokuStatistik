package sudoku.bild;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;

import sudoku.bild.leser.ZahlBildInfo;
import sudoku.kern.feldmatrix.FeldNummer;

/**
 * @author heroe
 * Die Bereiche innerhalb des Zahl-Rechtecks
 */
@SuppressWarnings("serial")
class ZahlBereiche extends HashMap<FeldNummer, Rectangle>{
	
	/**
	 * @param laengeGesamt
	 * @param nBereiche
	 * @return Möglichst gleichmäßig auf die Gesamtlänge aufgeteilte Bereiche
	 */
	private int[] gibLaengen(int laengeGesamt, int nBereiche){
		int laenge = laengeGesamt / nBereiche;
		int rest = laengeGesamt - nBereiche*laenge;
		int[] laengen = new int[nBereiche];
		
		for (int i = 0; i < laengen.length; i++) {
			laengen[i] = laenge;
		}
		
		// Rest aufteilen auf die äußeren Rechtecke
		int rest2 = rest / 2;
		for (int i = 0; i < rest2; i++) {
			laengen[i]++;
		}
		for (int i = 0; i < rest2; i++) {
			laengen[laengen.length - 1 - i]++;
		}
		
		rest = rest - 2*rest2;
		if (rest > 0){
			laengen[rest2]++;
		}
		return laengen;
	}
	
	/**
	 * @param laengen
	 * @param n Anzahl der zu summierenden Werte
	 * @return 
	 */
	private int gibSumme(int[] laengen, int n){
		int summe = 0;
		for (int i = 0; i < n; i++) {
			summe+=laengen[i];
		}
		return summe;
	}
	
	ZahlBereiche(Rectangle r){
		int[] breiten = gibLaengen(r.width, ZahlBildInfo.nLinien);
		int[] hoehen= gibLaengen(r.height, ZahlBildInfo.nLinien);
		
		for (int zeile = 1; zeile <= ZahlBildInfo.nLinien; zeile++) {
			for (int spalte = 1; spalte <= ZahlBildInfo.nLinien; spalte++) {
				int x = r.x + gibSumme(breiten, spalte-1);
				int y = r.y + gibSumme(hoehen, zeile-1);
				int breite = breiten[spalte-1];
				int hoehe = hoehen[zeile-1];
				Rectangle rBereich = new Rectangle(x, y, breite, hoehe);
				FeldNummer feldNummer = new FeldNummer(spalte, zeile);
				this.put(feldNummer, rBereich);
			}
		}
//		if (istTestAnzeige){
//			systemOut(r);
//		}
	}

	@SuppressWarnings("unused")
	private void systemOut(Rectangle zahlRechteck){
		System.out.println(String.format("%s ZahlRechteck %s", getClass().getName(), zahlRechteck));
		for (int zeile = 1; zeile <= ZahlBildInfo.nLinien; zeile++) {
			for (int spalte = 1; spalte <= ZahlBildInfo.nLinien; spalte++) {
				FeldNummer feldNummer = new FeldNummer(spalte, zeile);
				Rectangle r = this.get(feldNummer);
				System.out.print(String.format(" [%d %d %d %d]", r.x, r.y, r.width, r.height));
			}
			System.out.println();
		}
	}

	
	void rotiere90Grad(Dimension bildDimension){
		HashMap<FeldNummer, Rectangle> gedrehte = new HashMap<>();
		Set<Entry <FeldNummer, Rectangle>> eintraege = entrySet();
		
		for(Entry <FeldNummer, Rectangle> eintrag: eintraege){
			Rectangle neuesRechteck = Bild.rotiere90Grad(bildDimension, eintrag.getValue());
			gedrehte.put(eintrag.getKey(), neuesRechteck);
		}
		
		this.clear();
		this.putAll(gedrehte);
	}
	
	void male(BufferedImage image){
		for(Rectangle r: this.values()){
			Graphics2D g = image.createGraphics();
			g.setColor(Color.BLACK);
			g.drawRect(r.x, r.y, r.width, r.height);
		}
	}
}

