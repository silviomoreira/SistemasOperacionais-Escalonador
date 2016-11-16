package gui.leftLayout;

import java.util.EventObject;

public class LeftPanelEvent extends EventObject {
	private int identificadorProcesso;
	private String tempoTotalExecucao;
	private String estadoProcesso;
	private String tempoExecucaoRestante;
	private int prioridade;
	private String deadline;
	private int quantum;
	private String intervalo;
	
	public LeftPanelEvent(Object source) {
		super(source);
	}
	
	public LeftPanelEvent(Object source, String tempoTotalExecucao, 
			String estadoProcesso, String tempoExecucaoRestante, int prioridade, String deadline, int quantum, String intervalo) {
		super(source);
		
		this.tempoTotalExecucao = tempoTotalExecucao;
		this.estadoProcesso = estadoProcesso;
		this.tempoExecucaoRestante = tempoExecucaoRestante;
		this.prioridade = prioridade;
		this.deadline = deadline;
		this.quantum = quantum;
		this.intervalo = intervalo;
	}
	
	public LeftPanelEvent(Object source, int identificadorProcesso, String tempoTotalExecucao, 
			String estadoProcesso, String tempoExecucaoRestante, int prioridade, String deadline, int quantum, String intervalo) {
		super(source);
		
		this.identificadorProcesso = identificadorProcesso;
		this.tempoTotalExecucao = tempoTotalExecucao;
		this.estadoProcesso = estadoProcesso;
		this.tempoExecucaoRestante = tempoExecucaoRestante;
		this.prioridade = prioridade;
		this.deadline = deadline;
		this.quantum = quantum;
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

	public int getQuantum() {
		return quantum;
	}

	public void setQuantum(int quantum) {
		this.quantum = quantum;
	}

	public String getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(String intervalo) {
		this.intervalo = intervalo;
	}
	
	
}
