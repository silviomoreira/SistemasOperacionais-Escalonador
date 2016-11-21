package model;

public class BlocoMemoria {

	private static int id = 0;
	private int idBloco;
	private int tamanho; 	 // bytes
	private int espacoUsado; // bytes
	private int referenciaProxBloco;
	private int idProcesso;
 
	public BlocoMemoria(int tamanho, int espacoUsado, int referenciaProxBloco) {
		this.idBloco = ++BlocoMemoria.id;
		this.tamanho = tamanho;
		this.espacoUsado = espacoUsado;
		this.referenciaProxBloco = referenciaProxBloco;
	}

	public int getIdBloco() {
		return idBloco;
	}

	public void setIdBloco(int idBloco) {
		this.idBloco = idBloco;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public int getEspacoUsado() {
		return espacoUsado;
	}

	public void setEspacoUsado(int espacoUsado) {
		this.espacoUsado = espacoUsado;
	}

	public int getReferenciaProxBloco() {
		return referenciaProxBloco;
	}

	public void setReferenciaProxBloco(int referenciaProxBloco) {
		this.referenciaProxBloco = referenciaProxBloco;
	}

	public int getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(int idProcesso) {
		this.idProcesso = idProcesso;
	}
}
