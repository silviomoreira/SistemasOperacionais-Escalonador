package model;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;

public class QuickfitList extends MemoriaList {

	private int requisicoes_Chao = 1; // valor min. p/ a partir daí construir a lista de requisições de memória
	private int contadorRequisicoes = 0;
	private boolean bAtingiuChao = false;
	private RequisicaoMemoriaList requisicoesMemoria;
	private LinkedList<BlocoMemoria> listaBlocosLivres;
	private LinkedList<BlocoMemoria> listaBlocos1;
	private LinkedList<BlocoMemoria> listaBlocos2;
	private LinkedList<BlocoMemoria> listaBlocos3;
	private LinkedList<BlocoMemoria> listaBlocos4;
	private MemoriaList[] listaBlocos;
	 
	public QuickfitList(RequisicaoMemoriaList requisicoesMemoria) {
		super();
		if (requisicoes_Chao == 0)
			try {
				throw new Exception("Valor mín. de requisições inválido !");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		this.requisicoesMemoria = requisicoesMemoria; 
		listaBlocosLivres = new LinkedList<BlocoMemoria>();
		listaBlocos = new MemoriaList[4];
		listaBlocos[0] = new MemoriaList();
		listaBlocos[1] = new MemoriaList();
		listaBlocos[2] = new MemoriaList();
		listaBlocos[3] = new MemoriaList();
		/*listaBlocos1 = new LinkedList<BlocoMemoria>();
		listaBlocos2 = new LinkedList<BlocoMemoria>();
		listaBlocos3 = new LinkedList<BlocoMemoria>();
		listaBlocos4 = new LinkedList<BlocoMemoria>();*/
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
		if (!bAtingiuChao) {
			if (++contadorRequisicoes == requisicoes_Chao){
				bAtingiuChao = true;
			}
			else
				return;
		}
		RequisicaoMemoria rm = new RequisicaoMemoria(tamanhoBloco);
		requisicoesMemoria.add(rm);
		if (requisicoesMemoria.size() > 100)
			requisicoesMemoria.remove(0);
	}
	
	public void liberaMemoria(int idProcesso) {
		BlocoMemoria b;
		ListIterator<BlocoMemoria> liter = getAll().listIterator();
		while(liter.hasNext()){
			b = liter.next();
			if (b.getIdProcesso() == idProcesso) {
				b.setEspacoUsado(0);
				b.setIdProcesso(0);
				carregaListaBlocosLivres(b);
				break;
			}
		}
	}

	private void carregaListaBlocosLivres(BlocoMemoria b) {
		listaBlocosLivres.add(b);
		
		// Monta 4 listas (PENDENCIA: ver BREVE possibilidade de thread ou de ficar no loop do dispacher)
		if (bAtingiuChao){
			// ordena
			Collections.sort(requisicoesMemoria.getAll());
			ListIterator<RequisicaoMemoria> liter = requisicoesMemoria.getAll().listIterator();
			// montagem das listas (loop de 4)
			int i = 0;
			while(++i <= 4 && liter.hasNext()){
				RequisicaoMemoria r = liter.next();
				// Verificar se as 4 primeiras listas de blocos são de blocos do mesmo tamanho que os 4 1os. da
				// lista de requisições, daí seto o número da lista 
				for (int c=0; c < listaBlocos.length; c++) {
					if (listaBlocos[c].size() > 0 && r.getTamanhoBloco() == listaBlocos[c].get(0).getTamanho())
						r.setNumeroLista(c+1);					
				}
				/*if (r.getTamanhoBloco() == listaBlocos1.element().getTamanho())
					r.setNumeroLista(1);
				if (r.getTamanhoBloco() == listaBlocos2.element().getTamanho())
					r.setNumeroLista(2);
				if (r.getTamanhoBloco() == listaBlocos3.element().getTamanho())
					r.setNumeroLista(3);
				if (r.getTamanhoBloco() == listaBlocos4.element().getTamanho())
					r.setNumeroLista(4);*/
			} // Fim <while>
			/*i = 0;
			while(++i <= 4 && liter.hasNext()){
				RequisicaoMemoria r = liter.next(); 
				// Verifica se lista N vai mudar p/ uma lista de outro bloco
				if (r.getTamanhoBloco() != listaBlocos0.element().getTamanho() &&
					r.getTamanhoBloco() != listaBlocos1.element().getTamanho() &&
					r.getTamanhoBloco() != listaBlocos2.element().getTamanho() &&
					r.getTamanhoBloco() != listaBlocos3.element().getTamanho()) {
					if (verificaSeHaMemoriaDisponivelParaCriarEsteBloco(r.getTamanhoBloco())) {
						// Vê em qual lista vai guardar
						
						// Seta o número da lista em q a requisição está guardada
						
					} else {
						// Sai em busca do proximo bloco da lista de requisições que mais teve requisições a partir do 5o registro
						int iTamanhoBloco = retornaTamanhoProxBlocoQTeveMaisRequisicoesPartindoDo5oRegistro();
					}					
				} // Fim <if>
			} // Fim <while>
			*/
		} // Fim <if (bAtingiuChao)>		
	}

	private int retornaTamanhoProxBlocoQTeveMaisRequisicoesPartindoDo5oRegistro() {
		// TODO Auto-generated method stub
		return 0;
	}

	private boolean verificaSeHaMemoriaDisponivelParaCriarEsteBloco(
			int tamanhoBloco) {
		// TODO Auto-generated method stub
		return false;
	}

} 
