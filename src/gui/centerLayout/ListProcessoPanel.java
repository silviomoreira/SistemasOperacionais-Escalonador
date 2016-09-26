package gui.centerLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ListProcessoPanel extends JPanel {
	public ListProcessoPanel() {
		super();
		
		Border innerBorder = BorderFactory.createTitledBorder("Lista de Processos");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
	}
}
