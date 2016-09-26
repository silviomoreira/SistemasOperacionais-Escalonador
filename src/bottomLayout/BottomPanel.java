package bottomLayout;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class BottomPanel extends JPanel{
	public BottomPanel() {
		super();
		
		Dimension dim = getPreferredSize();
		dim.height = 750;
		setSize(dim);
		
		Border innerBorder = BorderFactory.createTitledBorder("Processadores");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		setLayout(new GridBagLayout());
	}
}
