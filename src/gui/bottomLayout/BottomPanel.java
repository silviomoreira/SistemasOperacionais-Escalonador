package gui.bottomLayout;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;

import model.BlocoMemoria;

public class BottomPanel extends JPanel {

	private JTable tableMemoria;
	private MemoriaTableModel tableModelMemoria;
	private MemoriaPanel memoriaPanel;

	public BottomPanel() {
		super();
		
		tableModelMemoria = new MemoriaTableModel();
		tableMemoria = new JTable(tableModelMemoria);
		memoriaPanel = new MemoriaPanel(tableMemoria);
		
		setLayout(new BorderLayout());
		
		add(memoriaPanel, BorderLayout.CENTER);	
	}

	public void setDataListMemoria(List<BlocoMemoria> blocosMemoria) {
		tableModelMemoria.setData(blocosMemoria);
	}
	
	public void refreshMemoria() {
		tableModelMemoria.fireTableDataChanged();
	}
}
