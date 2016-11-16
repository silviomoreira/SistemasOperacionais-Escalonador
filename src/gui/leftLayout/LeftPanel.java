package gui.leftLayout;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import gui.topLayout.TopPanelEvent;
import gui.topLayout.TopPanelListener;

public class LeftPanel extends JPanel{
	private JLabel identificadorProcessoLabel;
	private JTextField identificadorProcessoField;
	
	private JLabel tempoTotalExecucaoLabel;
	private JTextField tempoTotalExecucaoField;
	
	private JLabel estadoProcessoLabel;
	private JTextField estadoProcessoField;
	
	private JLabel tempoExecucaoRestanteLabel;
	private JTextField tempoExecucaoRestanteField;
	
	private JLabel prioridadeLabel;
	private JTextField prioridadeField;
	
	private JLabel deadlineLabel;
	private JTextField deadlineField;
	
	private JLabel quantumLabel;
	private JTextField quantumField;

	private JLabel intervaloLabel;
	private JTextField intervaloField;
	
	private JButton addBtn;
	
	private LeftPanelListener leftPanelListener;
	
	private GridBagConstraints gc;
	
	public LeftPanel() {
		super();
		
		initializeComponents();
		
		setupLayout();

		estadoProcessoField.setText("P");
		addLine(identificadorProcessoLabel, identificadorProcessoField);
		addLine(tempoTotalExecucaoLabel, tempoTotalExecucaoField);
		addLine(estadoProcessoLabel, estadoProcessoField);
		addLine(tempoExecucaoRestanteLabel, tempoExecucaoRestanteField);
		addLine(deadlineLabel, deadlineField);
		addLine(quantumLabel, quantumField);
		addLine(intervaloLabel, intervaloField);
		
		addLine(null, addBtn);
	}
	
	public void initializeComponents() {
		identificadorProcessoLabel = new JLabel("Id do Processo: ");
		identificadorProcessoField = new JTextField(10);
		
		tempoTotalExecucaoLabel = new JLabel("Tempo total: ");
		tempoTotalExecucaoField = new JTextField(10);
		
		estadoProcessoLabel = new JLabel("Estado: ");
		estadoProcessoField = new JTextField(10);
		
		tempoExecucaoRestanteLabel = new JLabel("Tempo restante: ");
		tempoExecucaoRestanteField = new JTextField(10);
		
		prioridadeLabel = new JLabel("Prioridade: ");
		prioridadeField = new JTextField(10);
		
		deadlineLabel = new JLabel("Deadline: ");
		deadlineField = new JTextField(10);
		
		quantumLabel = new JLabel("Quantum: ");
		quantumField = new JTextField(10);
		
		intervaloLabel = new JLabel("Intervalo: ");
		intervaloField = new JTextField(10);
		
		addBtn = new JButton("Adicionar!");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tempoTotalExecucao = tempoTotalExecucaoField.getText();
				String estadoProcesso = estadoProcessoField.getText();
				String tempoExecucaoRestante = tempoExecucaoRestanteField.getText();
				int prioridade = prioridadeField.getText().equals("") ? 0 : Integer.parseInt(prioridadeField.getText());
				String deadline = deadlineField.getText();
				int quantum = quantumField.getText().equals("") ? 0 : Integer.parseInt(quantumField.getText()); 
				String intervalo = intervaloField.getText();
				
				LeftPanelEvent lpe = new LeftPanelEvent(this, tempoTotalExecucao, estadoProcesso, tempoExecucaoRestante, prioridade, deadline, quantum, intervalo);
				
				if(leftPanelListener != null) {
					leftPanelListener.leftPanelEventOccurred(lpe);
				}
			}
		});
	}

	
	public void setupLayout() {
		Dimension dim = getPreferredSize();
		dim.width = 350;
		setPreferredSize(dim);
		
		Border innerBorder = BorderFactory.createTitledBorder("Adicionar novo processo");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		setLayout(new GridBagLayout());
		
		gc = new GridBagConstraints();

		// line 1 config
		gc.weightx = 1;
		gc.weighty = 0.1;
		gc.fill = GridBagConstraints.NONE;
		
		gc.gridx = 0;
		gc.gridy = 0;
	}
	
	public void addLine(JComponent label, JComponent field) {
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		if(label != null)
			add(label, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		if(field != null)
			add(field, gc);
		
		gc.gridy++;
	}
	
	public void setLeftPanelListener(LeftPanelListener leftPanelListener) {
		this.leftPanelListener = leftPanelListener;
	}
	
	public void setidentificadorProcessoField(String id) {
		this.identificadorProcessoField.setText(id);
		this.identificadorProcessoField.repaint();
	}
}


