package gui.centerLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ProcessadoresPanel extends JPanel{
	private List<CorePanel> corePanels;
	private int numProcessadores = 0;
	
	public ProcessadoresPanel() {
		super();
		Dimension dim = getPreferredSize();
		dim.height = 100;
		setPreferredSize(dim);
		
		Border innerBorder = BorderFactory.createTitledBorder("Processadores");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

	}
	
	public void setNumProcessadores(int numProcessadores){
		this.numProcessadores = numProcessadores;
		MontaAreaProcessadores();
	}
	
	private void MontaAreaProcessadores(){
		// Criação de caixas p/ representação dos processadores
		removeAll();
		corePanels = new ArrayList<CorePanel>();
		for(int i=0; i<numProcessadores; i++){
			corePanels.add(new CorePanel(i));
			add(corePanels.get(i), BorderLayout.WEST);			
		}
		this.repaint();
	}
}
