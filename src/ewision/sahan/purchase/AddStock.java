package ewision.sahan.purchase;

import ewision.sahan.sale.*;
import com.formdev.flatlaf.FlatClientProperties;
import ewision.sahan.application.main.DialogModal;
import ewision.sahan.loggers.DatabaseLogger;
import ewision.sahan.model.MySQL;
import ewision.sahan.model.Product;
import ewision.sahan.model.Stock;
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
public class AddStock extends javax.swing.JPanel {

    private CreateSale1 createSale;
    private Product product;
    private Stock stock;
    private String stock_id;

    /**
     * Creates new form SelectStock
     */
    public AddStock() {
        initComponents();
        init();
    }

    public AddStock(CreateSale1 createSale, String pid, String pname) {
        initComponents();
        this.createSale = createSale;
        productId.setText(pid);
        productName.setText(pname);
        init();
    }

    public AddStock(CreateSale1 createSale, Product product) {
        initComponents();
        this.createSale = createSale;
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
                + "WHERE `products_id`='" + product.getId() + "' AND `stocks`.`quantity`>'0' ";
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

    private void calculate() {
        double quantity = Double.valueOf(String.valueOf(quantitySpinner.getValue()));
        double discount = Double.parseDouble(discountField.getText());
        double tax = Double.parseDouble(taxField.getText());
        subTotalField.setText(String.valueOf(((stock == null) ? 0 : stock.getStock_price() * quantity) - discount - tax));
    }

    private boolean isExpireDate;
    
    private void toggleExpirePanel() {
        expireDateContainer.setVisible(isExpire.isSelected());
        isExpireDate = isExpire.isSelected();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        costField = new javax.swing.JFormattedTextField();
        isExpire = new javax.swing.JCheckBox();
        expireDateContainer = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        discountField = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        priceField = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        taxField = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        paymentField = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        subTotalField = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        subTotalField1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 5, 1));
        setNextFocusableComponent(searchField);

        stockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

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
        quantitySpinner.setEditor(new javax.swing.JSpinner.NumberEditor(quantitySpinner, ""));
        quantitySpinner.setNextFocusableComponent(addButton);
        quantitySpinner.setPreferredSize(new java.awt.Dimension(277, 33));
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

        jLabel2.setText("Quantity");

        jLabel5.setText("Cost");

        costField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        costField.setText("0.00");
        costField.setNextFocusableComponent(taxField);
        costField.setPreferredSize(new java.awt.Dimension(277, 33));
        costField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                costFieldFocusGained(evt);
            }
        });
        costField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                costFieldKeyReleased(evt);
            }
        });

        isExpire.setText("Expire Date");
        isExpire.setMinimumSize(new java.awt.Dimension(20, 5));
        isExpire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isExpireActionPerformed(evt);
            }
        });

        jLabel3.setText("Expire Date");

        jLabel4.setText("Manufacture Date");

        jDateChooser1.setPreferredSize(new java.awt.Dimension(277, 33));

        jDateChooser2.setPreferredSize(new java.awt.Dimension(277, 33));

        javax.swing.GroupLayout expireDateContainerLayout = new javax.swing.GroupLayout(expireDateContainer);
        expireDateContainer.setLayout(expireDateContainerLayout);
        expireDateContainerLayout.setHorizontalGroup(
            expireDateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expireDateContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(expireDateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(expireDateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)))
        );
        expireDateContainerLayout.setVerticalGroup(
            expireDateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expireDateContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(expireDateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(expireDateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        discountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        discountField.setText("0.00");
        discountField.setNextFocusableComponent(taxField);
        discountField.setPreferredSize(new java.awt.Dimension(277, 33));
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

        jLabel7.setText("Discount");

        jLabel10.setText("Price");

        priceField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        priceField.setText("0.00");
        priceField.setNextFocusableComponent(taxField);
        priceField.setPreferredSize(new java.awt.Dimension(277, 33));
        priceField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                priceFieldFocusGained(evt);
            }
        });
        priceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceFieldActionPerformed(evt);
            }
        });
        priceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                priceFieldKeyReleased(evt);
            }
        });

        jLabel11.setText("Tax");

        taxField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        taxField.setText("0.00");
        taxField.setNextFocusableComponent(taxField);
        taxField.setPreferredSize(new java.awt.Dimension(277, 33));
        taxField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                taxFieldFocusGained(evt);
            }
        });
        taxField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taxFieldActionPerformed(evt);
            }
        });
        taxField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                taxFieldKeyReleased(evt);
            }
        });

        jLabel12.setText("Cost");

        paymentField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        paymentField.setText("0.00");
        paymentField.setNextFocusableComponent(taxField);
        paymentField.setPreferredSize(new java.awt.Dimension(277, 33));
        paymentField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                paymentFieldFocusGained(evt);
            }
        });
        paymentField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paymentFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout addPanelLayout = new javax.swing.GroupLayout(addPanel);
        addPanel.setLayout(addPanelLayout);
        addPanelLayout.setHorizontalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expireDateContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(isExpire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(discountField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(costField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(taxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(paymentField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        addPanelLayout.setVerticalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(costField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taxField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(isExpire, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(expireDateContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        addButton.setText("Add");
        addButton.setNextFocusableComponent(discountField);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        subTotalField.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        subTotalField.setText("0");

        jLabel6.setText("Total");

        subTotalField1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        subTotalField1.setText("0");

        jLabel9.setText("Balance");

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
                        .addComponent(productName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subTotalField1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(addPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(productId)
                    .addComponent(productName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subTotalField)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subTotalField1)
                    .addComponent(jLabel9))
                .addGap(24, 24, 24)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
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
                stock.setStock_price(String.valueOf(stockTable.getValueAt(selectedRow, 5)));
                if (!(String.valueOf(stockTable.getValueAt(selectedRow, 6)).isBlank())) {
                    stock.setExp_date(String.valueOf(stockTable.getValueAt(selectedRow, 6)));
                }
                if (!(String.valueOf(stockTable.getValueAt(selectedRow, 7)).isBlank())) {
                    stock.setMfd_date(String.valueOf(stockTable.getValueAt(selectedRow, 7)));
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
    }//GEN-LAST:event_addButtonActionPerformed

    private void quantitySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_quantitySpinnerStateChanged
        // Quantity Change
        //System.out.println(String.valueOf(quantitySpinner.getValue()));
        calculate();
    }//GEN-LAST:event_quantitySpinnerStateChanged

    private void quantitySpinnerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_quantitySpinnerFocusGained
        // Select on Focus
        configSpinner();
    }//GEN-LAST:event_quantitySpinnerFocusGained

    private void costFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_costFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_costFieldFocusGained

    private void costFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_costFieldKeyReleased

    private void isExpireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isExpireActionPerformed
        // Toggle Service Panel
        toggleExpirePanel();
    }//GEN-LAST:event_isExpireActionPerformed

    private void discountFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_discountFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_discountFieldFocusGained

    private void discountFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_discountFieldKeyReleased

    private void priceFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_priceFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_priceFieldFocusGained

    private void priceFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_priceFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_priceFieldKeyReleased

    private void taxFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_taxFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_taxFieldFocusGained

    private void taxFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_taxFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_taxFieldKeyReleased

    private void paymentFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_paymentFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentFieldFocusGained

    private void paymentFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentFieldKeyReleased

    private void priceFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceFieldActionPerformed

    private void taxFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taxFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_taxFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel addPanel;
    private javax.swing.JFormattedTextField costField;
    private javax.swing.JFormattedTextField discountField;
    private javax.swing.JPanel expireDateContainer;
    private javax.swing.JCheckBox isExpire;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JFormattedTextField paymentField;
    private javax.swing.JFormattedTextField priceField;
    private javax.swing.JLabel productId;
    private javax.swing.JLabel productName;
    private javax.swing.JSpinner quantitySpinner;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable stockTable;
    private javax.swing.JLabel subTotalField;
    private javax.swing.JLabel subTotalField1;
    private javax.swing.JFormattedTextField taxField;
    // End of variables declaration//GEN-END:variables
}
