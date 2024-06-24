package ewision.sahan.list;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import ewision.sahan.model.ImageNText;
import javax.swing.SwingConstants;

public class ListImageTextRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        //Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        ImageNText imageNText = (ImageNText) value;

        setIcon((ImageIcon) imageNText.getIcon());
        setText(imageNText.getText());

        //setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setEnabled(true);

        return this;
    }

}
