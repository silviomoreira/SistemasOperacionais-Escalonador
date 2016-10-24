package controller;

import java.util.ArrayList;
import java.util.List;

import gui.centerLayout.CenterPanel;
import gui.leftLayout.LeftPanel;
import gui.topLayout.TopPanelEvent;
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
 * <4>Desenhar na Tela a execução dos processos e a lista de concluídos/abortados  (Dispatcher)
 * <5>Criar table p/ os processos em execução
 * <6>Criar table p/ processos concluídos/abortados
 */	
	
public class DispacherLTG implements Runnable {

	private CenterPanel centerPanel;
	private List<Processo> processoList;
	private ProcessoList processadoresList = new ProcessoList();	
	private ProcessoList abortadosConcluidosList = new ProcessoList();	
	private int numProcessadores = 0;
	private int numProcessosIniciais = 0;

	private volatile boolean pare = false;
	
	public DispacherLTG(List<Processo> processoList, 
			CenterPanel centerPanel, int numProcessadores, int numProcessosIniciais) {
		this.processoList = processoList;
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

	@Override
	public void run() {
		iniciarExecucaoDosProcessosLTG();		
	}

	public void iniciarExecucaoDosProcessosLTG()
	{
		aguardaEmMilisegundos(3000);
		List<Thread> threadsList = new ArrayList<Thread>();
		// Loop de processadores starta cada processo na tela em cada processador(core)
		for(int i=0; i<numProcessadores; i++) {
			if (processoList.size() > 0) 
			{
				aguardaEmMilisegundos(50); // Este tempo está bom ?
				// Exclui processo da lista de aptos e coloca na fila de processadores, startando-o
				processadoresList.add(processoList.get(0));
				processoList.remove(0); 
		    	atualizaTela();
				processadoresList.get(i).setEstadoProcesso("E");
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
				processoList.get(i).decrementaDeadLine();
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
					abortadosConcluidosList.add(processadoresList.get(i));
					mostraLogAbortadosConcluidos(abortadosConcluidosList.size()-1); //				
					if (processoList.size() > 0) {
						// insere novo processo da lista de aptos p/ executar
						processadoresList.set(i, processoList.get(0));
						processoList.remove(0); 
				    	atualizaTela();
						processadoresList.get(i).setEstadoProcesso("E");
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
		if (abortadosConcluidosList.get(i).isAtivalog())
			System.out.println("Id abortado/concluído: "+abortadosConcluidosList.get(i).getIdentificadorProcesso()+
					" | T. total "+abortadosConcluidosList.get(i).getTempoTotalExecucao()+
					" | T. restante: "+abortadosConcluidosList.get(i).getTempoExecucaoRestante()+
					" | Status: "+abortadosConcluidosList.get(i).getEstadoProcesso());
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
			centerPanel.refresh();
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
		abortadosConcluidosList.add(processoList.get(index));
		processoList.remove(index);
		mostraLogAbortadosConcluidos(abortadosConcluidosList.size()-1); //
	}
} 
