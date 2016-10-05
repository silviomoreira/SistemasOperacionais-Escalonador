package gui.centerLayout;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import model.Processo;

public class CenterPanel extends JPanel {
	
	private JTable table;
	private ProcessoTableModel tableModel;
	
	private ProcessadoresPanel processadoresPanel;
	
	
	public CenterPanel() {
		super();
		processadoresPanel = new ProcessadoresPanel();
		
		tableModel = new ProcessoTableModel();
		table = new JTable(tableModel);
		
		Border innerBorder = BorderFactory.createTitledBorder("Lista de Processos");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		JScrollPane scrollPaneProcessTable = new JScrollPane(table);
		scrollPaneProcessTable.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());
		
		add(processadoresPanel, BorderLayout.NORTH);
		add(scrollPaneProcessTable, BorderLayout.CENTER);
	}
	
	public void setData(List<Processo> processos, int numProcessadores) {
		tableModel.setData(processos);
		processadoresPanel.setNumProcessadores(numProcessadores);
	}
	
	public void refresh() {
		tableModel.fireTableDataChanged();
	}
	
	
}
