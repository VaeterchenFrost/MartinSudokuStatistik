package sudoku.logik;

import java.util.ArrayList;

class Logik_SchwertFisch extends LogikFeldPaareRing {

	public Logik_SchwertFisch(ArrayList<Gruppe> zeilen, ArrayList<Gruppe> spalten) {
		super(3, zeilen, spalten);
	}

	@Override
	public Logik_ID gibLogikID() {
		return Logik_ID.SCHWERTFISCH;
	}

	@Override
	public String gibKurzName() {
		return "SF";
	}

	@Override
	public String gibName() {
		return "SchwertFisch";
	}

	@Override
	public double gibKontrollZeit1() {
		return 60;
	}

}
