package model;

import java.util.ListIterator;

//import javax.swing.JOptionPane;

public class BestfitList extends MemoriaList {
	
	public BestfitList() {
		super();
	}

	@Override
	public boolean alocouMemoria(int tamanhoBloco, int tamanhoMemoriaRestante, int idProcesso) {
		// busca bloco ocupado pelo processo, qdo. devido ao quantum estaria bloqueado
		if (alocouMemoriaPeloProcesso(tamanhoBloco, idProcesso))
			return true;
		// busca bloco vazio de mesmo tamanho
		int iPosBlocoMaior = -1;
		for (int i=0; i < getAll().size(); i++) {
			if (tamanhoBloco <= getAll().get(i).getTamanho() && getAll().get(i).getEspacoUsado() == 0) {
				if (tamanhoBloco == getAll().get(i).getTamanho()) { // aloca bloco livre de mesmo tamanho
					aloqueMemoria(tamanhoBloco, idProcesso, i);
					return true;
				}
				else if (iPosBlocoMaior == -1) // guarda posição de bloco maior, acaso não encontre um igual ou 
					iPosBlocoMaior = i;		   // não possua mem. disponível p/ alocar um igual			
			}
		}
		if (tamanhoBloco <= tamanhoMemoriaRestante) { // aloca novo bloco com mesmo tamanho
			aloqueMemoria(tamanhoBloco, idProcesso);
			return true;
		}
		if (iPosBlocoMaior != -1) { // aloca bloco livre maior
			aloqueMemoria(tamanhoBloco, idProcesso, iPosBlocoMaior);
			return true;
		}
		return false;
	}
	
	private void aloqueMemoria(int tamanhoBloco, int idProcesso) {
		// caso 2: aloca novo bloco com mesmo tamanho (alocação completa)
		BlocoMemoria bm = new BlocoMemoria(tamanhoBloco, tamanhoBloco, idProcesso, null, 0);
		getAll().add(bm);
		this.setRemainingMemorySize(this.getRemainingMemorySize()-tamanhoBloco);
		this.setOccupiedMemorySize(this.getOccupiedMemorySize()+tamanhoBloco);
	}
	private void aloqueMemoria(int tamanhoBloco, int idProcesso, int i) {
		// caso 1: aloca bloco livre de mesmo tamanho
		// caso 3: aloca bloco livre maior
		getAll().get(i).setEspacoUsado(tamanhoBloco);	
		getAll().get(i).setIdProcesso(idProcesso);
		getAll().get(i).setIdLogicoBloco(getAll().get(i).getIdBloco());
		this.setOccupiedMemorySize(this.getOccupiedMemorySize()+tamanhoBloco);
	}

	public void liberaMemoria(int idProcesso) {
		BlocoMemoria b;
		ListIterator<BlocoMemoria> liter = getAll().listIterator();
		while(liter.hasNext()){
			b = liter.next();
			if (b.getIdProcesso() == idProcesso) {
				b.setEspacoUsado(0);
				b.setIdProcesso(0);
				this.setOccupiedMemorySize(this.getOccupiedMemorySize()-b.getTamanho());
				break;
			}
		}
	}
	
	public boolean alocouMemoriaPeloProcesso(int tamanhoBloco, int idProcesso) {
		BlocoMemoria b;
		ListIterator<BlocoMemoria> liter = getAll().listIterator();
		while(liter.hasNext()){
			b = liter.next();
			if (b.getIdProcesso() == idProcesso && b.getEspacoUsado() == 0) {
				b.setEspacoUsado(tamanhoBloco);	
				b.setIdLogicoBloco(b.getIdBloco());
				this.setOccupiedMemorySize(this.getOccupiedMemorySize()+tamanhoBloco);
				return true;
			}
		}		
		return false;
	}
	
	public boolean foiAlocadoPorMeioDoProcesso(int idProcesso) {
		BlocoMemoria b;
		ListIterator<BlocoMemoria> liter = getAll().listIterator();
		while(liter.hasNext()){
			b = liter.next();
			if (b.getIdProcesso() == idProcesso && b.getEspacoUsado() > 0) {
				return true;
			}
		}		
		return false;
	}
}
