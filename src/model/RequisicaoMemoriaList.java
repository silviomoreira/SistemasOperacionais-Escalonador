package model;

import java.util.LinkedList;
import java.util.ListIterator;

public class RequisicaoMemoriaList {

	protected LinkedList<RequisicaoMemoria> blocos;

	public RequisicaoMemoriaList() {
		blocos = new LinkedList<>();
	}

	public void add(RequisicaoMemoria r) {//, int x) {
		synchronized (this) {
			int i = buscaPorTamanhoBlocoERetPos(r.getTamanhoBloco());
			if (i == -1)
				blocos.add(r); //blocos.put(x, r);
			else
			{
				// atualiza incrementando a incidencia
				r.setIncidencia(1);
				blocos.set(i, r);
			}
		}		
	}
	public void remove(int x) {
		blocos.remove(x);
	}
	
	public RequisicaoMemoria get(int x){
		RequisicaoMemoria r = blocos.get(x);
		return r;
	}

	public int size(){
		return blocos.size();
	}
	
	public void reset() {
		blocos = new LinkedList<>();		
	}
	
	public RequisicaoMemoria buscaPorTamanhoBloco(int t){
		ListIterator<RequisicaoMemoria> liter = blocos.listIterator();
		while(liter.hasNext()){
			RequisicaoMemoria r = liter.next();
			if (r.getTamanhoBloco() == t)
				return r;			
		}
		return null;
	}
	public int buscaPorTamanhoBlocoERetPos(int t){
		ListIterator<RequisicaoMemoria> liter = blocos.listIterator();
		int i = 0;
		while(liter.hasNext()){
			if (liter.next().getTamanhoBloco() == t)
				return i;	
			i++;
		}
		return -1;
	}
	
	public LinkedList<RequisicaoMemoria> getAll(){	//public TreeMap getAll(){
		return blocos;
	}

}
