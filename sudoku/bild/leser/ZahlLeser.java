package sudoku.bild.leser;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class ZahlLeser {
	// =============================================================
	/**
	 * Kleinster Erf�llungsgrad der zum Akzeptieren einer Zahl durch
	 * BildInfo-Erkennung f�hrt
	 */
	static final private int erfuellungsGradMin = 75;
	static private boolean istSystemOut = false;
	static final int systemOutZahl1 = 1;
	static final int systemOutZahl2 = 4;

	static public boolean istSystemOut() {
		return istSystemOut;
	}

	static public boolean istSystemOutZahl(int zahl) {
		boolean istOut = istSystemOut() & ((systemOutZahl1 == zahl) | (systemOutZahl2 == zahl));
		return istOut;
	}

	static float gibDurchschnitt(ArrayList<Float> werte) {
		float durchschnitt = 0;
		for (Float wert : werte) {
			durchschnitt += wert;
		}
		durchschnitt /= werte.size();
		return durchschnitt;
	}

	static private ZahlLeser zahlLeser = new ZahlLeser();
	/**
	 * Die auf der BildInfo realisierten Kriterien
	 */
	static private KriteriumZahl_BildInfo[] kriterienBildInfo = { new KriteriumZahl1(), new KriteriumZahl4(),
			new KriteriumZahl7() };

	/**
	 * Die auf den Ausg�ngen basierenden Kriterien
	 */
	static private KriteriumZahl_PerAusgang[] kriterienAusgang = { new KriteriumZahl2(), new KriteriumZahl3(),
			new KriteriumZahl5(), new KriteriumZahl6(), new KriteriumZahl8(), new KriteriumZahl9() };

	static public String gibErgebnisString(ArrayList<ZahlErgebnis> ergebnisse) {
		String sErgebnisse = "";
		for (ZahlErgebnis zahlErgebnis : ergebnisse) {
			sErgebnisse += String.format(" %d=%1.1f%%", zahlErgebnis.zahl, zahlErgebnis.erfuellungsGrad);
		}
		return sErgebnisse;
	}

	/**
	 * @param zahlBildInfo
	 * @return Zahl oder null, wenn eine solche nicht identifizierbar ist
	 */
	static public ArrayList<ZahlErgebnis> gibZahlWahrscheinlichkeiten(ZahlBildInfo zahlBildInfo) {
		ArrayList<ZahlErgebnis> z = zahlLeser.gibZahlIntern(zahlBildInfo);
		return z;
	}

	/**
	 * @return Zahl oder null, wenn eine solche nicht identifizierbar ist
	 */
	static public Integer gibZahl(BufferedImage image, Rectangle rechteck, boolean istHierSystemOut) {
		AusgangInfo ausgangInfo = AusgangInfo.gibAusgangInfo(image, rechteck, istHierSystemOut);
		Integer zahl = zahlLeser.gibZahlIntern(ausgangInfo);

		if (istHierSystemOut) {
			String s = String.format("ZahlLeser.gibZahl (auf Basis der Ausg�nge): %s", zahl);
			System.out.println(s);
		}
		return zahl;
	}

	// =============================================================
	public class ZahlErgebnis implements Comparable<ZahlErgebnis> {
		public final Integer zahl;
		public final float erfuellungsGrad;

		/**
		 * @param zahl
		 * @param erfuellungsGrad
		 */
		ZahlErgebnis(Integer zahl, float erfuellungsGrad) {
			super();
			this.zahl = zahl;
			this.erfuellungsGrad = erfuellungsGrad;
		}

		@Override
		public int compareTo(ZahlErgebnis other) {
			int ret = -1 * Float.compare(erfuellungsGrad, other.erfuellungsGrad);
			return ret;
		}
	}

	// =============================================================

	/**
	 * @param zahlBildInfo
	 * @return Zahl oder null, wenn eine solche nicht identifizierbar ist
	 */
	private ArrayList<ZahlErgebnis> gibZahlIntern(ZahlBildInfo zahlBildInfo) {
		ArrayList<ZahlErgebnis> ergebnisse = new ArrayList<ZahlErgebnis>();

		for (KriteriumZahl_BildInfo zahlKriterium : kriterienBildInfo) {
			int zahl = zahlKriterium.zahl;
			boolean istSystemOutZahl = istSystemOutZahl(zahl);
			if (istSystemOutZahl) {
				System.out.println(String.format("Zahl %d Kriterien (%s.gibZahlIntern()", zahl, getClass().getName()));
			}
			float erfuellungsGrad = zahlKriterium.gibErfuellungsGrad(zahlBildInfo, istSystemOutZahl);
			ergebnisse.add(new ZahlErgebnis(zahl, erfuellungsGrad));
		}

		Collections.sort(ergebnisse);
		ZahlErgebnis bestesErgebnis = ergebnisse.get(0);
		if (bestesErgebnis.erfuellungsGrad < erfuellungsGradMin) {
			ergebnisse.add(0, new ZahlErgebnis(null, 0));
		}

		// if (istSystemOut()){
		// Integer zahl = ergebnisse.get(0).zahl;
		// String sErgebnisse = gibErgebnisString(ergebnisse);
		// System.out.println(String.format("%s.gibZahlIntern() Zahl=%s: %s",
		// getClass().getName(), zahl, sErgebnisse));
		// }
		return ergebnisse;
	}

	private Integer gibZahlIntern(AusgangInfo ausgangInfo) {
		for (KriteriumZahl_PerAusgang kriterium : kriterienAusgang) {
			boolean istErfuellt = kriterium.istErfuellt(ausgangInfo);
			if (istErfuellt) {
				return kriterium.zahl;
			}
		}
		return null;
	}
}
