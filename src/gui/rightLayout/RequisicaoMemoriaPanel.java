package gui.rightLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

public class RequisicaoMemoriaPanel  extends JPanel{

	private JTable table;

	public RequisicaoMemoriaPanel(JTable table) {
		super();
		Dimension dim = getPreferredSize();
		dim.height = 140;
		dim.width = 150;
		setPreferredSize(dim);

		this.table = table;

		Border innerBorder = BorderFactory.createTitledBorder("Req. Memória"); 
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		JScrollPane scrollPaneProcessTable = new JScrollPane(table);
		scrollPaneProcessTable.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());
		
		add(scrollPaneProcessTable, BorderLayout.CENTER);

	}

}
