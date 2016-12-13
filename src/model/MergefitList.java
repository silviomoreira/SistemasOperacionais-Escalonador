package model;

public class MergefitList extends MemoriaList {

	public MergefitList() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean alocouMemoria(int tamanhoBloco, int tamanhoMemoriaRestante, int idProcesso) {
		return true;
	}
	
	private void aloqueMemoria(int tamanhoBloco, int idProcesso) {
		// caso 2: aloca novo bloco com mesmo tamanho (alocação completa)

	}
	private void aloqueMemoria(int tamanhoBloco, int idProcesso, int i) {
		// caso 1: aloca bloco livre de mesmo tamanho
		// caso 3: aloca bloco livre maior

	}
	
	public void liberaMemoria(int idProcesso) {
		
	}
	
	public boolean alocouMemoriaPeloProcesso(int tamanhoBloco, int idProcesso) {
		return true;
	}

	// Refatorar:
	public boolean foiAlocadoPorMeioDoProcesso(int idProcesso) {
		return true;
	}
	/*
	public split(){
		
	}
	
	public merge(){
		
	}
	*/
}
