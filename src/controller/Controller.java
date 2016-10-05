package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import gui.centerLayout.CenterPanel;
import gui.leftLayout.LeftPanel;
import gui.leftLayout.LeftPanelEvent;
import gui.topLayout.TopPanelEvent;
import model.ProcessoList;
import model.Processo;

public class Controller {
	
	protected LeftPanel leftPanel;
	protected CenterPanel centerPanel;
	private ProcessoList processoList = new ProcessoList();
	private ProcessoList processadoresList = new ProcessoList();	
	private int numProcessadores = 0;
	private int numProcessosIniciais = 0;
	private String estrategia;

	public Controller(LeftPanel leftPanel, CenterPanel centerPanel){
		this.leftPanel = leftPanel;
		this.centerPanel = centerPanel;		
	}
	
	public List<Processo> getProcessos() {
		return processoList.getAll();
	}
	
	public void resetProcessos() {
		processoList.reset();
	}
	
	public void iniciarSimulacao(TopPanelEvent e) {
		estrategia = e.getEstrategia();
		System.out.println("Estrategia: " + estrategia);
		System.out.println("Quantidade de Processadores: " + e.getQdeProcessadores());
		System.out.println("Numero de processos iniciais: " + e.getNumProcessosIniciais());
		System.out.println("===============================");
		
		numProcessadores = e.getQdeProcessadores();
		numProcessosIniciais = e.getNumProcessosIniciais();
		if (estrategia == "Round Robin")
			for(int i=0; i<numProcessosIniciais; i++) {
				Processo p = new Processo("", "", "", 0, "", "");
				processoList.add(p);
			}
		else {
			iniciarAlgoritmoLTG(e);
		}
	}
	
	public void adicionarProcesso(LeftPanelEvent e) {
		System.out.println("Tempo Total Execucao: " + e.getTempoTotalExecucao());
		System.out.println("Estado: " + e.getEstadoProcesso());
		System.out.println("Tempo Execucao restante: " + e.getTempoExecucaoRestante());
		System.out.println("Deadline: " + e.getDeadline());
		System.out.println("Intervalo: " + e.getIntervalo());
		System.out.println("-------------------------");
		
		String tempoTotalExecucao = e.getTempoTotalExecucao();
		String estadoProcesso = e.getEstadoProcesso();
		int prioridade = e.getPrioridade();
		String tempoExecucaoRestante = e.getTempoExecucaoRestante();
		String deadline = e.getDeadline();
		String intervalo = e.getIntervalo();
	
		Processo p = new Processo(tempoTotalExecucao, estadoProcesso, tempoExecucaoRestante, prioridade, 
				deadline, intervalo);
		if (estrategia == "Round Robin")
			processoList.add(p);
		else {
			// O index é < 0 qdo. não achado o deadline em nenhum processo e é um número >= 0 quando representa o index de outro processo com o mesmo deadline
			int index = Collections.binarySearch(getProcessos(), p); 
            // Reposiciona index p/ correta inserção na posição adequada 
			if (index < 0) {
				index = (-1*index)-1;
			} else if (index >= 0) {
				index = index+totalProcessosComEsseDeadline(index, deadline);
			}
		    System.out.println("Índice do novo processo: "+index+". Deadline: "+deadline);    
			processoList.insert(index, p);
			// Atualiza campo Id
			e.setIdentificadorProcesso(p.getIdentificadorProcesso());
			leftPanel.setidentificadorProcessoField(String.valueOf(p.getIdentificadorProcesso()));
		}		
	}
	
	public int totalProcessosComEsseDeadline(int index, String deadline){
		int iTotal = 0;
		int iDeadline = Integer.valueOf(deadline);
		try {
			Integer dl = getProcessos().get(index).getDeadlineToSort();
			while (dl == iDeadline){
				++iTotal;
				dl = getProcessos().get(++index).getDeadlineToSort();
			}
		} catch(Exception e) { // ArrayIndexOutOfBounds			
		}
		return iTotal;
	}
	
	public int retornaRandom(int iLimiteMin, int iLimiteMax){
        Random gerador = new Random();
        int numero = gerador.nextInt(iLimiteMax-iLimiteMin+1) + iLimiteMin;
 		
		return numero;
	}
	
	public void iniciarAlgoritmoLTG(TopPanelEvent e)
	{
		for(int i=0; i<numProcessosIniciais; i++) {
			int iTempoTotalExecucao = retornaRandom(4, 20);
			String tempoTotalExecucao = String.valueOf(iTempoTotalExecucao);
			int iDeadline = retornaRandom(4, 20);
			String deadline = String.valueOf(iDeadline);
			Processo p = new Processo(tempoTotalExecucao, "P", tempoTotalExecucao, 0, deadline, "");
			processoList.add(p);
		}
		// ordena por deadline
		Collections.sort(processoList.getAll());
	}
	
	public void iniciarExecucaoDosProcessos(TopPanelEvent e)
	{
		if (estrategia == "Round Robin")
			iniciarExecucaoDosProcessosRoundRobin(e);
		else
			iniciarExecucaoDosProcessosLTG(e);		
	}

	public void iniciarExecucaoDosProcessosRoundRobin(TopPanelEvent e)
	{
		// ...
	}

	public void iniciarExecucaoDosProcessosLTG(TopPanelEvent e)
	{
		List<Thread> threadsList = new ArrayList<Thread>();
		// Loop de processadores starta cada processo na tela em cada processador
		for(int i=0; i<numProcessadores; i++) {
			if (processoList.size() > 0) 
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				// Exclui processo da lista de aptos e coloca na fila de processadores, startando-o
				processadoresList.add(processoList.get(0));
				processoList.remove(0); 
				centerPanel.refresh();
				Thread thread = new Thread(processadoresList.get(i));
				threadsList.add(thread);
				threadsList.get(i).start();
				if (processadoresList.get(i).isAtivalog())
					System.out.println("Id startado: "+processadoresList.get(i).getIdentificadorProcesso()+
							" | T. total "+processadoresList.get(i).getTempoTotalExecucao()+
							" | T. restante: "+processadoresList.get(i).getTempoExecucaoRestante());
			}
			else {
				// Imprime "core livre" nos cores restantes
			}
		}
		// Guarda o último valor de i q indica qual a ordem do próximo processo a rodar
		// ...
		// inicia thread de controle de mudanca de processos
		// DispatcherLTG();
		// - Decrementa deadline, atualiza lista de aptos
		// - atualiza lista de processos em execução
		// - mata processos e para threads
		// ...
	}
}
