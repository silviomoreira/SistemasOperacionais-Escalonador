package model;

import gui.bottomLayout.BottomPanel;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MemoriaHDList {

	protected LinkedList<BlocoMemoria> blocosMemoria;
	private int tamanhoMemoriaRAM;  
	private int threshold;
	//private MemoriaList blocosMemoriaRAM;
	private BottomPanel bottomPanel;
	
	public MemoriaHDList() {//MemoriaList blocosMemoriaRAM) {
		blocosMemoria = new LinkedList<>();
		//this.blocosMemoriaRAM = blocosMemoriaRAM;
	}

	public int getTamanhoMemoriaRAM() {
		return tamanhoMemoriaRAM;
	}

	public void setTamanhoMemoriaRAM(int tamanhoMemoriaRAM) {
		this.tamanhoMemoriaRAM = tamanhoMemoriaRAM;
	}

	public int getThreshold() {
		return threshold;
	}
	
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public BottomPanel getBottomPanel() {
		return bottomPanel;
	}

	public void setBottomPanel(BottomPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}

	public void calculaThreshold(int tamanhoMemoriaRAM) {
		this.setTamanhoMemoriaRAM(tamanhoMemoriaRAM);
		double dThreshold = (tamanhoMemoriaRAM*40)/100; // (70/100)); PENDENCIA: DESCOMENTAR
		this.setThreshold((int) Math.ceil(dThreshold)); 
	}
	
	public void add(BlocoMemoria blocoMemoria) {
		blocosMemoria.add(blocoMemoria);
	}
	
	public void insert(int index, BlocoMemoria blocoMemoria) {
		blocosMemoria.add(index, blocoMemoria);
	}
	
	public LinkedList<BlocoMemoria> getAll() {		
		return blocosMemoria;
	}
	
	public BlocoMemoria remove(int index) {
		return blocosMemoria.remove(index);
	}
	
	public int size() {
		return blocosMemoria.size();
	}
	
	public void reset() {
		blocosMemoria = new LinkedList<>();		
	}
	
	public BlocoMemoria get(int index) {
		return blocosMemoria.get(index);
	}
	
	public void set(int index, BlocoMemoria blocoMemoria) {
		blocosMemoria.set(index, blocoMemoria);
	}
	
	// Os 2 procedimentos abaixo devem ser realizados  em um loop de tempos em tempos.
	/*
     Toda a vez q a mem. RAM estiver com menos de 70% de ocupação, realiza-se um loop do 1o. processo até
     o último da lista de aptos e movem-se os processos q estiverem no HD, do HD p/ a RAM. A prioridade
     deste swap é maior, pois é preferível q o processo q for rodar mais cedo já esteja na RAM.
	 */
	public void swapHDMemoria(List<Processo> processoList, int memoriaDisponivel, MemoriaList blocosMemoriaRAM) {
		// t = 100  l = 70, qdo. disp >= (totalRam - treshold) ==> d >= 30
		if (memoriaDisponivel >= (tamanhoMemoriaRAM - threshold)) {
			//bottomPanel.refreshConsole("Swap HD->M) Tam. da lista de aptos: "+processoList.size());
			for(int i=0; i<processoList.size(); i++) {
				BlocoMemoria bm;
				ListIterator<BlocoMemoria> literbm = blocosMemoria.listIterator();
				while (literbm.hasNext()) {
					bm = literbm.next();
					if (bm.getIdProcesso() == processoList.get(i).getIdentificadorProcesso())
					{
						// precisa setar idlogico = id ... ?
						blocosMemoriaRAM.add(bm);
						literbm.remove();
					}
				}			
			} // Fim <for>
		}
	}
	/*
    É fixado um limiar(threshold), por exemplo de 70%, p/ determinar qdo. o processo deve ir p/ o HD.
    Toda a vez q a mem. RAM ultrapassar a 70%, inicia-se um loop inverso, na lista de aptos, do último
    processo até o 1o. e realiza-se o swap da RAM p/ o HD(tem menos prioridade do que o inverso).
	 */
	public void swapMemoriaHD(List<Processo> processoList, int memoriaDisponivel, MemoriaList blocosMemoriaRAM) {
		// t = 100  l = 70, qdo. disp < (totalRam - treshold) ==> d < 30
		if (memoriaDisponivel < (tamanhoMemoriaRAM - threshold)) {
			//bottomPanel.refreshConsole("Swap M->HD) Tam. da lista de aptos: "+processoList.size());
			for(int i=processoList.size()-1; i >= 0; i--) { // > | >=
				BlocoMemoria bm;
				ListIterator<BlocoMemoria> literbm = blocosMemoriaRAM.getAll().listIterator();
				while (literbm.hasNext()) {
					bm = literbm.next();
					if (bm.getIdProcesso() == processoList.get(i).getIdentificadorProcesso())
					{
						// precisa setar idlogico = id ... ? ou marcar outro campo e deixar registro lá ?
						blocosMemoria.add(bm);
						literbm.remove();
					}
				}			
			} // Fim <for>
		}
	}

}
