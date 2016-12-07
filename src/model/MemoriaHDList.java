package model;

import java.util.LinkedList;

public class MemoriaHDList {

	protected LinkedList<BlocoMemoria> blocosMemoria;
	private int tamanhoMemoriaRAM;  
	private int threshold;
	
	public MemoriaHDList() {
		blocosMemoria = new LinkedList<>();
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

	public void calculaThreshold(int tamanhoMemoriaRAM) {
		this.setTamanhoMemoriaRAM(tamanhoMemoriaRAM);
		this.setThreshold(tamanhoMemoriaRAM*(70/100));
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
	
	public void swapMemoriaHD() {
		
	}
	
	public void swapHDMemoria() {
		
	}
}
