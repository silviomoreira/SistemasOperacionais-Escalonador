package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class QuickfitList extends MemoriaList {

	private final int requisicoes_Chao = 4; // valor min. p/ a partir daí construir a lista de requisições de memória
	private int contadorRequisicoes = 0;
	private boolean bAtingiuChao = false;
	private RequisicaoMemoriaList requisicoesMemoria;
	private LinkedList<BlocoMemoria> listaBlocosLivres;
	private MemoriaList[] listaBlocos;
	private int iProxRegistroASerVerificado; 
	 
	public QuickfitList(RequisicaoMemoriaList requisicoesMemoria) {
		super();
		if (requisicoes_Chao < 4)
			try {
				throw new Exception("Valor mín. de requisições inválido !");
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
	/*
	 * 1) Varre os 4 1os. registros da lista de requisições p/ ver se o tamanho do bloco desejado está lá.
	 * 2)   Se estiver,     pega o numero da lista no campo e vai à respectiva lista alocando o 1o. bloco da mesma.
	 * 3)   Se não estiver, varre a listaBlocosLivres em busca do bloco desejado. 
	 * 4)     Se encontrar aloca
	 * 5)     Se não encontrar, existindo memória suficiente, aloca um novo bloco
	 * 6)       Se não existir mem. suficiente, realiza os passos do 1 ao 4 em busca de um bloco maior.
	 * 7)         Se encontrar aloca
	 * 8)         Se não encontrar, aborta com "out of memory"
	 */
	public boolean alocouMemoria(int tamanhoBloco, int tamanhoMemoriaRestante, int idProcesso) {
		synchronized (this) {
		boolean bRetorno = false;
		ListIterator<RequisicaoMemoria> liter;
		ListIterator<BlocoMemoria> lite2;
		RequisicaoMemoria r;
		BlocoMemoria b;
		int i;
		int idBloco;
		
		if (foiAlocadoPorMeioDoProcesso(idProcesso)) {
			guardaRequisicoes(tamanhoBloco);
			return true;
		}
		
		// 1...4) busca bloco vazio de mesmo tamanho
		if (bAtingiuChao) {
			liter = requisicoesMemoria.getAll().listIterator();
			i = 0;
			while(i < 4 && liter.hasNext()) {
				r = liter.next();
				if (tamanhoBloco == r.getTamanhoBloco()) {
					// procura em uma das 4 listas
					if (listaBlocos[r.getNumeroLista()].size() > 0) {
						System.out.println("=>X1 Achou na lista o bloco de "+tamanhoBloco);
						idBloco = listaBlocos[r.getNumeroLista()].get(0).getIdBloco();
						if (buscouEAlocouMemoria(tamanhoBloco, idProcesso, idBloco, String.valueOf(r.getNumeroLista()))) {
							listaBlocos[r.getNumeroLista()].remove(0);
							System.out.println("=> Alocou...");
							bRetorno = true;
							break;
						}
    				}
					else {
						break;
					}
				} // Fim <if (r.getTamanhoBloco() == tamanhoBloco)>									
				i++;
			} // Fim <while>
		} // Fim <if (bAtingiuChao)> 
		if (!bRetorno) {
			// procura na lista genérica
			lite2 = listaBlocosLivres.listIterator();
			while(lite2.hasNext()) {
				b = lite2.next();
				if (tamanhoBloco == b.getTamanho()) {
					System.out.println("=>X2 Achou na lista o bloco de "+tamanhoBloco);
					idBloco = b.getIdBloco();
					if (buscouEAlocouMemoria(tamanhoBloco, idProcesso, idBloco, "G")) {
						lite2.remove(); 
						System.out.println("=> Alocou...");
						bRetorno = true;
						break;
					}
				} // Fim <if (b.getTamanho() == tamanhoBloco)>									
			} // Fim <while>
		}
		 
		// aloca novo bloco com mesmo tamanho
		if (!bRetorno && tamanhoBloco <= tamanhoMemoriaRestante) { 
			aloqueMemoria(tamanhoBloco, idProcesso);
			guardaRequisicoes(tamanhoBloco);
			return true;
		}

		// 6...7) aloca bloco livre maior
		if (!bRetorno && bAtingiuChao) {
			liter = requisicoesMemoria.getAll().listIterator();
			i = 0;
			while(i < 4 && liter.hasNext()) {
				r = liter.next();
				if (tamanhoBloco <= r.getTamanhoBloco()) {
					// procura em uma das 4 listas
					if (listaBlocos[r.getNumeroLista()].size() > 0) {
						System.out.println("=>Y1 Busca alocar "+tamanhoBloco+". Achou na lista o bloco maior de "+r.getTamanhoBloco());
						idBloco = listaBlocos[r.getNumeroLista()].get(0).getIdBloco();
						if (buscouEAlocouMemoria(tamanhoBloco, idProcesso, idBloco, String.valueOf(r.getNumeroLista()))) {
							listaBlocos[r.getNumeroLista()].remove(0);
							System.out.println("=> Alocou...");
							bRetorno = true;
							break;
						}
    				}
					else {
						break;
					}
				} // Fim <if (tamanhoBloco <= r.getTamanhoBloco())>									
				i++;
			} // Fim <while>
		} // Fim <if (!bRetorno && bAtingiuChao)> 
        if (!bRetorno) {
		    // procura na lista genérica
			lite2 = listaBlocosLivres.listIterator();
			while(lite2.hasNext()) {
				b = lite2.next();
				if (tamanhoBloco <= b.getTamanho()) {
					System.out.println("=>Y2 Busca alocar "+tamanhoBloco+". Achou na lista o bloco maior de "+b.getTamanho());
					idBloco = b.getIdBloco();
					if (buscouEAlocouMemoria(tamanhoBloco, idProcesso, idBloco, "G")) {
						lite2.remove(); 
						System.out.println("=> Alocou...");
						bRetorno = true;
						break;
					}
				} // Fim <if (tamanhoBloco <= b.getTamanho())>									
			} // Fim <while>
		}
		guardaRequisicoes(tamanhoBloco);
		// 8) aborta
		return bRetorno; 
		} // Fim (synchronized)
	}

	/**
	 * @param tamanhoBloco
	 * @param idProcesso
	 * @param r
	 * @param idBloco
	 * @param sQualALista 
	 */
	private boolean buscouEAlocouMemoria(int tamanhoBloco, int idProcesso, int idBloco, String sQualALista) {
		ListIterator<BlocoMemoria> lite = getAll().listIterator(); // lista q representa a memória
		BlocoMemoria b;
		int n = 0;
		while(lite.hasNext()) {
			b = lite.next();
			if (b.getIdBloco() == idBloco) {
				aloqueMemoria(tamanhoBloco, idProcesso, n);
				b.setListaDeOrigem(sQualALista);
				return true;
			}
			n++;
		}
		return false;
	}

	
	/**
	 * @param i
	 * Objetivo: Exclui blocos livres das lista geral de blocos livres e das listas 0,1,2,3. Algoritmo: Varre a 
	 * lista de requisições, pelo campo numerolista, atrás do bloco do tamanho passado por parâmetro. Se encontrar(.1),
	 * exclui da lista indicada. Se não encontrar(.2), exclui da listaBlocosLivres. 
	 */
	private void removeDasListasBlocosLivres(int idBloco, int tamanhoBloco) {
		// ordena
		//Collections.sort(requisicoesMemoria.getAll()); // ver se é necessário, mas pode deixar lento
		// .1
		if (bAtingiuChao) {
			ListIterator<RequisicaoMemoria> liter = requisicoesMemoria.getAll().listIterator();
			ListIterator<BlocoMemoria> lite;
			int i = 0;
			while(i < 4 && liter.hasNext()) {
				RequisicaoMemoria r = liter.next();
				if (r.getTamanhoBloco() == tamanhoBloco) {
					lite = listaBlocos[r.getNumeroLista()].getAll().listIterator(); // Except no -1
					while(lite.hasNext()) {
						if (lite.next().getIdBloco() == idBloco) {
							lite.remove(); 
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
				liter2.remove(); 
				return;
			}									
		} // Fim <while>
	}
	
	private void aloqueMemoria(int tamanhoBloco, int idProcesso) {
		// caso 2: aloca novo bloco com mesmo tamanho (alocação completa)
		BlocoMemoria bm = new BlocoMemoria(tamanhoBloco, tamanhoBloco, idProcesso, null, 0);
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
		requisicoesMemoria.add(tamanhoBloco);
		if (bAtingiuChao || (contadorRequisicoes % requisicoes_Chao == 0))
			monta4ListasBlocosLivres();
		/*if (requisicoesMemoria.size() > 100)
			requisicoesMemoria.remove(0);*/
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
	 * Monta 4 listas a partir do momento em q se atingiu o número mín. de requisições estabelecido em requisicoes_Chao 
	 */
	private void monta4ListasBlocosLivres() {		
		// Realiza backup dos valores de tamanhos de blocos antes de reordenar, se não for a 1a vez que monta
		int i = 0;
		ListIterator<RequisicaoMemoria> liter;
		RequisicaoMemoria[] listaRequisicoesBkp = new RequisicaoMemoria[4];
		if (contadorRequisicoes != requisicoes_Chao) {
			liter = requisicoesMemoria.getAll().listIterator();
			// Como vai estar ordenado inversamente pelo núm. da lista + incidência(não padrão), só precisa ler os 4 1os.
			while(i < 4 && liter.hasNext()){
				listaRequisicoesBkp[i] = liter.next();
				i++;
			}
		}
		// (re)ordena inversamente pela incidência(ordenação padrão)
		requisicoesMemoria.bOrdenacaoPadrao = true;
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
			// P/ a remontagem das listas | a var. abaixo guarda os números das listas q vão permanecer inalteradas
			String sListasQuePermanecem = "";
			//ArrayList<Integer> aTamanhosBlocosQNaoPermanecem = new ArrayList<>();
			i = 0;
			while(i < 4 && liter.hasNext()){
				r = liter.next();
				// Verificar se as 4 primeiras listas de blocos são de blocos do mesmo tamanho que os 4 1os. da
				// lista de requisições, daí guardo em uma string os que forem encontrados
				for (int c=0; c < listaRequisicoesBkp.length; c++) {
					if (listaRequisicoesBkp[c] != null && r.getTamanhoBloco() == listaRequisicoesBkp[c].getTamanhoBloco())
						sListasQuePermanecem += String.valueOf(listaRequisicoesBkp[c].getNumeroLista())+",";
				}
				i++;
			} // Fim <while>
			// Guarda em uma lista os blocos q serão reinseridos na listaBlocoslivres, pois não fazem mais parte dos 4+
			/*for (int c=0; c < listaRequisicoesBkp.length; c++) {
				if (listaRequisicoesBkp[c] != null && 
						!sListasQuePermanecem.contains(String.valueOf(listaRequisicoesBkp[c].getNumeroLista())))
					aTamanhosBlocosQNaoPermanecem.add(listaRequisicoesBkp[c].getTamanhoBloco());
			}*/
			// Se todas as listas permanecem com os mesmos tamanhos de bloco, isto é, não houve mudança nas requisições 
			// prioritárias, então aborta
			if (sListasQuePermanecem.length() == 8) {
				// ordena inversamente pelo núm. da lista + incidencia(ordenação não padrão)
				System.out.println("==>listas permanecem sem alterações: "+sListasQuePermanecem); 
				requisicoesMemoria.bOrdenacaoPadrao = false;
				Collections.sort(requisicoesMemoria.getAll());
				return;
			}
			System.out.println("==>verificando se listas serão alteradas...");
			// Verifica se lista N vai mudar p/ uma lista de outro bloco, verificando se cada bloco na lista de 
			// requisições, não está encaixado em nenhuma lista
			liter = requisicoesMemoria.getAll().listIterator();
			int iTamanhoBloco;
			i = 0;
			iProxRegistroASerVerificado = 5;
			RequisicaoMemoria rProxima = null;
			while(i < 4 && liter.hasNext()){
				r = liter.next(); 
				// Verifica se lista N vai mudar p/ o bloco atual na lista de requisições
				if (r.getTamanhoBloco() != listaRequisicoesBkp[0].getTamanhoBloco() &&
					r.getTamanhoBloco() != listaRequisicoesBkp[1].getTamanhoBloco() &&
					r.getTamanhoBloco() != listaRequisicoesBkp[2].getTamanhoBloco() &&
					r.getTamanhoBloco() != listaRequisicoesBkp[3].getTamanhoBloco()) {
					System.out.println("==>verificando bloco tamanho: "+r.getTamanhoBloco()+" de incidencia: "+r.getIncidencia());
					boolean bPodeRealocar = false;
					boolean bBlocoPos5oRegistro = false; 
					if (verificaSeHaMemoriaDisponivelParaCriarEsteBloco(r.getTamanhoBloco())) {
						System.out.println("==>Teve memória disponível para ele...");
						iTamanhoBloco = r.getTamanhoBloco();
						bPodeRealocar = true;
					} else {
						// Sai em busca do proximo bloco da lista de requisições que mais teve requisições a partir do
						// 5o registro e q tenha mem. disponível p/ criar
						System.out.println("==>Não Teve memória disponível para ele. Buscando após o 5o reg...");
						rProxima = retornaRequisicaoProxBlocoQTeveMaisRequisicoesPartindoDo5oRegistro();
						iTamanhoBloco = rProxima == null ? 0 : rProxima.getTamanhoBloco();
						bPodeRealocar = (iTamanhoBloco > 0);
						bBlocoPos5oRegistro = true;
					}
					if (bPodeRealocar) {
						System.out.println("==>Pode realocar...");
						int iNumeroListaQVaiMudar = veEmQualListaVaiGuardar(sListasQuePermanecem);//, i);
						if (iNumeroListaQVaiMudar != -1)
						{
							// Seta p/ -1 o "número da lista" do bloco ref. à lista que vai mudar
							apagaNumerodaListaDoBlocoAnterior(iNumeroListaQVaiMudar);
							System.out.println("==>lista será refeita: "+iNumeroListaQVaiMudar+" com bloco de "+iTamanhoBloco+"b");
							realocaBlocoNasListasBlocosLivres(iTamanhoBloco, iNumeroListaQVaiMudar);
							// Seta o número da lista em q a requisição será guardada
							if (bBlocoPos5oRegistro)
								rProxima.setNumeroLista(iNumeroListaQVaiMudar);
							else
								r.setNumeroLista(iNumeroListaQVaiMudar);
						}
					} else {
						System.out.println("==>Não foi possível realocar...");
					}
				}	
				else {
					// realimenta listaBlocos dos 4+ q vao permanecer
					if (r.getNumeroLista() != -1)
						montaListaBlocoMaisRequisitadoEExcluiDaGenerica(r.getTamanhoBloco(),r.getNumeroLista());
					else
						System.out.println("Infelizmente não deu p/ realimentar a lista do bloco de "+r.getTamanhoBloco());
				} // Fim <if>
				i++;
			} // Fim <while>
		} // Fim <if (contadorRequisicoes == requisicoes_Chao)>		
		requisicoesMemoria.bOrdenacaoPadrao = false;
		Collections.sort(requisicoesMemoria.getAll());
	}

	private void apagaNumerodaListaDoBlocoAnterior(int iNumeroListaQVaiMudar) {
		ListIterator<RequisicaoMemoria> liter = requisicoesMemoria.getAll().listIterator();
		RequisicaoMemoria r;
		while(liter.hasNext()){
			r = liter.next();			
			if (iNumeroListaQVaiMudar == r.getNumeroLista()) {
				r.setNumeroLista(-1);
				break;
			}
		}		
	}

	/*
	// Insere blocos da lista q vai mudar na listaBlocosLivres, apaga a lista listaBlocos[iNumeroListaQVaiMudar]
	// Retira de listaBlocosLivres o novo bloco que vai constar entre os 4+ e coloca em listaBlocos[iNumeroListaQVaiMudar]
	// Exemplo: vamos supor que foi de 20b, cria-se uma lista com os blocos livres de 20b, e a lista de 16b é destruída, 
	 * porém antes, os blocos livres desta lista de 16b, são inseridos na lista geral de blocos livres
	 */
	private void realocaBlocoNasListasBlocosLivres(int iTamanhoBloco, int iNumeroListaQVaiMudar) {
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
	private int veEmQualListaVaiGuardar(String sListasQuePermanecem) {//, int i) {
		// Entra em loop em busca de uma lista que não está mais entre as 4 primeiras e retorna o número dela
		int c = 0;
		int iNumeroListaQVaiMudar = -1;						
		while (c < 4 && iNumeroListaQVaiMudar == -1){
			if (!sListasQuePermanecem.contains( String.valueOf(c) ))
				iNumeroListaQVaiMudar = c;
			c++;
		}
		return iNumeroListaQVaiMudar;
	}
	
	private RequisicaoMemoria retornaRequisicaoProxBlocoQTeveMaisRequisicoesPartindoDo5oRegistro() { 
		int iTamanhoBloco;
		ListIterator<RequisicaoMemoria> liter = requisicoesMemoria.getAll().listIterator(iProxRegistroASerVerificado);
		RequisicaoMemoria r;
		while(liter.hasNext()){
			r = liter.next();
			iTamanhoBloco = r.getTamanhoBloco();
			if (verificaSeHaMemoriaDisponivelParaCriarEsteBloco(iTamanhoBloco)) {
				iProxRegistroASerVerificado++;
				return r;
			}
			iProxRegistroASerVerificado++;
		}		
		return null;
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
