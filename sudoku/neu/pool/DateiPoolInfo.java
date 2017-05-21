package sudoku.neu.pool;

import java.io.File;
import java.util.EnumMap;

import sudoku.logik.Schwierigkeit;
import sudoku.neu.NeuTyp;
import sudoku.tools.Verzeichnis;

class DateiPoolInfo {

	static private EnumMap<Schwierigkeit, TopfInfo> gibInfoVerfuegbare(DateiPool dateiPool) {
		EnumMap<Schwierigkeit, TopfInfo> poolInfo = new EnumMap<Schwierigkeit, TopfInfo>(Schwierigkeit.class);
		Schwierigkeit[] wieSchwerArray = Schwierigkeit.values();

		for (int iSchwierigkeit = 0; iSchwierigkeit < wieSchwerArray.length; iSchwierigkeit++) {
			Schwierigkeit wieSchwer = wieSchwerArray[iSchwierigkeit];
			NeuTyp typ = new NeuTyp(wieSchwer);
			String verzeichnisName = dateiPool.gibTopfName(typ);
			File fVerzeichnis = new File(verzeichnisName);
			File[] fArray = fVerzeichnis.listFiles();
			TopfInfo schwierigkeitInfo = new TopfInfo();

			for (int iFile = 0; iFile < fArray.length; iFile++) {
				File file = fArray[iFile];
				String fname = file.getName();
				boolean istSudoku = Verzeichnis.istSudoku(fname);
				if (istSudoku) {
					boolean istDoppel = fname.indexOf(" ") > 0;
					long fGroesse = file.length();
					int fLoesungsZeit = Verzeichnis.gibSudokuLoesungsZeit(fname);
					long fModified = file.lastModified();
					TopfInfo dateiInfo = new TopfInfo(1, istDoppel ? 1 : 0, fGroesse, fLoesungsZeit, fLoesungsZeit,
							fModified, fModified);
					schwierigkeitInfo.add(dateiInfo);
				} // if (istSudoku){
			} // for (int iFile = 0; iFile < fArray.length; iFile++) {

			poolInfo.put(wieSchwer, schwierigkeitInfo);
		} // for (int iSchwierigkeit

		return poolInfo;
	}

	static private EnumMap<Schwierigkeit, TopfInfo> gibInfoEntnommene(DateiPool dateiPool) {
		EnumMap<Schwierigkeit, TopfInfo> poolInfo = new EnumMap<Schwierigkeit, TopfInfo>(Schwierigkeit.class);
		Schwierigkeit[] wieSchwerArray = Schwierigkeit.values();

		for (int iSchwierigkeit = 0; iSchwierigkeit < wieSchwerArray.length; iSchwierigkeit++) {
			Schwierigkeit wieSchwer = wieSchwerArray[iSchwierigkeit];
			String verzeichnisName = DateiPoolEntnahmeProtokoll.gibPfadName();
			File fVerzeichnis = new File(verzeichnisName);
			File[] fArray = fVerzeichnis.listFiles();
			TopfInfo schwierigkeitInfo = new TopfInfo();

			for (int iFile = 0; iFile < fArray.length; iFile++) {
				File file = fArray[iFile];
				String fname = file.getName();
				boolean istSudoku = Verzeichnis.istSudoku(fname);
				if (istSudoku) {
					boolean istGesucht = DateiPoolEntnahmeProtokoll.istGesucht(file, wieSchwer, null);
					if (istGesucht) {
						boolean istDoppel = fname.indexOf(" ") > 0;
						long fGroesse = file.length();
						int fLoesungsZeit = DateiPoolEntnahmeProtokoll.gibLoesungsZeit(fname);
						long fModified = file.lastModified();
						TopfInfo dateiInfo = new TopfInfo(1, istDoppel ? 1 : 0, fGroesse, fLoesungsZeit, fLoesungsZeit,
								fModified, fModified);
						schwierigkeitInfo.add(dateiInfo);
					}
				} // if (istSudoku){
			} // for (int iFile = 0; iFile < fArray.length; iFile++) {

			poolInfo.put(wieSchwer, schwierigkeitInfo);
		} // for (int iSchwierigkeit

		return poolInfo;
	}

	static PoolInfo gibPoolInfo(DateiPool dateiPool) {
		final EnumMap<Schwierigkeit, TopfInfo> verfuegbare = gibInfoVerfuegbare(dateiPool);
		final EnumMap<Schwierigkeit, TopfInfo> entnommene = gibInfoEntnommene(dateiPool);

		PoolInfo poolInfo = new PoolInfo(verfuegbare, entnommene);
		return poolInfo;
	}

}
