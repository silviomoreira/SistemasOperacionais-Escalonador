package topLayout;

import java.util.EventObject;

public class TopPanelEvent extends EventObject {
	int qdeProcessadores;
	int numProcessosIniciais;
	
	public TopPanelEvent(Object source) {
		super(source);
	}
	
	public TopPanelEvent(Object source, int qdeProcessadores, int numProcessosIniciais) {
		super(source);
		
		this.qdeProcessadores = qdeProcessadores;
		this.numProcessosIniciais= numProcessosIniciais;
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
	
}