package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import gui.centerLayout.CenterPanel;
import gui.leftLayout.LeftPanel;
import gui.topLayout.TopPanel;
import gui.topLayout.TopPanelEvent;
import gui.topLayout.TopPanelListener;

public class MainFrame extends JFrame {
	
	private TopPanel topPanel;
	private LeftPanel leftPanel;
	private CenterPanel centerPanel;
	
	public MainFrame() {
		super("Simulador de Escalonador de Processos");
		
		topPanel = new TopPanel();
		leftPanel = new LeftPanel();
		centerPanel = new CenterPanel();
		
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
		add(centerPanel, BorderLayout.CENTER);
		
		setVisible(true);
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
