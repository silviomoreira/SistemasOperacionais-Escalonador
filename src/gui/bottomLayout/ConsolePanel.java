package gui.bottomLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class ConsolePanel extends JPanel{

	private JTextArea area; 
	
	public ConsolePanel(JTextArea area) {
		super();
		Dimension dim = getPreferredSize();
		dim.height = 170; 
		dim.width = 450; 
		setPreferredSize(dim);
		
		this.area = area; 

		Border innerBorder = BorderFactory.createTitledBorder("Console-Log"); 
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		JScrollPane scrollPaneconsoleField = new JScrollPane(area);
		scrollPaneconsoleField.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());
		
		add(scrollPaneconsoleField, BorderLayout.CENTER);
	}
}
