package gui.topLayout;

import java.util.EventObject;

public class TopPanelEvent extends EventObject {
	int qdeProcessadores;
	int numProcessosIniciais;
	String estrategia;
	float quantum;
	
	public TopPanelEvent(Object source) {
		super(source);
	}
	
	public TopPanelEvent(Object source, String estrategia, int qdeProcessadores, int numProcessosIniciais, float quantum) {
		super(source);
		
		this.estrategia = estrategia;
		this.qdeProcessadores = qdeProcessadores;
		this.numProcessosIniciais= numProcessosIniciais;
		this.quantum = quantum;
	}

	public int getQdeProcessadores() {
		return qdeProcessadores;
	}

	public void setQdeProcessadores(int qdeProcessadores) {
		this.qdeProcessadores = qdeProcessadores;
	}

	public int getNumProcessosIniciais() {
		return numProcessosIniciais;
	}

	public void setNumProcessosIniciais(int numProcessosIniciais) {
		this.numProcessosIniciais = numProcessosIniciais;
	}
	
	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}
	
	public float getQuantum() {
		return quantum;
	}

	public void setQuantum(float quantum) {
		this.quantum = quantum;
	}
}