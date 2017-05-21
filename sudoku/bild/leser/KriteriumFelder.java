package sudoku.bild.leser;

import java.util.ArrayList;

import sudoku.kern.feldmatrix.FeldNummer;

class KriteriumFelder implements KriteriumBildInfo{
	/**
	 * @param soll Sollwert
	 * @param ist Istwert
	 * @param erfuellung0Min Bei diesem Wert, der kleiner als der Sollwert ist, 
	 * 			ist das Kriterium zu 0 Prozent erfüllt, also nicht erfüllt.
	 * @param erfuellung0Max Bei diesem Wert, der größer als der Sollwert ist, 
	 * 			ist das Kriterium zu 0 Prozent erfüllt, also nicht erfüllt.
	 * @return Erfüllungsgrad des Sollwertes durch den Istwert in Prozent
	 */
	static float gibErfuellungsGrad(SollSchwarz sollSchwarz, float ist){
		float erfuellungsGrad = 0;
		if (ist == sollSchwarz.soll){
			erfuellungsGrad = 100;
		}
		else{
			if (ist < sollSchwarz.soll){
				if (sollSchwarz.erfuellung0Min == null){
					erfuellungsGrad = 100;
				}
				else{
					float diffIst = ist - sollSchwarz.erfuellung0Min;
					float diff100 = sollSchwarz.soll - sollSchwarz.erfuellung0Min;
					erfuellungsGrad = (100.0f * diffIst) / diff100;
				}
			}
			else{
				if (sollSchwarz.erfuellung0Max == null){
					erfuellungsGrad = 100;
				}
				else{
					float diffIst = sollSchwarz.erfuellung0Max - ist;
					float diff100 = sollSchwarz.erfuellung0Max - sollSchwarz.soll;
					erfuellungsGrad = (100.0f * diffIst) / diff100;
				}
			}
			
			if (erfuellungsGrad < 0){
				erfuellungsGrad = 0;
			}
			if (erfuellungsGrad > 100){
				erfuellungsGrad = 100;
			}
		}
		return erfuellungsGrad;
	}
	
	// ========================================================
	private class FeldErgebnis{
		final FeldNummer feldNummer;
		final float ist;
		float erfuellungsGrad;
		/**
		 * @param feldNummer
		 * @param ist
		 * @param erfuellungsGrad
		 */
		FeldErgebnis(FeldNummer feldNummer, float ist, float erfuellungsGrad) {
			super();
			this.feldNummer = feldNummer;
			this.ist = ist;
			this.erfuellungsGrad = erfuellungsGrad;
		}
	}
	
	// ========================================================
	private String name;
	private FeldNummer[] felder; 
	private SollSchwarz sollSchwarz;	
	/**
	 * @param name
	 * @param felder
	 * @param sollSchwarz
	 */
	KriteriumFelder(String name, SollSchwarz sollSchwarz,	FeldNummer[] felder) {
		super();
		this.name = name;
		this.felder = felder;
		this.sollSchwarz = sollSchwarz;
	}

	KriteriumFelder(String name, SollSchwarz sollSchwarz,	FeldNummer feld) {
		super();
		this.name = name;
		this.felder = new FeldNummer[1];
		this.felder[0] = feld;
		this.sollSchwarz = sollSchwarz;
	}

	/**
	 * @param name
	 * @param vonFeld Benennung des Bereichs: Feld oben links
	 * @param bisFeld Benennung des Bereichs: Feld unten rechts
	 * @param sollSchwarz
	 * @param erfuellung0Min
	 * @param erfuellung0Max
	 */
	KriteriumFelder(String name, SollSchwarz sollSchwarz, FeldNummer vonFeld, FeldNummer bisFeld) {
		super();
		this.name = name;
		this.sollSchwarz = sollSchwarz;

		int anzahlSpalten = bisFeld.spalte - vonFeld.spalte +1;
		int anzahlZeilen = bisFeld.zeile - vonFeld.zeile +1;
		this.felder = new FeldNummer[anzahlSpalten * anzahlZeilen];
		int i = 0;
		
		for (int zeile = vonFeld.zeile; zeile <= bisFeld.zeile; zeile++) {
			for (int spalte = vonFeld.spalte; spalte <= bisFeld.spalte; spalte++) {
				FeldNummer feldNummer = new FeldNummer(spalte, zeile);
				this.felder[i] = feldNummer;
				i++;
			}
		}
	}

	public String gibName(){
		return name;
	}
	
	public float gibErfuellungsGrad(ZahlBildInfo zahlBildInfo, boolean istHierSystemOut) {
		ArrayList<FeldErgebnis> feldErgebnisse = new ArrayList<FeldErgebnis>();
	
		float durchschnitt = 0;
		for (int i = 0; i < felder.length; i++) {
			FeldNummer feldNummer = felder[i];
			float ist = zahlBildInfo.gibSchwarzAnteil(feldNummer);
			float erfuellungsGrad = KriteriumFelder.gibErfuellungsGrad(sollSchwarz, ist);
			FeldErgebnis feldErgebnis = new FeldErgebnis(feldNummer, ist, erfuellungsGrad);
			feldErgebnisse.add(feldErgebnis);
			durchschnitt += erfuellungsGrad;
		}
		durchschnitt/=felder.length;
		
		if (istHierSystemOut){
			String sErgebnisse = "";
			for(FeldErgebnis feldErgebnis: feldErgebnisse){
				sErgebnisse += String.format(" %s=%1.1f=>%1.1f%%", 
						feldErgebnis.feldNummer, feldErgebnis.ist, feldErgebnis.erfuellungsGrad);
			}
			System.out.println(String.format("Erfüllt=%1.1f%%  %s: %s %s", durchschnitt, this.name, sErgebnisse, this));
		}
		
		return durchschnitt;
	}

	@Override
	public String toString() {
//		String sFelder = "";
//		for (int i = 0; i < felder.length; i++) {
//			FeldNummer feldNummer = felder[i];
//			sFelder += " " + feldNummer.toString();
//		}
		return "Kriterium [" + name + ":" + //sFelder + 
				", 0Min=" + sollSchwarz.erfuellung0Min + ", soll=" + sollSchwarz.soll + ", 0Max=" + sollSchwarz.erfuellung0Max + "]";
	}

}
