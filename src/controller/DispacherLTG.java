package controller;

import gui.centerLayout.CenterPanel;
import gui.centerLayout.ProcessadoresTableModel;

import java.util.ArrayList;
import java.util.List;

import model.Processo;
import model.ProcessoList;

/*
 * <OK>Executar iniciarAlgoritmoLTG();  
 * <OK>Ao criar os processos na fila de aptos, calcular Tempo total e Deadline via Random()
 * ==> public Processo(String tempoTotalExecucao, 
				   String estadoProcesso, 
				   String tempoExecucaoRestante, 
				   int prioridade, 
				   String deadline, 
				   String intervalo)
 * <OK>Ajuste no encaixe dos parametros				   
 * <OK>Ordenar processos pelo deadline
 *  <OK>=> Testa inserção ordenada de novo processo com binarySearch(). E se existir mais de 2 deadlines repetidos. Campo Id(a conc.)
 * <OK>Decrementar TempoRestante na classe Processo
 * <OK-1>Decrementar Deadline (Dispatcher)
 * <OK-2>Quando o deadline do processo acabar, se o processo não estiver executando, matar o mesmo (Dispatcher)
 * <OK-3>Quando o processo acabar, remover da lista de processos executando e inserir na lista de concluídos, (Dispatcher)
 *   se for o último, mostra "core livre"
 *   senão, inserir outro processo em espera da fila de aptos 
 * <OK-4>Desenhar na Tela a execução dos processos e a lista de concluídos/abortados  (Dispatcher)
 * <OK-5>Criar table p/ os processos em execução
 * <OK-6>Criar table p/ processos concluídos/abortados
 */
	
public class DispacherLTG implements Runnable {

	private CenterPanel centerPanel;
	private List<Processo> processoList;
	////private ProcessoList processadoresList = new ProcessoList();	
	////private ProcessoList concluidosEAbortadosList = new ProcessoList();	
	private List<Processo> processadoresList;/////ProcessadoresList processadoresList; 	
	private List<Processo> concluidosEAbortadosList;
																	  	
	private int numProcessadores = 0;
	private int numProcessosIniciais = 0;

	private volatile boolean pare = false;
	
	/////public DispacherLTG(List<Processo> processoList, ProcessadoresList processadoresList, 
	public DispacherLTG(List<Processo> processoList, List<Processo> processadoresList,
			List<Processo> concluidosEAbortadosList,
			CenterPanel centerPanel, int numProcessadores, int numProcessosIniciais) {
		this.processoList = processoList;
		this.processadoresList = processadoresList;
		this.concluidosEAbortadosList = concluidosEAbortadosList;
		this.centerPanel = centerPanel;
		this.numProcessadores = numProcessadores;
		this.numProcessosIniciais = numProcessosIniciais;
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
		return processadoresList;////.getAll();
	}
    //// comentado
	/*public void setProcessadoresList(List<Processo> processadoresList) {
		this.processadoresList = processadoresList;
	}*/

	public List<Processo> getConcluidosEAbortadosList() {
		return concluidosEAbortadosList;
	}
	
	@Override
	public void run() {
		iniciarExecucaoDosProcessosLTG();		
	}

	public void iniciarExecucaoDosProcessosLTG()
	{
		aguardaEmMilisegundos(4000); // Possibilita dar uma olhada na lista de aptos antes de iniciar
		List<Thread> threadsList = new ArrayList<Thread>();
		// Loop de processadores starta cada processo na tela em cada processador(core)
		for(int i=0; i<numProcessadores; i++) {
			if (processoList.size() > 0) 
			{   
				aguardaEmMilisegundos(50); // Este tempo está bom ?
				// Exclui processo da lista de aptos e coloca na fila de processadores, startando-o
			    //// P/ Testes
				/*if (i==0){
 				Processo p = new Processo("10", "E", "10", 0, "5", "");
 				processadoresList.add(p);
 				atualizaTela();
				}*/
				processadoresList.add(processoList.get(0));
				processoList.remove(0); 
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
		}

		// Controle de mudança de processos
		while (!pare) {
			aguardaEmMilisegundos(1000);
			// - Decrementa deadline
			for(int i=0; i<processoList.size(); i++) {
				if (processoList.get(i).getEstadoProcesso() != "E"){
					processoList.get(i).decrementaDeadLine();
				}
			}
	    	atualizaTela();
			// - mata processos 
			for(int i=0; i<processoList.size(); i++) {
				if (processoList.get(i).getEstadoProcesso() == "A"){
					mataProcesso(i);
				}
			}
	    	atualizaTela(); 
			// Quando o processo acabar, remover da lista de processos executando,
			// se for o último, mostra "core livre"
			// senão, inserir outro processo em espera da fila de aptos
			for(int i=0; i<processadoresList.size(); i++) {
				if (processadoresList.get(i).getEstadoProcesso() == "OK"){
					// Insere na lista de concluídos
					concluidosEAbortadosList.add(processadoresList.get(i));
					mostraLogAbortadosConcluidos(concluidosEAbortadosList.size()-1); //				
					if (processoList.size() > 0) {
						// insere novo processo da lista de aptos p/ executar
						/*Processo p = new Processo(processoList.get(0).getTempoTotalExecucao(),
								"E", processoList.get(0).getTempoTotalExecucao(), 0, 
								processoList.get(0).getDeadline(), ""); // novo - não resolveu esta nova implementação de 2 linhas e retirada da linha abaixo
						
						processadoresList.set(i, p);*/
						processadoresList.set(i, processoList.get(0));
						processoList.remove(0); 
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
} 
