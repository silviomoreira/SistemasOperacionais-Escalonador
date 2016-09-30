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
	
	public ArrayList<Processo> getAll() {
		return processos;
	}
	
	public Processo remove(int index) {
		return processos.remove(index);
	}
	
	public int size() {
		return processos.size();
	}
}
