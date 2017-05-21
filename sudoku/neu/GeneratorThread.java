package sudoku.neu;

import java.time.LocalDateTime;

import sudoku.neu.pool.Pool0;

class GeneratorThread extends Thread{
	private Pool0 pool;
	private sudoku.tools.AusnahmeBehandlung externeAusnahmeBehandlung;

	/**
	 * @param pool Der Pool, der die Sudokus verwaltet
	 * @param externeAusnahmeBehandlung falls != null wird im Thread 
	 * 				diese Ausnahmebehandlung f�r nicht gefangene Ausnahmen eingeklinkt, 
	 * 				ansonsten die Standardbehandlung des genannten Typs. 
	 */
	GeneratorThread(Pool0 pool, sudoku.tools.AusnahmeBehandlung externeAusnahmeBehandlung) {
		super(GeneratorThread.class.getName());
		this.pool = pool;
		this.externeAusnahmeBehandlung = externeAusnahmeBehandlung;
//		System.out.println("GeneratorThread: Ohne Start!!!");
		this.start();
	}

	@Override
	public void run() {
		if (externeAusnahmeBehandlung != null){
			externeAusnahmeBehandlung.einklinken();
		}
		else{
			sudoku.tools.AusnahmeBehandlung ausnahmeBehandlung = new sudoku.tools.AusnahmeBehandlung();
			ausnahmeBehandlung.einklinken();
		}
        int speicherMinute = LocalDateTime.now().getMinute();

//        int i = 0;
//		if (i == 0){
//			throw new UnerwarteterInhalt("Hallo Exception - Test im GeneratorThread");
//		}
		
		while (! this.isInterrupted()){
			NeuTyp forderung = this.pool.gibForderung();
			long sleepTime = 100;
			
			if (forderung != null){
				sleepTime = 10;
				Generator.GeneratorErgebnis neues = Generator.gibNeuesSudoku(forderung);
				Boolean istImPool = null;
				if (neues.infoSudoku != null){
					istImPool = this.pool.setze(neues.neuTyp, neues.infoSudoku, neues.loesungsZeit);
				}
				if (SudokuPool.generatorStatistik != null){
					String topfName = pool.gibTopfName(forderung);
					SudokuPool.generatorStatistik.neuesSudoku(neues.neuTyp, neues.infoSudoku, istImPool, neues.loesungsZeit, topfName);
				}
			}
			
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				// Dann soll wohl der Thread beenden
				break;
			}
			
	        LocalDateTime now = LocalDateTime.now();
	        int jetztMinute = now.getMinute();
	        if (speicherMinute != jetztMinute){
	        	speicherMinute = jetztMinute;
	        	Generator.logErfolgreicheLogiken();
	        }
	        
		} // while (! this.isInterrupted()){
	}
}
