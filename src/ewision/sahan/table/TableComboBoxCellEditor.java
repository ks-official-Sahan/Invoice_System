package ewision.sahan.table;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class TableComboBoxCellEditor extends DefaultCellEditor {

    private DefaultComboBoxModel model;
    private JComboBox comboBox;
    private JTable table;
    private int row;
    private int column;

    //public TableComboBoxCellEditor(DefaultComboBoxModel model, JComboBox comboBox) {
    public TableComboBoxCellEditor(DefaultComboBoxModel model) {
        //super(comboBox);
        super(new JComboBox());
        this.comboBox = new JComboBox();
        //this.comboBox = comboBox;
        this.model = model;
        comboBox.setModel(model);
        comboBox.addActionListener((e) -> {
            setValue();
        });
    }

//    public TableComboBoxCellEditor(JComboBox comboBox) {
//        super(comboBox);
//    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        this.column = column;
        this.table = table;

        comboBox.setSelectedItem((String) value);
        
        return comboBox;
    }


    @Override
    public Object getCellEditorValue() {
        return String.valueOf(comboBox.getSelectedItem());
    }

    private void setValue() {
        table.setValueAt(String.valueOf(comboBox.getSelectedItem()), row, column);
    }

}
