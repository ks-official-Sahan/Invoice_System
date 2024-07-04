package ewision.sahan.sale;

import com.formdev.flatlaf.FlatClientProperties;
import ewision.sahan.application.main.DialogModal;
import ewision.sahan.loggers.DatabaseLogger;
import ewision.sahan.model.MySQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ksoff
 */
public class SelectStock1 extends javax.swing.JPanel {

    private CreateSale1 createSale;
    private String stock_id;

    /**
     * Creates new form SelectStock
     */
    public SelectStock1() {
        initComponents();
        init();
    }

    public SelectStock1(CreateSale1 createSale, String pid, String pname) {
        initComponents();
        this.createSale = createSale;
        productId.setText(pid);
        productName.setText(pname);
        init();
    }

    private void init() {
        styleComponents();
        searchField.grabFocus();
        searchField.requestFocus();
        loadStocks("");
    }

    private void styleComponents() {
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search stocks by keywords");
        addPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;"
                + "background:$SubPanel.background");
        addButton.putClientProperty(FlatClientProperties.STYLE, "arc:20;");
        JTextField[] fields = {searchField, taxField, discountField};
        for (JTextField field : fields) {
            field.putClientProperty(FlatClientProperties.STYLE, "arc:15");
        }
        quantitySpinner.putClientProperty(FlatClientProperties.STYLE, "arc:15");
        jScrollPane1.putClientProperty(FlatClientProperties.STYLE, "arc:15");
    }

    private void loadStocks(String stock) {
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        model.setRowCount(0);

        /* test Data */
        //model.addRow(new Object[]{0, "Apple", "0001", "150.00", "100.00", "120.00", " ", " "});
        //model.addRow(new Object[]{1, "Orange", "0002", "40.00", "80.00", "100.00", "2024-07-01", "2024-09-01"});
        String query = "SELECT * FROM `stocks` "
                + "INNER JOIN `products` ON `stocks`.`products_id`=`products`.`id` "
                + "INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` "
                + "INNER JOIN `brands` ON `brands`.`id`=`products`.`brand_id` "
                + "WHERE `products_id`='" + productId.getText() + "' AND `stocks`.`quantity`>'0' ";
        if (!stock.isBlank()) {
            query += "AND (`stocks`.`id` LIKE '%" + stock + "%' "
                    + "OR `stocks`.`name` LIKE '%" + stock + "%' "
                    + "OR `stocks`.`code` LIKE '%" + stock + "%' "
                    + "OR `stocks`.`cost` LIKE '%" + stock + "%' "
                    + "OR `stocks`.`price` LIKE '%" + stock + "%' "
                    //+ "OR `products`.`id` LIKE '%" + stock + "%' "
                    //+ "OR `products`.`name` LIKE '%" + stock + "%' "
                    //+ "OR `products`.`code` LIKE '%" + stock + "%' "
                    + "OR `stocks`.`exp_date` LIKE '%" + stock + "%' "
                    + "OR `stocks`.`mfd_date` LIKE '%" + stock + "%') ";
        }
        query += "ORDER BY `stocks`.`id` ASC";

        try {
            ResultSet resultSet = MySQL.execute(query);

            while (resultSet.next()) {
                Vector rowData = new Vector();

                rowData.add(resultSet.getString("stocks.id"));
                rowData.add(resultSet.getString("stocks.name"));
                rowData.add(resultSet.getString("stocks.code"));
                rowData.add(resultSet.getString("stocks.quantity"));
                rowData.add(resultSet.getString("stocks.cost"));
                rowData.add(resultSet.getString("stocks.price"));

                if (resultSet.getString("stocks.is_expire").equals("1")) {
                    rowData.add(resultSet.getString("stocks.exp_date"));
                    rowData.add(resultSet.getString("stocks.mfd_date"));
                } else {
                    rowData.add(" ");
                    rowData.add(" ");
                }

                model.addRow(rowData);
            }
        } catch (SQLException e) {
            DatabaseLogger.logger.log(Level.SEVERE, "SQLException in Order Products Search: " + e.getMessage(), e.getMessage());
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
        stockTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        productId = new javax.swing.JLabel();
        productName = new javax.swing.JLabel();
        addPanel = new javax.swing.JPanel();
        quantitySpinner = new javax.swing.JSpinner();
        addButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        discountField = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        taxField = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 5, 1));
        setNextFocusableComponent(searchField);

        stockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Code", "Quantity", "Cost", "Price", "EXP", "MFD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        stockTable.setNextFocusableComponent(quantitySpinner);
        stockTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stockTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(stockTable);
        if (stockTable.getColumnModel().getColumnCount() > 0) {
            stockTable.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        jLabel1.setText("Product:");

        productId.setText("000000000");

        productName.setText("Name");

        addPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        quantitySpinner.setModel(new javax.swing.SpinnerNumberModel(1.0d, 1.0d, null, 1.0d));
        quantitySpinner.setNextFocusableComponent(addButton);

        addButton.setText("Add");
        addButton.setNextFocusableComponent(discountField);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Quantity");

        jLabel3.setText("Discount");

        discountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        discountField.setText("0.00");
        discountField.setNextFocusableComponent(taxField);

        jLabel4.setText("Tax");

        taxField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        taxField.setText("0.00");
        taxField.setNextFocusableComponent(searchField);

        javax.swing.GroupLayout addPanelLayout = new javax.swing.GroupLayout(addPanel);
        addPanel.setLayout(addPanelLayout);
        addPanelLayout.setHorizontalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(taxField, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addGap(42, 42, 42)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(quantitySpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(addButton)
                .addContainerGap())
        );
        addPanelLayout.setVerticalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanelLayout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel8.setText("Stocks");

        searchField.setFocusCycleRoot(true);
        searchField.setNextFocusableComponent(stockTable);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(searchField))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(productId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(productName)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(productId)
                    .addComponent(productName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        // Search Stock
        if (!searchField.getText().isBlank()) {
            loadStocks(searchField.getText());
        } else {
            loadStocks("");
        }
    }//GEN-LAST:event_searchFieldKeyReleased

    private void stockTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stockTableMouseClicked
        // Select
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = stockTable.getSelectedRow();

            if (selectedRow != -1) {
                this.stock_id = String.valueOf(stockTable.getValueAt(selectedRow, 0));
                SpinnerNumberModel model = (SpinnerNumberModel) quantitySpinner.getModel();
                model.setMaximum(Double.valueOf(String.valueOf(stockTable.getValueAt(selectedRow, 3))));
                quantitySpinner.requestFocus();
            }
        }

    }//GEN-LAST:event_stockTableMouseClicked

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // Add to Invoice
        if (createSale != null) {
            createSale.addProductToInvoice(stock_id, String.valueOf(quantitySpinner.getValue()), discountField.getText(), taxField.getText());
        }
    }//GEN-LAST:event_addButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel addPanel;
    private javax.swing.JFormattedTextField discountField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel productId;
    private javax.swing.JLabel productName;
    private javax.swing.JSpinner quantitySpinner;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable stockTable;
    private javax.swing.JFormattedTextField taxField;
    // End of variables declaration//GEN-END:variables
}