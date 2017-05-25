package sudoku.bild;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;

import sudoku.bild.leser.ZahlBildInfo;
import sudoku.kern.animator.Animator;
import sudoku.kern.feldmatrix.FeldNummer;

@SuppressWarnings("serial")
class SchwarzAnteile extends HashMap<FeldNummer, Float> implements ZahlBildInfo {

	SchwarzAnteile(BufferedImage image, ZahlBereiche bereiche) {
		Set<Entry<FeldNummer, Rectangle>> eintraege = bereiche.entrySet();
		for (Entry<FeldNummer, Rectangle> eintrag : eintraege) {
			Rectangle r = eintrag.getValue();
			LinienWeiss linienWeiss = new LinienWeiss(image, r);
			float schwarzAnteil = linienWeiss.gibSchwarzAnteil();
			this.put(eintrag.getKey(), schwarzAnteil);
		}
	}

	public void drehen(Animator animator) {
		Set<Entry<FeldNummer, Float>> schwarzAnteile = this.entrySet();

		HashMap<FeldNummer, Float> gedrehte = new HashMap<>();
		for (Entry<FeldNummer, Float> schwarzAnteil : schwarzAnteile) {
			FeldNummer neueFeldNummer = animator.gibFeldNummer(schwarzAnteil.getKey(), ZahlBildInfo.nLinien);
			gedrehte.put(neueFeldNummer, new Float(schwarzAnteil.getValue()));
		}

		this.clear();
		this.putAll(gedrehte);
	}

	@Override
	public float gibSchwarzAnteil(FeldNummer feldNummer) {
		Float f = this.get(feldNummer);
		return f;
	}

	void systemOut(FeldNummer bildFeldNummer) {
		System.out.println(String.format("BildFeld %s Schwarzanteile", bildFeldNummer)); // ,
																							// getClass().getName()));
		for (int zeile = 1; zeile <= ZahlBildInfo.nLinien; zeile++) {
			for (int spalte = 1; spalte <= ZahlBildInfo.nLinien; spalte++) {
				FeldNummer feldNummer = new FeldNummer(spalte, zeile);
				Float f = this.get(feldNummer);
				System.out.print(String.format(" %5.1f", f));
			}
			System.out.println();
		}
	}

}
