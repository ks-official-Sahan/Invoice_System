package ewision.sahan.sale;

import ewision.sahan.application.Application;
import ewision.sahan.application.main.DialogModal;
import ewision.sahan.loggers.DatabaseLogger;
import ewision.sahan.model.Constants;
import ewision.sahan.model.MySQL;
import ewision.sahan.model.Shop;
import ewision.sahan.report.PrintReport;
import ewision.sahan.utils.SQLDateFormatter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

/**
 *
 * @author ksoff
 */
public class SalesReport extends javax.swing.JPanel {

    /**
     * Creates new form SalesReport
     */
    public SalesReport() {
        initComponents();
        init();
    }

    private void init() {
        loadSales("", "", "");
    }

    private DialogModal modal;

    public void setModal(DialogModal modal) {
        this.modal = modal;
    }

    int payment = 0;
    int balance = 0;
    String duration;

    private void loadSales(String txt, String from, String to) {
        try {
            Date today = new Date();
            String todayString = new SQLDateFormatter().getStringDate(today);

            String query = "SELECT * FROM `sales` "
                    + "INNER JOIN `users` ON `users`.`id`=`sales`.`user_id` "
                    + "INNER JOIN `clients` ON `clients`.`id`=`sales`.`client_id` "
                    + "WHERE (`sales`.`Ref` LIKE '%" + txt + "%' "
                    + "OR `users`.`username` LIKE '%" + txt + "%' "
                    + "OR `clients`.`name` LIKE '%" + txt + "%')";

            if (from.isBlank() && to.isBlank()) {
                duration = "ALL";
//            } else if (from.isBlank() && !to.isBlank()) {
//                query += " AND (`sales`.`date` BETWEEN '2000/01/01' AND '" + to + "')";
//                duration = from + " - Present";
//            } else if (!from.isBlank() && to.isBlank()) {
//                query += " AND (`sales`.`date` BETWEEN '" + from + "' AND '" + todayString + "')";
//                duration = "Until " + to;
//            } else {
//                query += " AND (`sales`.`date` BETWEEN '" + from + "' AND '" + to + "')";
//                duration = from + " - " + to;
//            }
            } else if (from.isBlank()) {
                // Only "to" date provided
                query += " AND (`sales`.`date` <= '"+to+"')";
                duration = "Until " + to;
            } else if (to.isBlank()) {
                // Only "from" date provided
                query += " AND (`sales`.`date` >= '"+from+"')";
                duration = "From " + from + " - Present";
            } else {
                // Both "from" and "to" dates provided
                query += " AND (`sales`.`date` BETWEEN '"+from+"' AND '"+to+"')";
                duration = from + " - " + to;
            }

            query += " AND `statut`<>'delete' "
                    + "ORDER BY `date` DESC, `sales`.`created_at` DESC";
            ResultSet resultSet = MySQL.execute(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            int count = 0;
            int value = 0;
            this.payment = 0;
            this.balance = 0;

            while (resultSet.next()) {
                Vector row = new Vector();

                double due = 0.00;
                //due = (Double.parseDouble(resultSet.getString("sales.GrandTotal")) - Double.parseDouble(resultSet.getString("sales.discount")) - Double.parseDouble(resultSet.getString("sales.paid_amount")));                
                double total = Double.parseDouble(resultSet.getString("sales.GrandTotal"));
                double payment = Double.parseDouble(resultSet.getString("sales.paid_amount"));
                this.payment += payment;
                value += total;
                this.balance -= total - payment;
                due = (total - payment);
                due = (due >= 0 ? due : 0.00);
                payment = (due >= 0 ? payment : total);

                //row.add(false);
                row.add(resultSet.getString("sales.Ref"));

                String date = resultSet.getString("date");
                row.add(date);
                //row.add(resultSet.getString("user_id"));
                row.add(resultSet.getString("users.username"));
                //row.add(resultSet.getString("client_id"));
                row.add(resultSet.getString("clients.phone"));
                //row.add(resultSet.getString(""));
                row.add(resultSet.getString("sales.statut"));
                row.add(resultSet.getString("sales.GrandTotal"));
                row.add(resultSet.getString("sales.paid_amount"));
                //row.add("");
                row.add(String.valueOf(due));
                row.add(resultSet.getString("sales.payment_statut"));

                model.addRow(row);

                if (date.equalsIgnoreCase(todayString)) {
                    value++;
                }
                count++;

                totalCount.setText(String.valueOf(count));
                totalValue.setText(String.valueOf(value));
            }
        } catch (SQLException ex) {
            //ex.printStackTrace();
            DatabaseLogger.logger.log(Level.SEVERE, "Sale loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        totalCount = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        totalValue = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new java.awt.Dimension(720, 558));

        jTable1.setBackground(Constants.TRANSPARENT);
        jTable1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ref", "Date", "Added By", "Customer", "Status", "Grand Total", "Paid", "Due", "Payment Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 1));
        jTable1.setMinimumSize(new java.awt.Dimension(0, 80));
        jTable1.setOpaque(false);
        jTable1.setRowHeight(80);
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 255));
        jButton1.setText("Filter");
        jButton1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(51, 51, 255)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(204, 0, 51));
        jButton2.setText("PDF");
        jButton2.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 0, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Sale Report");

        totalCount.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        totalCount.setText("0");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setText("No of Sales");

        totalValue.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        totalValue.setText("0");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setText("Net Value");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("From");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setText("To");

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 204, 204));
        jButton3.setText("Reset");
        jButton3.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 153, 153)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(totalCount, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(totalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalCount)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalValue)
                    .addComponent(jLabel5))
                .addGap(11, 11, 11))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // Update UI
        //        Thread t = new Thread(() -> {
        ////            jTable1.revalidate();
        ////            jTable1.repaint();
        ////            try {
        ////                SwingUtilities.updateComponentTreeUI(jTable1);
        ////            } catch (NullPointerException e) {
        ////            }
        //        });
        //        t.start();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Filter
        SQLDateFormatter dateFormatter = new SQLDateFormatter();

        Date f = jDateChooser1.getDate();
        String from = "";
        if (f != null) {
            from = dateFormatter.getStringDate(f);
        }
        Date t = jDateChooser2.getDate();
        String to = "";
        if (t != null) {
            to = dateFormatter.getStringDate(t);
        }

        loadSales("", from, to);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Print Report
        Shop shop = Application.getShop();
        SQLDateFormatter dateFormatter = new SQLDateFormatter();
        String currentDate = dateFormatter.getStringDate(new Date());
        String currentDateTime = dateFormatter.getStringDate(new Date(), "yyyy-MM-dd HH:mm:ss");

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("ShopName", shop.getName());
        parameters.put("Logo", shop.getLogo2Path());
        parameters.put("InvoiceNo", String.valueOf((int) System.currentTimeMillis()));
        parameters.put("Time", currentDateTime.replace(currentDate, ""));
        parameters.put("Date", currentDate);
        parameters.put("Method", "Cash");
        parameters.put("Total", totalCount.getText());
        parameters.put("NetAmount ", totalValue.getText());
        parameters.put("Payment", String.valueOf(payment));
        parameters.put("Balance", String.valueOf(balance));
        parameters.put("Duration", String.valueOf(duration));
        new PrintReport().PrintViewReport("/ewision/sahan/report/jasper/salesReportA4.jasper", parameters, new JRTableModelDataSource(jTable1.getModel()));

        JOptionPane.showMessageDialog(this, "Successfully completed!", "Successful", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Reset Filter
        loadSales("", "", "");
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel totalCount;
    private javax.swing.JLabel totalValue;
    // End of variables declaration//GEN-END:variables
}
