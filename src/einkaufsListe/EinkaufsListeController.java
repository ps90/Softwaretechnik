package einkaufsListe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import javax.swing.JFrame;
import javax.swing.table.TableColumn;

public class EinkaufsListeController implements ActionListener{

	private EinkaufsListePanel panel;
	private EinkaufsListeModel model;
	private ArrayList<TableColumn> columns = new ArrayList<TableColumn>();
	
	public EinkaufsListeController(){
		model = new EinkaufsListeModel();
		panel = new EinkaufsListePanel(this,model);
		
		columns.add(panel.getTable().getColumn("Gekaufte Menge (in Stk)"));
		columns.add(panel.getTable().getColumn("Preis (in €)"));
		columns.add(panel.getTable().getColumn("Zeile löschen"));
		setEditable(false);
		try {
			model.loadEinkaufsListe();
		} catch (EinkaufDAOLoadException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(panel,e.getMessage());
			
		}
	}
	
	public void setEditable(boolean editable){
		for (JButton button: panel.getButtons()){
			button.setVisible(editable);
		}
		
		if(editable){
			// Sonst eventuell doppelte Columns bei mehrfachaufruf von setEditable(true)
			for( TableColumn c: columns){
				panel.getTable().removeColumn(c);
			}
			for( TableColumn c: columns){
				panel.getTable().addColumn(c);
			}
		}else{
			for( TableColumn c: columns){
				panel.getTable().removeColumn(c);
			}
		}	
		model.setEditable(editable);
		model.fireTableDataChanged();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(((JButton)e.getSource()).getName()){
		case EinkaufsListePanel.BUTTONNAME_ADD: addRow();break;
		case EinkaufsListePanel.BUTTONNAME_SAVE:saveList();break;
		default:
			model.deleteEinkauf((Integer.parseInt(((JButton)e.getSource()).getName())));
			getPanel().getTable().removeEditor();
			break;
		}

	}
	
	public void addRow(){
		model.addEinkaufzeile(new EinkaufListeRow(new Einkauf(),new Rueckmeldung()));
	}
	
	public void saveList(){
		try {	
		if (panel.getTable().isEditing()){
			if (panel.getTable().getCellEditor().stopCellEditing()){
				model.clearEmptyRows();
				model.saveEinkaufsListe();
				JOptionPane.showMessageDialog(panel, "Es wurde erfolgreich gespeichert");
			}
			else{
				JOptionPane.showMessageDialog(panel, "Die Zellenbearbeitung muss erst abgeschlossen werden");
			}
		}else{
			model.clearEmptyRows();
			model.saveEinkaufsListe();	
			JOptionPane.showMessageDialog(panel, "Es wurde erfolgreich gespeichert");
		}	
		} catch (RueckmeldungReqFieldException | EinkaufReqFieldException | EinkaufDAOLoadException | EinkaufDAOSaveException | RueckmeldungDAOSaveException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(panel,e.getMessage());
		}
		
	}
	
	public void show() {

        JFrame frame = new JFrame("WG-Kasse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getPanel().setOpaque(true); //content panes must be opaque
        frame.setContentPane(getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EinkaufsListeController().show();
            }
        });
    }


	public EinkaufsListePanel getPanel() {
		return panel;
	}
}


