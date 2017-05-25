package sudoku.knacker.bericht;

public class KB_KlareSetzeMoegliche implements KB_BerichtEintrag {
	private sudoku.logik.bericht.BerichtLogik berichtMoegliche;

	public KB_KlareSetzeMoegliche(sudoku.logik.bericht.BerichtLogik berichtMoegliche) {
		this.berichtMoegliche = berichtMoegliche;
	}

	@Override
	public void systemOut() {
		System.out.println(this.getClass().getPackage().getName() + "." + this.getClass().getName());
		berichtMoegliche.systemOut();
	}

	public sudoku.logik.bericht.BerichtLogik gibBericht() {
		return berichtMoegliche;
	}

}
