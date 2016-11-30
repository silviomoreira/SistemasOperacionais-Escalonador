package gui.rightLayout;

import java.awt.BorderLayout;
import java.util.List;

import gui.bottomLayout.MemoriaPanel;
import gui.bottomLayout.MemoriaTableModel;

import javax.swing.JPanel;
import javax.swing.JTable;

import model.BlocoMemoria;
import model.RequisicaoMemoria;

public class RightPanel extends JPanel {

	private JTable tableRequisicoesMemoria;
	private RequisicaoMemoriaTableModel tableModelRequisicoesMemoria;
	private RequisicaoMemoriaPanel requisicoesMemoriaPanel;

	public RightPanel() {
		super();
		
		tableModelRequisicoesMemoria = new RequisicaoMemoriaTableModel();
		tableRequisicoesMemoria = new JTable(tableModelRequisicoesMemoria);
		requisicoesMemoriaPanel = new RequisicaoMemoriaPanel(tableRequisicoesMemoria);
		
		setLayout(new BorderLayout());
		
		add(requisicoesMemoriaPanel, BorderLayout.CENTER);	
	}
	
	public void setDataListRequisicoesMemoria(List<RequisicaoMemoria> requisicoesMemoria) {
		tableModelRequisicoesMemoria.setData(requisicoesMemoria);
	}
	
	public void refreshRequisicoesMemoria() {
		tableModelRequisicoesMemoria.fireTableDataChanged();
	}
}
