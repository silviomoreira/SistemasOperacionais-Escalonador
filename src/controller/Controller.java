package controller;

import java.util.List;

import gui.leftLayout.LeftPanelEvent;
import gui.topLayout.TopPanelEvent;
import model.ProcessoList;
import model.Processo;

public class Controller {
	
	private ProcessoList processoList = new ProcessoList();
	
	public List<Processo> getProcessos() {
		return processoList.getAll();
	}
	
	public void iniciarSimulacao(TopPanelEvent e) {
		System.out.println("Estrategia: " + e.getEstrategia());
		System.out.println("Quantidade de Processadores: " + e.getQdeProcessadores());
		System.out.println("Numero de processos iniciais: " + e.getNumProcessosIniciais());
	}
	
	public void adicionarProcesso(LeftPanelEvent e) {
		System.out.println("Tempo Total Execucao: " + e.getTempoTotalExecucao());
		System.out.println("Estado: " + e.getEstadoProcesso());
		System.out.println("Tempo restante: " + e.getTempoExecucaoRestante());
		System.out.println("Deadline: " + e.getDeadline());
		System.out.println("Intervalo: " + e.getIntervalo());
		System.out.println("Tempo Execucao restante: " + e.getTempoExecucaoRestante());
		
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
}
