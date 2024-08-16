package ewision.sahan.sale;

import com.formdev.flatlaf.FlatClientProperties;
import ewision.sahan.application.main.DialogModal;
import ewision.sahan.loggers.DatabaseLogger;
import ewision.sahan.model.MySQL;
import ewision.sahan.model.Product;
import ewision.sahan.model.Stock;
import ewision.sahan.pos.POSUI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatter;

/**
 *
 * @author ksoff
 */
public class SelectStock1 extends javax.swing.JPanel {

    private CreateSale1 createSale;
    private POSUI posui;
    private Product product;
    private Stock stock;
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

    public SelectStock1(CreateSale1 createSale, Product product) {
        initComponents();
        this.createSale = createSale;
        this.product = product;
        setUIData();
        init();
    }

    public SelectStock1(POSUI posui, Product product) {
        initComponents();
        this.posui = posui;
        this.product = product;
        setUIData();
        init();
    }

    private void setUIData() {
        productId.setText(String.valueOf(product.getId()));
        productName.setText(product.getName());
    }

    private void init() {
        styleComponents();
        configSpinner();
        searchField.grabFocus();
        searchField.requestFocus();
        loadStocks("");
        toggleFields(false);
        reset();
    }

    private void toggleFields(boolean enable) {
        quantitySpinner.setEnabled(enable);
        taxField.setEnabled(enable);
        discountField.setEnabled(enable);
        addButton.setEnabled(enable);
    }

    private void reset() {
        toggleFields(false);
        taxField.setText("0.00");
        discountField.setText("0.00");
        quantitySpinner.setValue(1);
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

    private void configSpinner() {
        // Centering
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) quantitySpinner.getEditor();

        JFormattedTextField quantityField = editor.getTextField();
        quantityField.setHorizontalAlignment(SwingConstants.CENTER);
        quantityField.selectAll();
        quantityField.getCaret().setDot(quantitySpinner.getValue().toString().length());
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
                //+ "WHERE `products_id`='" + productId.getText() + "' AND `stocks`.`quantity`>'0' ";
                + "WHERE `products_id`='" + product.getId() + "' AND `stocks`.`quantity`>'0'";
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
                rowData.add(resultSet.getString("stocks.sale_price"));
                rowData.add(resultSet.getString("stocks.price"));
                rowData.add(resultSet.getString("stocks.commission_price"));

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

    private void calculate() {
        double quantity = Double.valueOf(String.valueOf(quantitySpinner.getValue()));
        double discount = Double.parseDouble(discountField.getText());
        double tax = Double.parseDouble(taxField.getText());
        subTotalField.setText(String.valueOf(((stock == null) ? 0 : stock.getStock_price() * quantity) - discount + tax));
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
        subTotalField = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        isSalePrice = new javax.swing.JCheckBox();
        isCommissionPrice = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 5, 1));
        setNextFocusableComponent(searchField);

        stockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Code", "Quantity", "Cost", "Sale Price", "Price", "Commission Price", "EXP", "MFD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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
        quantitySpinner.setEditor(new javax.swing.JSpinner.NumberEditor(quantitySpinner, ""));
        quantitySpinner.setNextFocusableComponent(addButton);
        quantitySpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                quantitySpinnerStateChanged(evt);
            }
        });
        quantitySpinner.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                quantitySpinnerFocusGained(evt);
            }
        });

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
        discountField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                discountFieldFocusGained(evt);
            }
        });
        discountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discountFieldKeyReleased(evt);
            }
        });

        jLabel4.setText("Tax");

        taxField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        taxField.setText("0.00");
        taxField.setNextFocusableComponent(searchField);
        taxField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                taxFieldFocusGained(evt);
            }
        });
        taxField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                taxFieldKeyReleased(evt);
            }
        });

        subTotalField.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        subTotalField.setText("0");

        javax.swing.GroupLayout addPanelLayout = new javax.swing.GroupLayout(addPanel);
        addPanel.setLayout(addPanelLayout);
        addPanelLayout.setHorizontalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(discountField, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(taxField, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addGap(42, 42, 42)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(quantitySpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subTotalField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                            .addComponent(jLabel2)
                            .addComponent(subTotalField))
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

        isSalePrice.setText("Sale Price");
        isSalePrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isSalePriceActionPerformed(evt);
            }
        });

        isCommissionPrice.setText("Commission Price");
        isCommissionPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isCommissionPriceActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(isCommissionPrice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(isSalePrice))
                    .addComponent(searchField))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(isSalePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(isCommissionPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)))
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
        if (evt.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = stockTable.getSelectedRow();

            if (selectedRow != -1) {
                //if (stock == null) {
                //    stock = new Stock();
                //}
                stock = new Stock();
                stock.setProduct(product);

                stock.setStock_id(String.valueOf(stockTable.getValueAt(selectedRow, 0)));
                if (!(String.valueOf(stockTable.getValueAt(selectedRow, 1)).isBlank())) {
                    stock.setStock_name(String.valueOf(stockTable.getValueAt(selectedRow, 1)));
                }
                if (!(String.valueOf(stockTable.getValueAt(selectedRow, 2)).isBlank())) {
                    stock.setStock_code(String.valueOf(stockTable.getValueAt(selectedRow, 2)));
                }

                stock.setStock_quantity(String.valueOf(stockTable.getValueAt(selectedRow, 3)));
                stock.setStock_cost(String.valueOf(stockTable.getValueAt(selectedRow, 4)));
                stock.setPrice(String.valueOf(stockTable.getValueAt(selectedRow, 5)));
                stock.setStock_price(String.valueOf(stockTable.getValueAt(selectedRow, 6)));
                stock.setStock_commission_price(String.valueOf(stockTable.getValueAt(selectedRow, 7)));

                if (!(String.valueOf(stockTable.getValueAt(selectedRow, 8)).isBlank())) {
                    stock.setExp_date(String.valueOf(stockTable.getValueAt(selectedRow, 8)));
                }
                if (!(String.valueOf(stockTable.getValueAt(selectedRow, 9)).isBlank())) {
                    stock.setMfd_date(String.valueOf(stockTable.getValueAt(selectedRow, 9)));
                }

                subTotalField.setText(String.valueOf(stock.getStock_price()));

                reset();
                toggleFields(true);

                SpinnerNumberModel model = (SpinnerNumberModel) quantitySpinner.getModel();
                model.setMaximum(Double.valueOf(String.valueOf(stockTable.getValueAt(selectedRow, 3))));
                quantitySpinner.requestFocus();
            }
        }

    }//GEN-LAST:event_stockTableMouseClicked

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // Add to Invoice
        if (createSale != null) {
            if (stock == null) {
                stockTable.requestFocus();
            } else {
                stock.setQuantity(String.valueOf(quantitySpinner.getValue()));
                stock.setStock_discount(discountField.getText());
                stock.setStock_tax(taxField.getText());

                //createSale.addProductToInvoice(stock_id, String.valueOf(quantitySpinner.getValue()), discountField.getText(), taxField.getText());
                createSale.addProductToInvoice(stock);
                reset();
                createSale.closeModal();
            }
        }
        if (posui != null) {
            if (stock == null) {
                stockTable.requestFocus();
            } else {
                stock.setQuantity(String.valueOf(quantitySpinner.getValue()));
                stock.setStock_discount(discountField.getText());
                stock.setStock_tax(taxField.getText());

                //createSale.addProductToInvoice(stock_id, String.valueOf(quantitySpinner.getValue()), discountField.getText(), taxField.getText());
                posui.addProductToInvoice(stock);
                reset();
                posui.closeModal();
            }
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void quantitySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_quantitySpinnerStateChanged
        // Quantity Change
        //System.out.println(String.valueOf(quantitySpinner.getValue()));
        calculate();
    }//GEN-LAST:event_quantitySpinnerStateChanged

    private void discountFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountFieldKeyReleased
        // Discount Change
        calculate();
    }//GEN-LAST:event_discountFieldKeyReleased

    private void taxFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_taxFieldKeyReleased
        // Tax Change
        calculate();
    }//GEN-LAST:event_taxFieldKeyReleased

    private void discountFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_discountFieldFocusGained
        // Select on Focus
        discountField.selectAll();
    }//GEN-LAST:event_discountFieldFocusGained

    private void taxFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_taxFieldFocusGained
        // Select on Focus
        taxField.selectAll();
    }//GEN-LAST:event_taxFieldFocusGained

    private void quantitySpinnerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_quantitySpinnerFocusGained
        // Select on Focus
        configSpinner();
    }//GEN-LAST:event_quantitySpinnerFocusGained

    private void isSalePriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isSalePriceActionPerformed
        // Sale Price Trigger
        double sale_price = stock.getPrice();
        double stock_price = stock.getStock_price();
        double commission_price = stock.getStock_commission_price();
        if (isSalePrice.isSelected()) {
            if (isCommissionPrice.isSelected()) {
                isCommissionPrice.setSelected(false);
                double stock = stock_price;
                stock_price = commission_price;
                commission_price = stock;
                this.stock.setIsCommissionPrice(false);
            }
            stock.setPrice(stock_price);
            stock.setStock_price(sale_price);
            stock.setStock_commission_price(commission_price);
            stock.setIsSalePrice(true);
        } else {
            stock.setPrice(stock_price);
            stock.setStock_price(sale_price);
            stock.setStock_commission_price(commission_price);
        }
        calculate();
    }//GEN-LAST:event_isSalePriceActionPerformed

    private void isCommissionPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isCommissionPriceActionPerformed
        // Commission PriceTrigger
        double sale_price = stock.getPrice();
        double stock_price = stock.getStock_price();
        double commission_price = stock.getStock_commission_price();
        if (isCommissionPrice.isSelected()) {
            if (isSalePrice.isSelected()) {
                isSalePrice.setSelected(false);
                double stock = stock_price;
                stock_price = sale_price;
                sale_price = stock;
                this.stock.setIsSalePrice(false);
            }
            stock.setStock_commission_price(stock_price);
            stock.setStock_price(commission_price);
            stock.setPrice(sale_price);
            stock.setIsCommissionPrice(true);
        } else {
            stock.setStock_commission_price(stock_price);
            stock.setStock_price(commission_price);
            stock.setPrice(sale_price);
        }
        calculate();

    }//GEN-LAST:event_isCommissionPriceActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel addPanel;
    private javax.swing.JFormattedTextField discountField;
    private javax.swing.JCheckBox isCommissionPrice;
    private javax.swing.JCheckBox isSalePrice;
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
    private javax.swing.JLabel subTotalField;
    private javax.swing.JFormattedTextField taxField;
    // End of variables declaration//GEN-END:variables
}
