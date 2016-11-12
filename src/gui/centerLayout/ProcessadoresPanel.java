package gui.centerLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import model.Processo;

public class ProcessadoresPanel extends JPanel{
//	private List<CorePanel> corePanels;
	private int numProcessadores = 0;
	private JTable table;//
	private ProcessadoresTableModel tableModel;///private ProcessoTableModel tableModel;//
		
	public ProcessadoresPanel() {
		super();
		Dimension dim = getPreferredSize();
		dim.height = 100;
		setPreferredSize(dim);

		tableModel = new ProcessadoresTableModel();///tableModel = new ProcessoTableModel();///
		table = new JTable(tableModel);
		
		Border innerBorder = BorderFactory.createTitledBorder("Processadores");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5); // TOP, LEFT, BOTTOM, RIGHT
//		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		JScrollPane scrollPaneProcessTable = new JScrollPane(table);
		scrollPaneProcessTable.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());
		
		add(scrollPaneProcessTable, BorderLayout.CENTER);
	}

	public void setData(List<Processo> processos) {
		tableModel.setData(processos);
	}
	
	public void refresh() {
		tableModel.fireTableDataChanged();
	}
	
	public void setNumProcessadores(int numProcessadores){
		this.numProcessadores = numProcessadores;
//		MontaAreaProcessadores();
	}
/*	
	private void MontaAreaProcessadores(){
		// Criação de caixas p/ representação dos processadores
		removeAll();
		corePanels = new ArrayList<CorePanel>();
		for(int i=0; i<numProcessadores; i++){
			corePanels.add(new CorePanel(i));
			add(corePanels.get(i), BorderLayout.WEST);			
		}
		this.repaint();
	}*/
}
