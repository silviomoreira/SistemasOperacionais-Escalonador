package gui.centerLayout;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Processo;

public class ProcessoTableModel extends AbstractTableModel {
		
	private List<Processo> processos;
	private String[] colNames = 
			{"Id", "Tempo Total", "Estado", "Tempo Restante", "Prioridade", "Deadline", "Intervalo"};
	
	public void ProcessoTableModel() {
	}
	
	public void setData(List<Processo> processos) {
		this.processos = processos;
	}
	
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return colNames[column];
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return processos.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		Processo processo = processos.get(row);
		switch (col) {
		case 0:
			return processo.getIdentificadorProcesso();
		case 1:
			return processo.getTempoTotalExecucao();
		case 2:
			return processo.getEstadoProcesso();
		case 3:
			return processo.getTempoExecucaoRestante();
		case 4:
			return processo.getPrioridade();
		case 5:
			return processo.getDeadline();
		case 6:
			return processo.getIntervalo();
		}
		
		return null;
	}
}
