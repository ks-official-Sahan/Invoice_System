package ewision.sahan.expenses;

import com.formdev.flatlaf.FlatClientProperties;
import ewision.sahan.application.Application;
import ewision.sahan.application.main.DialogModal;
import ewision.sahan.loggers.DatabaseLogger;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

/**
 *
 * @author ksoff
 */
public class CreateExpense extends javax.swing.JPanel {

    private String sale = "0";
    private String customer = "0";

    /**
     * Creates new form SelectStock
     */
    public CreateExpense() {
        initComponents();
        init();
    }

    private void init() {
        int ref = (int) System.currentTimeMillis();
        codeField.setText(String.valueOf((int) (ref > 0 ? ref : -(ref))));

        styleComponents();
        initData();
        toggleFields(false);
        reset();
    }

    private void initData() {
        //stockTable.setEnabled(false);
        loadSales("");
        loadCustomers("");
        loadCategories();
    }

    private void toggleFields(boolean enable) {
        commissionPriceField.setEnabled(enable);
        if (!codeField.getText().isEmpty()) {
            codeField.setEnabled(false);
        } else {
            codeField.setEnabled(true);
        }
    }

    private void reset() {
        toggleFields(true);
    }

    private void styleComponents() {
        salesField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search sale by keywords");
        addPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;"
                + "background:$SubPanel.background");
        addButton.putClientProperty(FlatClientProperties.STYLE, "arc:20;");
//        JTextField[] fields = {salesField, customerField};
//        for (JTextField field : fields) {
//            field.putClientProperty(FlatClientProperties.STYLE, "arc:15");
//        }
    }

    private void loadCategories() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `expense_categories` ORDER BY `name`");

            Vector data = new Vector();
            data.add("Select");
            categoryMap.clear();

            while (resultSet.next()) {
                data.add(resultSet.getString("name"));
                categoryMap.put(resultSet.getString("name"), resultSet.getInt("id"));
            }

            model.addAll(data);
            model.setSelectedItem("Select");
            categoryBox.setModel(model);
        } catch (SQLException ex) {
            DatabaseLogger.logger.log(Level.SEVERE, "Category loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    private HashMap<String, Integer> categoryMap = new HashMap<>();
    private HashMap<String, Integer> salesMap = new HashMap<>();

    private void loadCustomers(String customer) {
        customerContainer.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0);

        model.addRow(new Object[]{0, "walk-in-customer", "default", "default"});

        String query = "SELECT * FROM `clients` WHERE `id` <> '0' ";
        if (!customer.isEmpty()) {
            query += "  AND `name` LIKE '%" + customer + "%' "
                    + "OR `email` LIKE '%" + customer + "%' "
                    + "OR `phone` LIKE '%" + customer + "%' "
                    + "OR `id` LIKE '%" + customer + "%' "
                    + "OR `code` LIKE '%" + customer + "%'";
        }

        try {
            ResultSet resultSet = MySQL.execute(query);

            while (resultSet.next()) {
                Vector rowData = new Vector();

                rowData.add(resultSet.getString("id"));
                rowData.add(resultSet.getString("name"));
                rowData.add(resultSet.getString("email"));
                rowData.add(resultSet.getString("phone"));

                model.addRow(rowData);
            }
        } catch (SQLException e) {
            DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Customer Search: " + e.getMessage(), e.getMessage());
        }
    }

    private void loadSales(String txt) {
        try {
            String query = "SELECT * FROM `sales` "
                    + "INNER JOIN `users` ON `users`.`id`=`sales`.`user_id` "
                    + "INNER JOIN `clients` ON `clients`.`id`=`sales`.`client_id` "
                    + "WHERE (`sales`.`Ref` LIKE '%" + txt + "%' "
                    + "OR `users`.`username` LIKE '%" + txt + "%' "
                    + "OR `clients`.`name` LIKE '%" + txt + "%') AND `statut`<>'delete' "
                    + "ORDER BY `date` DESC";
            ResultSet resultSet = MySQL.execute(query);

            DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
            model.setRowCount(0);
            salesMap.clear();

//            int count = 0;
//            int todayCount = 0;
//            Date today = new Date();
//            String todayString = new SQLDateFormatter().getStringDate(today);
            while (resultSet.next()) {
                Vector row = new Vector();

                double due = 0.00;
                //due = (Double.parseDouble(resultSet.getString("sales.GrandTotal")) - Double.parseDouble(resultSet.getString("sales.discount")) - Double.parseDouble(resultSet.getString("sales.paid_amount")));                
                double total = Double.parseDouble(resultSet.getString("sales.GrandTotal"));
                double payment = Double.parseDouble(resultSet.getString("sales.paid_amount"));
                due = (total - payment);
                due = (due >= 0 ? due : 0.00);
                payment = (due >= 0 ? payment : total);

                //row.add(false);
                String ref = resultSet.getString("sales.Ref");
                row.add(ref);
                salesMap.put(ref, resultSet.getInt("id"));

                String date = resultSet.getString("date");
                row.add(date);
                //row.add(resultSet.getString("user_id"));
//                row.add(resultSet.getString("users.username"));
                //row.add(resultSet.getString("client_id"));
                row.add(resultSet.getString("clients.phone"));
                //row.add(resultSet.getString(""));
//                row.add(resultSet.getString("sales.statut"));
                row.add(total);
//                row.add(payment);
                //row.add("");
                row.add(due);
//                row.add(resultSet.getString("sales.payment_statut"));
//                row.add(resultSet.getString("sales.shipping_status"));

                model.addRow(row);
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

        addPanel = new javax.swing.JPanel();
        codeLabel = new javax.swing.JLabel();
        codeField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        commissionPriceField = new javax.swing.JFormattedTextField();
        categoryBox = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        salesPanel = new javax.swing.JPanel();
        salesContainer = new javax.swing.JPanel();
        salesScroll = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        allSalesButton = new javax.swing.JButton();
        salesField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        customerPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        customerContainer = new javax.swing.JPanel();
        customerScroll = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        allCustomersButton = new javax.swing.JButton();
        customerField = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 5, 1));

        addPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        codeLabel.setText("Refference No");

        codeField.setNextFocusableComponent(addButton);
        codeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                codeFieldFocusGained(evt);
            }
        });

        jLabel14.setText("Amount");

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

        categoryBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category" }));

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setText("+");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel11.setText("Expense Category");

        javax.swing.GroupLayout addPanelLayout = new javax.swing.GroupLayout(addPanel);
        addPanel.setLayout(addPanelLayout);
        addPanelLayout.setHorizontalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commissionPriceField, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .addComponent(codeField)
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(codeLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanelLayout.createSequentialGroup()
                        .addComponent(categoryBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        addPanelLayout.setVerticalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(codeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codeField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(categoryBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commissionPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        addButton.setText("Submit");
        addButton.setRolloverEnabled(false);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        salesPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        salesPanel.setMaximumSize(new java.awt.Dimension(32767, 250));
        salesPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        salesContainer.setMinimumSize(new java.awt.Dimension(20, 5));

        salesScroll.setMinimumSize(new java.awt.Dimension(20, 5));

        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "REF", "Date", "Customer", "Grand Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        salesTable.setMinimumSize(new java.awt.Dimension(20, 5));
        salesTable.setShowHorizontalLines(true);
        salesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesTableMouseClicked(evt);
            }
        });
        salesScroll.setViewportView(salesTable);

        javax.swing.GroupLayout salesContainerLayout = new javax.swing.GroupLayout(salesContainer);
        salesContainer.setLayout(salesContainerLayout);
        salesContainerLayout.setHorizontalGroup(
            salesContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(salesScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        salesContainerLayout.setVerticalGroup(
            salesContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(salesScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel8.setMinimumSize(new java.awt.Dimension(20, 5));

        allSalesButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allSalesButton.setIconTextGap(2);
        allSalesButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allSalesButton.setMinimumSize(new java.awt.Dimension(20, 5));
        allSalesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allSalesButtonActionPerformed(evt);
            }
        });

        salesField.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 15));
        salesField.setMinimumSize(new java.awt.Dimension(20, 5));
        salesField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salesFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(salesField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(allSalesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(allSalesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salesField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel7.setText("Sale");
        jLabel7.setMinimumSize(new java.awt.Dimension(20, 5));

        javax.swing.GroupLayout salesPanelLayout = new javax.swing.GroupLayout(salesPanel);
        salesPanel.setLayout(salesPanelLayout);
        salesPanelLayout.setHorizontalGroup(
            salesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(salesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(salesContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(salesPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        salesPanelLayout.setVerticalGroup(
            salesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(salesContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        customerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        customerPanel.setMaximumSize(new java.awt.Dimension(32767, 250));
        customerPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setText("Customer");
        jLabel6.setMinimumSize(new java.awt.Dimension(20, 5));

        customerContainer.setMinimumSize(new java.awt.Dimension(20, 5));

        customerScroll.setMinimumSize(new java.awt.Dimension(20, 5));

        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Email", "Mobile"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customerTable.setMinimumSize(new java.awt.Dimension(20, 5));
        customerTable.setShowHorizontalLines(true);
        customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerTableMouseClicked(evt);
            }
        });
        customerScroll.setViewportView(customerTable);

        javax.swing.GroupLayout customerContainerLayout = new javax.swing.GroupLayout(customerContainer);
        customerContainer.setLayout(customerContainerLayout);
        customerContainerLayout.setHorizontalGroup(
            customerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(customerScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        customerContainerLayout.setVerticalGroup(
            customerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(customerScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel6.setMinimumSize(new java.awt.Dimension(20, 5));

        allCustomersButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allCustomersButton.setIconTextGap(2);
        allCustomersButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allCustomersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allCustomersButtonActionPerformed(evt);
            }
        });

        customerField.setFocusCycleRoot(true);
        customerField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(customerField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allCustomersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allCustomersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout customerPanelLayout = new javax.swing.GroupLayout(customerPanel);
        customerPanel.setLayout(customerPanelLayout);
        customerPanelLayout.setHorizontalGroup(
            customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(customerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        customerPanelLayout.setVerticalGroup(
            customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(customerContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addPanel, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(salesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(customerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(salesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(customerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(addPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // Insert Expense
        String code = codeField.getText();
        String amount = commissionPriceField.getText();
        String category = String.valueOf(categoryBox.getSelectedItem());

        SQLDateFormatter dateFormatter = new SQLDateFormatter();
        String currentDate = dateFormatter.getStringDate(new Date());
        String currentDateTime = dateFormatter.getStringDateTime(new Date());

        boolean isSuccess = true;
        // insert query
        // String[] keys = {"id"};
        String expenseQuery = "INSERT INTO "
                + "`expenses` (`date`, `Ref`, `details`, `amount`, `expense_category_id`, `user_id`, `clientsId`, `sales_id`, `shopId`) "
                + "VALUES ('" + currentDate + "', '" + code + "', 'N/A', '" + amount + "', '" + categoryMap.get(category) + "', '" + Application.getUser().getStringId() + "', '" + customer + "', '" + salesMap.get(sale) + "', '" + Application.getShop().getStringId() + "')";

        try {
            MySQL.execute(expenseQuery);
        } catch (SQLException e) {
            isSuccess = false;
            DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Expense insert: " + e.getMessage(), e.getMessage());
        }

        if (isSuccess) {
            Shop shop = Application.getShop();

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("ShopName", shop.getName());
            parameters.put("Email", shop.getEmail());
            parameters.put("Mobile", shop.getMobile());
            parameters.put("Address", shop.getAddress());
            parameters.put("Logo", shop.getLogo2Path());
            parameters.put("InvoiceNo", code);
            parameters.put("Customer", customerField.getText());
            parameters.put("Time", currentDateTime.replace(currentDate, ""));
            parameters.put("Date", currentDate);
            parameters.put("Type", category);
            parameters.put("Amount", String.valueOf(amount));
            parameters.put("Method", "Cash");
            parameters.put("Total", String.valueOf(amount));
            parameters.put("Payment", String.valueOf(String.valueOf(amount)));
            parameters.put("Balance", String.valueOf("0"));
            new PrintReport().PrintReport("/ewision/sahan/report/jasper/expenseInvoice.jasper", parameters, new JREmptyDataSource());

            JOptionPane.showMessageDialog(this, "Successfully completed!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Something went wrong!", "Warning", JOptionPane.WARNING_MESSAGE);
            isSuccess = false;
        }

        if (isSuccess) {
            reset();
            Application.appService.openCreateExpense();
        } else {
            JOptionPane.showMessageDialog(this, "Something went wrong", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void codeFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_codeFieldFocusGained
        // Select on Focus
        codeField.selectAll();
    }//GEN-LAST:event_codeFieldFocusGained

    private void commissionPriceFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commissionPriceFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_commissionPriceFieldFocusGained

    private void commissionPriceFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_commissionPriceFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_commissionPriceFieldKeyReleased

    private void salesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesTableMouseClicked
        // Select Sale
        if (evt.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = salesTable.getSelectedRow();

            if (selectedRow != -1) {
                String ref = String.valueOf(salesTable.getValueAt(selectedRow, 0));
                salesField.setText(ref);
                System.out.println(ref);
                System.out.println(salesField.getText());
                this.sale = ref;

                toggleSalesSearch();
            }
        }
    }//GEN-LAST:event_salesTableMouseClicked

    private void allSalesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allSalesButtonActionPerformed
        // Search All Sales
        toggleSalesSearch();
    }//GEN-LAST:event_allSalesButtonActionPerformed

    private void salesFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salesFieldKeyReleased
        // Search SALE
        String sale = salesField.getText();
        if (sale.isEmpty()) {
            salesContainer.setVisible(false);
        } else {
            loadSales(sale);
        }
    }//GEN-LAST:event_salesFieldKeyReleased

    private void customerTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerTableMouseClicked
        // Select Customer
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = customerTable.getSelectedRow();

            if (selectedRow != -1) {
                String id = String.valueOf(customerTable.getValueAt(selectedRow, 0));
                String mobile = String.valueOf(customerTable.getValueAt(selectedRow, 1));
                this.customer = id;
                customerField.setText(mobile);
                customerContainer.setVisible(false);
            }
        }
    }//GEN-LAST:event_customerTableMouseClicked

    private void allCustomersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allCustomersButtonActionPerformed
        // Search All Customers
        this.customer = "";
        if (customerContainer.isVisible()) {
            customerContainer.setVisible(!customerContainer.isVisible());
        } else {
            loadCustomers("");
        }
    }//GEN-LAST:event_allCustomersButtonActionPerformed

    private void customerFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerFieldKeyReleased
        // Search Customer
        String customer = customerField.getText();
        if (customer.isEmpty()) {
            customerContainer.setVisible(false);
        } else {
            loadCustomers(customer);
        }
        this.customer = "";
    }//GEN-LAST:event_customerFieldKeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Add Category
        DialogModal modal = new DialogModal(this);
        modal.openExpenseCategoryReg();
        modal.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void toggleSalesSearch() {
        salesField.setText("");
        salesContainer.setVisible(!salesContainer.isVisible());
        loadSales("");
        salesField.requestFocus();
    }

    /* loadProducts */
    private void loadSale(String sale) {
        salesContainer.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
        model.setRowCount(0);

        //model.addRow(new Object[]{0, "Apple", "1025", "150.00", "-", "Fruit"});
        //String query = "SELECT * FROM `products` INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` INNER JOIN `brands` ON `brands`.`id`=`products`.`brand_id` INNER JOIN `units` ON `units`.`id`=`products`.`unit_id` ";
        String query = "SELECT * FROM `products` "
                + "INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` "
                + "INNER JOIN `brands` ON `brands`.`id`=`products`.`brand_id` "
                + "INNER JOIN `units` ON `units`.`id`=`products`.`unit_id` "
                + "WHERE `product_type`='product' AND `is_active`='1' ";
        if (!sale.isEmpty()) {
            //query += "WHERE `products`.`name` LIKE '%" + product + "%' "
            query += "AND (`products`.`name` LIKE '%" + sale + "%' "
                    + "OR `products`.`id` LIKE '%" + sale + "%' "
                    + "OR `products`.`code` LIKE '%" + sale + "%') ";
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
    private javax.swing.JButton allCustomersButton;
    private javax.swing.JButton allSalesButton;
    private javax.swing.JComboBox<String> categoryBox;
    private javax.swing.JTextField codeField;
    private javax.swing.JLabel codeLabel;
    private javax.swing.JFormattedTextField commissionPriceField;
    private javax.swing.JPanel customerContainer;
    private javax.swing.JTextField customerField;
    private javax.swing.JPanel customerPanel;
    private javax.swing.JScrollPane customerScroll;
    private javax.swing.JTable customerTable;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel salesContainer;
    private javax.swing.JTextField salesField;
    private javax.swing.JPanel salesPanel;
    private javax.swing.JScrollPane salesScroll;
    private javax.swing.JTable salesTable;
    // End of variables declaration//GEN-END:variables
}
