package gui.bottomLayout;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.BlocoMemoria;

public class MemoriaTableModel extends AbstractTableModel {

	private List<BlocoMemoria> blocosMemoria;
	private String[] colNames =
		{"Id", "Tamanho", "Espaço usado", "Id do Processo", "Id lógico", "ListaDeOrigem"};//"Referência prox. bloco"};
	
	public MemoriaTableModel() {
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
		return 6;
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
		case 5: 
			return blocoMemoria.getListaDeOrigem();
		}

		return null;
	}
	
}
