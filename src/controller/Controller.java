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
	
	private LeftPanel leftPanel;
	private CenterPanel centerPanel;//exc depois
	private ProcessoList processoList = new ProcessoList();
	private ProcessoList processadoresList = new ProcessoList();
	private ProcessoList concluidosEAbortadosList = new ProcessoList();
	/////private ProcessadoresList processadoresList = new ProcessadoresList();
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
		processadoresList.reset();
		
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
		System.out.println("< Processo adicionado via botão >");		
		System.out.println("-------------------------");
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
		    System.out.println("Índice do novo processo(posição lista): "+index+". Deadline: "+deadline);    
			System.out.println("--------------------------------------------------------------");
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
			if (i == 2) { iDeadline = 20; }; // FORÇA O 3o PROCESSO A SER EXECUTADO - RETIRAR DEPOIS
			String deadline = String.valueOf(iDeadline);
			Processo p = new Processo(tempoTotalExecucao, "P", tempoTotalExecucao, 0, deadline, "");
			processoList.add(p);
		}
		// ordena por deadline
		Collections.sort(processoList.getAll());
	}
	
}
