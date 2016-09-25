package topLayout;

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
	private JLabel qdeProcessadoresLabel;
	private JComboBox qdeProcessadoresField;
	
	private JLabel numProcessosIniciaisLabel;
	private JTextField numProcessosIniciaisField;
	
	private JButton iniciarBtn;

	private TopPanelListener topPanelListener;
	
	public TopPanel() {
		setBorder(BorderFactory.createEtchedBorder());
		
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
				int qdeProcessadores = Integer.parseInt(qdeProcessadoresField.getSelectedItem().toString());
				int numProcessosIniciais = Integer.parseInt(numProcessosIniciaisField.getText());
				
				TopPanelEvent tpe = new TopPanelEvent(this, qdeProcessadores, numProcessosIniciais);
				
				if(topPanelListener != null) {
					topPanelListener.topPanelEventOccurred(tpe);
				}
			}
		});
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
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
