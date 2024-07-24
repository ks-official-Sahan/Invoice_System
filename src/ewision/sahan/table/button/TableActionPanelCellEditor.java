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
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

public class TableActionPanelCellEditor extends DefaultCellEditor {

    private int buttonPanelType;
    private HashMap<String, ActionButtonEvent> eventMap;

    public TableActionPanelCellEditor(int buttonPanelType, HashMap eventMap) {
        super(new JCheckBox());
        this.buttonPanelType = buttonPanelType;
        this.eventMap = eventMap;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        try {
            JPanel actionPanel;
            switch (buttonPanelType) {
                case ActionButton.ACTION_BUTTON: {
                    ActionButtonPanel1 panel = new ActionButtonPanel1(table, eventMap);
                    panel.initEvent(row);
                    actionPanel = (JPanel) panel;
                    break;
                }
                case ActionButton.EDIT_DELETE_BUTTON: {
                    ActionButtonPanel2 panel = new ActionButtonPanel2(table, eventMap);
                    panel.initEvent(row);
                    actionPanel = (JPanel) panel;
                    break;
                }
                case ActionButton.VIEW_EDIT_DELETE_BUTTON: {
                    ActionButtonPanel3 panel = new ActionButtonPanel3(table, eventMap);
                    panel.initEvent(row);
                    actionPanel = (JPanel) panel;
                    break;
                }
                case ActionButton.DELETE_BUTTON: {
                    ActionButtonPanel4 panel = new ActionButtonPanel4(table, eventMap);
                    panel.initEvent(row);
                    actionPanel = (JPanel) panel;
                    break;
                }
                case ActionButton.VIEW_BUTTON: {
                    ActionButtonPanel5 panel = new ActionButtonPanel5(table, eventMap);
                    panel.initEvent(row);
                    actionPanel = (JPanel) panel;
                    break;
                }
                case ActionButton.EDIT_BUTTON: {
                    ActionButtonPanel6 panel = new ActionButtonPanel6(table, eventMap);
                    panel.initEvent(row);
                    actionPanel = (JPanel) panel;
                    //return panel;
                    break;
                }
                case ActionButton.VIEW_EDIT_BUTTON: {
                    ActionButtonPanel7 panel = new ActionButtonPanel7(table, eventMap);
                    panel.initEvent(row);
                    actionPanel = (JPanel) panel;
                    //return panel;
                    break;
                }
                default: {
                    ActionButtonPanel4 panel = new ActionButtonPanel4(table, eventMap);
                    panel.initEvent(row);
                    actionPanel = (JPanel) panel;
                    //return panel;
                    break;
                }
            }
            return actionPanel;
        } catch (Exception e) {
            CommonLogger.logger.log(Level.SEVERE, e.getMessage());
            JOptionPane.showMessageDialog(new JPanel(), e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return new JPanel();
        }
    }

}
