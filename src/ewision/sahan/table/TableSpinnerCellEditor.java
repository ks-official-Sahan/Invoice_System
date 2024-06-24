package ewision.sahan.table;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatter;

public class TableSpinnerCellEditor extends DefaultCellEditor {

    private JSpinner spinner;
    private JTable table;
    private int row;
    private int currntQty;

    public TableSpinnerCellEditor() {
        super(new JCheckBox());

        this.spinner = new JSpinner();
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        model.setMinimum(1); // Initial Value

        // Making changes apply on edit
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) spinner.getEditor();
        DefaultFormatter formatter = (DefaultFormatter) editor.getTextField().getFormatter();
        formatter.setCommitsOnValidEdit(true);

        // Centering
        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);

        // Adding Change Event
        spinner.addChangeListener(((e) -> {
            inputChange();
        }));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.row = row;
        
        int qty = Integer.parseInt(String.valueOf(value));
        this.currntQty = qty;
        
        spinner.setValue(qty);
        spinner.setEnabled(false);
        enable();
        
        return spinner;
    }

    private void enable() {
        // for smoothing process
        new Thread(() -> {
            try {
                Thread.sleep(100);
                spinner.setEnabled(true);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    private void inputChange() {
        int qty = Integer.parseInt(String.valueOf(spinner.getValue()));
        //System.out.println(qty);
        double total = Double.parseDouble(String.valueOf(table.getValueAt(row, 4)));
        double price = total/currntQty;
        //double price = Double.parseDouble(String.valueOf(table.getValueAt(row, 4)));
        double newTotal = qty * price;
        table.setValueAt(newTotal, row, 4);
    }

}
