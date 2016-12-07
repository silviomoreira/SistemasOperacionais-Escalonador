package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import controller.Controller;
import controller.DispacherLTG;
import controller.DispacherRoundRobin;
import gui.bottomLayout.BottomPanel;
import gui.bottomLayout.BottomPanelEvent;//
import gui.centerLayout.CenterPanel;
import gui.centerLayout.CenterPanelEvent;
import gui.leftLayout.LeftPanel;
import gui.leftLayout.LeftPanelEvent;
import gui.leftLayout.LeftPanelListener;
import gui.rightLayout.RightPanel;
import gui.topLayout.TopPanel;
import gui.topLayout.TopPanelEvent;
import gui.topLayout.TopPanelListener;

public class MainFrame extends JFrame {
	
	private TopPanel topPanel;
	protected LeftPanel leftPanel;
	protected CenterPanel centerPanel;
	protected BottomPanel bottomPanel;
	protected RightPanel rightPanel;
	
	private Controller controller;
	
	public MainFrame() {
		super("Simulador de Escalonador de Processos");
		
		topPanel = new TopPanel();
		leftPanel = new LeftPanel();
		centerPanel = new CenterPanel();
		bottomPanel = new BottomPanel();
		rightPanel = new RightPanel();
		
		controller = new Controller(leftPanel, centerPanel);

		centerPanel.setDataListProcessos(controller.getProcessos());
		centerPanel.setDataListProcessadores(controller.getProcessadoresList(), 0);
		centerPanel.setDataListConcluidosEAbortados(controller.getConcluidosEAbortadosList());
		bottomPanel.setDataListMemoria(controller.getMemoriaList());//controller.getBestfitList());
		bottomPanel.setDataListMemoriaHD(controller.getMemoriaHDList()); 
		rightPanel.setDataListRequisicoesMemoria(controller.getRequisicaoMemoriaList());
		setLayout(new BorderLayout());

		topPanel.setTopPanelListener(new TopPanelListener() {	
			@Override
			public void topPanelEventOccurred(TopPanelEvent e) {
				controller.resetProcessos();
				centerPanel.setDataListProcessos(controller.getProcessos());
				centerPanel.setDataListProcessadores(controller.getProcessadoresList(), e.getQdeProcessadores());
				centerPanel.setDataListConcluidosEAbortados(controller.getConcluidosEAbortadosList());	
				if (e.getEstrategiaMem() == "Best fit") {
					bottomPanel.setDataListMemoria(controller.getBestfitList());//controller.getMemoriaList());
					bottomPanel.setDataListMemoriaHD(controller.getMemoriaHDList()); 			
				}
				else if (e.getEstrategiaMem() == "Quick fit"){
					bottomPanel.setDataListMemoria(controller.getQuickfitList());
					bottomPanel.setDataListMemoriaHD(controller.getMemoriaHDList()); 
					rightPanel.setDataListRequisicoesMemoria(controller.getRequisicaoMemoriaList());					
				}
				/*else if (e.getEstrategiaMem() == "Merge fit")
					bottomPanel.setDataListMemoria(controller.getQuickfitList());*/
				
				controller.iniciarSimulacao(e);
				// refresh na tela com os processos iniciais prontos p/ serem iniciados 
				centerPanel.refreshProcessos();
				centerPanel.refreshProcessadores();
				centerPanel.refreshConcluidosEAbortados();
				bottomPanel.refreshMemoria();
				bottomPanel.refreshMemoriaHD();
				if (e.getEstrategia() == "Round Robin") {
					DispacherRoundRobin dispacherRR = null;
					if (e.getEstrategiaMem() == "Best fit") 
						dispacherRR = new DispacherRoundRobin(
								controller.getProcessos(), controller.getProcessadoresList(),
								controller.getConcluidosEAbortadosList(), 
								centerPanel, e.getQdeProcessadores(), e.getNumProcessosIniciais(), 
								e.getQuantum(), bottomPanel, e.getTamanhoMem(), controller.getBestfitList(),
								controller.getBestfitObj(), rightPanel/*, controller.getRequisicaoMemoriaList(),
								controller.getRequisicaoMemoriaObj()*/);//controller.getMemoriaList());
					else if (e.getEstrategiaMem() == "Quick fit") {
						rightPanel.refreshRequisicoesMemoria();						
						dispacherRR = new DispacherRoundRobin(
								controller.getProcessos(), controller.getProcessadoresList(),
								controller.getConcluidosEAbortadosList(), 
								centerPanel, e.getQdeProcessadores(), e.getNumProcessosIniciais(), 
								e.getQuantum(), bottomPanel, e.getTamanhoMem(), controller.getQuickfitList(),
								controller.getQuickfitObj(), rightPanel/*, controller.getRequisicaoMemoriaList(),
								controller.getRequisicaoMemoriaObj()*/);
					}
					/*else if (e.getEstrategiaMem() == "Merge fit")
						dispacherRR = new DispacherRoundRobin(
								controller.getProcessos(), controller.getProcessadoresList(),
								controller.getConcluidosEAbortadosList(), 
								centerPanel, e.getQdeProcessadores(), e.getNumProcessosIniciais(), 
								e.getQuantum(), bottomPanel, e.getTamanhoMem(), controller.getMergefitList(),
								controller.getMergefitObj());*/
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
		add(rightPanel, BorderLayout.EAST);
		
		setVisible(true);
		setSize(1300, 600);//(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
