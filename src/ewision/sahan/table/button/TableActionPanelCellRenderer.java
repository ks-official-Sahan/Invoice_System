package ewision.sahan.table.button;

import ewision.sahan.components.action_button.ActionButton;
import ewision.sahan.components.action_button.ActionButtonEvent;
import ewision.sahan.components.action_panels.ActionButtonPanel1;
import ewision.sahan.components.action_panels.ActionButtonPanel2;
import ewision.sahan.components.action_panels.ActionButtonPanel3;
import ewision.sahan.components.action_panels.ActionButtonPanel4;
import ewision.sahan.components.action_panels.ActionButtonPanel5;
import ewision.sahan.components.action_panels.ActionButtonPanel6;
import ewision.sahan.components.action_panels.ActionButtonPanel7;
import ewision.sahan.loggers.CommonLogger;
import java.awt.Component;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableActionPanelCellRenderer extends DefaultTableCellRenderer {

    private int buttonPanelType;
    private HashMap<String, ActionButtonEvent> eventMap;

    public TableActionPanelCellRenderer(int buttonPanelType, HashMap eventMap) {
        this.buttonPanelType = buttonPanelType;
        this.eventMap = eventMap;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        try {
            Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            table.getColumnModel().getColumn(column).setCellEditor(new TableActionPanelCellEditor(buttonPanelType, eventMap));

            JPanel panel;
            System.out.println(buttonPanelType);
            panel = switch (buttonPanelType) {
                case ActionButton.ACTION_BUTTON ->
                    new ActionButtonPanel1(table);
                case ActionButton.EDIT_DELETE_BUTTON ->
                    new ActionButtonPanel2(table);
                case ActionButton.VIEW_EDIT_DELETE_BUTTON ->
                    new ActionButtonPanel3(table);
                case ActionButton.DELETE_BUTTON ->
                    new ActionButtonPanel4(table);
                case ActionButton.VIEW_BUTTON ->
                    new ActionButtonPanel5(table);
                case ActionButton.EDIT_BUTTON ->
                    new ActionButtonPanel6(table);
                case ActionButton.VIEW_EDIT_BUTTON ->
                    new ActionButtonPanel7(table);
                default ->
                    new ActionButtonPanel4(table);
            };
            return panel;
        } catch (Exception e) {
            CommonLogger.logger.log(Level.SEVERE, e.getMessage());
            JOptionPane.showMessageDialog(new JPanel(), e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return new JPanel();
        }
    }

}
