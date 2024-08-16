package ewision.sahan.stock;

import ewision.sahan.purchase.*;
import com.formdev.flatlaf.FlatClientProperties;
import ewision.sahan.application.Application;
import ewision.sahan.loggers.CommonLogger;
import ewision.sahan.loggers.DatabaseLogger;
import ewision.sahan.model.Constants;
import ewision.sahan.model.MySQL;
import ewision.sahan.model.Product;
import ewision.sahan.model.Stock;
import ewision.sahan.utils.SQLDateFormatter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ksoff
 */
public class UpdateStock extends javax.swing.JPanel {
    
    private CreatePurchase1 createPurchase;
    private Product product;
    private Stock stock;
    private String stock_id;

    /**
     * Creates new form SelectStock
     */
    public UpdateStock() {
        initComponents();
        init();
    }
    
    public UpdateStock(CreatePurchase1 createPurchase, String pid, String pname) {
        initComponents();
        this.createPurchase = createPurchase;
        productId.setText(pid);
        productName.setText(pname);
        init();
    }
    
    public UpdateStock(CreatePurchase1 createPurchase, Product product) {
        initComponents();
        this.createPurchase = createPurchase;
        this.product = product;
        setUIData();
        init();
    }
    
    private void setUIData() {
        productId.setText(String.valueOf(product.getId()));
        productName.setText(product.getName());
    }
    
    private void init() {
        int ref = (int) System.currentTimeMillis();
        codeField.setText(String.valueOf((int) (ref > 0 ? ref : -(ref))));
        
        styleComponents();
        configSpinner();
        initData();
        loadProducts("");
//        loadStocks("");
        toggleExpirePanel();
        toggleFields(false);
        reset();
    }
    
    private void initData() {
        jDateChooser1.setDate(new Date());
        jDateChooser2.setDate(new Date((System.currentTimeMillis() + (long) 2 * 30 * 24 * 60 * 60 * 1000)));
        //stockTable.setEnabled(false);
    }
    
    private void toggleFields(boolean enable) {
        //taxField.setEnabled(!enable);
        //discountField.setEnabled(!enable);
        priceField.setEnabled(enable);
        costField.setEnabled(enable);
        jDateChooser1.setEnabled(enable);
        jDateChooser2.setEnabled(enable);
        if (!codeField.getText().isEmpty()) {
            codeField.setEnabled(false);
        } else {
            codeField.setEnabled(true);
        }
    }
    
    private void reset() {
        toggleFields(true);
        quantitySpinner.setValue(1);
    }
    
    private void styleComponents() {
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search stocks by keywords");
        addPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;"
                + "background:$SubPanel.background");
        addButton.putClientProperty(FlatClientProperties.STYLE, "arc:20;");
        JTextField[] fields = {searchField};
        for (JTextField field : fields) {
            field.putClientProperty(FlatClientProperties.STYLE, "arc:15");
        }
        quantitySpinner.putClientProperty(FlatClientProperties.STYLE, "arc:15");
        jScrollPane1.putClientProperty(FlatClientProperties.STYLE, "arc:15");
        expireDateContainer.setBackground(Constants.TRANSPARENT);
    }
    
    private void configSpinner() {
        // Centering
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) quantitySpinner.getEditor();
        
        JFormattedTextField quantityField = editor.getTextField();
        quantityField.setHorizontalAlignment(SwingConstants.CENTER);
        quantityField.selectAll();
        quantityField.getCaret().setDot(quantitySpinner.getValue().toString().length());
        
        SpinnerNumberModel model = (SpinnerNumberModel) quantitySpinner.getModel();
        model.setMinimum(0);
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
        double quantity = 0;
        try {
            quantity = Double.valueOf(String.valueOf(quantitySpinner.getValue()));
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate quantity: " + e.getMessage(), e.getMessage());
        }
        
        double cost = 0.00;
        try {
            cost = Double.parseDouble(String.valueOf(costField.getText()));
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate cost: " + e.getMessage(), e.getMessage());
        }
        
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
        jLabel10 = new javax.swing.JLabel();
        priceField = new javax.swing.JFormattedTextField();
        codeLabel = new javax.swing.JLabel();
        codeField = new javax.swing.JTextField();
        salePriceField = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        commissionPriceField = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        productPanel = new javax.swing.JPanel();
        productContainer = new javax.swing.JPanel();
        productScroll = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        allProductsButton = new javax.swing.JButton();
        productField = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 5, 1));
        setNextFocusableComponent(searchField);

        stockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

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
        quantitySpinner.setNextFocusableComponent(salePriceField);
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
        costField.setNextFocusableComponent(priceField);
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
        isExpire.setNextFocusableComponent(jDateChooser1);
        isExpire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isExpireActionPerformed(evt);
            }
        });

        jLabel3.setText("Expire Date");

        jLabel4.setText("Manufacture Date");

        jDateChooser1.setNextFocusableComponent(jDateChooser2);
        jDateChooser1.setPreferredSize(new java.awt.Dimension(210, 33));

        jDateChooser2.setNextFocusableComponent(quantitySpinner);
        jDateChooser2.setPreferredSize(new java.awt.Dimension(210, 33));

        javax.swing.GroupLayout expireDateContainerLayout = new javax.swing.GroupLayout(expireDateContainer);
        expireDateContainer.setLayout(expireDateContainerLayout);
        expireDateContainerLayout.setHorizontalGroup(
            expireDateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expireDateContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(expireDateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(expireDateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
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

        jLabel10.setText("Price");

        priceField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        priceField.setText("0.00");
        priceField.setPreferredSize(new java.awt.Dimension(277, 33));
        priceField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                priceFieldFocusGained(evt);
            }
        });
        priceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                priceFieldKeyReleased(evt);
            }
        });

        codeLabel.setText("Code");

        codeField.setNextFocusableComponent(addButton);
        codeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                codeFieldFocusGained(evt);
            }
        });

        salePriceField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        salePriceField.setText("0.00");
        salePriceField.setNextFocusableComponent(codeField);
        salePriceField.setPreferredSize(new java.awt.Dimension(277, 33));
        salePriceField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salePriceFieldFocusGained(evt);
            }
        });
        salePriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salePriceFieldKeyReleased(evt);
            }
        });

        jLabel13.setText("Sale Price");

        jLabel14.setText("Commission Price");

        commissionPriceField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        commissionPriceField.setText("0.00");
        commissionPriceField.setNextFocusableComponent(codeField);
        commissionPriceField.setPreferredSize(new java.awt.Dimension(277, 33));
        commissionPriceField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commissionPriceFieldFocusGained(evt);
            }
        });
        commissionPriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                commissionPriceFieldKeyReleased(evt);
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
                            .addComponent(jLabel14)
                            .addComponent(isExpire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(costField, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(commissionPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(codeField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel10)
                            .addComponent(priceField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(salePriceField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(codeLabel))))
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
                .addComponent(isExpire, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addComponent(expireDateContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(salePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(codeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(commissionPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codeField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        addButton.setText("Update");
        addButton.setNextFocusableComponent(costField);
        addButton.setRolloverEnabled(false);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        productPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        productPanel.setMaximumSize(new java.awt.Dimension(32767, 250));
        productPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        productContainer.setMinimumSize(new java.awt.Dimension(20, 5));

        productScroll.setMinimumSize(new java.awt.Dimension(20, 5));

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Code", "Unit", "Brand", "Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productTable.setMinimumSize(new java.awt.Dimension(20, 5));
        productTable.setShowHorizontalLines(true);
        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productTableMouseClicked(evt);
            }
        });
        productScroll.setViewportView(productTable);

        javax.swing.GroupLayout productContainerLayout = new javax.swing.GroupLayout(productContainer);
        productContainer.setLayout(productContainerLayout);
        productContainerLayout.setHorizontalGroup(
            productContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(productScroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        productContainerLayout.setVerticalGroup(
            productContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(productScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel8.setMinimumSize(new java.awt.Dimension(20, 5));

        allProductsButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allProductsButton.setIconTextGap(2);
        allProductsButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allProductsButton.setMinimumSize(new java.awt.Dimension(20, 5));
        allProductsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allProductsButtonActionPerformed(evt);
            }
        });

        productField.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 15));
        productField.setMinimumSize(new java.awt.Dimension(20, 5));
        productField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(productField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(allProductsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(allProductsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout productPanelLayout = new javax.swing.GroupLayout(productPanel);
        productPanel.setLayout(productPanelLayout);
        productPanelLayout.setHorizontalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        productPanelLayout.setVerticalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(productContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(addPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(productId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(productName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(productId)
                    .addComponent(productName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(addPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        if (evt.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = stockTable.getSelectedRow();
            
            if (selectedRow != -1) {
                stock = new Stock();
                stock.setProduct(product);
                
                stock.setStock_id(String.valueOf(stockTable.getValueAt(selectedRow, 0)));
                
                String name = String.valueOf(stockTable.getValueAt(selectedRow, 1));
                if (!name.isBlank()) {
                    stock.setStock_name(name);
                }
                String code = String.valueOf(stockTable.getValueAt(selectedRow, 2));
                if (!code.isBlank() && !code.equals("null")) {
                    stock.setStock_code(code);
                    codeField.setText(code);
                } else {
                    codeField.setText("");
                }
                
                stock.setStock_quantity(String.valueOf(stockTable.getValueAt(selectedRow, 3)));
                
                String cost = String.valueOf(stockTable.getValueAt(selectedRow, 4));
                stock.setStock_cost(cost);
                costField.setText(cost);
                
                String sale_price = String.valueOf(stockTable.getValueAt(selectedRow, 5));
                stock.setPrice(sale_price);
                salePriceField.setText(sale_price);
                
                String price = String.valueOf(stockTable.getValueAt(selectedRow, 6));
                stock.setStock_price(price);
                priceField.setText(price);
                
                String commission_price = String.valueOf(stockTable.getValueAt(selectedRow, 7));
                stock.setStock_commission_price(commission_price);
                commissionPriceField.setText(commission_price);
                
                String exp = String.valueOf(stockTable.getValueAt(selectedRow, 8));
                if (!exp.isBlank()) {
                    isExpireDate = true;
                    isExpire.setSelected(true);
                    stock.setExp_date(exp);
                    jDateChooser1.setDate(new SQLDateFormatter().getDate(exp));
                }
                String mfd = String.valueOf(stockTable.getValueAt(selectedRow, 9));
                if (!mfd.isBlank()) {
                    isExpireDate = true;
                    isExpire.setSelected(true);
                    stock.setMfd_date(mfd);
                    jDateChooser2.setDate(new SQLDateFormatter().getDate(mfd));
                }
                toggleExpirePanel();
                
                reset();
//                toggleFields(true);

//                SpinnerNumberModel model = (SpinnerNumberModel) quantitySpinner.getModel();
//                model.setMinimum(Double.valueOf(String.valueOf(stockTable.getValueAt(selectedRow, 3))));
                quantitySpinner.requestFocus();
                
                calculate();
            }
        }
    }//GEN-LAST:event_stockTableMouseClicked

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // Update stock
        String code = codeField.getText();
        String quantity = String.valueOf(quantitySpinner.getValue());
        String currentQuantity = "0";
        //String currentQuantity = quantity;
        String cost = costField.getText();
        String price = priceField.getText();
        String sale_price = salePriceField.getText();
        String commission_price = commissionPriceField.getText();
        
        Date exp = jDateChooser1.getDate();
        Date mfd = jDateChooser2.getDate();

        //if (code.isBlank()) {
        //} else if (cost.isBlank()) {
        if (cost.isBlank()) {
        } else if (price.isBlank() || price.equals("0.00")) {
        } else if (sale_price.isBlank() || sale_price.equals("0.00")) {
        } else {
            
            boolean isStock = true;
            
            if (stock == null) {
                isStock = false;
                stock = new Stock();
                stock.setProduct(product);
            } else {
                currentQuantity = String.valueOf(stock.getStock_quantity());
                //currentQuantity = String.valueOf(stock.getStock_quantity() + Double.parseDouble(quantity));
            }
            
            if (isExpire.isSelected()) {
                if (exp == null) {
                }
                if (mfd == null) {
                }
                stock.setExp_date(exp);
                stock.setMfd_date(mfd);
            }
            
            stock.setStock_code(code);
            stock.setQuantity(quantity);
            stock.setStock_quantity(currentQuantity);
            
            stock.setStock_cost(cost);
            stock.setStock_price(price);
            stock.setPrice(sale_price);
            stock.setStock_commission_price(commission_price);
            
            boolean isSuccess = true;
            if (isStock) {
                // update query
                String stockQuery = "UPDATE `stocks` SET `quantity`='" + quantity + "',`price`='" + price + "', `sale_price`='" + sale_price + "', `commission_price`='" + commission_price + "' WHERE `id`='" + stock.getStock_id() + "';";
                try {
                    MySQL.execute(stockQuery);
                } catch (SQLException e) {
                    isSuccess = false;
                    DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Purchase Submit Stock Update row: " + e.getMessage(), e.getMessage());
                }
                
            } else {
                // insert query
                // String[] keys = {"id"};
                String stockQuery = "INSERT INTO "
                        + "`stocks` (`name`, `code`, `cost`, `price`, `sale_price`, `is_expire`, `exp_date`, `mfd_date`, `quantity`, `products_id`, `commission_price`) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                try {
                    //PreparedStatement preparedStatement = MySQL.getPreparedStatement(stockQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                    //PreparedStatement preparedStatement = MySQL.getPreparedStatement(stockQuery, keys);
                    PreparedStatement preparedStatement = MySQL.getPreparedStatement(stockQuery);
                    
                    preparedStatement.setString(1, stock.getStock_name());
                    preparedStatement.setString(2, stock.getStock_code());
                    preparedStatement.setDouble(3, stock.getStock_cost());
                    preparedStatement.setDouble(4, stock.getStock_price());
                    preparedStatement.setDouble(5, stock.getPrice());
                    preparedStatement.setInt(6, (stock.getExp_date() == null ? 0 : 1));
                    preparedStatement.setDate(7, (java.sql.Date) stock.getExp_date());
                    preparedStatement.setDate(8, (java.sql.Date) stock.getExp_date());
                    preparedStatement.setDouble(9, stock.getStock_quantity() + stock.getQuantity());
                    preparedStatement.setInt(10, stock.getId());
                    preparedStatement.setDouble(11, stock.getStock_commission_price());
                    
                    MySQL.executeInsert(preparedStatement);
                } catch (SQLException e) {
                    isSuccess = false;
                    DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Stock insert: " + e.getMessage(), e.getMessage());
                }
                
                if (isSuccess) {
                    JOptionPane.showMessageDialog(this, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                    reset();
                    Application.appService.openUpdateStock();
                } else {
                    JOptionPane.showMessageDialog(this, "Something went wrong", "Warning", JOptionPane.INFORMATION_MESSAGE);                    
                }
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
        calculate();
    }//GEN-LAST:event_quantitySpinnerFocusGained

    private void costFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_costFieldFocusGained
        // Select on Focus
        costField.selectAll();
    }//GEN-LAST:event_costFieldFocusGained

    private void costFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costFieldKeyReleased
        // Calculate on Value Change
        String text = costField.getText();
        costField.setText(text.isBlank() ? "0.00" : text);
        if (!costField.getText().equals("0.00")) {
            calculate();
        }
    }//GEN-LAST:event_costFieldKeyReleased

    private void isExpireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isExpireActionPerformed
        // Toggle Service Panel
        toggleExpirePanel();
    }//GEN-LAST:event_isExpireActionPerformed

    private void priceFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_priceFieldFocusGained
        // Select on Focus
        priceField.selectAll();
    }//GEN-LAST:event_priceFieldFocusGained

    private void priceFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_priceFieldKeyReleased
        // Calculate on Value Change
        String text = priceField.getText();
        priceField.setText(text.isBlank() ? "0.00" : text);
//        if (!priceField.getText().equals("0.00")) {
//            calculate();
//        }
    }//GEN-LAST:event_priceFieldKeyReleased

    private void codeFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_codeFieldFocusGained
        // Select on Focus
        codeField.selectAll();
    }//GEN-LAST:event_codeFieldFocusGained

    private void salePriceFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salePriceFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_salePriceFieldFocusGained

    private void salePriceFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salePriceFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_salePriceFieldKeyReleased

    private void commissionPriceFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commissionPriceFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_commissionPriceFieldFocusGained

    private void commissionPriceFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_commissionPriceFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_commissionPriceFieldKeyReleased

    private void productTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTableMouseClicked
        // Select Product
        if (evt.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = productTable.getSelectedRow();
            
            if (selectedRow != -1) {
                //this.product = String.valueOf(productTable.getValueAt(selectedRow, 0));
                //productField.setText(String.valueOf(productTable.getValueAt(selectedRow, 1)));
                //productContainer.setVisible(false);
                Product product = new Product();
                product.setProductData(
                        Integer.parseInt(String.valueOf(productTable.getValueAt(selectedRow, 0))),
                        String.valueOf(productTable.getValueAt(selectedRow, 1)),
                        String.valueOf(productTable.getValueAt(selectedRow, 2)),
                        String.valueOf(productTable.getValueAt(selectedRow, 3)));
                
                this.product = product;
                setUIData();
                toggleProductSearch();
                loadStocks("");
            }
        }
    }//GEN-LAST:event_productTableMouseClicked

    private void allProductsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allProductsButtonActionPerformed
        // Search All Products
        toggleProductSearch();
    }//GEN-LAST:event_allProductsButtonActionPerformed

    private void productFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productFieldKeyReleased
        // Search Product
        String product = productField.getText();
        if (product.isEmpty()) {
            productContainer.setVisible(false);
        } else {
            loadProducts(product);
        }
    }//GEN-LAST:event_productFieldKeyReleased
    
    private void toggleProductSearch() {
        productField.setText("");
        if (productContainer.isVisible()) {
            productContainer.setVisible(!productContainer.isVisible());
        } else {
            loadProducts("");
        }
        productField.requestFocus();
    }


    /* loadProducts */
    private void loadProducts(String product) {
        productContainer.setVisible(true);
        
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);

        //model.addRow(new Object[]{0, "Apple", "1025", "150.00", "-", "Fruit"});
        //String query = "SELECT * FROM `products` INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` INNER JOIN `brands` ON `brands`.`id`=`products`.`brand_id` INNER JOIN `units` ON `units`.`id`=`products`.`unit_id` ";
        String query = "SELECT * FROM `products` "
                + "INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` "
                + "INNER JOIN `brands` ON `brands`.`id`=`products`.`brand_id` "
                + "INNER JOIN `units` ON `units`.`id`=`products`.`unit_id` "
                + "WHERE `product_type`='product' AND `is_active`='1' ";
        if (!product.isEmpty()) {
            //query += "WHERE `products`.`name` LIKE '%" + product + "%' "
            query += "AND (`products`.`name` LIKE '%" + product + "%' "
                    + "OR `products`.`id` LIKE '%" + product + "%' "
                    + "OR `products`.`code` LIKE '%" + product + "%') ";
        }
        query += "ORDER BY `products`.`name` ASC";
        
        try {
            ResultSet resultSet = MySQL.execute(query);
            
            while (resultSet.next()) {
                Vector rowData = new Vector();
                
                rowData.add(resultSet.getString("products.id"));
                rowData.add(resultSet.getString("products.name"));
                rowData.add(resultSet.getString("products.code"));
                rowData.add(resultSet.getString("units.shortName"));
                rowData.add(resultSet.getString("brands.name"));
                rowData.add(resultSet.getString("categories.name"));
                
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Products Search: " + e.getMessage(), e.getMessage());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel addPanel;
    private javax.swing.JButton allProductsButton;
    private javax.swing.JTextField codeField;
    private javax.swing.JLabel codeLabel;
    private javax.swing.JFormattedTextField commissionPriceField;
    private javax.swing.JFormattedTextField costField;
    private javax.swing.JPanel expireDateContainer;
    private javax.swing.JCheckBox isExpire;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JFormattedTextField priceField;
    private javax.swing.JPanel productContainer;
    private javax.swing.JTextField productField;
    private javax.swing.JLabel productId;
    private javax.swing.JLabel productName;
    private javax.swing.JPanel productPanel;
    private javax.swing.JScrollPane productScroll;
    private javax.swing.JTable productTable;
    private javax.swing.JSpinner quantitySpinner;
    private javax.swing.JFormattedTextField salePriceField;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable stockTable;
    // End of variables declaration//GEN-END:variables
}
