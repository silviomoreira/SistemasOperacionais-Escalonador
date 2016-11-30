package model;

public class RequisicaoMemoria implements Comparable<RequisicaoMemoria> {

	private int tamanhoBloco;
	private int incidencia; 
	private int numeroLista;
		
	public RequisicaoMemoria() {
		this.incidencia = 1;
	}
	public RequisicaoMemoria(int t) {
		this.tamanhoBloco = t;
		this.incidencia = 1;
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

	@Override
	public int compareTo(RequisicaoMemoria requisicaoMemoria) {
		// Ordenada inversamente por incidencia
		if (this.getIncidencia() < requisicaoMemoria.incidencia)
			return 1;
		if (this.getIncidencia() > requisicaoMemoria.incidencia)
			return -1;
		return 0; 
	}
	
}
