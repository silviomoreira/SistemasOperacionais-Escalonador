package gui.centerLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

public class ProcessoPanel extends JPanel{

	private JTable table;
	
	public ProcessoPanel(JTable table) {
		super();
		Dimension dim = getPreferredSize();
		dim.height = 260;
		setPreferredSize(dim);

		this.table = table;

		Border innerBorder = BorderFactory.createTitledBorder("Lista de Processos"); 
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		JScrollPane scrollPaneProcessTable = new JScrollPane(table);
		scrollPaneProcessTable.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());
		
		add(scrollPaneProcessTable, BorderLayout.CENTER);
	}
	
}
