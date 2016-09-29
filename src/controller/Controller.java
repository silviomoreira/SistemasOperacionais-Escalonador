package controller;

import gui.leftLayout.LeftPanelEvent;
import gui.topLayout.TopPanelEvent;

public class Controller {
	public void iniciarSimulacao(TopPanelEvent e) {
		System.out.println("Estrategia: " + e.getEstrategia());
		System.out.println("Quantidade de Processadores: " + e.getQdeProcessadores());
		System.out.println("Numero de processos iniciais: " + e.getNumProcessosIniciais());
	}
	
	public void adicionarProcesso(LeftPanelEvent e) {
		System.out.println("Tempo Total: " + e.getTempoTotalExecucao());
		System.out.println("Estado: " + e.getEstadoProcesso());
		System.out.println("Tempo restante: " + e.getTempoExecucaoRestante());
		System.out.println("Deadline: " + e.getDeadline());
		System.out.println("Intervalo: " + e.getIntervalo());
		System.out.println("Tempo Total: " + e.getTempoTotalExecucao());
	}
}
