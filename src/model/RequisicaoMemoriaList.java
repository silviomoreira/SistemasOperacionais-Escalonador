package model;

import java.util.LinkedList;
import java.util.ListIterator;

public class RequisicaoMemoriaList {

	protected LinkedList<RequisicaoMemoria> blocos;
    private int iPosicaoNaLista;
	// chaveia a ordenação entre a padrão: incidencia | ou a não-padrão: número da lista + incidencia
	public static volatile boolean bOrdenacaoPadrao = true; 
    
	public RequisicaoMemoriaList() {
		blocos = new LinkedList<>();
	}

	public void add(int iTamanhoBloco) { //, int x) {
		synchronized (this) {
			RequisicaoMemoria r = buscaPorTamanhoBloco(iTamanhoBloco);
			if (r == null) {
				r = new RequisicaoMemoria(iTamanhoBloco);
				blocos.add(r); //blocos.put(x, r);
			}
			else
			{
				// atualiza incrementando a incidencia
				r.setIncidencia(1);
				blocos.set(iPosicaoNaLista, r);
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
		iPosicaoNaLista = 0;
		while(liter.hasNext()){
			RequisicaoMemoria r = liter.next();
			if (r.getTamanhoBloco() == t)
				return r;			
			iPosicaoNaLista++;
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
