package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import controller.Controller;
import controller.DispacherLTG;
import gui.centerLayout.CenterPanel;
import gui.centerLayout.CenterPanelEvent;
import gui.leftLayout.LeftPanel;
import gui.leftLayout.LeftPanelEvent;
import gui.leftLayout.LeftPanelListener;
import gui.topLayout.TopPanel;
import gui.topLayout.TopPanelEvent;
import gui.topLayout.TopPanelListener;

public class MainFrame extends JFrame {
	
	private TopPanel topPanel;
	protected LeftPanel leftPanel;
	protected CenterPanel centerPanel;
	
	private Controller controller;
	
	public MainFrame() {
		super("Simulador de Escalonador de Processos");
		
		topPanel = new TopPanel();
		leftPanel = new LeftPanel();
		centerPanel = new CenterPanel();
		
		controller = new Controller(leftPanel, centerPanel);

		centerPanel.setData(controller.getProcessos(),0);
		
		setLayout(new BorderLayout());

		topPanel.setTopPanelListener(new TopPanelListener() {	
			@Override
			public void topPanelEventOccurred(TopPanelEvent e) {
				controller.resetProcessos();
				centerPanel.setData(controller.getProcessos(), e.getQdeProcessadores());
				controller.iniciarSimulacao(e);
				// refresh na tela com os processos iniciais prontos p/ serem iniciados 
				centerPanel.refresh();
				if (e.getEstrategia() != "Round Robin") {
					DispacherLTG dispacherLTG = new DispacherLTG(controller.getProcessos(), centerPanel, 
							e.getQdeProcessadores(), e.getNumProcessosIniciais());
					Thread thread = new Thread(dispacherLTG);
					thread.start();
				}
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
