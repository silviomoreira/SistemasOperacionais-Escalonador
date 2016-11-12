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
	private JTable tableConcluidosEAbortados;///
	private ConcluidosEAbortadosTableModel tableModelConcluidosEAbortados;///
	
	private ProcessoPanel processoPanel; 
	private ProcessadoresPanel processadoresPanel;///
	private int numProcessadores = 0;///	
	private ConcluidosEAbortadosPanel concluidosEAbortadosPanel;
	
	public CenterPanel() {
		super();

		tableModelProcessos = new ProcessoTableModel();
		tableProcessos = new JTable(tableModelProcessos);
		tableModelProcessadores = new ProcessadoresTableModel();///
		tableProcessadores = new JTable(tableModelProcessadores);///
		tableModelConcluidosEAbortados = new ConcluidosEAbortadosTableModel();///
		tableConcluidosEAbortados = new JTable(tableModelConcluidosEAbortados);///
		
		processadoresPanel = new ProcessadoresPanel(tableProcessadores);/////
		concluidosEAbortadosPanel = new ConcluidosEAbortadosPanel(tableConcluidosEAbortados);
		processoPanel = new ProcessoPanel(tableProcessos);
		///// comentado
		/*Border innerBorderProcessadores = BorderFactory.createTitledBorder("Processadores");
		Border outerBorderProcessadores = BorderFactory.createEmptyBorder(5, 5, 5, 5); // TOP, LEFT, BOTTOM, RIGHT
		JScrollPane scrollPaneProcessadoresTable = new JScrollPane(tableProcessadores);
		//scrollPaneProcessadoresTable.setBorder(BorderFactory.createCompoundBorder(outerBorderProcessadores, innerBorderProcessadores));
		setLayout(new BorderLayout());*/
	    ////// comentado	
		/*Border innerBorder = BorderFactory.createTitledBorder("Lista de Processos");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		JScrollPane scrollPaneProcessTable = new JScrollPane(tableProcessos);
		scrollPaneProcessTable.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());*/

		/////add(scrollPaneProcessadoresTable, BorderLayout.NORTH);///
		//add(processadoresPanel, BorderLayout.NORTH);///
		//add(scrollPaneProcessTable, BorderLayout.CENTER);///

		setLayout(new BorderLayout());
		
		add(processadoresPanel, BorderLayout.NORTH);///NORTH
		add(concluidosEAbortadosPanel, BorderLayout.CENTER);///CENTER
		add(processoPanel, BorderLayout.SOUTH);///SOUTH
	}
	
	public void setDataListProcessos(List<Processo> processos) {
		tableModelProcessos.setData(processos);
	}
	public void setDataListProcessadores(List<Processo> processos, int numProcessadores) {
		tableModelProcessadores.setData(processos); 
		setNumProcessadores(numProcessadores);
	}
	public void setDataListConcluidosEAbortados(List<Processo> processos) {
		tableModelConcluidosEAbortados.setData(processos);
	}

	public void refreshProcessos() {
		tableModelProcessos.fireTableDataChanged();
	}
	public void refreshProcessadores() {
		tableModelProcessadores.fireTableDataChanged();
	}	
	public void refreshConcluidosEAbortados() {
		tableModelConcluidosEAbortados.fireTableDataChanged();
	}
	
	public void setNumProcessadores(int numProcessadores){
		this.numProcessadores = numProcessadores;
	}

	public ProcessadoresPanel getProcessadoresPanel(){
		return processadoresPanel;
	}
	public ConcluidosEAbortadosPanel getConcluidosEAbortadosPanel(){
		return concluidosEAbortadosPanel;
	}
}
