package gui.centerLayout;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class CorePanel extends JPanel{
	private int numCore = -1;
	
	public int getNumCore() {
		return numCore;
	}

	public CorePanel(int numCore){
		super();
		this.numCore = numCore;
		Dimension dim = getPreferredSize();
		dim.height = 70;
		dim.width = 200;
		setPreferredSize(dim);
		
		Border innerBorder = BorderFactory.createTitledBorder("Core "+numCore);
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
	}
}
