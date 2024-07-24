package ewision.sahan.components.action_panels;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import ewision.sahan.components.action_button.ActionButtonEvent;
import ewision.sahan.model.Constants;
import ewision.sahan.utils.ImageScaler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JTable;

public class ActionButtonPanel7 extends javax.swing.JPanel {

    private JTable table;

    /**
     * Creates new form ActionButtonPanel
     */
    public ActionButtonPanel7(JTable table) {
        initComponents();
        renderButtons();
        this.table = table;
    }

    public ActionButtonPanel7(JTable table, HashMap eventMap) {
        initComponents();
        init(eventMap);
        this.table = table;
    }

    private void init(HashMap eventMap) {
        renderButtons();
        view.setEvent((ActionButtonEvent) eventMap.get("view"));
        edit.setEvent((ActionButtonEvent) eventMap.get("edit"));
    }

    private void renderButtons() {
        //ImageScaler scaler = new ImageScaler();
        edit.setIcon(new FlatSVGIcon(getClass().getResource("/ewision/sahan/icon/svg/edit.svg")));
        view.setIcon(new FlatSVGIcon(getClass().getResource("/ewision/sahan/icon/svg/view.svg")));
        //edit.setIcon(new FlatSVGIcon(getClass().getResource("/ewision/sahan/icon/svg/edit.svg").getPath(), 30, 30));
        //view.setIcon(new FlatSVGIcon(getClass().getResource("/ewision/sahan/icon/svg/view.svg").getPath(), 30, 30));
        //edit.setIcon(scaler.getSvgIcon("/edit.svg", 30));
        //view.setIcon(scaler.getSvgIcon("/view.svg", 30));
    }

    public void initEvent(int row) {
        view.addActionListener((ActionEvent evt) -> {
            //onEdit(row);
            view.getEvent().run(row);
        });
        edit.addActionListener((ActionEvent evt) -> {
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

        view = new ewision.sahan.components.action_button.ActionButton();
        edit = new ewision.sahan.components.action_button.ActionButton();

        view.setDoubleBuffered(true);

        edit.setDoubleBuffered(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(view, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edit, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(view, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ewision.sahan.components.action_button.ActionButton edit;
    private ewision.sahan.components.action_button.ActionButton view;
    // End of variables declaration//GEN-END:variables
}
