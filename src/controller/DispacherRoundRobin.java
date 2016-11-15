package controller;

import gui.centerLayout.CenterPanel;
import gui.centerLayout.ListProcessoPanel;

import java.util.ArrayList;
import java.util.List;

import model.Processo;
import model.ProcessoList;

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
 * <-3> Desenvolver if de troca de processos se "B" ou "OK". 
 *     - Se "B", volta p/ a fila de prioridades dela e pega o processo da fila seguinte que tiver processos a escalonar.
 *     - Se "OK", apenas pega o processo da fila seguinte que tiver processos a escalonar.   
 * -4) Ver ordenação por Prioridade+Id na listProcesso e onde ordenar.
 * <O-5> Desenvolver removeProcessoDaFilaDeAptos(), p/ remover da fila de prioridades específica(filaprioNList) 
 *     e da listProcesso, após buscar Id.       
 * <-6> Desenvolver addProcessoNaFilaDeAptos(), p/ incluir no final da fila de prioridades específica(filaprioNList) 
 *     e na listProcesso, em determinada posição, ordenada pelos campos Prioridade+Id.
 *  => PAREI em retornaProcessoBloqueadoParaAFilaDePrioridades() ...        
 */

public class DispacherRoundRobin implements Runnable {

	private CenterPanel centerPanel;
	private List<Processo> processoList;
	private List<Processo> processadoresList; 	
	private List<Processo> concluidosEAbortadosList;
	/*private List<Processo> filaprio0List;
	private List<Processo> filaprio1List;
	private List<Processo> filaprio2List;
	private List<Processo> filaprio3List;*/
	/*private ProcessoList filaprio0List = new ProcessoList();
	private ProcessoList filaprio1List = new ProcessoList();
	private ProcessoList filaprio2List = new ProcessoList();
	private ProcessoList filaprio3List = new ProcessoList();*/
	private List<Integer> filaprio0List;
	private List<Integer> filaprio1List;
	private List<Integer> filaprio2List;
	private List<Integer> filaprio3List;
		
	private int numProcessadores = 0;
	private int numProcessosIniciais = 0;
	private float quantum0 = 0;
	private float quantum1 = 0;
	private float quantum2 = 0;
	private float quantum3 = 0;	
	private int iPrioridadeDaVez = 0;
	
	private volatile boolean pare = false;
	
	public DispacherRoundRobin(List<Processo> processoList, List<Processo> processadoresList,
			List<Processo> concluidosEAbortadosList,
			CenterPanel centerPanel, int numProcessadores, int numProcessosIniciais, float quantum0) {
		this.processoList = processoList;
		this.processadoresList = processadoresList;
		this.concluidosEAbortadosList = concluidosEAbortadosList;
		this.centerPanel = centerPanel;
		this.numProcessadores = numProcessadores;
		this.numProcessosIniciais = numProcessosIniciais;
		this.quantum0 = quantum0;
		// Distribui processos(o id deles) entre as filas de prioridade
		int iPrioridade;
		for(int i=0; i<numProcessosIniciais; i++) {
			iPrioridade = processoList.get(i).getPrioridade();
			switch (iPrioridade) {
			case 0:
				filaprio0List.add(processoList.get(i).getIdentificadorProcesso());
				break;
			case 1:
				filaprio1List.add(processoList.get(i).getIdentificadorProcesso());
				quantum1 = quantum0/2; // PENDENCIA: DEBUGAR quantum
				break;
			case 2:
				filaprio2List.add(processoList.get(i).getIdentificadorProcesso());
				quantum2 = quantum0/3;
				break;
			case 3:
				filaprio3List.add(processoList.get(i).getIdentificadorProcesso());
				quantum3 = quantum0/4;
				break;
			}					
		}
	}

	public CenterPanel getCenterPanel() {
		return centerPanel;
	}

	public void setCenterPanel(CenterPanel centerPanel) {
		this.centerPanel = centerPanel;
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
	
	@Override
	public void run() {
		iniciarExecucaoDosProcessosRR();		
	}

	public void iniciarExecucaoDosProcessosRR()
	{
		aguardaEmMilisegundos(4000); // Possibilita dar uma olhada na lista de aptos antes de iniciar
		List<Thread> threadsList = new ArrayList<Thread>();
		int iPrioridadeDaVez = 0;
		int idProcesso;
		int iPosProcesso;
		// Loop de processadores starta cada processo na tela em cada processador(core)
		for(int i=0; i<numProcessadores; i++) {
			if (processoList.size() > 0) //if (filaprio0List.size() > 0 || filaprio1List.size() > 0 || filaprio2List.size() > 0 || filaprio3List.size() > 0) 
			{   
				aguardaEmMilisegundos(50); // Este tempo está bom ?
				// Exclui processo da lista de aptos e coloca na fila de processadores, startando-o
			    //// P/ Testes
				/*if (i==0){
 				Processo p = new Processo("10", "E", "10", 0, "5", "");
 				processadoresList.add(p);
 				atualizaTela();
				}*/
				idProcesso = excluiProcessoDaProximaFilaDePrioridadeERetornaIdDoMesmo();
				// Retorna posição do processo em processoList buscando pelo id
				iPosProcesso = retornaPosicaoProcesso(idProcesso);
				processadoresList.add(processoList.get(iPosProcesso));
				processoList.remove(iPosProcesso); 
				processadoresList.get(i).setEstadoProcesso("E");
		    	atualizaTela();
				Thread thread = new Thread(processadoresList.get(i));
				threadsList.add(thread);
				threadsList.get(i).start();
			}
			else {
				// Imprime "core livre" nos cores restantes
				Processo p = new Processo(0, "0", "CORE_LIVRE", "0", 0, "0", "");
				processadoresList.add(p);
			}
			mostraLogProcessadores(i); //
		} // Fim <for>
		
		// Controle de mudança de processos
		while (!pare) {
			aguardaEmMilisegundos(1000);
			// - Decrementa quantum
			for(int i=0; i<processadoresList.size(); i++) {
				if (processadoresList.get(i).getEstadoProcesso() == "E"){
					processadoresList.get(i).decrementaQuantum();
				}
			}
	    	atualizaTela();
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
				if (processadoresList.get(i).getEstadoProcesso() == "OK"){
					// Insere na lista de concluídos
					concluidosEAbortadosList.add(processadoresList.get(i));
					mostraLogAbortadosConcluidos(concluidosEAbortadosList.size()-1); //				
					if (processoList.size() > 0) {
						idProcesso = excluiProcessoDaProximaFilaDePrioridadeERetornaIdDoMesmo();
						// Retorna posição do processo em processoList buscando pelo id
						iPosProcesso = retornaPosicaoProcesso(idProcesso);
						processadoresList.set(i, processoList.get(iPosProcesso));
						processoList.remove(iPosProcesso); 
						processadoresList.get(i).setEstadoProcesso("E");
				    	atualizaTela();
						Thread thread = new Thread(processadoresList.get(i));
						threadsList.add(thread);				
						threadsList.get(threadsList.size()-1).start();
					} else
					{
						// libera core
						Processo p = new Processo(0, "0", "CORE_LIVRE", "0", 0, "0", "");
						processadoresList.set(i, p);
					}
					mostraLogProcessadores(i); //
				} else  	
				if (processadoresList.get(i).getEstadoProcesso() == "B"){
					//retornaProcessoBloqueadoParaAFilaDePrioridades(i);
					//escalonaProximoProcesso();
				}					
			}
	    	atualizaTela();
	    	
	    	// Testa condição de saída do Dispacher
	    	if (processoList.size() == 0 && !HaAlgumProcessoEmExecucao()){
	    		pare = true;
	    		System.out.println("< ESCALONADOR TERMINOU ! >"); System.out.println();
	    	}
		} // Fim <while>
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
					" | Deadline: "+processadoresList.get(i).getDeadline());
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
						iPrioridadeDaVez = 0; // ?
						break;
					} else
						iPrioridadeDaVez = 0; // ?
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

    private void retornaProcessoBloqueadoParaAFilaDePrioridades(int iProcessador) {
    	
    }
}
