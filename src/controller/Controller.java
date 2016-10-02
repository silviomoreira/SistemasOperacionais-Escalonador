package controller;

import java.util.List;
import java.util.Random;

import gui.leftLayout.LeftPanelEvent;
import gui.topLayout.TopPanelEvent;
import model.ProcessoList;
import model.Processo;

public class Controller {
	
	private ProcessoList processoList = new ProcessoList();
	private int numProcessosIniciais = 0;
	
	public List<Processo> getProcessos() {
		return processoList.getAll();
	}
	
	public void resetProcessos() {
		processoList.reset();
	}
	
	public void iniciarSimulacao(TopPanelEvent e) {
		System.out.println("Estrategia: " + e.getEstrategia());
		System.out.println("Quantidade de Processadores: " + e.getQdeProcessadores());
		System.out.println("Numero de processos iniciais: " + e.getNumProcessosIniciais());
		System.out.println("==============================");
		
		numProcessosIniciais = e.getNumProcessosIniciais();
		if (e.getEstrategia() == "Round Robin")
			for(int i=0; i<numProcessosIniciais; i++) {
				Processo p = new Processo("", "", "", 0, "", "");
				processoList.add(p);
			}
		else
			iniciarAlgoritmoLTG(e);
	}
	
	public void adicionarProcesso(LeftPanelEvent e) {
		System.out.println("Tempo Total Execucao: " + e.getTempoTotalExecucao());
		System.out.println("Estado: " + e.getEstadoProcesso());
		System.out.println("Tempo restante: " + e.getTempoExecucaoRestante());
		System.out.println("Deadline: " + e.getDeadline());
		System.out.println("Intervalo: " + e.getIntervalo());
		System.out.println("Tempo Execucao restante: " + e.getTempoExecucaoRestante());
		System.out.println("-------------------------");
		
		String tempoTotalExecucao = e.getTempoTotalExecucao();
		String estadoProcesso = e.getEstadoProcesso();
		int prioridade = e.getPrioridade();
		String tempoExecucaoRestante = e.getTempoExecucaoRestante();
		String deadline = e.getDeadline();
		String intervalo = e.getIntervalo();
		
		
		Processo p = new Processo(tempoTotalExecucao, estadoProcesso, tempoExecucaoRestante, prioridade, 
				deadline, intervalo);
		processoList.add(p);
	}
	
	public int retornaRandom(int iLimiteMin, int iLimiteMax){
        Random gerador = new Random();
        int numero = gerador.nextInt(iLimiteMax) + iLimiteMin;
 		
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
		/* Comparator<Processo> c = new Comparator<Processo>() {
		      public int compare(Processo p1, Processo p2) {
		        return p1.getDeadlineToSort().compareTo(p2.getDeadlineToSort());
		      }
		 };*/
	}
    
}
