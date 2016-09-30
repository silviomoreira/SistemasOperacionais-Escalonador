package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import controller.Controller;
import gui.centerLayout.CenterPanel;
import gui.leftLayout.LeftPanel;
import gui.leftLayout.LeftPanelEvent;
import gui.leftLayout.LeftPanelListener;
import gui.topLayout.TopPanel;
import gui.topLayout.TopPanelEvent;
import gui.topLayout.TopPanelListener;

public class MainFrame extends JFrame {
	
	private TopPanel topPanel;
	private LeftPanel leftPanel;
	private CenterPanel centerPanel;
	
	private Controller controller;
	
	public MainFrame() {
		super("Simulador de Escalonador de Processos");
		
		topPanel = new TopPanel();
		leftPanel = new LeftPanel();
		centerPanel = new CenterPanel();
		
		controller = new Controller();
		
		centerPanel.setData(controller.getProcessos());
		
		setLayout(new BorderLayout());
		
		topPanel.setTopPanelListener(new TopPanelListener() {	
			@Override
			public void topPanelEventOccurred(TopPanelEvent e) {
				controller.iniciarSimulacao(e);
			}
		});
		
		leftPanel.setLeftPanelListener(new LeftPanelListener() {
			
			@Override
			public void leftPanelEventOccurred(LeftPanelEvent e) {
				
				controller.adicionarProcesso(e);
				centerPanel.refresh();
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
