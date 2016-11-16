package model;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class Processo implements Comparable<Processo>, Runnable {
	// Para ativar demonstra��o por log (alternativa)
	private static final boolean _ativaLog = true;
	public boolean isAtivalog() {
		return _ativaLog;
	}

	private static int id = 0;
	private int identificadorProcesso;
	private String tempoTotalExecucao;
	private String estadoProcesso;
	private String tempoExecucaoRestante;
	private int prioridade;
	private String deadline;
	private String intervalo;
	private float quantum;
	private volatile boolean pare = false;
	
	public Processo(String tempoTotalExecucao, 
					String estadoProcesso, String tempoExecucaoRestante, int prioridade, String deadline, String intervalo) {
		
		this.identificadorProcesso = ++Processo.id;
		this.tempoTotalExecucao = tempoTotalExecucao;
		this.estadoProcesso = estadoProcesso; // P = Pronto | E = Executando | B = Bloqueado | A = Abortado(para o alg. LTG) | OK = Conclu�do
		this.tempoExecucaoRestante = tempoExecucaoRestante;
		this.prioridade = prioridade;
		this.deadline = deadline;
		this.intervalo = intervalo;
	}

	// Para representar Core de processador livre
	public Processo(int idCoreLivre, String tempoTotalExecucao, 
			String estadoProcesso, String tempoExecucaoRestante, int prioridade, String deadline, String intervalo) {
		this.identificadorProcesso = idCoreLivre;
		this.tempoTotalExecucao = tempoTotalExecucao;
		this.estadoProcesso = estadoProcesso;  // CORE_LIVRE
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
	
	public Integer getDeadlineToSort() {
		return Integer.valueOf(deadline);
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
	
	public float getQuantum() {
		return quantum;
	}

	public void setQuantum(float quantum) {
		this.quantum = quantum;
	}

	public int compareTo(Processo processo) {
		int iRetorno = 0;
		if (Integer.valueOf(processo.deadline) == 0) {
			// Ordena��o por Prioridade + Id (para o algoritmo Round robin com fila de Prioridades)
//			int iRetorno = 0;
			if (this.getPrioridade() < processo.prioridade) {
				return -1;
			} else if (this.getPrioridade() > processo.prioridade) {
				return 1;
			}
			iRetorno = compareTo_Id(processo); 
		}
		else {
			// Ordena��o por Deadline (para o algoritmo LTG)
//			int iRetorno = 0;
			if (this.getDeadlineToSort() < Integer.valueOf(processo.deadline)) {
				return -1;
			} else if (this.getDeadlineToSort() > Integer.valueOf(processo.deadline)) {
				return 1;
			}
		}
		return iRetorno;
	}
	
	public int compareTo_Id(Processo processo) {
		if (this.getIdentificadorProcesso() < processo.identificadorProcesso) {
			return -1;
		} else if (this.getIdentificadorProcesso() > processo.identificadorProcesso) {
			return 1;
		} else
			return 0;
	}
	
	@Override
	public void run() {
		decrementaTempoRestante(); 
    }

    public void decrementaTempoRestante() {
    	while (!pare)
    	{
	    	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	int iTempoExecucaoRestante = Integer.valueOf(this.tempoExecucaoRestante);
	    	setTempoExecucaoRestante(String.valueOf(--iTempoExecucaoRestante));
	    	if (iTempoExecucaoRestante == 0 || this.estadoProcesso == "B")
	    		stop();
	    	if (_ativaLog)
	    		System.out.println("Id rodando: "+identificadorProcesso+
						" | T. total "+tempoTotalExecucao+
						" | T. restante: "+tempoExecucaoRestante);
    	}
    	if (_ativaLog)
    		System.out.println("Id parado: "+identificadorProcesso+
					" | T. total "+tempoTotalExecucao+
					" | T. restante: "+tempoExecucaoRestante+
					" | �lt. estado: "+estadoProcesso);
    	int iTempoExecucaoRestante = Integer.valueOf(this.tempoExecucaoRestante);
    	if (iTempoExecucaoRestante == 0)
    		setEstadoProcesso("OK");
    }

    public void stop() {
    	this.pare = true;
    }

    public void decrementaDeadLine(){
    	int iDeadline = Integer.valueOf(this.deadline);
    	if (iDeadline != 0)
		{
	    	setDeadline(String.valueOf(--iDeadline));			
		} else
		{
			setEstadoProcesso("A");
		}			
    }    
    
    public void decrementaQuantum() {
    	if (this.quantum != 0)
		{
	    	setQuantum(--this.quantum);			
		} else
		{
			setEstadoProcesso("B");
		}	   	
    }
}
