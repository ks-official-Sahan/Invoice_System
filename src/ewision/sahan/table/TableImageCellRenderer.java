package ewision.sahan.table;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

//public class TableImageCellRenderer implements TableCellRenderer {
public class TableImageCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        JLabel label = new JLabel();
        if (value == null) {
            // No Image
            label.setText("No Image");
        } else {
            if (value instanceof ImageIcon || value instanceof Icon) {
                // Has Image
                ImageIcon image = (ImageIcon) value;
                label = new JLabel(image);
            } else {
                // Image Updating
                label.setText("Updating");
            }
        }
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setOpaque(isSelected);
        label.setBackground(com.getBackground());
        return label;
        //return (Component) value;
    }

}
