package einkaufsListe;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Locale;


public class EinkaufsListePanel extends JPanel {

	public final static String BUTTONNAME_SAVE = "Save";
	public final static String BUTTONNAME_ADD = "Add";
	
	private JTable table;
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	
    public EinkaufsListePanel(EinkaufsListeController controller,EinkaufsListeModel model) {
        super(new GridLayout(1,0));

        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(700, 300));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        JButton btnSave = new JButton("Speichern");
        btnSave.addActionListener(controller);
        btnSave.setName(BUTTONNAME_SAVE);
        buttons.add(btnSave);
        JButton btnAdd = new JButton("+");
        btnAdd.setName(BUTTONNAME_ADD);
        buttons.add(btnAdd);
        btnAdd.addActionListener(controller);
        buttonPanel.add(btnAdd, BorderLayout.WEST);
        buttonPanel.add(btnSave, BorderLayout.EAST);
        
        
        //Set up column sizes.
        initColumnSizes(table);

        //column cell editors/renderers.
        for(int i = 0 ; i < table.getColumnCount(); i++){
        	setColumn(controller,table,table.getColumnModel().getColumn(i));
        }

        //set Layout
        setLayout(new BorderLayout());
        
        //Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        
    }
    public ArrayList<JButton> getButtons(){
    	return buttons;
    }
    
    public JTable getTable(){
    	return table;
    }

    /*
     * This method picks good column sizes.
     * If all column heads are wider than the column's cells'
     * contents, then you can just use column.sizeWidthToFit().
     */
   public void initColumnSizes(JTable table) {
        EinkaufsListeModel model = (EinkaufsListeModel)table.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        table.getTableHeader().setReorderingAllowed(false);
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(),false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

            comp = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table, table.getColumnName(i),false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }
   
   

    public void setColumn(final EinkaufsListeController controller,
    						final JTable table,
                                 TableColumn column) {
        
    	// Priorität
    	if(column.getModelIndex() == EinkaufsListeModel.COMBOBOX_COLUMN){
            column.setCellEditor(new PrioCellEditor(this));
            column.setCellRenderer(new ColorTableCellRenderer(this));     
    	}
    	// Löschen-Button
    	else if( column.getModelIndex() == EinkaufsListeModel.BUTTON_COLUMN){
    		column.setCellEditor(new DeleteButtonCellEditor(controller));
    		column.setCellRenderer(new DeleteButtonCellRenderer());
    	}
    	// Preis 
    	else if(column.getModelIndex() == EinkaufsListeModel.PREIS_COLUMN){
    		column.setCellEditor(new NumberCellEditor());
    		column.setCellRenderer(new NumberCellRenderer(this));
    	}
    	// Color
    	else{
    		column.setCellRenderer(new ColorTableCellRenderer(this));
    	}
    }
    
    
    public void setCompBackground(Component comp,int row){
	     String s =  table.getModel().getValueAt(row, 0 ).toString();
	     if(s.equalsIgnoreCase("1")){
	    	 comp.setBackground(Color.yellow);
	     }
	     else if(s.equalsIgnoreCase("2")){
	    	 comp.setBackground(Color.cyan);
	     }
	     else  {
	         comp.setBackground(null);
	     }	
    }

}


/*
 * CellEditor's
 */

// Preis TextField
@SuppressWarnings("serial")
class NumberCellEditor extends DefaultCellEditor {
	private JFormattedTextField editor;
    private Border red = new LineBorder(Color.red);
    private Border black = new LineBorder(Color.black);
public NumberCellEditor(){
    super(new JFormattedTextField());
}

@Override
public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    editor = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
    NumberFormat numberFormatB = NumberFormat.getInstance(Locale.getDefault());
    numberFormatB.setMaximumFractionDigits(2);
    numberFormatB.setMinimumFractionDigits(2);
    numberFormatB.setMinimumIntegerDigits(1);
    editor.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(numberFormatB)));
    return editor;
}

@Override
public boolean stopCellEditing() {
    try {
        this.getCellEditorValue();
        editor.setBorder(black);
        return super.stopCellEditing();
    } catch (Exception ex) {
    	editor.setBorder(red);
        return false;
    }

}

@Override
public Object getCellEditorValue() {
    // get content of textField
    String str = (String) super.getCellEditorValue();
    if (str == null || str.length() == 0) {
        return null;
    }
    	ParsePosition pos = new ParsePosition(0);
        Number n = NumberFormat.getInstance().parse(str,pos); 
        if (pos.getIndex() != str.length())throw new RuntimeException();
        else return new Double(n.doubleValue());
   
}
}

// Priorität ComboBox
@SuppressWarnings("serial")
class PrioCellEditor extends DefaultCellEditor{
	
	@SuppressWarnings("unchecked")
	public PrioCellEditor(final EinkaufsListePanel panel){
		super(new JComboBox<String>(EinkaufsListeModel.COMBOBOX_VALUES));
		((JComboBox<String>) this.getComponent()).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.getTable().repaint();	
			}});
    }
}


// Delete-Button
class DeleteButtonCellEditor implements TableCellEditor{
	private EinkaufsListeController controller;
	
	public DeleteButtonCellEditor(EinkaufsListeController controller){
		this.controller = controller;
	}
	
	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void cancelCellEditing() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
        table.setShowGrid(true); 
        table.setGridColor(Color.LIGHT_GRAY); 
        JButton button = new JButton();
	        button.setText(value.toString()); 
	        button.setName(Integer.toString(row));
	        button.setToolTipText("Zeile löschen"); 
	        button.addActionListener(controller);
	        return button;

	}
}



/*
 * CellRenderer
 * 
 */


// Delete-Button
class DeleteButtonCellRenderer implements TableCellRenderer{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JButton button = new JButton("-");
		return button;
	}
	
}

// Farbe
@SuppressWarnings("serial")
class ColorTableCellRenderer extends DefaultTableCellRenderer{
	
	private EinkaufsListePanel panel;
	
	public ColorTableCellRenderer(EinkaufsListePanel panel){
		this.panel = panel;
	}
	public Component getTableCellRendererComponent(  
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
			     Component comp = super.getTableCellRendererComponent(
			                      table,  value, isSelected, hasFocus, row, col);
			     panel.setCompBackground(comp,row);
			 
			     return( comp );
	}
	
}

// Preis TextField
class NumberCellRenderer extends DefaultTableCellRenderer {
    private Number numberValue;
    private NumberFormat nf;
    private EinkaufsListePanel panel;


    public NumberCellRenderer(EinkaufsListePanel panel) {
        super();
        this.panel = panel;
        setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
    }
	public Component getTableCellRendererComponent(  
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
			     Component comp = super.getTableCellRendererComponent(
			                      table,  value, isSelected, hasFocus, row, col);
			     panel.setCompBackground(comp,row);
			 
			     return( comp );
	}
    @Override
    public void setValue(Object value) {
        if ((value != null) && (value instanceof Number)) {
            numberValue = (Number) value;
            value = nf.format(numberValue.doubleValue());
         }
          else{
            	
          }
        super.setValue(value);
    }
}

    