package controller;

import gui.bottomLayout.BottomPanel;
import gui.centerLayout.CenterPanel;
import gui.rightLayout.RightPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JOptionPane;

import model.BlocoMemoria;
import model.MemoriaHDList;
import model.MemoriaList;
import model.Processo;
import model.RequisicaoMemoria;
import model.RequisicaoMemoriaList;

/*
   Como funciona: Os processos da fila de aptos rodam a cada tempo de quantum, q não pode ser muito pequeno, porque 
   deixará o pc lento, e nem muito grande, p/ não prejudicar os processos pequenos. Quando o processo vai rodar, o 
   mesmo sai da fila de aptos e vai p/ a fila de cores. Após passado o tempo de quantum p/ este processo, se ele ainda 
   não houver terminado, volta p/ o final da fila de aptos e fica aguardando a próx. vez de rodar.

 - FILA DE PRIORIDADES COM ROUND ROBIN
   - Seja Q o quantum  

   F A S E  1
   ----------
   Cores -> | | | | |
   Prio0 -> |P0|P1|P2| | | | | | 
   Prio1 -> |P3|P4|P5| | | | | | 
   Prio2 -> |P6|P7| | | | | | | 
   Prio3 -> |P8| | | | | | | | 

   F A S E  2
   ----------
   Cores -> |P0|P3|P6|P8|
   Prio0 -> |  |P1|P2| | | | | |   (roda em Q)
   Prio1 -> |  |P4|P5| | | | | |   (roda em Q/2)
   Prio2 -> |  |P7| | | | | | |    (roda em Q/3)
   Prio3 -> |  | | | | | | | |     (roda em Q/4)

   Como funciona: Do mesmo jeito do anterior, só q pega 1 processo de cada fila de prioridade p/ rodar no tempo de quantum 
   ref. àquela fila de prioridade. O pc entra em um loop de leitura p/ ler as filas de prioridades e,  
   ao terminar um processo, o processador pega o processo da fila corrente(próxima), de acordo com a var. de loop.
   
 * Criar laço onde se testará o quantum de cada processo, conforme a prioridade e preparará o processo p/ ser 
 *   interrompido pelo método (interrupt() ou stop() mesmo) ou setando o estado do processo p/ "B" (bloqueado).
 * <OK-1> Criar decrementaQuantum().
 * <OK-2> Criar método excluiProcessoDaProximaFilaDePrioridadeERetornaIdDoMesmo(). 
 * <OK-3> Desenvolver if de troca de processos se "B" ou "OK". 
 *     - Se "B", volta p/ a fila de prioridades dela e pega o processo da fila seguinte que tiver processos a escalonar.
 *     - Se "OK", apenas pega o processo da fila seguinte que tiver processos a escalonar.   
 * <OK-4> Desenvolver remoção de Processo Da Fila De Aptos, p/ remover da fila de prioridades específica(filaprioNList) 
 *     e da listProcesso, após buscar Id.       
 * <OK-5> Desenvolver adição de Processo na Fila De Aptos, p/ incluir no final da fila de prioridades específica(filaprioNList) 
 *     e na listProcesso, em determinada posição, ordenada pelos campos Prioridade+Id.
 * <OK-6> Ver detalhes sobre a parada do processo bloqueado e como reestartá-lo p/ continuar a
 *     contagem do tempo a partir do TempoRestante.
 * <O-7> Ver ordenação por Prioridade+Id na listProcesso e onde ordenar<OK>. 
 *     Fazer para a adição de novo processo tbém<>. 
 * <OK-8> Quantum
 *      -<OK> Ver campo Quantum na grid. 
 *      -<OK-Em teste> Ver retomada do quantum pelo valor inicial ao reiniciar processo. 
 *      -<OK> Ver melhor local onde tratar o bloqueio do processo. Nao esta desbloqueando processo.
 * <OK-9> Suspender ordenação pela F.P. e tornar semelhante ao LTG.     
 *  => PAREI em ...
 */

public class DispacherRoundRobin implements Runnable {
	private CenterPanel centerPanel;
	private List<Processo> processoList;
	private List<Processo> processadoresList; 	
	private List<Processo> concluidosEAbortadosList;
	private List<BlocoMemoria> memoriaList;
	private MemoriaList memoriaObj;
	private MemoriaHDList memoriaHDObj;
//	private List<RequisicaoMemoria> requisicaoMemoriaList;//
//	private RequisicaoMemoriaList requisicaoMemoriaObj;//
	/*private List<Processo> filaprio0List;
	private List<Processo> filaprio1List;
	private List<Processo> filaprio2List;
	private List<Processo> filaprio3List;*/
	/*private ProcessoList filaprio0List = new ProcessoList();
	private ProcessoList filaprio1List = new ProcessoList();
	private ProcessoList filaprio2List = new ProcessoList();
	private ProcessoList filaprio3List = new ProcessoList();*/
	private List<Integer> filaprio0List = new ArrayList<Integer>();
	private List<Integer> filaprio1List = new ArrayList<Integer>();
	private List<Integer> filaprio2List = new ArrayList<Integer>();
	private List<Integer> filaprio3List = new ArrayList<Integer>();
		
	private int numProcessadores = 0;
	private int numProcessosIniciais = 0;
	private int quantum0 = 0;
	private int quantum1 = 0;
	private int quantum2 = 0;
	private int quantum3 = 0;	
	private int iPrioridadeDaVez = 0;
	private BottomPanel bottomPanel;  
	private int tamanhoMemoria = 0;
	private boolean bMostraMemoria = false;
	private boolean bAtivaLog;
	private RightPanel rightPanel;
	
	private volatile boolean pare = false;
	
	public DispacherRoundRobin(List<Processo> processoList, List<Processo> processadoresList,
			List<Processo> concluidosEAbortadosList,
			CenterPanel centerPanel, int numProcessadores, int numProcessosIniciais, int quantum0,
			BottomPanel bottomPanel, int tamanhoMem, List<BlocoMemoria> memoriaList, MemoriaList memoriaObj,
			RightPanel rightPanel, MemoriaHDList memoriaHDObj/*, List<RequisicaoMemoria> requisicaoMemoriaList, 
			RequisicaoMemoriaList requisicaoMemoriaObj*/) {
		this.processoList = processoList;
		this.processadoresList = processadoresList;
		this.concluidosEAbortadosList = concluidosEAbortadosList;
		this.centerPanel = centerPanel;
		this.numProcessadores = numProcessadores;
		this.numProcessosIniciais = numProcessosIniciais;
		this.quantum0 = quantum0;
		this.bottomPanel = bottomPanel;
		this.tamanhoMemoria = tamanhoMem;
		this.bMostraMemoria = (this.tamanhoMemoria> 0);
		this.memoriaList = memoriaList;
		this.memoriaObj = memoriaObj;
		this.rightPanel = rightPanel;
		this.memoriaHDObj = memoriaHDObj;
		//this.requisicaoMemoriaList = requisicaoMemoriaList;//
		//this.requisicaoMemoriaObj = requisicaoMemoriaObj;//
		this.bAtivaLog = processoList.get(0).isAtivalog();
		// Distribui processos(o id deles) entre as filas de prioridade
		int iPrioridade;
		for(int i=0; i<numProcessosIniciais; i++) {
			iPrioridade = processoList.get(i).getPrioridade();
			switch (iPrioridade) {
			case 0:
				filaprio0List.add(processoList.get(i).getIdentificadorProcesso());
				processoList.get(i).setQuantum(quantum0);
				processoList.get(i).setQuantumInicial(quantum0);
				break;
			case 1:
				filaprio1List.add(processoList.get(i).getIdentificadorProcesso());
				quantum1 = Math.round(quantum0/2);
				processoList.get(i).setQuantum(quantum1);
				processoList.get(i).setQuantumInicial(quantum1);
				break;
			case 2:
				filaprio2List.add(processoList.get(i).getIdentificadorProcesso());
				quantum2 = Math.round(quantum0/3);
				processoList.get(i).setQuantum(quantum2);				
				processoList.get(i).setQuantumInicial(quantum2);
				break;
			case 3:
				filaprio3List.add(processoList.get(i).getIdentificadorProcesso());
				quantum3 = Math.round(quantum0/4);
				processoList.get(i).setQuantum(quantum3);
				processoList.get(i).setQuantumInicial(quantum3);
				break;
			}					
		}
	}

	public CenterPanel getCenterPanel() {
		return centerPanel;
	}

	public List<Processo> getProcessoList() {
		return processoList;
	}

	public void setProcessoList(List<Processo> processoList) {
		this.processoList = processoList;
	}

	public int getNumProcessadores() {
		return numProcessadores;
	}

	public void setNumProcessadores(int numProcessadores) {
		this.numProcessadores = numProcessadores;
	}

	public int getNumProcessosIniciais() {
		return numProcessosIniciais;
	}

	public void setNumProcessosIniciais(int numProcessosIniciais) {
		this.numProcessosIniciais = numProcessosIniciais;
	}

	public List<Processo> getProcessadoresList() {
		return processadoresList;
	}

	public List<Processo> getConcluidosEAbortadosList() {
		return concluidosEAbortadosList;
	}
	
	public BottomPanel getBottomPanel() {
		return bottomPanel;
	}

	public List<BlocoMemoria> getMemoriaList() {
		return memoriaList;
	}
	
/*	public List<RequisicaoMemoria> getRequisicaoMemoriaList() {
		return requisicaoMemoriaList;
	}
*/	
	@Override
	public void run() {
		iniciarExecucaoDosProcessosRR();		
	}

	public void iniciarExecucaoDosProcessosRR()
	{
		aguardaEmMilisegundos(4000); // Possibilita dar uma olhada na lista de aptos antes de iniciar
		List<Thread> threadsList = new ArrayList<Thread>();
		String statusProcessoNoProcessador; 
		memoriaObj.setRemainingMemorySize(tamanhoMemoria);
		boolean bMemoriaAlocada = true;
		// Loop de processadores starta cada processo na tela em cada processador(core)
		for(int i=0; i<numProcessadores; i++) {
			if (processoList.size() > 0) //if (filaprio0List.size() > 0 || filaprio1List.size() > 0 || filaprio2List.size() > 0 || filaprio3List.size() > 0) 
			{   
				aguardaEmMilisegundos(50); 
				// AlocaMem
				if (bMostraMemoria) {
					bMemoriaAlocada = tentaAlocarMemoria(i, bMemoriaAlocada, true);
				}
				if (bMemoriaAlocada) {
					// Exclui processo da lista de aptos e coloca na fila de processadores, startando-o
					//idProcesso = excluiProcessoDaProximaFilaDePrioridadeERetornaIdDoMesmo();
					// Retorna posição do processo em processoList buscando pelo id
					//iPosProcesso = retornaPosicaoProcesso(idProcesso);
					processadoresList.add(processoList.get(0));//processadoresList.add(processoList.get(iPosProcesso));
					processoList.remove(0);//processoList.remove(iPosProcesso); 
					processadoresList.get(i).setEstadoProcesso("E");
			    	atualizaTela();
					Thread thread = new Thread(processadoresList.get(i));
					threadsList.add(thread);
					threadsList.get(threadsList.size()-1).start(); // get(i).start();
				}
			}
			else {
				// Imprime "core livre" nos cores restantes
				Processo p = new Processo(0, "0", "CORE_LIVRE", "0", 0, "0", "");
				processadoresList.add(p);
			}
			if (bMemoriaAlocada)
				mostraLogProcessadores(i); //
		} // Fim <for>
		
		// Controle de mudança de processos
		while (!pare) {
			aguardaEmMilisegundos(1000);
			// - Swap
			//memoriaHDObj.swapHDMemoria(processoList, memoriaObj.getRemainingMemorySize(), memoriaObj);
			memoriaHDObj.swapMemoriaHD(processoList, memoriaObj.getRemainingMemorySize(), memoriaObj);
			atualizaTela();
			// - Decrementa quantum
			for(int i=0; i<processadoresList.size(); i++) {
				if (processadoresList.get(i).getEstadoProcesso() == "E"){
					processadoresList.get(i).decrementaQuantum();
				}
			}
	    	atualizaTela();
	    	// Ver a possibilidade de realizar o tratamento do processo bloqueado logo
	    	// aqui, ou em Processo.DecrementaTempoRestante(), qdo. já finalizaria a execução do processo 
	    	// e setaria o estado p/ "B" lá. Foi realizado lá, pois aqui não funcionou bem.
	    	// ...
	    	// - mata processos (no Round Robin, ocorre apenas no caso de "Out of Memory"(gerado pelo Algoritmo de memória)) 
			for(int i=0; i<processoList.size(); i++) {
				if (processoList.get(i).getEstadoProcesso() == "A"){
					mataProcesso(i);
				}
			}
	    	atualizaTela(); 
			// Quando o processo acabar, remover da lista de processos executando(processadores),
			// se for o último, mostra "core livre"
			// senão, inserir outro processo em espera da fila de aptos da seguinte forma:
	    	// * - Se status="B", volta p/ a fila de prioridades dela e pega o processo da fila seguinte que tiver processos a escalonar.
	    	// * - Se status="OK", apenas pega o processo da fila seguinte que tiver processos a escalonar.   
			for(int i=0; i<processadoresList.size(); i++) {
				bMemoriaAlocada = true;
				statusProcessoNoProcessador = processadoresList.get(i).getEstadoProcesso();
				if (statusProcessoNoProcessador == "OK" || statusProcessoNoProcessador == "CORE_LIVRE" ){
					if (statusProcessoNoProcessador == "OK") {
						// Insere na lista de concluídos
						boolean bFoiIncluido = false;
						for(int k=0; k<concluidosEAbortadosList.size(); k++) {
							bFoiIncluido = (processadoresList.get(i).getIdentificadorProcesso() == concluidosEAbortadosList.get(k).getIdentificadorProcesso());
							if (bFoiIncluido)
								break;
						}
						if (!bFoiIncluido) {
							concluidosEAbortadosList.add(processadoresList.get(i));
							mostraLogAbortadosConcluidos(concluidosEAbortadosList.size()-1); //
						}
					}
					memoriaObj.liberaMemoria(processadoresList.get(i).getIdentificadorProcesso());
					atualizaTela();
					if (processoList.size() > 0) {
						bMemoriaAlocada = tentaAlocarMemoria(i, bMemoriaAlocada, false);
						if (bMemoriaAlocada) { 
							// escalona Proximo Processo
							//idProcesso = excluiProcessoDaProximaFilaDePrioridadeERetornaIdDoMesmo();
							// Retorna posição do processo em processoList buscando pelo id
							//iPosProcesso = retornaPosicaoProcesso(idProcesso);
							processadoresList.set(i, processoList.get(0));//processadoresList.set(i, processoList.get(iPosProcesso));
							processoList.remove(0);//processoList.remove(iPosProcesso); 
							processadoresList.get(i).setEstadoProcesso("E");
					    	atualizaTela();
							Thread thread = new Thread(processadoresList.get(i));
							threadsList.add(thread);				
							threadsList.get(threadsList.size()-1).start();
						}
					} else
					{
						// libera core
						Processo p = new Processo(0, "0", "CORE_LIVRE", "0", 0, "0", "");
						processadoresList.set(i, p);
					}
					if (bMemoriaAlocada)
						mostraLogProcessadores(i); 
				} else  	
				if (processadoresList.get(i).getEstadoProcesso() == "B"){
					//recolocaProcessoBloqueadoNaFilaDePrioridades(i);
					processoList.add(processadoresList.get(i)); //recolocaProcessoBloqueadoNalistaDeProcessosGeral(i);
					// ? .... TEM de dar vazão p/ os processos começarem a rodar e continuar
					// a contagem do quantum
				//**	memoriaObj.liberaMemoria(processadoresList.get(i).getIdentificadorProcesso());
					if (processoList.size() > 0) {
						bMemoriaAlocada = tentaAlocarMemoria(i, bMemoriaAlocada, false);
						if (bMemoriaAlocada) {
							// escalona Proximo Processo // escalonaProximoProcesso();
							//idProcesso = excluiProcessoDaProximaFilaDePrioridadeERetornaIdDoMesmo();
							// Retorna posição do processo em processoList buscando pelo id
							//iPosProcesso = retornaPosicaoProcesso(idProcesso);
							processadoresList.set(i, processoList.get(0));//processadoresList.set(i, processoList.get(iPosProcesso));
							processoList.remove(0);//processoList.remove(iPosProcesso); 
							processadoresList.get(i).setEstadoProcesso("E");
					    	atualizaTela();
							Thread thread = new Thread(processadoresList.get(i));
							threadsList.add(thread);				
							threadsList.get(threadsList.size()-1).start();
						}
					} else
					{
						// libera core
						Processo p = new Processo(0, "0", "CORE_LIVRE", "0", 0, "0", "");
						processadoresList.set(i, p);
					}
					if (bMemoriaAlocada)
						mostraLogProcessadores(i);		
				} // Fim <if geral>					
			} // Fim <for>
	    	atualizaTela();
	    	
	    	// Testa condição de saída do Dispacher
	    	if (processoList.size() == 0 && !HaAlgumProcessoEmExecucao()){
	    		pare = true;
	    		System.out.println("< ESCALONADOR TERMINOU ! >"); System.out.println();
	    	}
		} // Fim <while>
	}

	/**
	 * @param bMemoriaAlocada
	 * @return
	 */
	private boolean tentaAlocarMemoria(int i, boolean bMemoriaAlocada, boolean bCargaInicial) {
		if (bMostraMemoria) {
			bMemoriaAlocada = memoriaObj.alocouMemoria(processoList.get(0).getQtdBytes(), 
							 						   memoriaObj.getRemainingMemorySize(),
							 						   processoList.get(0).getIdentificadorProcesso());		
			if (bMemoriaAlocada) {
				bottomPanel.refreshConsole("O processo "+processoList.get(0).getIdentificadorProcesso()+
						" alocou "+processoList.get(0).getQtdBytes()+
						"b e ficaram "+memoriaObj.getRemainingMemorySize()+" bytes disponíveis");
			} else {
				JOptionPane.showMessageDialog(null,  "Out of memory ! ) Solicitado: "+processoList.get(0).getQtdBytes()+" | Remanescente: "+ memoriaObj.getRemainingMemorySize());//"Out of memory");
				mostraLogMemoria(0);
				processoList.get(0).setEstadoProcesso("A");
				mataProcesso(0);
				Processo p = new Processo(0, "0", "CORE_LIVRE", "0", 0, "0", "");
				if (bCargaInicial)
					processadoresList.add(p);
				else
					processadoresList.set(i, p);	
				atualizaTela();
			}
		}
		return bMemoriaAlocada;
	}

	/**
	 * @param i - posição(index) na lista
	 */
	private void mostraLogProcessadores(int i) {
		if (!processadoresList.get(i).isAtivalog())
			return;
		if (processadoresList.get(i).getEstadoProcesso() != "CORE_LIVRE")
		{
			System.out.println("Id startado: "+processadoresList.get(i).getIdentificadorProcesso()+
					" | T. total "+processadoresList.get(i).getTempoTotalExecucao()+
					" | T. restante: "+processadoresList.get(i).getTempoExecucaoRestante()+
					" | Core "+i+
					" | Quantum: "+processadoresList.get(i).getQuantumInicial());
		} else {
			System.out.println("Core "+i+" livre");
		}
	}
	private void mostraLogAbortadosConcluidos(int i) {
		if (concluidosEAbortadosList.get(i).isAtivalog())
			System.out.println("Id abortado/concluído: "+concluidosEAbortadosList.get(i).getIdentificadorProcesso()+
					" | T. total "+concluidosEAbortadosList.get(i).getTempoTotalExecucao()+
					" | T. restante: "+concluidosEAbortadosList.get(i).getTempoExecucaoRestante()+
					" | Status: "+concluidosEAbortadosList.get(i).getEstadoProcesso());
	}
	private void mostraLogMemoria(int i) {
		String msg = "Id processo abortado p/ falta de mem.: "+processoList.get(i).getIdentificadorProcesso()+
			" | Mem. requisitada: "+processoList.get(i).getQtdBytes()+
			" | Mem. restante: "+memoriaObj.getRemainingMemorySize();
		if (bAtivaLog && bMostraMemoria) {
			System.out.println(msg);		
		}
		bottomPanel.refreshConsole(msg);		
	}
	
	private boolean HaAlgumProcessoEmExecucao() {
		boolean bRetorno = false;
		for(int i=0; i<processadoresList.size(); i++) {
			if (processadoresList.get(i).getEstadoProcesso() != "CORE_LIVRE") {
				bRetorno = true;
				break;
			}				
		}
		return (bRetorno);
	}
	
	/**
	 * Atualiza lista de aptos, cores e concluídos/abortados
	 */
	private void atualizaTela() {
		synchronized (this) {
			centerPanel.refreshProcessos();
			centerPanel.refreshProcessadores();
			centerPanel.refreshConcluidosEAbortados();
			bottomPanel.refreshMemoria();
			bottomPanel.refreshMemoriaHD();
			rightPanel.refreshRequisicoesMemoria();
		}
	}
	
	private void aguardaEmMilisegundos(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Insere na lista de processos abortados/concluídos e Exclui da lista de aptos 
	 */
	private void mataProcesso(int index){
		concluidosEAbortadosList.add(processoList.get(index));
		processoList.remove(index);
		mostraLogAbortadosConcluidos(concluidosEAbortadosList.size()-1); //
	}

	private int excluiProcessoDaProximaFilaDePrioridadeERetornaIdDoMesmo() { 
		int idProcesso = 0;
		while (idProcesso == 0) {
			switch (iPrioridadeDaVez) {
				case 0:
					if (filaprio0List.size() > 0) {
						idProcesso = filaprio0List.get(0);							
						filaprio0List.remove(0); 
						iPrioridadeDaVez++;
						break;
					} else
						iPrioridadeDaVez++;
				case 1:
					if (filaprio1List.size() > 0) {
						idProcesso = filaprio1List.get(0);							
						filaprio1List.remove(0); 
						iPrioridadeDaVez++;
						break;
					} else
						iPrioridadeDaVez++;
				case 2:
					if (filaprio2List.size() > 0) {
						idProcesso = filaprio2List.get(0);							
						filaprio2List.remove(0);
						iPrioridadeDaVez++;
						break;
					} else
						iPrioridadeDaVez++;
				case 3:
					if (filaprio3List.size() > 0) {
						idProcesso = filaprio3List.get(0);							
						filaprio3List.remove(0); 
						iPrioridadeDaVez = 0; 
						break;
					} else
						iPrioridadeDaVez = 0; 
			}
		} // Fim <while>
		return idProcesso;
	}
	
	private int retornaPosicaoProcesso(int idProcesso) {
		int iPos = -1;
		for (int i = 0; i < processoList.size(); i++) {
			if (processoList.get(i).getIdentificadorProcesso() == idProcesso)
				iPos = i;
		}
		return iPos;
	}

    private void recolocaProcessoBloqueadoNaFilaDePrioridades(int iProcessador) {
		switch (processadoresList.get(iProcessador).getPrioridade()) {
			case 0:
				filaprio0List.add(processadoresList.get(iProcessador).getIdentificadorProcesso());
				break;
			case 1:
				filaprio1List.add(processadoresList.get(iProcessador).getIdentificadorProcesso());
				break;
			case 2:
				filaprio2List.add(processadoresList.get(iProcessador).getIdentificadorProcesso());
				break;
			case 3:
				filaprio3List.add(processadoresList.get(iProcessador).getIdentificadorProcesso());
				break;
		}    	
    }
    
    private void recolocaProcessoBloqueadoNalistaDeProcessosGeral(int iProcessador) {
    	if (processoList.size() > 0) {
	    	int iPrioridade = processadoresList.get(iProcessador).getPrioridade();
	    	if (iPrioridade < 3){
	    		// busca adicionar antes do próximo processo, que equivale ao 1o. processo que
	    		// tiver a próxima prioridade
	    		int iPosicaoProcesso = -1;
	        	int iProxPrioridade = iPrioridade+1;
	        	// Realiza uma recursividade em busca de filas de prioridades preenchidas
	        	while (iProxPrioridade < 4 && iPosicaoProcesso == -1) {
	        		iPosicaoProcesso = retornaPosicaoDoUltimoProcessoDaFila(iProxPrioridade);
	        		iProxPrioridade++;
	        	}
	        	// -> É impossível acontecer a condição seguinte se o algoritmo estiver correto
	        	if (iPosicaoProcesso == -1 && processadoresList.get(iProcessador).isAtivalog())
	        		System.out.println("Verifique o algoritmo: recolocaProcessoBloqueadoNalistaDeProcessosGeral()");
	    		for (int i = 0; i < processoList.size(); i++) {
	    			if (processoList.get(i).getPrioridade() == iProxPrioridade) {
	    				processoList.add(iPosicaoProcesso, processadoresList.get(iProcessador));
	    			}    				
	    		}
	    	} else if (iPrioridade == 3) {
	    		processoList.add(processadoresList.get(iProcessador));
	    	}
    	} else {
    		processoList.add(processadoresList.get(iProcessador));    		
    	}
    }
    
	/**
	 * Para a lista de processos ordenada por Prioridade+IdProcesso, retorna a posição do
	 * último processo da fila específica para que o processo seja inserido após o último 
	 */
    private int retornaPosicaoDoUltimoProcessoDaFila(int iProxPrioridade) {
    	int iPosicao = -1;
		switch (iProxPrioridade) {
			case 0:
				if (filaprio0List.size() > 0) 
					iPosicao = retornaPosicaoProcesso(filaprio0List.get(0));							
				break;			
			case 1:
				if (filaprio1List.size() > 0) 
					iPosicao = retornaPosicaoProcesso(filaprio1List.get(0));			
				break;
			case 2:
				if (filaprio2List.size() > 0) 
					iPosicao = retornaPosicaoProcesso(filaprio2List.get(0));							
				break;
			case 3:
				if (filaprio3List.size() > 0)
					iPosicao = retornaPosicaoProcesso(filaprio3List.get(0));							
				break;
		}    	
    	return iPosicao-1;
    }
}
