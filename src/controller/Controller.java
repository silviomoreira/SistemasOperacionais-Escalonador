package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import gui.centerLayout.CenterPanel;
import gui.leftLayout.LeftPanel;
import gui.leftLayout.LeftPanelEvent;
import gui.topLayout.TopPanelEvent;
import model.BestfitList;
import model.BlocoMemoria;
import model.MemoriaHDList;
import model.MemoriaList;
import model.ProcessoList;
import model.Processo;
import model.QuickfitList;
import model.RequisicaoMemoria;
import model.RequisicaoMemoriaList;

public class Controller {
	
	private LeftPanel leftPanel;
	private CenterPanel centerPanel;//exc depois
	private ProcessoList processoList = new ProcessoList();
	private ProcessoList processadoresList = new ProcessoList();/////private ProcessadoresList processadoresList = new ProcessadoresList();
	private ProcessoList concluidosEAbortadosList = new ProcessoList();
	private MemoriaList memoriaList = new MemoriaList();
	private BestfitList bestfitList = new BestfitList();
	private RequisicaoMemoriaList requisicaoMemoriaList = new RequisicaoMemoriaList();
	private QuickfitList quickfitList = new QuickfitList(requisicaoMemoriaList);
	private MemoriaHDList memoriaHDList = new MemoriaHDList(bestfitList);
	
	private int numProcessadores = 0;
	private int numProcessosIniciais = 0;
	private String estrategia;
	private String estrategiaMem;
	private int tamanhoMemoria = 0;

	public Controller(LeftPanel leftPanel, CenterPanel centerPanel){
		this.leftPanel = leftPanel;
		this.centerPanel = centerPanel;		
	}
	
	public List<Processo> getProcessos() {
		return processoList.getAll();
	}
	
	public void resetProcessos() {
		processoList.reset();
		processadoresList.reset();
		concluidosEAbortadosList.reset();
		memoriaList.reset();
		memoriaHDList.reset();
		bestfitList.reset();
		quickfitList.reset();
		quickfitList.resetComplementar();
		requisicaoMemoriaList.reset();
	}
	
	public List<Processo> getProcessadoresList() {
		return processadoresList.getAll();
	}
    ///// código comentado
	/*public ProcessadoresList getProcessadoresObj() {
		return processadoresList;
	}*/
	
	public List<Processo> getConcluidosEAbortadosList() {
		return concluidosEAbortadosList.getAll();
	}

	public MemoriaList getMemoriaObj() {
		return memoriaList;
	}
	
	public List<BlocoMemoria> getMemoriaList() {
		return memoriaList.getAll();
	}
	
	public BestfitList getBestfitObj() {
		return bestfitList;
	}

	public List<BlocoMemoria> getBestfitList() { 
		return bestfitList.getAll();
	}
	
	public QuickfitList getQuickfitObj() {
		return quickfitList;
	}
	
	public List<BlocoMemoria> getQuickfitList() {
		return quickfitList.getAll();
	}

	public RequisicaoMemoriaList getRequisicaoMemoriaObj() {
		return requisicaoMemoriaList;
	}
	
	public List<RequisicaoMemoria> getRequisicaoMemoriaList() {
		return requisicaoMemoriaList.getAll();
	}

	public MemoriaHDList getMemoriaHDObj() {
		return memoriaHDList;
	}
	
	public List<BlocoMemoria> getMemoriaHDList() {
		return memoriaHDList.getAll();
	}
	
	public void iniciarSimulacao(TopPanelEvent e) {
		estrategia = e.getEstrategia();
		System.out.println("Estrategia: " + estrategia);
		System.out.println("Quantidade de Processadores: " + e.getQdeProcessadores());
		System.out.println("Numero de processos iniciais: " + e.getNumProcessosIniciais());
		System.out.println("===============================");
		
		numProcessadores = e.getQdeProcessadores();
		numProcessosIniciais = e.getNumProcessosIniciais();
		tamanhoMemoria = e.getTamanhoMem();
		memoriaHDList.calculaThreshold(tamanhoMemoria);
		if (estrategia == "Round Robin")
			iniciarAlgoritmoRoundRobin(e);			
		else if (estrategia == "Least Time to Go (LTG)")
			iniciarAlgoritmoLTG(e);
	}
	
	public void adicionarProcesso(LeftPanelEvent e) {
		System.out.println("< Processo adicionado via botão >");		
		System.out.println("-------------------------");
		System.out.println("Tempo Total Execucao: " + e.getTempoTotalExecucao());
		System.out.println("Estado: " + e.getEstadoProcesso());
		System.out.println("Tempo Execucao restante: " + e.getTempoExecucaoRestante());
		System.out.println("Deadline: " + e.getDeadline());
		System.out.println("Quantum: " + e.getQuantum());
		System.out.println("Intervalo: " + e.getIntervalo());
		System.out.println("-------------------------");
		
		String tempoTotalExecucao = e.getTempoTotalExecucao();
		String estadoProcesso = e.getEstadoProcesso();
		int prioridade = e.getPrioridade();
		String tempoExecucaoRestante = e.getTempoExecucaoRestante();
		String deadline = e.getDeadline(); 
		int quantum = e.getQuantum();
		String intervalo = e.getIntervalo();
		int qtdBytes = 0;
		
		Processo p = new Processo(tempoTotalExecucao, estadoProcesso, tempoExecucaoRestante, prioridade, 
				deadline, intervalo, qtdBytes);
		if (estrategia == "Round Robin")
		{
			p.setQuantum(quantum);
			p.setQuantumInicial(quantum);
			p.setQtdBytes(retornaRandom(32, 1024));
			processoList.add(p);
		    // adicionar na posição específica se for round robin com fila de prioridades
			// ...		
		}
		else {
			// O index é < 0 qdo. não achado o deadline em nenhum processo e é um número >= 0 quando representa o index de outro processo com o mesmo deadline
			int index = Collections.binarySearch(getProcessos(), p); 
            // Reposiciona index p/ correta inserção na posição adequada 
			if (index < 0) {
				index = (-1*index)-1;
			} else if (index >= 0) {
				index = index+totalProcessosComEsseDeadline(index, deadline);
			}
		    System.out.println("Índice do novo processo(posição lista): "+index+". Deadline: "+deadline);    
			System.out.println("--------------------------------------------------------------");
			processoList.insert(index, p);
		}		
		// Atualiza campo Id
		e.setIdentificadorProcesso(p.getIdentificadorProcesso());
		leftPanel.setidentificadorProcessoField(String.valueOf(p.getIdentificadorProcesso()));
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
			Processo p = new Processo(tempoTotalExecucao, "P", tempoTotalExecucao, 0, deadline, "", 0);
			processoList.add(p);
		}
		// ordena por deadline
		Collections.sort(processoList.getAll());
	}
	
	private void iniciarAlgoritmoRoundRobin(TopPanelEvent e) {
		for(int i=0; i<numProcessosIniciais; i++) {
			// Passa novos parâmetros se for escolhido rodar a demonstração do funcionamento da memória
			int iTempoTotalExecucao = retornaRandom(tamanhoMemoria > 0 ? 10 : 4, tamanhoMemoria > 0 ? 30 : 20);
			String tempoTotalExecucao = String.valueOf(iTempoTotalExecucao);
			int iPrioridade = retornaRandom(0, 3);
			String deadline = "0";
			int iQtdBytes = retornaRandom(32, 1024);//iQtdBytes = 200;
			Processo p = new Processo(tempoTotalExecucao, "P", tempoTotalExecucao, iPrioridade, deadline, "", iQtdBytes);
			processoList.add(p);
		}
		// ordena por prioridade + id
		Collections.sort(processoList.getAll());
		if (tamanhoMemoria > 0)
		{
			if (estrategiaMem == "Best fit")
				bestfitList.setMemorySize(tamanhoMemoria);
			if (estrategiaMem == "Quick fit")
				quickfitList.setMemorySize(tamanhoMemoria);
			/*if (estrategiaMem == "Merge fit")
				mergefitList.setMemorySize(tamanhoMemoria);*/
		}
	}
}
