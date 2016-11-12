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
	
	private JTable tableProcessos;
	private ProcessoTableModel tableModelProcessos;
	
	private JTable tableProcessadores;///
	private ProcessadoresTableModel tableModelProcessadores;///
	private ProcessadoresPanel processadoresPanel;///
	private int numProcessadores = 0;///	
	
	public CenterPanel() {
		super();
		/////processadoresPanel = new ProcessadoresPanel();///

		tableModelProcessos = new ProcessoTableModel();
		tableProcessos = new JTable(tableModelProcessos);
		tableModelProcessadores = new ProcessadoresTableModel();///
		tableProcessadores = new JTable(tableModelProcessadores);///
		
		processadoresPanel = new ProcessadoresPanel(tableProcessadores);/////
		///// comentado
		/*Border innerBorderProcessadores = BorderFactory.createTitledBorder("Processadores");
		Border outerBorderProcessadores = BorderFactory.createEmptyBorder(5, 5, 5, 5); // TOP, LEFT, BOTTOM, RIGHT
		//setBorder(BorderFactory.createCompoundBorder(outerBorderProcessadores, innerBorderProcessadores));
		JScrollPane scrollPaneProcessadoresTable = new JScrollPane(tableProcessadores);
		//scrollPaneProcessadoresTable.setBorder(BorderFactory.createCompoundBorder(outerBorderProcessadores, innerBorderProcessadores));
		setLayout(new BorderLayout());*/
		
		Border innerBorder = BorderFactory.createTitledBorder("Lista de Processos");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		JScrollPane scrollPaneProcessTable = new JScrollPane(tableProcessos);
		scrollPaneProcessTable.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());

		add(processadoresPanel, BorderLayout.NORTH);///
		/////add(scrollPaneProcessadoresTable, BorderLayout.NORTH);///
		add(scrollPaneProcessTable, BorderLayout.CENTER);///	
	}
	
	public void setDataListProcessos(List<Processo> processos) {
		tableModelProcessos.setData(processos);
	}
	public void setDataListProcessadores(List<Processo> processos, int numProcessadores) {
		tableModelProcessadores.setData(processos);////setDataProcessadores(processos);/// 
		setNumProcessadores(numProcessadores);
	}

	public void refreshProcessos() {
		tableModelProcessos.fireTableDataChanged();
	}

	public void refreshProcessadores() {
		tableModelProcessadores.fireTableDataChanged();
	}
	
	public void setNumProcessadores(int numProcessadores){
		this.numProcessadores = numProcessadores;
	}

	public ProcessadoresPanel getProcessadoresPanel(){
		return processadoresPanel;
	}
}
