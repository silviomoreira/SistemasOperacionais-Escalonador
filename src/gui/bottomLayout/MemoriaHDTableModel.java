package gui.bottomLayout;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.BlocoMemoria;

public class MemoriaHDTableModel extends AbstractTableModel {

	private List<BlocoMemoria> blocosMemoria;
	private String[] colNames =
		{"Id", "Tamanho", "Espa�o usado", "Id do Processo", "Id l�gico"};

	public MemoriaHDTableModel() {
	}

	public void setData(List<BlocoMemoria> blocosMemoria) {
		this.blocosMemoria = blocosMemoria;
	}

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return colNames[column];
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return blocosMemoria.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public Object getValueAt(int row, int col) {
		BlocoMemoria blocoMemoria = blocosMemoria.get(row);
		switch (col) {
		case 0:
			return blocoMemoria.getIdBloco();
		case 1:
			return blocoMemoria.getTamanho();
		case 2:
			return blocoMemoria.getEspacoUsado();
		case 3:
			return blocoMemoria.getIdProcesso();
		case 4:
			return blocoMemoria.getIdLogicoBloco();
		}

		return null;
	}

}
