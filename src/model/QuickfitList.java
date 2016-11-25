package model;

import java.util.LinkedList;
import java.util.ArrayList;

public class QuickfitList extends MemoriaList {

	private int requisicoes = 5; 
	private ArrayList<RequisicaoMemoria> requisicoesMemoria;
	private LinkedList<BlocoMemoria> listaBlocosLivres;
	private LinkedList<BlocoMemoria> listaBlocos0;
	private LinkedList<BlocoMemoria> listaBlocos1;
	private LinkedList<BlocoMemoria> listaBlocos2;
	private LinkedList<BlocoMemoria> listaBlocos3;

	public QuickfitList() {
		super();
		requisicoesMemoria = new ArrayList<>(); 
		listaBlocosLivres = new LinkedList<>();
		listaBlocos0 = new LinkedList<>();
		listaBlocos1 = new LinkedList<>();
		listaBlocos2 = new LinkedList<>();
		listaBlocos3 = new LinkedList<>();
	}

	@Override
	public boolean alocouMemoria(int tamanhoBloco, int tamanhoMemoriaRestante, int idProcesso) {
		int iPosBlocoMaior = -1;
		// busca bloco vazio de mesmo tamanho
		for (int i=0; i < getAll().size(); i++) {
			if (tamanhoBloco <= getAll().get(i).getTamanho() && getAll().get(i).getEspacoUsado() == 0) {
				if (tamanhoBloco == getAll().get(i).getTamanho()) { // aloca bloco livre de mesmo tamanho
					aloqueMemoria(tamanhoBloco, idProcesso, i);
					guardaRequisicoes(tamanhoBloco);
					return true;
				}
				else if (iPosBlocoMaior == -1) // guarda posição de bloco maior, acaso não encontre um igual ou 
					iPosBlocoMaior = i;		   // não possua mem. disponível p/ alocar um igual			
			}
		}
		if (tamanhoBloco <= tamanhoMemoriaRestante) { // aloca novo bloco com mesmo tamanho
			aloqueMemoria(tamanhoBloco, idProcesso);
			guardaRequisicoes(tamanhoBloco);
			return true;
		}
		if (iPosBlocoMaior != -1) { // aloca bloco livre maior
			aloqueMemoria(tamanhoBloco, idProcesso, iPosBlocoMaior);
			guardaRequisicoes(tamanhoBloco);
			return true;
		}
		return false;
	}
	
	private void aloqueMemoria(int tamanhoBloco, int idProcesso) {
		// caso 2: aloca novo bloco com mesmo tamanho (alocação completa)
		BlocoMemoria bm = new BlocoMemoria(tamanhoBloco, tamanhoBloco, idProcesso, null);
		getAll().add(bm);
		this.setRemainingMemorySize(this.getRemainingMemorySize()-tamanhoBloco);
	}
	private void aloqueMemoria(int tamanhoBloco, int idProcesso, int i) {
		// caso 1: aloca bloco livre de mesmo tamanho
		// caso 3: aloca bloco livre maior
		getAll().get(i).setEspacoUsado(tamanhoBloco);	
		getAll().get(i).setIdProcesso(idProcesso);		
	}
	
	private void guardaRequisicoes(int tamanhoBloco) {
		RequisicaoMemoria rm = new RequisicaoMemoria();
		rm.setTamanhoBloco(tamanhoBloco);
		requisicoesMemoria.add(rm);
	}
} 
