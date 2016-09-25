package gui;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class TopPanel extends JPanel{
	private JLabel qdeProcessadoresLabel;
	private JTextField qdeProcessadoresField;
	
	private JLabel numProcessosIniciaisLabel;
	private JTextField numProcessosIniciaisField;
	
	private JButton iniciarBtn;

	public TopPanel() {
		setBorder(BorderFactory.createEtchedBorder());
		
		qdeProcessadoresLabel = new JLabel("Qde. Processadores: ");
		qdeProcessadoresField = new JTextField(3);

		numProcessosIniciaisLabel = new JLabel("Num. Processos Iniciais: ");
		numProcessosIniciaisField = new JTextField(3);
		
		iniciarBtn = new JButton("Iniciar!");
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(qdeProcessadoresLabel);
		add(qdeProcessadoresField);
		
		add(numProcessosIniciaisLabel);
		add(numProcessosIniciaisField);
		
		add(iniciarBtn);
	}
}
