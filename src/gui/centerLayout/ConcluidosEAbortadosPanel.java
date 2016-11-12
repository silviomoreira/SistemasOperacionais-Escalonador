package gui.centerLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;


public class ConcluidosEAbortadosPanel extends JPanel{

	private JTable table;
	
	public ConcluidosEAbortadosPanel(JTable table) {
		super();
		Dimension dim = getPreferredSize();
		dim.height = 100;
		setPreferredSize(dim);

		this.table = table;
		
		Border innerBorder = BorderFactory.createTitledBorder("Concluidos/Abortados");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5); // TOP, LEFT, BOTTOM, RIGHT
		JScrollPane scrollPaneProcessTable = new JScrollPane(table);
		scrollPaneProcessTable.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());
		
		add(scrollPaneProcessTable, BorderLayout.CENTER);
	}

}
