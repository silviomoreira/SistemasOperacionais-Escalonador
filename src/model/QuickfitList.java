package model;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;

public class QuickfitList extends MemoriaList {

	private final int requisicoes_Chao = 4; // valor min. p/ a partir da� construir a lista de requisi��es de mem�ria
	private int contadorRequisicoes = 0;
	private boolean bAtingiuChao = false;
	private RequisicaoMemoriaList requisicoesMemoria;
	private LinkedList<BlocoMemoria> listaBlocosLivres;
	private MemoriaList[] listaBlocos;
	 
	public QuickfitList(RequisicaoMemoriaList requisicoesMemoria) {
		super();
		if (requisicoes_Chao <= 4)
			try {
				throw new Exception("Valor m�n. de requisi��es inv�lido !");
			} catch (Exception e) {
				e.printStackTrace();
			}
		this.requisicoesMemoria = requisicoesMemoria; 
		listaBlocosLivres = new LinkedList<BlocoMemoria>();
		listaBlocos = new MemoriaList[4];
		listaBlocos[0] = new MemoriaList();
		listaBlocos[1] = new MemoriaList();
		listaBlocos[2] = new MemoriaList();
		listaBlocos[3] = new MemoriaList();
	}

	public void resetComplementar() {
		BlocoMemoria bm = new BlocoMemoria(0, 0, 0, null);
		bm.resetId();
		int contadorRequisicoes = 0;
		boolean bAtingiuChao = false;
		listaBlocosLivres = new LinkedList<BlocoMemoria>();
		listaBlocos = new MemoriaList[4];
		listaBlocos[0] = new MemoriaList();
		listaBlocos[1] = new MemoriaList();
		listaBlocos[2] = new MemoriaList();
		listaBlocos[3] = new MemoriaList();	
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
					removeDasListasBlocosLivres(getAll().get(i).getIdBloco(), tamanhoBloco); 
					return true;
				}
				else if (iPosBlocoMaior == -1) // guarda posi��o de bloco maior, acaso n�o encontre um igual ou 
					iPosBlocoMaior = i;		   // n�o possua mem. dispon�vel p/ alocar um igual			
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
			removeDasListasBlocosLivres(getAll().get(iPosBlocoMaior).getIdBloco(), 
					getAll().get(iPosBlocoMaior).getTamanho()); 
			return true;
		}
		return false;
	}

	/**
	 * @param i
	 * Objetivo: Exclui blocos livres das lista geral de blocos livres e das listas 0,1,2,3. Algoritmo: Varre a 
	 * lista de requisi��es, pelo campo numerolista, atr�s do bloco do tamanho passado por par�metro. Se encontrar(.1),
	 * exclui da lista indicada. Se n�o encontrar(.2), exclui da listaBlocosLivres. 
	 */
	private void removeDasListasBlocosLivres(int idBloco, int tamanhoBloco) {
		// ordena
//		Collections.sort(requisicoesMemoria.getAll()); // ver se � necess�rio, mas pode deixar lento
		// .1
		ListIterator<RequisicaoMemoria> liter = requisicoesMemoria.getAll().listIterator();
		int i = 0;
		while(i < 4 && liter.hasNext()) {
			RequisicaoMemoria r = liter.next();
			if (r.getTamanhoBloco() == tamanhoBloco) {
				ListIterator<BlocoMemoria> lite = listaBlocos[r.getNumeroLista()].getAll().listIterator();
				while(lite.hasNext()) {
					if (lite.next().getIdBloco() == idBloco) {
						lite.remove(); // PENDENCIA: TESTAR
						return;
					}
				}
			}									
			i++;
		} // Fim <while>
		// .2
		ListIterator<BlocoMemoria> liter2 = listaBlocosLivres.listIterator();
		while(liter2.hasNext()) {
			if (liter2.next().getIdBloco() == idBloco) { 
				liter2.remove(); // PENDENCIA: TESTAR
				return;
			}									
		} // Fim <while>
	}
	
	private void aloqueMemoria(int tamanhoBloco, int idProcesso) {
		// caso 2: aloca novo bloco com mesmo tamanho (aloca��o completa)
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
		requisicoesMemoria.add(tamanhoBloco);
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
			// montagem das listas (loop de 4) | a var. abaixo guarda os n�meros das listas q v�o permanecer inalteradas
			String sListasQuePermanecem = ""; 
			int i = 0;
			while(i < 4 && liter.hasNext()){
				RequisicaoMemoria r = liter.next();
				// Verificar se as 4 primeiras listas de blocos s�o de blocos do mesmo tamanho que os 4 1os. da
				// lista de requisi��es, da� guardo em uma string os que forem encontrados
				for (int c=0; c < listaBlocos.length; c++) {
					if (listaBlocos[c].size() > 0 && r.getTamanhoBloco() == listaBlocos[c].get(0).getTamanho())
						sListasQuePermanecem += String.valueOf(c)+",";					
				}
				i++;
			} // Fim <while>
			// Se todas as listas permanecem com os mesmos tamanhos de bloco, isto �, n�o houve mudan�a nas requisi��es 
			// priorit�rias, ent�o aborta
			if (sListasQuePermanecem.length() == 8) {
				return;
			}
			// Verifica se lista N vai mudar p/ uma lista de outro bloco, verificando se cada bloco na lista de 
			// requisi��es, n�o est� encaixado em nenhuma lista
			i = 0;
			while(i < 4 && liter.hasNext()){
				RequisicaoMemoria r = liter.next(); 
				// Verifica se lista N vai mudar p/ o bloco atual na lista de requisi��es
				if (r.getTamanhoBloco() != listaBlocos[0].get(0).getTamanho() &&
					r.getTamanhoBloco() != listaBlocos[1].get(0).getTamanho() &&
					r.getTamanhoBloco() != listaBlocos[2].get(0).getTamanho() &&
					r.getTamanhoBloco() != listaBlocos[3].get(0).getTamanho()) {
					if (verificaSeHaMemoriaDisponivelParaCriarEsteBloco(r.getTamanhoBloco())) { // ...
						int iNumeroListaQVaiMudar = veEmQualListaVaiGuardar(sListasQuePermanecem, i);
						if (iNumeroListaQVaiMudar != -1)
						{
							// Seta o n�mero da lista em q a requisi��o ser� guardada
							r.setNumeroLista(iNumeroListaQVaiMudar); // VER A QUEST�O DO -1 NAS LISTAS							
							// realoca --> VER se n�o vai ficar FORA do IFZ�O
							// ...
						}
					} else {
						// Sai em busca do proximo bloco da lista de requisi��es que mais teve requisi��es a partir do 5o registro
						int iTamanhoBloco = retornaTamanhoProxBlocoQTeveMaisRequisicoesPartindoDo5oRegistro(); // ...
						if (verificaSeHaMemoriaDisponivelParaCriarEsteBloco(r.getTamanhoBloco())) { // ...
							// V� em qual lista vai guardar
							
							// Seta o n�mero da lista em q a requisi��o ser� guardada
							
						}						
					}					
				} // Fim <if>
				i++;
			} // Fim <while>
			
		} // Fim <if (bAtingiuChao)>		
	}

	/**
	 * @param sListasQuePermanecem
	 * @param i
	 * @return
	 */
	private int veEmQualListaVaiGuardar(String sListasQuePermanecem, int i) {
		// Entra em loop em busca de uma lista que n�o est� mais entre as 4 primeiras e retorna o n�mero dela
		int j = i;
		int c = 0;
		int iNumeroListaQVaiMudar = -1;						
		while (c++ < 4 && iNumeroListaQVaiMudar == -1){
			if (!sListasQuePermanecem.contains( String.valueOf(j) ))
				iNumeroListaQVaiMudar = j;
			j++;
			// no caso em q o loop parte de valor intermedi�rio, garante q os 1os valores ser�o checados 
			if (j == 4)
				j = 0;
		}
		return iNumeroListaQVaiMudar;
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

	/*if (r.getTamanhoBloco() == listaBlocos1.element().getTamanho())
	r.setNumeroLista(1);
	if (r.getTamanhoBloco() == listaBlocos2.element().getTamanho())
		r.setNumeroLista(2);
	if (r.getTamanhoBloco() == listaBlocos3.element().getTamanho())
		r.setNumeroLista(3);
	if (r.getTamanhoBloco() == listaBlocos4.element().getTamanho())
		r.setNumeroLista(4);*/
} 
