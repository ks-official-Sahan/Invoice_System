package ewision.sahan.table;

import ewision.sahan.components.action_button.ActionButton;
import ewision.sahan.components.action_panels.ActionButtonPanel1;
import ewision.sahan.components.action_panels.ActionButtonPanel2;
import ewision.sahan.components.action_panels.ActionButtonPanel3;
import ewision.sahan.components.action_panels.ActionButtonPanel4;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableActionPanelCellRenderer extends DefaultTableCellRenderer {

    int buttonPanelType;

    public TableActionPanelCellRenderer(int buttonPanelType) {
        this.buttonPanelType = buttonPanelType;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        table.getColumnModel().getColumn(column).setCellEditor(new TableActionPanelCellEditor(buttonPanelType));

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
