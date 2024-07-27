package ewision.sahan.components.action_panels;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import ewision.sahan.components.action_button.ActionButtonEvent;
import ewision.sahan.model.Constants;
import ewision.sahan.utils.ImageScaler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JTable;

public class ActionButtonPanel5 extends javax.swing.JPanel {

    private JTable table;

    /**
     * Creates new form ActionButtonPanel
     */
    public ActionButtonPanel5(JTable table) {
        initComponents();
        renderButtons();
        this.table = table;
    }

    public ActionButtonPanel5(JTable table, HashMap eventMap) {
        initComponents();
        init(eventMap);
        this.table = table;
    }

        private void init(HashMap eventMap) {
        renderButtons();
        view.setEvent((ActionButtonEvent) eventMap.get("view"));
        //setBackground(Constants.TRANSPARENT);
    }

    
    private void renderButtons() {
        view.setIcon(new FlatSVGIcon(getClass().getResource("/ewision/sahan/icon/svg/view.svg")));
        //view.setIcon(new FlatSVGIcon(getClass().getResource("/ewision/sahan/icon/svg/view.svg").getPath(), 30, 30));
        //view.setIcon(new ImageScaler().getSvgIcon("/view.svg", 30));
    }

    public void initEvent(int row) {
        view.addActionListener((ActionEvent evt) -> {
            //onDelete(row);
            view.getEvent().run(row);
        });
    }

//    private void onDelete(int row) {
//        if (table.isEditing()) {
//            table.getCellEditor().stopCellEditing();
//            System.out.println(String.valueOf(table.getValueAt(row, 3)));
//        }
//        delete.getEvent().run(row);
//        //System.out.println("Delete: " + row);
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        view = new ewision.sahan.components.action_button.ActionButton();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(view, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(view, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ewision.sahan.components.action_button.ActionButton view;
    // End of variables declaration//GEN-END:variables
}