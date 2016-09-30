package gui.centerLayout;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;

import model.Processo;

public class ListProcessoPanel extends JPanel {
	private JTable table;
	private ProcessoTableModel tableModel;
	
	public ListProcessoPanel() {
		super();
		
		table = new JTable();
		tableModel = new ProcessoTableModel();
		
		Border innerBorder = BorderFactory.createTitledBorder("Lista de Processos");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		add(table, BorderLayout.CENTER);
	}
	
	public void setData(List<Processo> processos) {
		tableModel.setData(processos); 
	}
}
