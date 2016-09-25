package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	
	private TopPanel topPanel;
	
	public MainFrame() {
		super("Simulador de Escalonador de Processos");
		
		topPanel = new TopPanel();
		
		setLayout(new BorderLayout());
		
		add(topPanel, BorderLayout.NORTH);
		
		setVisible(true);
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
