package model;

import javax.swing.JOptionPane;

public class BestfitList extends MemoriaList {
	
	public BestfitList() {
		super();
	}

	@Override
	public boolean alocouMemoria(int tamanhoBloco, int tamanhoMemoriaRestante) {
		int iPosBlocoMaior = -1;
		// busca bloco vazio de mesmo tamanho
		for (int i=0; i < getAll().size(); i++) {
			if (tamanhoBloco <= getAll().get(i).getTamanho() && getAll().get(i).getEspacoUsado() == 0) {
				if (tamanhoBloco == getAll().get(i).getTamanho())
					return true;
				else if (iPosBlocoMaior == -1) // guarda posição de bloco maior, acaso não encontre um igual ou 
					iPosBlocoMaior = i;		   // não possua mem. disponível p/ alocar um igual			
			}
		}
		if (tamanhoBloco <= tamanhoMemoriaRestante)
			return true;
		if (iPosBlocoMaior != -1)
			return true;
		return false;
	}
	
	private void aloqueMemoria(BlocoMemoria bm, int tamanho, boolean bNovo) {
		// caso 1: aloca bloco livre de mesmo tamanho
		// caso 2: aloca novo bloco com mesmo tamanho (alocação completa)
		// caso 3: aloca bloco livre maior
		if (bNovo){ // caso 2
			
		} else {    // caso 1 ou 3

		}
		/*tamanhoBloco = processoList.get(0).getQtdBytes(); 
		espacoUsado = tamanhoBloco;
		bMemoriaAlocada = false;
		if (tamanhoBloco <= memoriaObj.getRemainingMemorySize()) {
			BlocoMemoria bm = new BlocoMemoria(tamanhoBloco, espacoUsado, referenciaProxBloco);
			memoriaList.add(bm);
			memoriaObj.setRemainingMemorySize(memoriaObj.getRemainingMemorySize()-tamanhoBloco);
			bMemoriaAlocada = true;
		} else {
			JOptionPane.showMessageDialog(null, "Out of memory");
			processoList.get(0).setEstadoProcesso("A");
			mataProcesso(0);
		}*/
	}
}
