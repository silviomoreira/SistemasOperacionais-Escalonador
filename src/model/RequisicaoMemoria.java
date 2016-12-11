package model;

public class RequisicaoMemoria implements Comparable<RequisicaoMemoria> {

	private int tamanhoBloco;
	private int incidencia; 
	private int numeroLista;
	
	// chaveia a ordenação entre a padrão: incidencia | ou a não-padrão: número da lista + incidencia
	private volatile boolean bOrdenacaoPadrao = true; 
		
	public RequisicaoMemoria() {
		this.incidencia = 1;
		this.numeroLista = -1;
	}
	public RequisicaoMemoria(int t) {
		this.tamanhoBloco = t;
		this.incidencia = 1;
		this.numeroLista = -1;		
	}

	public int getTamanhoBloco() {
		return tamanhoBloco;
	}

	public void setTamanhoBloco(int tamanhoBloco) {
		this.tamanhoBloco = tamanhoBloco;
	}

	public int getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(int incidencia) {
		this.incidencia += incidencia;
	}

	public int getNumeroLista() {
		return numeroLista;
	}

	public void setNumeroLista(int numeroLista) {
		this.numeroLista = numeroLista;
	}

	public boolean isbOrdenacaoPadrao() {
		return bOrdenacaoPadrao;
	}
	public void setbOrdenacaoPadrao(boolean bOrdenacaoPadrao) {
		this.bOrdenacaoPadrao = bOrdenacaoPadrao;
	}
	@Override
/*	public int compareTo(RequisicaoMemoria requisicaoMemoria) {
		// Ordenada inversamente por incidencia
		if (this.getIncidencia() < requisicaoMemoria.incidencia)
			return 1;
		if (this.getIncidencia() > requisicaoMemoria.incidencia)
			return -1;
		return 0; 
	}*/
	public int compareTo(RequisicaoMemoria requisicaoMemoria) {
		if (bOrdenacaoPadrao) {
			// Ordenada inversamente por incidencia   
			if (this.getIncidencia() < requisicaoMemoria.incidencia)
				return 1;
			if (this.getIncidencia() > requisicaoMemoria.incidencia)
				return -1;
			return 0;			
		} else {
			// Ordenada inversamente por numero da lista + incidencia   
			if (this.getNumeroLista() < requisicaoMemoria.numeroLista)
				return 1;
			if (this.getNumeroLista() > requisicaoMemoria.numeroLista)
				return -1;
			else
				return compareTo_Incidencia(requisicaoMemoria); 
		}
	}
	private int compareTo_Incidencia(RequisicaoMemoria requisicaoMemoria) {
		if (this.getIncidencia() < requisicaoMemoria.incidencia)
			return 1;
		if (this.getIncidencia() > requisicaoMemoria.incidencia)
			return -1;
		else
			return 0;
	}
}
