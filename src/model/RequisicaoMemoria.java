package model;

public class RequisicaoMemoria implements Comparable<RequisicaoMemoria> {

	private int tamanhoBloco;
	private int incidencia; 
	private int numeroLista;
	
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

	@Override
	public int compareTo(RequisicaoMemoria requisicaoMemoria) {
		if (RequisicaoMemoriaList.bOrdenacaoPadrao) {
			//System.out.println("ordenado por incidencia(padrão)"); // OK
			// Ordenada inversamente por incidencia   
			if (this.getIncidencia() < requisicaoMemoria.incidencia)
				return 1;
			if (this.getIncidencia() > requisicaoMemoria.incidencia)
				return -1;
			return 0;			
		} else {
			//System.out.println("ordenado por num. da lista + incidencia(n padrão)"); // OK
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
