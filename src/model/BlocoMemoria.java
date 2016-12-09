package model;

public class BlocoMemoria {

	private static int id = 0;
	private int idBloco;
	private int tamanho; 	 // bytes
	private int espacoUsado; // bytes
	private BlocoMemoria referenciaProxBloco;
	private int idProcesso;
	private int idLogicoBloco;  // utilizado p/ o swap
	private String listaDeOrigem; // utilizado no quickfit p/ mostrar de qual das 5 listas de blocos livres veio o bloco
 
	public BlocoMemoria(int tamanho, int espacoUsado, int idProcesso, BlocoMemoria referenciaProxBloco) {
		this.idBloco = ++BlocoMemoria.id;
		this.tamanho = tamanho;
		this.espacoUsado = espacoUsado;
		this.idProcesso = idProcesso;
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

	public BlocoMemoria getReferenciaProxBloco() {
		return referenciaProxBloco;
	}

	public void setReferenciaProxBloco(BlocoMemoria referenciaProxBloco) {
		this.referenciaProxBloco = referenciaProxBloco;
	}

	public int getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(int idProcesso) {
		this.idProcesso = idProcesso;
	}
	
	public int getIdLogicoBloco() {
		return idLogicoBloco;
	}

	public void setIdLogicoBloco(int idLogicoBloco) {
		this.idLogicoBloco = idLogicoBloco;
	}

	public String getListaDeOrigem() {
		return listaDeOrigem;
	}

	public void setListaDeOrigem(String listaDeOrigem) {
		this.listaDeOrigem = listaDeOrigem;
	}

	public void resetId() {
		BlocoMemoria.id = 0;
	}
}
