package ewision.sahan.table;

import ewision.sahan.components.action_button.ActionButton;
import ewision.sahan.components.action_panels.ActionButtonPanel1;
import ewision.sahan.components.action_panels.ActionButtonPanel2;
import ewision.sahan.components.action_panels.ActionButtonPanel3;
import ewision.sahan.components.action_panels.ActionButtonPanel4;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class TableActionPanelCellEditor extends DefaultCellEditor {

    private int buttonPanelType;

    public TableActionPanelCellEditor(int buttonPanelType) {
        super(new JCheckBox());
        this.buttonPanelType = buttonPanelType;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (buttonPanelType == ActionButton.ACTION_BUTTON) {
            ActionButtonPanel1 panel = new ActionButtonPanel1(table);
            panel.initEvent(row);
            return panel;
        } else if (buttonPanelType == ActionButton.EDIT_DELETE_BUTTON) {
            ActionButtonPanel2 panel = new ActionButtonPanel2(table);
            panel.initEvent(row);
            return panel;
        } else if (buttonPanelType == ActionButton.VIEW_EDIT_DELETE_BUTTON) {
            ActionButtonPanel3 panel = new ActionButtonPanel3(table);
            panel.initEvent(row);
            return panel;
        } else {
            ActionButtonPanel4 panel = new ActionButtonPanel4(table);
            panel.initEvent(row);
            return panel;
        }
    }

}
