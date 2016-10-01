package model;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class Processo {
	private static int id = 0;
	private int identificadorProcesso;
	private String tempoTotalExecucao;
	private String estadoProcesso;
	private String tempoExecucaoRestante;
	private int prioridade;
	private String deadline;
	private String intervalo;
	
	public Processo(String tempoTotalExecucao, 
					String estadoProcesso, String tempoExecucaoRestante, int prioridade, String deadline, String intervalo) {
		this.identificadorProcesso = ++Processo.id;
		this.tempoTotalExecucao = tempoTotalExecucao;
		this.estadoProcesso = estadoProcesso; // P = Pronto | E = Executando | B = Bloqueado
		this.tempoExecucaoRestante = tempoExecucaoRestante;
		this.prioridade = prioridade;
		this.deadline = deadline;
		this.intervalo = intervalo;
	}

	public int getIdentificadorProcesso() {
		return identificadorProcesso;
	}

	public void setIdentificadorProcesso(int identificadorProcesso) {
		this.identificadorProcesso = identificadorProcesso;
	}

	public String getTempoTotalExecucao() {
		return tempoTotalExecucao;
	}

	public void setTempoTotalExecucao(String tempoTotalExecucao) {
		this.tempoTotalExecucao = tempoTotalExecucao;
	}

	public String getEstadoProcesso() {
		return estadoProcesso;
	}

	public void setEstadoProcesso(String estadoProcesso) {
		this.estadoProcesso = estadoProcesso;
	}

	public String getTempoExecucaoRestante() {
		return tempoExecucaoRestante;
	}

	public void setTempoExecucaoRestante(String tempoExecucaoRestante) {
		this.tempoExecucaoRestante = tempoExecucaoRestante;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(String intervalo) {
		this.intervalo = intervalo;
	}
	
	
}
