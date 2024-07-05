package ewision.sahan.table;

import ewision.sahan.purchase.CreatePurchase;
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
    private int currntQty;
    private int row;
    private int priceColumn;
    private int totalColumn;
    private int maxColumn;
    private boolean isMax;

    public TableSpinnerCellEditor() {
        super(new JCheckBox());
        init();
    }

    public TableSpinnerCellEditor(int column) {
        super(new JCheckBox());
        this.priceColumn = column;
        init();
    }

    public TableSpinnerCellEditor(int priceColumn, int totalColumn) {
        super(new JCheckBox());
        this.priceColumn = priceColumn;
        this.totalColumn = totalColumn;
        init();
    }

    public TableSpinnerCellEditor(int priceColumn, int totalColumn, int maxColumn) {
        super(new JCheckBox());
        this.priceColumn = priceColumn;
        this.totalColumn = totalColumn;
        this.maxColumn = maxColumn;
        this.isMax = true;
        init();
    }

    private void initSpinner() {
        this.spinner = new JSpinner();
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        model.setMinimum(1); // Initial Value

        // Making changes apply on edit
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) spinner.getEditor();
        DefaultFormatter formatter = (DefaultFormatter) editor.getTextField().getFormatter();
        formatter.setCommitsOnValidEdit(true);

        // Centering
        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void init() {
        initSpinner();

        // Adding Change Event
        if (priceColumn != 0 && totalColumn != 0) {
            spinner.addChangeListener(((e) -> {
                inputChange(priceColumn, totalColumn);
            }));
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.row = row;

        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        if (isMax) {
            try {
                int max = Integer.parseInt(String.valueOf(table.getValueAt(row, maxColumn)));
                model.setMaximum(max); // Max Value        
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println(e.getMessage());
            }
        }

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

    private void inputChange(int priceColumn, int totalColumn) {
        try {
            int qty = Integer.parseInt(String.valueOf(spinner.getValue()));

            //double total = Double.parseDouble(String.valueOf(table.getValueAt(row, 4)));
            //double price = total / currntQty;
            double price = Double.parseDouble(String.valueOf(table.getValueAt(row, priceColumn)));

            double newTotal = qty * price;
            table.setValueAt(newTotal, row, totalColumn);
            
        } catch (NumberFormatException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

}
