package gui.centerLayout;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ProcessadoresPanel extends JPanel{
	public ProcessadoresPanel() {
		super();
		Dimension dim = getPreferredSize();
		dim.height = 100;
		setPreferredSize(dim);
		
		Border innerBorder = BorderFactory.createTitledBorder("Processadores");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
	}
}
