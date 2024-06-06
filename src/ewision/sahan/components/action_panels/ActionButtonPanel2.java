package ewision.sahan.components.action_panels;

import ewision.sahan.components.action_button.ActionButtonEvent;
import ewision.sahan.utils.ImageScaler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JTable;

public class ActionButtonPanel2 extends javax.swing.JPanel {

    private JTable table;

    /**
     * Creates new form ActionButtonPanel
     */
    public ActionButtonPanel2(JTable table) {
        initComponents();
        renderButtons();
        this.table = table;
    }

    public ActionButtonPanel2(JTable table, HashMap eventMap) {
        initComponents();
        edit.setEvent((ActionButtonEvent) eventMap.get("edit"));
        delete.setEvent((ActionButtonEvent) eventMap.get("delete"));
        renderButtons();
        this.table = table;
    }

    private void renderButtons() {
        ImageScaler scaler = new ImageScaler();
        delete.setIcon(scaler.getSvgIcon("/delete.svg", 30));
        edit.setIcon(scaler.getSvgIcon("/edit.svg", 30));
    }

    public void initEvent(int row) {
        edit.addActionListener((ActionEvent evt) -> {
            //onEdit(row);
            edit.getEvent().run(row);
        });
        delete.addActionListener((ActionEvent evt) -> {
            //onDelete(row);
            edit.getEvent().run(row);
        });
    }

//    private void onEdit(int row) {
//        System.out.println("Edit: " + row);
//        edit.getEvent().run(row);
//    }
//
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

        edit = new ewision.sahan.components.action_button.ActionButton();
        delete = new ewision.sahan.components.action_button.ActionButton();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ewision.sahan.components.action_button.ActionButton delete;
    private ewision.sahan.components.action_button.ActionButton edit;
    // End of variables declaration//GEN-END:variables
}
