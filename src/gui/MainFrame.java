package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import leftLayout.LeftPanel;
import topLayout.TopPanel;
import topLayout.TopPanelEvent;
import topLayout.TopPanelListener;

public class MainFrame extends JFrame {
	
	private TopPanel topPanel;
	private LeftPanel leftPanel;
	
	public MainFrame() {
		super("Simulador de Escalonador de Processos");
		
		topPanel = new TopPanel();
		leftPanel = new LeftPanel();
		
		setLayout(new BorderLayout());
		
		topPanel.setTopPanelListener(new TopPanelListener() {
			
			@Override
			public void topPanelEventOccurred(TopPanelEvent e) {
				System.out.println("Estrategia: " + e.getEstrategia());
				System.out.println("Quantidade de Processadores: " + e.getQdeProcessadores());
				System.out.println("Numero de processos iniciais: " + e.getNumProcessosIniciais());
			}
		});
		
		add(topPanel, BorderLayout.NORTH);
		add(leftPanel, BorderLayout.WEST);
		
		setVisible(true);
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
