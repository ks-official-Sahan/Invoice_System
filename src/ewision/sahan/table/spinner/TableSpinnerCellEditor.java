package ewision.sahan.table.spinner;

import ewision.sahan.loggers.CommonLogger;
import ewision.sahan.loggers.DatabaseLogger;
import java.awt.Component;
import java.util.logging.Level;
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
    private double currntQty;
    private int row;
    private int priceColumn;
    private int totalColumn;
    private int maxColumn;
    private boolean isMax;
    private SpinnerChangeEvent event;

    public TableSpinnerCellEditor() {
        super(new JCheckBox());
        init();
    }

    public TableSpinnerCellEditor(SpinnerChangeEvent event) {
        super(new JCheckBox());
        this.event = event;
        init();
    }

    public TableSpinnerCellEditor(SpinnerChangeEvent event, int maxColumn) {
        super(new JCheckBox());
        this.event = event;
        this.maxColumn = maxColumn;
        this.isMax = true;
        init();
    }

//    public TableSpinnerCellEditor(int priceColumn) {
//        super(new JCheckBox());
//        this.priceColumn = priceColumn;
//        init();
//    }
//
//    public TableSpinnerCellEditor(int priceColumn, int totalColumn) {
//        super(new JCheckBox());
//        this.priceColumn = priceColumn;
//        this.totalColumn = totalColumn;
//        init();
//    }
//
//    public TableSpinnerCellEditor(int priceColumn, int totalColumn, int maxColumn) {
//        super(new JCheckBox());
//        this.priceColumn = priceColumn;
//        this.totalColumn = totalColumn;
//        this.maxColumn = maxColumn;
//        this.isMax = true;
//        init();
//    }

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
        if (event != null) {
            spinner.addChangeListener(((e) -> {
                event.run(row, spinner);
            }));
        }
    }

    private void setMax() {
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        if (isMax) {
            try {
                double max = Double.parseDouble(String.valueOf(table.getValueAt(row, maxColumn)));
                model.setMaximum((int) max); // Max Value        
            } catch (NumberFormatException | NullPointerException e) {
                CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " setMax: " + e.getMessage(), e.getMessage());
            }
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.row = row;

        setMax();

        double qty = Double.parseDouble(String.valueOf(value));
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
                CommonLogger.logger.log(Level.SEVERE, "InterruptedException in " + getClass().getName() + " TableSpinnerCellEditor enable Thread: " + e.getMessage(), e.getMessage());
            }
        }).start();
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    private void inputChange(int priceColumn, int totalColumn) {
        try {
            double qty = Double.parseDouble(String.valueOf(spinner.getValue()));

            //double total = Double.parseDouble(String.valueOf(table.getValueAt(row, 4)));
            //double price = total / currntQty;
            double price = Double.parseDouble(String.valueOf(table.getValueAt(row, priceColumn)));

            double newTotal = qty * price;
            table.setValueAt(newTotal, row, totalColumn);
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " inputChange: " + e.getMessage(), e.getMessage());
        }
    }

}
