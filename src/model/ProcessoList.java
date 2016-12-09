package model;

import java.util.ArrayList;

public class ProcessoList {
	private ArrayList<Processo> processos;
	
	public ProcessoList() {
		processos = new ArrayList<>();		
	}
	
	public void add(Processo processo) {
		processos.add(processo);
	}
	
	public void insert(int index, Processo processo) {
		processos.add(index, processo);
	}
	
	public ArrayList<Processo> getAll() {		
		return processos;
	}
	
	public Processo remove(int index) {
		return processos.remove(index);
	}
	
	public int size() {
		return processos.size();
	}
	
	public void reset() {
		processos = new ArrayList<>();
		Processo p = new Processo(0, "0", "CORE_LIVRE", "0", 0, "0", "");
		p.resetId();
	}
	
	public Processo get(int index) {
		return processos.get(index);
	}
	
	public void set(int index, Processo processo) {
		processos.set(index, processo);
	}
}
