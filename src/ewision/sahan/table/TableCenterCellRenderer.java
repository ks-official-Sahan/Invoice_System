package ewision.sahan.table;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class TableCenterCellRenderer {
    
    public void renderTables(JTable table) {
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, render);
    }

}
