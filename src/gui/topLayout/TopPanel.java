package gui.topLayout;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class TopPanel extends JPanel{
	private JLabel estrategiaLabel;
	private JComboBox estrategiaField;
	
	private JLabel qdeProcessadoresLabel;
	private JComboBox qdeProcessadoresField;
	
	private JLabel numProcessosIniciaisLabel;
	private JTextField numProcessosIniciaisField;
	
	private JButton iniciarBtn;

	private TopPanelListener topPanelListener;
	
	public TopPanel() {
		setBorder(BorderFactory.createEtchedBorder());
		
		estrategiaLabel = new JLabel("Estrategia: ");
		estrategiaField = new JComboBox<>();
		DefaultComboBoxModel estrategiaModel = new DefaultComboBoxModel();
		estrategiaModel.addElement("Round Robin");
		estrategiaModel.addElement("Least Time to Go (LTG)");
		estrategiaField.setModel(estrategiaModel);
		
		qdeProcessadoresLabel = new JLabel("Qde. Processadores: ");
		qdeProcessadoresField = new JComboBox();
		DefaultComboBoxModel qdeProcessadoresModel = new DefaultComboBoxModel();
		for(int i=1; i<=64; i++) 
			qdeProcessadoresModel.addElement(i);
		qdeProcessadoresField.setModel(qdeProcessadoresModel);
		
		numProcessosIniciaisLabel = new JLabel("Num. Processos Iniciais: ");
		numProcessosIniciaisField = new JTextField(3);
		
		iniciarBtn = new JButton("Iniciar!");
		iniciarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String estrategia = estrategiaField.getSelectedItem().toString();
				int qdeProcessadores = Integer.parseInt(qdeProcessadoresField.getSelectedItem().toString());
				int numProcessosIniciais = numProcessosIniciaisField.getText().equals("") ? 0 : Integer.parseInt(numProcessosIniciaisField.getText());
				
				TopPanelEvent tpe = new TopPanelEvent(this, estrategia, qdeProcessadores, numProcessosIniciais);
				
				if(topPanelListener != null) {
					topPanelListener.topPanelEventOccurred(tpe);
				}
			}
		});
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(estrategiaLabel);
		add(estrategiaField);
		
		add(qdeProcessadoresLabel);
		add(qdeProcessadoresField);
		
		add(numProcessosIniciaisLabel);
		add(numProcessosIniciaisField);
		
		add(iniciarBtn);
	}
	
	public void setTopPanelListener(TopPanelListener topPanelListener) {
		this.topPanelListener = topPanelListener;
	}
}
