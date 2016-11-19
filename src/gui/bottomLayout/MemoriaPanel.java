package gui.bottomLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
//import java.util.ArrayList;
//import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

public class MemoriaPanel extends JPanel{

	//private List<CorePanel> corePanels;
	private JTable table;
	
	public MemoriaPanel(JTable table) {
		super();
		Dimension dim = getPreferredSize();
		dim.height = 140;
		setPreferredSize(dim);

		this.table = table;

		Border innerBorder = BorderFactory.createTitledBorder("Mem�ria"); 
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		JScrollPane scrollPaneProcessTable = new JScrollPane(table);
		scrollPaneProcessTable.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());
		//MontaAreaMemoria();
		
		add(scrollPaneProcessTable, BorderLayout.CENTER);
	}
	
	/*
	 * Id�ia de mostrar os blocos lateralmente. Funciona s� que tinha de contornar o problema
	 * da limita��o do BorderLayout. Talvez utilizar um esquema de layoutmais avan�ado que o
	 * BorderLayout.
	 */
    /*private void MontaAreaMemoria(){
		// Cria��o de caixas p/ representa��o dos blocos de mem�ria
		removeAll();
		corePanels = new ArrayList<CorePanel>();
		for(int i=0; i<2; i++){
			corePanels.add(new CorePanel(i));
			if (i < 1)
				add(corePanels.get(i), BorderLayout.WEST);
			else
				add(corePanels.get(i), BorderLayout.EAST);
		}
		this.repaint();
	}*/
	
}
