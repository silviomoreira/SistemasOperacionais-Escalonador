package gui.centerLayout;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class CenterPanel extends JPanel {
	
	private ProcessadoresPanel processadoresPanel;
	private ListProcessoPanel listProcessoPanel;
	
	public CenterPanel() {
		super();
		
		processadoresPanel = new ProcessadoresPanel();
		listProcessoPanel = new ListProcessoPanel();
		
		setLayout(new BorderLayout());
		
		add(processadoresPanel, BorderLayout.NORTH);
		add(listProcessoPanel, BorderLayout.CENTER);
	}
}
