package sudoku.langerprozess;

/**
 * @author heroe
 * Organisiert einen langen Prozess (eine lang dauernde Routine) als extra Thread.
 * L‰ﬂt w‰hrend des Prozess-Laufes eine Fortschritt-Anzeige erscheinen.
 * Diese bestimmt, ob der Aufrufer-Programmpfad w‰hrend des Prozess-Laufes angehalten wird. 
 */
public class ProcessOrganisator {
	/**
	 * @param prozess  Dieser (lange) Prozess wird als extra Thread laufen gelassen.
	 * @param fortschrittAnzeige Erscheint w‰hrend des Prozess-Laufes
	 */
	public static void laufenLassen(LangerProzess prozess, FortschrittAnzeige fortschrittAnzeige){
		new ProcessOrganisator().new MeinThread(prozess, fortschrittAnzeige).start();
		
		fortschrittAnzeige.starten(prozess.gibTitel(), prozess.gibFortschrittBereich());
	}
	
	// ====================================================================
	private class MeinThread extends Thread{
		private LangerProzess prozess;
		private FortschrittAnzeige fortschrittAnzeige;
		
		public MeinThread(LangerProzess prozess, FortschrittAnzeige fortschrittAnzeige) {
			super (prozess.gibTitel());
			this.prozess = prozess;
			this.fortschrittAnzeige = fortschrittAnzeige;
		}

		@Override
		public void run() {
			try
			{
				this.prozess.prozess(this.fortschrittAnzeige);
			}
			finally
			{
				this.fortschrittAnzeige.beenden();
			}
		}		
	}
	
	ProcessOrganisator(){
	}
}
