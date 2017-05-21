package sudoku.schwer.daten;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;

import sudoku.logik.Klugheit;
import sudoku.logik.Logik_ID;
import sudoku.logik.SudokuLogik;

/**
 * @author Hendrick
 * Jede mögliche Logik mit der Anzahl ihrer Nutzung
 */
public class LogikAnzahlen {
	private EnumMap<Logik_ID, Integer> logikAnzahlen; 

	public LogikAnzahlen() {
		logikAnzahlen = new EnumMap<>(Logik_ID.class);
	}

	/**
	 * @param logik Für diese Logik wird die Anzahl um 1 vergrößert
	 */
	public void add(Logik_ID logik, int anzahl){
		Integer anzahlIst = logikAnzahlen.get(logik);
		if (anzahlIst == null){
			anzahlIst = new Integer(anzahl);
		}
		else{
			anzahlIst+=anzahl;
		}
		logikAnzahlen.put(logik, anzahlIst);
	}

	/**
	 * @param andere Dessen Anzahlen werden zu meinen hinzuaddiert
	 */
	public void add(LogikAnzahlen andere) {
		Iterator<Logik_ID> andereLogiken = andere.logikAnzahlen.keySet().iterator();
		
		while(andereLogiken.hasNext()){
			Logik_ID logik = andereLogiken.next();
			Integer anzahlAndere = andere.logikAnzahlen.get(logik);

			Integer anzahlMeine = logikAnzahlen.get(logik);
			if (anzahlMeine == null){
				anzahlMeine = new Integer(0);
			}

			anzahlMeine += anzahlAndere;
			logikAnzahlen.put(logik, anzahlMeine);
		}
	}
	
	/**
	 * @param logik
	 * @return Die Anzahl dieser Logik ab 0.
	 */
	public int gibAnzahl(Logik_ID logik){
		Integer anzahl = logikAnzahlen.get(logik);
		if (anzahl == null){
			return 0;
		}
		else{
			return anzahl.intValue();
		}
	}
	
	/**
	 * @return Alle Logiken mit Anzahl > 0.
	 * 				Auf Index 0 steht die leichteste Logik, auf dem größten Index die schwerste Logik
	 */
	public Logik_ID[] gibLogiken() {
		ArrayList<Logik_ID> logikListe = new ArrayList<>();
		Logik_ID logiken[] = Logik_ID.values();

		for (int i = 0; i < logiken.length; i++) {
			Logik_ID logik = logiken[i];
			
			int anzahl = gibAnzahl(logik);
			if (anzahl > 0){
				logikListe.add(logik);
			}
		}
		
		Logik_ID[] logikArray = new Logik_ID[logikListe.size()];
		logikListe.toArray(logikArray);
		return logikArray;
	}

	
	/**
	 */
	public int gibAnzahlSumme() {
		int summe = 0;
		Iterator<Logik_ID> enumKeySet = logikAnzahlen.keySet().iterator();
		while(enumKeySet.hasNext()){
			Logik_ID logik = enumKeySet.next();
			Integer anzahl1 = logikAnzahlen.get(logik);
			summe += anzahl1;
		}
		return summe;
	}
	
	/**
	 * @param klugheit
	 * @return true wenn ich die größte der geforderten Klugheiten besitze
	 */
	public boolean istSoKlug(Klugheit klugheit){
		Klugheit soll = new Klugheit(klugheit);
		Logik_ID maxSoll = soll.gibGroessteLogik();
		if (maxSoll == null){
			return true;
		}
		// besitze ich max?
		Integer ist = logikAnzahlen.get(maxSoll);
		if (ist == null){
			return false;
		}
		return (ist > 0);
	}

	/**
	 * @return true wenn die Logik SOLISTEN vorhanden ist und zwar als einzige mit Anzahl > 0.
	 * Es wird eine Anzahl-Summe von > 0 vorausgesetzt.
	 */
	public boolean istNurLogikOrtFest1(){
		Integer solistAnzahl = logikAnzahlen.get(Logik_ID.ORTFEST1);
		
		if (solistAnzahl == null){
			return false;
		}
		
		if (solistAnzahl == 0){
			return false;
		}
		
		int anzahlSumme = this.gibAnzahlSumme();
		
		return solistAnzahl == anzahlSumme;
	}
	
	/**
	 * @return Die größte Logik mit Anzahl>0
	 */
	public Logik_ID gibGroessteLogik(){
		Logik_ID logik = Logik_ID.gibMinimum();
		Iterator<Entry<Logik_ID, Integer>> iterator = logikAnzahlen.entrySet().iterator();
		
		while(iterator.hasNext()){
			Entry<Logik_ID, Integer> entry = iterator.next();
			int anzahl = entry.getValue();
			if (anzahl > 0){
				Logik_ID dieseLogik = entry.getKey();
				if (dieseLogik.ordinal() > logik.ordinal()){
					logik = dieseLogik;
				}
			}
		}		
		return logik;
	}

	public interface LogikAnzahlFormatierer{
		/**
		 * @param logikID
		 * @param anzahl
		 * @return Text des Parameter-Paares
		 */
		public String gibString (Logik_ID logikID, int anzahl);
	}
	
	/**
	 * @param logikAnzahlFormatierer wenn hier null kommt erfolgt Standardformatierung:
	 * 						Logik-Kurzname + "=" + anzahl + " ". Im Beispiel "O1=13 "
	 * @return String mit allen LogikAnzahlen ab der größten Logik bis zur kleinsten.
	 */
	public String gibString(LogikAnzahlFormatierer logikAnzahlFormatierer) {
		ArrayList<String> texte = new ArrayList<>();
		Iterator<Logik_ID> enumKeySetIterator = logikAnzahlen.keySet().iterator();
		
		while(enumKeySetIterator.hasNext()){
			Logik_ID logikID = enumKeySetIterator.next();
			Integer anzahl = logikAnzahlen.get(logikID);
			if (anzahl > 0){
				String s = null;
				if (logikAnzahlFormatierer == null){
					String sLogik = SudokuLogik.gibNameKurz(logikID);
					s = String.format("%s=%d ", sLogik, anzahl);
				}
				else{
					s = logikAnzahlFormatierer.gibString(logikID, anzahl);
				}
				texte.add(s);
			}
		}

		String sAnzeige = new String();

		for (int i = texte.size()-1; i >= 0; i--) {
//		for (int i = 0; i < texte.size();  i++) {
			sAnzeige += texte.get(i);
		}
		return sAnzeige;
	}

	@Override
	public String toString() {
		String s = gibString(null);
		return s;
	}
}
