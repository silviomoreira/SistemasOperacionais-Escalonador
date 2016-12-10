package gui.rightLayout;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.RequisicaoMemoria;

public class RequisicaoMemoriaTableModel extends AbstractTableModel {

	private List<RequisicaoMemoria> requisicoesMemoria;
	private String[] colNames =
		{"T.bloco", "Incidencia", "Lista"};

	public RequisicaoMemoriaTableModel() {
	}
	
	public void setData(List<RequisicaoMemoria> requisicoesMemoria) {
		this.requisicoesMemoria = requisicoesMemoria;
	}
	
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return colNames[column];
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return requisicoesMemoria.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getValueAt(int row, int col) {
		RequisicaoMemoria requisicaoMemoria = requisicoesMemoria.get(row);
		switch (col) {
		case 0:
			return requisicaoMemoria.getTamanhoBloco();
		case 1:
			return requisicaoMemoria.getIncidencia();
		case 2:
			return requisicaoMemoria.getNumeroLista();
		}

		return null;
	}

}
