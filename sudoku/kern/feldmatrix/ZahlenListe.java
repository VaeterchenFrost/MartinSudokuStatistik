package sudoku.kern.feldmatrix;

import java.util.ArrayList;

/**
 * @author heroe
 * Ist eine ArrayList von FeldNummerMitZahl
 */
@SuppressWarnings("serial")
public class ZahlenListe  extends ArrayList<FeldNummerMitZahl> {

	/**
	 * @param basisListe
	 * @param zahl
	 * @return true wenn die zahl in basisListe existiert
	 */
	public static boolean istEineZahlInDerBasis (ArrayList<ZahlenListe> basisListe, FeldNummerMitZahl zahl){
		for(ZahlenListe zahlen1: basisListe){
			for(FeldNummerMitZahl feldNummerMitZahl: zahlen1){
				if (feldNummerMitZahl.equals(zahl)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param basisListe
	 * @param zahlen
	 * @return true wenn eine Zahl der zahlen in basisListe existiert
	 */
	public static boolean istEineZahlInDerBasis (ArrayList<ZahlenListe> basisListe, ArrayList<ZahlenListe> zahlen){
		// Mit allen zahlen
		for (ZahlenListe zahlen1: zahlen){
			for (FeldNummerMitZahl feldNummerMitZahl: zahlen1){
				if (istEineZahlInDerBasis (basisListe, feldNummerMitZahl)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param zahlenListeArrayList
	 * @return Eine ZahlenListe, in der alle Zahlen enthalten sind.
	 */
	public static ZahlenListe gibAlsEineZahlenListe(ArrayList<ZahlenListe> zahlenListeArrayList){
		ZahlenListe ergebnis = new ZahlenListe();
		
		for (ZahlenListe zahlen1: zahlenListeArrayList){
			ergebnis.add(zahlen1);
		}
		return ergebnis;
	}

	// -------------------------------------------------------------------------
	public ZahlenListe() {
	}

	/**
	 * Konstruiert eine neu Instanz einer Zahlenliste und füllt sie mit allen Elementen von src
	 * @param src
	 */
	public ZahlenListe(ZahlenListe src) {
		super();
		this.addAll(src);
	}

	public ZahlenListe(FeldNummerMitZahl feldNummerMitZahl) {
		super();
		add(feldNummerMitZahl);
	}

	public ZahlenListe(FeldNummerListe feldNummerListe, int zahl){
		super();
		for (FeldNummer feldNummer: feldNummerListe){
			FeldNummerMitZahl feldNummerMitZahl = new FeldNummerMitZahl(feldNummer, zahl);
			add(feldNummerMitZahl);
		}
	}

	 public ArrayList<ZahlenListe> gibAlsArrayList(){
		 ArrayList<ZahlenListe> arrayListe = new ArrayList<>();
		 arrayListe.add(this);
		 return arrayListe;
	 }
	 
	public void add (ZahlenListe zahlen) {
		if (zahlen != null){
			this.addAll(zahlen);
		}
	}
	 
	public void loesche (ZahlenListe loeschZahlen) {
		if (loeschZahlen != null){
			this.removeAll(loeschZahlen);
		}
	}
		 
	/**
	 * @param andere
	 * @return true wenn diese Liste und die andere gleiche Länge und gleiche Elemente besitzen.
	 * 							Die Elemente dürfen in unterschiedlicher Reihenfolge stehen
	 */
	public boolean istGleicherInhalt(ZahlenListe andere){
		if (andere == null){
			return false;
		}
		if (this.size() != andere.size()){
			return false;
		}
		for(FeldNummerMitZahl andereFeldNummerMitZahl: andere){
			if ( ! this.contains(andereFeldNummerMitZahl)){
				return false;
			}
		}
		return true;
	}
}
