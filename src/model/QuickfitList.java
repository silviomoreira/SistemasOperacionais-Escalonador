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
		if (requisicoes_Chao < 4)
			try {
				throw new Exception("Valor m�n. de requisi��es inv�lido !");
			} catch (Exception e) {
				e.printStackTrace();
			}
		this.requisicoesMemoria = requisicoesMemoria; 
		resetComplementar();
	}

	public void resetComplementar() {
		contadorRequisicoes = 0;
		bAtingiuChao = false;
		listaBlocosLivres = new LinkedList<BlocoMemoria>();
		listaBlocos = new MemoriaList[4];
		listaBlocos[0] = new MemoriaList();
		listaBlocos[1] = new MemoriaList();
		listaBlocos[2] = new MemoriaList();
		listaBlocos[3] = new MemoriaList();	
	}
	
	@Override
	/*public boolean alocouMemoria(int tamanhoBloco, int tamanhoMemoriaRestante, int idProcesso) {
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
	}*/
	/*
	 * 1) Varre os 4 1os. registros da lista de requisi��es p/ ver se o tamanho do bloco desejado est� l�.
	 * 2)   Se estiver,     pega o numero da lista no campo e vai � respectiva lista alocando o 1o. bloco da mesma.
	 * 3)   Se n�o estiver, varre a listaBlocosLivres em busca do bloco desejado. 
	 * 4)     Se encontrar aloca
	 * 5)     Se n�o encontrar, existindo mem�ria suficiente, aloca um novo bloco
	 * 6)       Se n�o existir mem. suficiente, realiza os passos do 1 ao 4 em busca de um bloco maior.
	 * 7)         Se encontrar aloca
	 * 8)         Se n�o encontrar, aborta com "out of memory"
	 */
	public boolean alocouMemoria(int tamanhoBloco, int tamanhoMemoriaRestante, int idProcesso) {
        // ...
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
		//Collections.sort(requisicoesMemoria.getAll()); // ver se � necess�rio, mas pode deixar lento
		// .1
		if (bAtingiuChao) {
			ListIterator<RequisicaoMemoria> liter = requisicoesMemoria.getAll().listIterator();
			ListIterator<BlocoMemoria> lite;
			int i = 0;
			while(i < 4 && liter.hasNext()) {
				RequisicaoMemoria r = liter.next();
				if (r.getTamanhoBloco() == tamanhoBloco) {
					lite = listaBlocos[r.getNumeroLista()].getAll().listIterator(); // DANDO ERRO AQUI POR CAUSA DO -1
					while(lite.hasNext()) {
						if (lite.next().getIdBloco() == idBloco) {
							lite.remove(); // PENDENCIA: TESTAR
							return;
						}
					}
				}									
				i++;
			} // Fim <while>
		}
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
			if (++contadorRequisicoes == requisicoes_Chao)
				bAtingiuChao = true;
		}
		else
			++contadorRequisicoes;
		RequisicaoMemoria rm = new RequisicaoMemoria(tamanhoBloco);
		requisicoesMemoria.add(tamanhoBloco);
		if (bAtingiuChao)
			monta4ListasBlocosLivres();
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
				listaBlocosLivres.add(b);
				break;
			}
		}
	}

	/*
	 * Monta 4 listas a partir do momento em q se atingiu o n�mero m�n. de requisi��es estabelecido em requisicoes_Chao 
	 */
	private void monta4ListasBlocosLivres() {		
		// Realiza backup dos valores de tamanhos de blocos antes de reordenar, se n�o for a 1a vez que monta
		int i = 0;
		ListIterator<RequisicaoMemoria> liter;
		RequisicaoMemoria[] listaRequisicoesBkp = new RequisicaoMemoria[4];
		if (contadorRequisicoes != requisicoes_Chao) {
			liter = requisicoesMemoria.getAll().listIterator();
			// Como vai estar ordenado inversamente pelo n�m. da lista + incid�ncia, s� precisa ler os 4 1os.
			while(i < 4 && liter.hasNext()){
				listaRequisicoesBkp[i] = liter.next();
				i++;
			}
		}
		// (re)ordena inversamente pela incid�ncia(ordena��o padr�o)
		// PENDENCIA ATUAL: SETAR BOLEANO PARA ORDENACAO C/ VAR. PUBLICA 
		//  requisicoesMemoria.get(0).setbOrdenacaoPadrao(true);
		Collections.sort(requisicoesMemoria.getAll());
		liter = requisicoesMemoria.getAll().listIterator();
		RequisicaoMemoria r;
		// Se for a 1a vez que monta
		if (contadorRequisicoes == requisicoes_Chao) {
			i = 0;
			while(i < 4 && liter.hasNext()){
				r = liter.next();
				montaListaBlocoMaisRequisitadoEExcluiDaGenerica(r.getTamanhoBloco(),i);
				r.setNumeroLista(i);
				i++;
			}
		} 
		else {	
			// P/ a remontagem das listas | a var. abaixo guarda os n�meros das listas q v�o permanecer inalteradas
			String sListasQuePermanecem = ""; 
			i = 0;
			while(i < 4 && liter.hasNext()){
				r = liter.next();
				// Verificar se as 4 primeiras listas de blocos s�o de blocos do mesmo tamanho que os 4 1os. da
				// lista de requisi��es, da� guardo em uma string os que forem encontrados
				for (int c=0; c < listaRequisicoesBkp.length; c++) {
					if (listaRequisicoesBkp[c] != null && r.getTamanhoBloco() == listaRequisicoesBkp[c].getTamanhoBloco())
						sListasQuePermanecem += String.valueOf(listaRequisicoesBkp[c].getNumeroLista())+",";					
				}
				i++;
			} // Fim <while>
			// Se todas as listas permanecem com os mesmos tamanhos de bloco, isto �, n�o houve mudan�a nas requisi��es 
			// priorit�rias, ent�o aborta
			if (sListasQuePermanecem.length() == 8) {
				// ordena inversamente pelo n�m. da lista + incidencia(ordena��o n�o padr�o)
				Collections.sort(requisicoesMemoria.getAll());
				return;
			}
			//
			// Verifica se lista N vai mudar p/ uma lista de outro bloco, verificando se cada bloco na lista de 
			// requisi��es, n�o est� encaixado em nenhuma lista
			liter = requisicoesMemoria.getAll().listIterator();
			int iTamanhoBloco;
			i = 0;
			while(liter.hasNext()){
				r = liter.next(); 
				// Verifica se lista N vai mudar p/ o bloco atual na lista de requisi��es
				if (r.getNumeroLista()  != -1)
				{
					// verif. os 4 mais requisitados
					if (++i == 5)
						break;
					if (r.getTamanhoBloco() != listaRequisicoesBkp[0].getTamanhoBloco() &&
						r.getTamanhoBloco() != listaRequisicoesBkp[1].getTamanhoBloco() &&
						r.getTamanhoBloco() != listaRequisicoesBkp[2].getTamanhoBloco() &&
						r.getTamanhoBloco() != listaRequisicoesBkp[3].getTamanhoBloco()) {
						boolean bPodeRealocar = false;
						if (verificaSeHaMemoriaDisponivelParaCriarEsteBloco(r.getTamanhoBloco())) { 
							iTamanhoBloco = r.getTamanhoBloco();
							bPodeRealocar = true;
						} else {
							// Sai em busca do proximo bloco da lista de requisi��es que mais teve requisi��es a partir do
							// 5o registro e q tenha mem. dispon�vel p/ criar
							iTamanhoBloco = retornaTamanhoProxBlocoQTeveMaisRequisicoesPartindoDo5oRegistro();
							bPodeRealocar = (iTamanhoBloco > 0);
						}
						if (bPodeRealocar) {
							int iNumeroListaQVaiMudar = veEmQualListaVaiGuardar(sListasQuePermanecem, i);
							if (iNumeroListaQVaiMudar != -1)
							{
								// Seta o n�mero da lista em q a requisi��o ser� guardada
								r.setNumeroLista(iNumeroListaQVaiMudar); // VER A QUEST�O DO -1 NAS LISTAS
								realocaBlocoNasListasBlocosLivres(iTamanhoBloco, iNumeroListaQVaiMudar, listaRequisicoesBkp);
							}
						}
					} // Fim <if>
				} // Fim <if (r.getNumeroLista()  != -1)>						
			} // Fim <while>
			Collections.sort(requisicoesMemoria.getAll());
			
		} // Fim <if (contadorRequisicoes == requisicoes_Chao)>		
	}

	/*
	// Insere blocos da lista q vai mudar na listaBlocosLivres, apaga a lista listaBlocos[iNumeroListaQVaiMudar]
	// Retira de listaBlocosLivres o novo bloco que vai constar entre os 4+ e coloca em listaBlocos[iNumeroListaQVaiMudar]
	// Exemplo: vamos supor que foi de 20b, cria-se uma lista com os blocos livres de 20b, e a lista de 16b � destru�da, 
	 * por�m antes, os blocos livres desta lista de 16b, s�o inseridos na lista geral de blocos livres
	 */
	private void realocaBlocoNasListasBlocosLivres(int iTamanhoBloco,
			int iNumeroListaQVaiMudar, RequisicaoMemoria[] listaRequisicoesBkp) {
		ListIterator<BlocoMemoria> literbm = listaBlocos[iNumeroListaQVaiMudar].getAll().listIterator();
		while (literbm.hasNext()) {
			listaBlocosLivres.add(literbm.next());
			literbm.remove();
		}
		
		montaListaBlocoMaisRequisitadoEExcluiDaGenerica(iTamanhoBloco,iNumeroListaQVaiMudar);		
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
		int iTamanhoBloco;
		ListIterator<RequisicaoMemoria> liter = requisicoesMemoria.getAll().listIterator(5);
		RequisicaoMemoria r;
		int i = 0;
		while(i < 4 && liter.hasNext()){
			r = liter.next();
			iTamanhoBloco = r.getTamanhoBloco();
			if (verificaSeHaMemoriaDisponivelParaCriarEsteBloco(iTamanhoBloco))
				return iTamanhoBloco;
			i++;
		}		
		return 0;
	}

	private boolean verificaSeHaMemoriaDisponivelParaCriarEsteBloco(
			int tamanhoBloco) {
		return (tamanhoBloco <= getRemainingMemorySize());
	}

	private void montaListaBlocoMaisRequisitadoEExcluiDaGenerica(int iTamanhoBloco,
			int iNumeroLista) {
		BlocoMemoria bm;
		ListIterator<BlocoMemoria> literbm = listaBlocosLivres.listIterator();
		while (literbm.hasNext()) {
			bm = literbm.next();
			if (bm.getTamanho() == iTamanhoBloco)
			{
				listaBlocos[iNumeroLista].add(bm);
				literbm.remove();
			}
		}
	}

} 
