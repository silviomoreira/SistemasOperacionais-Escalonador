package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import controller.Controller;
import controller.DispacherLTG;
import controller.DispacherRoundRobin;
import gui.bottomLayout.BottomPanel;
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
	protected BottomPanel bottomPanel;
	
	private Controller controller;
	
	public MainFrame() {
		super("Simulador de Escalonador de Processos");
		
		topPanel = new TopPanel();
		leftPanel = new LeftPanel();
		centerPanel = new CenterPanel();
		bottomPanel = new BottomPanel();
		
		controller = new Controller(leftPanel, centerPanel);// passar bottomPanel ?

		centerPanel.setDataListProcessos(controller.getProcessos());
		centerPanel.setDataListProcessadores(controller.getProcessadoresList(), 0);
		centerPanel.setDataListConcluidosEAbortados(controller.getConcluidosEAbortadosList());
		bottomPanel.setDataListMemoria(controller.getMemoriaList());
		setLayout(new BorderLayout());

		topPanel.setTopPanelListener(new TopPanelListener() {	
			@Override
			public void topPanelEventOccurred(TopPanelEvent e) {
				controller.resetProcessos();
				centerPanel.setDataListProcessos(controller.getProcessos());
				centerPanel.setDataListProcessadores(controller.getProcessadoresList(), e.getQdeProcessadores());
				centerPanel.setDataListConcluidosEAbortados(controller.getConcluidosEAbortadosList());				
				bottomPanel.setDataListMemoria(controller.getMemoriaList());
				
				controller.iniciarSimulacao(e);
				// refresh na tela com os processos iniciais prontos p/ serem iniciados 
				centerPanel.refreshProcessos();
				centerPanel.refreshProcessadores();
				centerPanel.refreshConcluidosEAbortados();
				bottomPanel.refreshMemoria();
				if (e.getEstrategia() == "Round Robin") {
					DispacherRoundRobin dispacherRR = new DispacherRoundRobin(
							controller.getProcessos(), controller.getProcessadoresList(),
							controller.getConcluidosEAbortadosList(), 
							centerPanel, e.getQdeProcessadores(), e.getNumProcessosIniciais(), 
							e.getQuantum(), controller.getMemoriaList());
					Thread thread = new Thread(dispacherRR);
					thread.start();
				} else if (e.getEstrategia() == "Least Time to Go (LTG)") {
					DispacherLTG dispacherLTG = new DispacherLTG(
							/////controller.getProcessos(), controller.getProcessadoresObj(),
							controller.getProcessos(), controller.getProcessadoresList(),
							controller.getConcluidosEAbortadosList(),
							centerPanel, e.getQdeProcessadores(), e.getNumProcessosIniciais());
					Thread thread = new Thread(dispacherLTG);				
					thread.start();
				}
			}
		});
		
		leftPanel.setLeftPanelListener(new LeftPanelListener() {
			
			@Override
			public void leftPanelEventOccurred(LeftPanelEvent e) {
				
				controller.adicionarProcesso(e);
				centerPanel.refreshProcessos();
			}
		});
		
		
		
		
		add(topPanel, BorderLayout.NORTH);
		add(leftPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
		setVisible(true);
		setSize(1200, 600);//(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
