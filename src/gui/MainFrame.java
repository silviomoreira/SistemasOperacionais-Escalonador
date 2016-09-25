package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import topLayout.TopPanel;
import topLayout.TopPanelEvent;
import topLayout.TopPanelListener;

public class MainFrame extends JFrame {
	
	private TopPanel topPanel;
	
	public MainFrame() {
		super("Simulador de Escalonador de Processos");
		
		topPanel = new TopPanel();
		
		setLayout(new BorderLayout());
		
		topPanel.setTopPanelListener(new TopPanelListener() {
			
			@Override
			public void topPanelEventOccurred(TopPanelEvent e) {
				System.out.println("Quantidade de Processadores: " + e.getQdeProcessadores());
				System.out.println("Numero de processos iniciais: " + e.getNumProcessosIniciais());
			}
		});
		
		add(topPanel, BorderLayout.NORTH);
		
		setVisible(true);
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
