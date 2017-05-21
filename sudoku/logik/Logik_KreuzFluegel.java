package sudoku.logik;

import java.util.ArrayList;

class Logik_KreuzFluegel extends LogikFeldPaareRing {

	public Logik_KreuzFluegel(ArrayList<Gruppe> zeilen, ArrayList<Gruppe> spalten) {
		super(2, zeilen, spalten);
	}

	@Override
	public Logik_ID gibLogikID() {
		return Logik_ID.KREUZFLUEGEL;
	}

	@Override
	public String gibKurzName() {
		return "KF";
	}

	@Override
	public String gibName() {
		return "Kreuzflügel";
	}

	@Override
	public double gibKontrollZeit1() {
		return 60;
	}

}
