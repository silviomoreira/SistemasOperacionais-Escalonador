package model;

import java.util.LinkedList;

public class MemoriaList {

	protected LinkedList<BlocoMemoria> blocosMemoria;
	protected int memorySize;
	protected int remainingMemorySize;

	public MemoriaList() {
		blocosMemoria = new LinkedList<>();		
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

	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}
	
	public int getRemainingMemorySize() {
		return remainingMemorySize;
	}

	public void setRemainingMemorySize(int remainingMemorySize) {
		this.remainingMemorySize = remainingMemorySize;
	}

	public boolean alocouMemoria(int tamanhoBloco, int tamanhoMemoriaRestante, int idProcesso) {
		return true;
	}
	
	public void liberaMemoria(int idProcesso) {		
	}
}
