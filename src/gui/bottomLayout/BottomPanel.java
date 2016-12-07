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
	private JTable tableMemoriaHD;
	private MemoriaHDTableModel tableModelMemoriaHD;
	private MemoriaHDPanel memoriaHDPanel;

	public BottomPanel() {
		super();
		
		tableModelMemoria = new MemoriaTableModel();
		tableMemoria = new JTable(tableModelMemoria);
		memoriaPanel = new MemoriaPanel(tableMemoria);
		tableModelMemoriaHD = new MemoriaHDTableModel();
		tableMemoriaHD = new JTable(tableModelMemoriaHD);
		memoriaHDPanel = new MemoriaHDPanel(tableMemoriaHD);
		
		setLayout(new BorderLayout());
		
		add(memoriaPanel, BorderLayout.CENTER);	
		add(memoriaHDPanel, BorderLayout.SOUTH);	
	}

	public void setDataListMemoria(List<BlocoMemoria> blocosMemoria) {
		tableModelMemoria.setData(blocosMemoria);
	}
	
	public void refreshMemoria() {
		tableModelMemoria.fireTableDataChanged();
	}

	public void setDataListMemoriaHD(List<BlocoMemoria> blocosMemoria) {
		tableModelMemoriaHD.setData(blocosMemoria);
	}
	
	public void refreshMemoriaHD() {
		tableModelMemoriaHD.fireTableDataChanged();
	}
}
