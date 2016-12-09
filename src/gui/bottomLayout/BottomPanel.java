package gui.bottomLayout;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;

import model.BlocoMemoria;

public class BottomPanel extends JPanel {

	private JTable tableMemoria;
	private MemoriaTableModel tableModelMemoria;
	private MemoriaPanel memoriaPanel;
	private ConsolePanel consolePanel;
	private JTextArea consoleArea; 
	private JTable tableMemoriaHD;
	private MemoriaHDTableModel tableModelMemoriaHD;
	private MemoriaHDPanel memoriaHDPanel;

	public BottomPanel() {
		super();
		
		tableModelMemoria = new MemoriaTableModel();
		tableMemoria = new JTable(tableModelMemoria);
		memoriaPanel = new MemoriaPanel(tableMemoria);
		consoleArea = new JTextArea();
		consolePanel = new ConsolePanel(consoleArea);
		tableModelMemoriaHD = new MemoriaHDTableModel();
		tableMemoriaHD = new JTable(tableModelMemoriaHD);
		memoriaHDPanel = new MemoriaHDPanel(tableMemoriaHD);
		
		setLayout(new BorderLayout());
		
		add(memoriaPanel, BorderLayout.WEST);	
		add(consolePanel, BorderLayout.CENTER);
		add(memoriaHDPanel, BorderLayout.EAST); 
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

	public void resetConsole() {
		this.consoleArea.setText(""); 
	}
	public void refreshConsole(String linha) {
		this.consoleArea.append(linha+"\n");
	}
}
