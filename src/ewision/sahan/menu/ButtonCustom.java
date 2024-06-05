package ewision.sahan.menu;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import javax.swing.JButton;

/**
 *
 * @author ksoff
 */
public class ButtonCustom extends JButton {

    private boolean mouseOver;

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public ButtonCustom() {
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        //setBackground(new Color(65, 152, 216));
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (mouseOver) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(200, 200, 200, 50));
            //g2.setColor(getBackground());
            g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 20, 20);
        }
        super.paintComponent(g);
    }

}
