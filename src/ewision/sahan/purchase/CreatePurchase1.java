package ewision.sahan.purchase;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import ewision.sahan.application.Application;
import ewision.sahan.application.main.DialogModal;
import ewision.sahan.components.action_button.ActionButton;
import ewision.sahan.components.action_button.ActionButtonEvent;
import ewision.sahan.loggers.CommonLogger;
import ewision.sahan.loggers.DatabaseLogger;
import ewision.sahan.model.MySQL;
import ewision.sahan.model.Product;
import ewision.sahan.model.Service;
import ewision.sahan.model.Shop;
import ewision.sahan.model.Stock;
import ewision.sahan.report.PrintReport;
import ewision.sahan.table.button.TableActionPanelCellRenderer;
import ewision.sahan.table.TableCenterCellRenderer;
import ewision.sahan.table.spinner.SpinnerChangeEvent;
import ewision.sahan.table.spinner.TableSpinnerCellEditor;
import ewision.sahan.utils.ImageScaler;
import ewision.sahan.utils.SQLDateFormatter;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

/**
 *
 * @author ksoff
 */
public class CreatePurchase1 extends javax.swing.JPanel {

    /**
     * Creates new form CreatePurchase
     */
    public CreatePurchase1() {
        initComponents();
        init();
    }

    /*
    * Init
     */
    // <editor-fold defaultstate="collapsed" desc="Intializing">
    private void init() {
        render();
        toggleServicePanel();
        styleComponents();
        renderTables();
        initData();
    }

    private void initData() {
        jDateChooser1.setDate(new Date());
        int ref = (int) System.currentTimeMillis();
        referenceNoField.setText(String.valueOf((int) (ref > 0 ? ref : -(ref))));
        //loadOrderProducts("");
        loadStatus();
        loadPaymentStatus();
    }

    private void render() {
        FlatSVGIcon refreshSvgIcon = new ImageScaler().getSvgIcon("refresh", 28);
        JButton[] buttons = {allSuppliersButton, allProductsButton, allServicesButton, allWarehousesButton};
        for (JButton button : buttons) {
            button.setIcon(refreshSvgIcon);
        }
        //allCustomersButton.setIcon(refreshSvgIcon);
        //allWarehousesButton.setIcon(refreshSvgIcon);
        //allProductsButton.setIcon(refreshSvgIcon);
        //allServicesButton.setIcon(refreshSvgIcon);
    }

    private void styleComponents() {
        productField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search products by keywords");
        referenceNoField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Refference No");

        JPanel[] containers = {productContainer, serviceContainer, servicePanel, jPanel14, serviceChargePanel, dateContainer, supplierContainer, warehouseContainer};
        for (JPanel container : containers) {
            container.setVisible(false);
            container.putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:$SubPanel.background;"
                    + "arc:20;");
        }

        headerPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        bodyPanel.putClientProperty(FlatClientProperties.STYLE, "arc:0;"
                + "background:$Body.background");
        bodyScroll.putClientProperty(FlatClientProperties.STYLE, "arc:20    ;"
                + "background:$Body.background");
    }

    private void renderTables() {
        TableCenterCellRenderer tableCenterCellRenderer = new TableCenterCellRenderer();
        tableCenterCellRenderer.renderTables(supplierTable);
        tableCenterCellRenderer.renderTables(productChargeTable);
        tableCenterCellRenderer.renderTables(productTable);
        tableCenterCellRenderer.renderTables(serviceChargeTable);
        tableCenterCellRenderer.renderTables(serviceTable);
        tableCenterCellRenderer.renderTables(warehouseTable);

        setupProductChargeTable();
        setupServiceChargeTable();
    }
    // </editor-fold>

    /*
    *
    * Table Operations Setup
     */
    // <editor-fold defaultstate="collapsed" desc="Table Operations Setup">
    private void setupProductChargeTable() {
        SpinnerChangeEvent event = (int row, JSpinner spinner) -> {
            try {
                double qty = Double.parseDouble(String.valueOf(spinner.getValue()));

                double price = Double.parseDouble(String.valueOf(productChargeTable.getValueAt(row, 2)));

                double newTotal = qty * price;
                productChargeTable.setValueAt(newTotal, row, 7);

                Stock currentStock = stockMap.get(String.valueOf(productChargeTable.getValueAt(row, 0)));
                if (currentStock != null) {
                    currentStock.setQuantity(qty);
                    //currentStock.setStock_quantity(currentStock.getStock_quantity() + (qty - currentStock.getStock_quantity()));
                } else {
                    Service currentService = serviceMap.get(String.valueOf(productChargeTable.getValueAt(row, 0)));
                    if (currentService != null) {
                        currentService.setQuantity(qty);
                    }
                }

                calculate();
            } catch (NumberFormatException | NullPointerException e) {
                CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " Product Spinner Event: " + e.getMessage(), e.getMessage());
            }
        };

        TableSpinnerCellEditor tableSpinnerCellEditor = new TableSpinnerCellEditor(event);
        //TableSpinnerCellEditor tableSpinnerCellEditor = new TableSpinnerCellEditor(event, 3);
        //TableSpinnerCellEditor tableSpinnerCellEditor = new TableSpinnerCellEditor(2, 7, 3);
        productChargeTable.getColumnModel().getColumn(4).setCellEditor(tableSpinnerCellEditor);

        HashMap<String, ActionButtonEvent> actionButtonEventMap = new HashMap<>();
        actionButtonEventMap.put("delete", (ActionButtonEvent) (int row) -> {
            System.out.println("Delete: " + row);
            System.out.println("Delete: " + String.valueOf(productChargeTable.getValueAt(row, 1)));

            if (productChargeTable.isEditing()) {
                productChargeTable.getCellEditor().stopCellEditing();
                //System.out.println(String.valueOf(table.getValueAt(row, 3))); //data access
            }

            Stock removedItem = stockMap.remove(String.valueOf(productChargeTable.getValueAt(row, 0)));
            if (removedItem == null) {
                serviceMap.remove(String.valueOf(productChargeTable.getValueAt(row, 0)));
            }
            //loadItems();

            DefaultTableModel model = (DefaultTableModel) productChargeTable.getModel();
            model.removeRow(row);

            calculate();
        });
        actionButtonEventMap.put("edit", (ActionButtonEvent) (int row) -> {
            System.out.println("Edit: " + row);
        });
        TableActionPanelCellRenderer tableActionPanelCellRenderer = new TableActionPanelCellRenderer(ActionButton.EDIT_DELETE_BUTTON, actionButtonEventMap);
        productChargeTable.getColumnModel().getColumn(8).setCellRenderer(tableActionPanelCellRenderer);
    }

    /* Service Charge Table */
    private void setupServiceChargeTable() {
        SpinnerChangeEvent event = (int row, JSpinner spinner) -> {
            try {
                double qty = Double.parseDouble(String.valueOf(spinner.getValue()));

                double price = Double.parseDouble(String.valueOf(serviceChargeTable.getValueAt(row, 2)));

                double newTotal = qty * price;
                serviceChargeTable.setValueAt(newTotal, row, 6);

                Service currentService = serviceMap.get(String.valueOf(serviceChargeTable.getValueAt(row, 0)));
                currentService.setQuantity(qty);

                calculate();
            } catch (NumberFormatException | NullPointerException e) {
                CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " Service Spinner Event: " + e.getMessage(), e.getMessage());
            }
        };

        TableSpinnerCellEditor tableSpinnerCellEditor = new TableSpinnerCellEditor(event);
        //TableSpinnerCellEditor tableSpinnerCellEditor = new TableSpinnerCellEditor(2, 6);
        serviceChargeTable.getColumnModel().getColumn(3).setCellEditor(tableSpinnerCellEditor);

        HashMap<String, ActionButtonEvent> actionButtonEventMap = new HashMap<>();
        actionButtonEventMap.put("delete", (ActionButtonEvent) (int row) -> {
            //System.out.println("Delete: " + row);
            //System.out.println("Delete: " + String.valueOf(serviceChargeTable.getValueAt(row, 1)));

            if (serviceChargeTable.isEditing()) {
                serviceChargeTable.getCellEditor().stopCellEditing();
                //System.out.println(String.valueOf(table.getValueAt(row, 3))); //data access
            }

            serviceMap.remove(String.valueOf(serviceChargeTable.getValueAt(row, 0)));
            //loadItems();

            DefaultTableModel model = (DefaultTableModel) serviceChargeTable.getModel();
            model.removeRow(row);

            calculate();
        });
        actionButtonEventMap.put("edit", (ActionButtonEvent) (int row) -> {
            System.out.println("Edit: " + row);
        });
        TableActionPanelCellRenderer tableActionPanelCellRenderer = new TableActionPanelCellRenderer(ActionButton.EDIT_DELETE_BUTTON, actionButtonEventMap);
        serviceChargeTable.getColumnModel().getColumn(7).setCellRenderer(tableActionPanelCellRenderer);
    }
    // </editor-fold>

    /*
    *
    * Service UI Actions
     */
    // <editor-fold defaultstate="collapsed" desc="Service UI">
    private boolean isService = false;

    private void toggleServicePanel() {
        jPanel14.setVisible(true);
        servicePanel.setVisible(isServicesBox.isSelected());
        if (!isServicesBox.isSelected()) {
            serviceChargePanel.setVisible(isServicesBox.isSelected());
            if (isService) {
                isService = false;
                DefaultTableModel model = (DefaultTableModel) serviceChargeTable.getModel();
                model.setRowCount(0);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Unused ServiceChargePanel">
    /* Service Charge Panel Visibility */
    private void toggleServiceChargePanel() {
        serviceChargePanel.setVisible(isService);
    }
    /* Service Charge Panel Visibility */
    // </editor-fold>
    // </editor-fold>

    /*
    *
    * Data Loading
    * &
    * Selection
     */
    // <editor-fold defaultstate="collapsed" desc="Data Loading & Selection">
    private String supplier = "0";
    private String warehouse = "0";
    private String product = "";
    private String currency = "Rs.";

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    /* loadServices */
    private void loadServices(String service) {
        serviceContainer.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) serviceTable.getModel();
        model.setRowCount(0);

        model.addRow(new Object[]{0, "Service", "0602", "1500.00", "service", "Assembly Charge"});

        //String query = "SELECT * FROM `services` INNER JOIN `categories` ON `categories`.`id`=`services`.`categories_id` ";
        //if (!service.isEmpty()) {
        //    query += "WHERE `services`.`name` LIKE '%" + service + "%' "
        //            + "OR `services`.`id` LIKE '%" + service + "%' "
        //            + "OR `services`.`code` LIKE '%" + service + "%' ";
        //}
        //query += "ORDER BY `services`.`name` ASC";
        String query = "SELECT * FROM `products` "
                + "INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` "
                + "WHERE `product_type`='service' ";
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

                //rowData.add(resultSet.getString("services.id"));
                //rowData.add(resultSet.getString("services.name"));
                //rowData.add(resultSet.getString("services.code"));
                //rowData.add(resultSet.getString("services.price"));
                //rowData.add(resultSet.getString("categories.name"));
                //rowData.add(resultSet.getString("services.description"));
                rowData.add(resultSet.getString("products.id"));
                rowData.add(resultSet.getString("products.name"));
                rowData.add(resultSet.getString("products.code"));
                rowData.add(resultSet.getString("products.price"));
                rowData.add(resultSet.getString("categories.name"));
                rowData.add(resultSet.getString("products.note"));

                model.addRow(rowData);
            }
        } catch (SQLException e) {
            DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Services Search: " + e.getMessage(), e.getMessage());
        }
    }

    private void loadSuppliers(String supplier) {
        supplierContainer.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) supplierTable.getModel();
        model.setRowCount(0);

        model.addRow(new Object[]{0, "default-supplier", "default", "default"});

        String query = "SELECT * FROM `providers` WHERE `id` <> '0' ";
        if (!supplier.isEmpty()) {
            query += " AND `name` LIKE '%" + supplier + "%' "
                    + "OR `email` LIKE '%" + supplier + "%' "
                    + "OR `phone` LIKE '%" + supplier + "%' "
                    + "OR `id` LIKE '%" + supplier + "%' "
                    + "OR `code` LIKE '%" + supplier + "%'";
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
            DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Supplier Search: " + e.getMessage(), e.getMessage());
        }
    }

    private void loadWarehouses(String warehouse) {
        warehouseContainer.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) warehouseTable.getModel();
        model.setRowCount(0);

        model.addRow(new Object[]{0, "default", "default", "default"});

        String query = "SELECT * FROM `warehouses`";
        if (!warehouse.isEmpty()) {
            query += "  WHERE `name` LIKE '%" + warehouse + "%' "
                    + "OR `email` LIKE '%" + warehouse + "%' "
                    + "OR `mobile` LIKE '%" + warehouse + "%' "
                    + "OR `id` LIKE '%" + warehouse + "%' ";
        }

        try {

            ResultSet resultSet = MySQL.execute(query);

            while (resultSet.next()) {
                Vector rowData = new Vector();

                rowData.add(resultSet.getString("id"));
                rowData.add(resultSet.getString("name"));
                rowData.add(resultSet.getString("email"));
                rowData.add(resultSet.getString("mobile"));

                model.addRow(rowData);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1146) {
                System.out.println("Warehouse table isn't in the DB");
            } else {
                DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Warehouse Search: " + e.getMessage(), e.getMessage());
            }
        }
    }

    private void loadStatus() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        Vector data = new Vector();
        data.add("Completed");
        data.add("Pending");
        data.add("Ordered");

        model.addAll(data);
        model.setSelectedItem("Completed");

        statusComboBox.setModel(model);
    }

    private void loadPaymentStatus() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        Vector data = new Vector();
        data.add("Paid");
        data.add("Pending");
        data.add("Partial");

        model.addAll(data);
        model.setSelectedItem("Paid");

        paymentStatusComboBox.setModel(model);
    }

    // <editor-fold defaultstate="collapsed" desc="Unused TestData">
    /* testdata */
    private void loadOrderProducts(String product) {
        DefaultTableModel model = (DefaultTableModel) productChargeTable.getModel();
        model.setRowCount(0);

        model.addRow(new Object[]{0, "Apple", "150.00", "1025", "10", "10", "10", "1500", ""});

        String query = "SELECT * FROM `products` INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` INNER JOIN `brands` ON `brands`.`id`=`products`.`brand_id` ";
        if (!product.isEmpty()) {
            query += "WHERE `products`.`name` LIKE '%" + product + "%' "
                    + "OR `products`.`id` LIKE '%" + product + "%' "
                    + "OR `products`.`code` LIKE '%" + product + "%' ";
        }
        query += "ORDER BY `products`.`name` ASC";

        try {
            ResultSet resultSet = MySQL.execute(query);

            while (resultSet.next()) {
                Vector rowData = new Vector();

                rowData.add(resultSet.getString("products.id"));
                rowData.add(resultSet.getString("products.name"));
                rowData.add(resultSet.getString("products.price"));
                //rowData.add(resultSet.getString("100"));
                rowData.add("100");
                rowData.add("2");
                rowData.add("10");
                rowData.add("10");
                rowData.add(Double.parseDouble(resultSet.getString("products.price")) * 2);
                rowData.add("");

                model.addRow(rowData);
            }
        } catch (SQLException e) {
            DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Order Products Search: " + e.getMessage(), e.getMessage());
        }
    }
    // </editor-fold>

    private void reset() {
        this.supplier = "";
        this.warehouse = "";
        JPanel[] containers = {dateContainer, supplierContainer, warehouseContainer};
        for (JPanel container : containers) {
            container.setVisible(false);
        }
        JTextField[] fields = {supplierField, warehouseField};
        for (JTextField field : fields) {
            field.setText("");
        }
        //jDateChooser1.setDate();
        //loadPassengerTable();
    }

    private void toggleProductSearch() {
        this.product = "";
        productField.setText("");
        if (productContainer.isVisible()) {
            productContainer.setVisible(!productContainer.isVisible());
        } else {
            loadProducts("");
        }
        productField.requestFocus();
    }

    private void toggleServiceSearch() {
        serviceField.setText("");
        if (serviceContainer.isVisible()) {
            serviceContainer.setVisible(!serviceContainer.isVisible());
        } else {
            loadServices("");
        }
        serviceField.requestFocus();
    }
    // </editor-fold>

    /*
    *
    * Item (Product) Selection
     */
    // <editor-fold defaultstate="collapsed" desc="Item (Product) Selection">
    //public void addProductToInvoice(String stock_id, String quantity, String discount, String tax) {
    //    System.out.println(stock_id + " : " + quantity + " : " + discount + " : " + tax);
    //}
    private DialogModal modal;

    private void loadItems() {
        DefaultTableModel tModel = (DefaultTableModel) productChargeTable.getModel();
        tModel.setRowCount(0);

        double total = 0;
        //double total = totalLabel.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel.getText());
        try {
            total = totalLabel1.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel1.getText().replace(getCurrency(), ""));
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " loadItems: " + e.getMessage(), e.getMessage());
        }

        //int count = 0;
        for (Stock stock : stockMap.values()) {
            Vector<String> rowData = new Vector<>();

            //rowData.add(String.valueOf(++count));
            rowData.add(stock.getStringStock_id());
            rowData.add(stock.getName());
            //rowData.add(stock.getCode());
            rowData.add(String.valueOf(stock.getStock_cost()));
            rowData.add(String.valueOf(stock.getStock_quantity()));
            rowData.add(String.valueOf(stock.getQuantity()));
            rowData.add(String.valueOf(stock.getStock_discount()));
            rowData.add(String.valueOf(stock.getStock_tax()));

            double itemTotal = (stock.getQuantity() * stock.getStock_cost()) - stock.getStock_discount() + stock.getStock_tax();
            total += itemTotal;

            rowData.add(String.valueOf(itemTotal));

            tModel.addRow(rowData);
        }

        for (Service service : serviceMap.values()) {
            Vector<String> rowData = new Vector<>();

            //rowData.add(String.valueOf(++count));
            rowData.add(service.getStringId());
            rowData.add(service.getName());
            //rowData.add(stock.getCode());
            rowData.add(String.valueOf(service.getPrice()));
            rowData.add("N/A");
            rowData.add(String.valueOf(service.getQuantity()));
            rowData.add(String.valueOf(service.getDiscount()));
            rowData.add(String.valueOf(service.getTax()));

            double itemTotal = (service.getQuantity() * service.getPrice()) - service.getDiscount() + service.getTax();
            total += itemTotal;

            rowData.add(String.valueOf(itemTotal));

            tModel.addRow(rowData);
        }

        totalLabel1.setText(String.valueOf(total));
        calculate();
    }

    private HashMap<String, Stock> stockMap = new HashMap<>();

    public void addProductToInvoice(Stock stock) {
        //System.out.println(stock.getStock_id() + " : " + stock.getQuantity() + " : " + stock.getStock_discount() + " : " + stock.getStock_tax());

        if (stockMap.get(stock.getStringStock_id()) == null) {
            stockMap.put(stock.getStringStock_id(), stock);
        } else {
            Stock currentStock = stockMap.get(stock.getStringStock_id());
            int result = JOptionPane.showConfirmDialog(modal, "Do you want to update the Quantity?", "Message", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                double qty = currentStock.getQuantity() + stock.getQuantity();
                currentStock.setQuantity((qty > stock.getStock_quantity() ? stock.getStock_quantity() : qty));
                currentStock.setStock_discount(currentStock.getStock_discount() + stock.getStock_discount());
                currentStock.setStock_tax(currentStock.getStock_tax() + stock.getStock_tax());
            }
        }
        loadItems();
        toggleProductSearch();
        //reset();
    }

    public void closeModal() {
        modal.dispose();
        modal = null;
        System.gc();
    }

    // <editor-fold defaultstate="collapsed" desc="Unused loadProductToOrderTable">
    /* loadProducts to Order Table */
    private void loadOrderItems() {
        DefaultTableModel tModel = (DefaultTableModel) productChargeTable.getModel();
        tModel.setRowCount(0);

        double total = 0;
        //double total = totalLabel1.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel1.getText());
        try {
            total = totalLabel1.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel1.getText().replace(getCurrency(), ""));
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " loadOrderItems: " + e.getMessage(), e.getMessage());
        }

        //int count = 0;
        for (Stock stock : stockMap.values()) {
            Vector<String> rowData = new Vector<>();

            //rowData.add(String.valueOf(++count));
            rowData.add(stock.getStringStock_id());
            rowData.add(stock.getName());
            //rowData.add(stock.getCode());
            rowData.add(String.valueOf(stock.getStock_price()));
            rowData.add(String.valueOf(stock.getStock_quantity()));
            rowData.add(String.valueOf(stock.getQuantity()));
            rowData.add(String.valueOf(stock.getStock_discount()));
            rowData.add(String.valueOf(stock.getStock_tax()));

            double itemTotal = (stock.getQuantity() * stock.getStock_price()) - stock.getStock_discount() + stock.getStock_tax();
            total += itemTotal;

            rowData.add(String.valueOf(itemTotal));

            tModel.addRow(rowData);
        }

        totalLabel1.setText(String.valueOf(total));
        calculate();
    }
    /* loadProducts to Order Table */
    // </editor-fold>

    private HashMap<String, Service> serviceMap = new HashMap<>();

    private void addService() {
        Service service = new Service();

        int selectedRow = serviceTable.getSelectedRow();

        if (selectedRow != -1) {

            service.setId(String.valueOf(serviceTable.getValueAt(selectedRow, 0)));
            service.setName(String.valueOf(serviceTable.getValueAt(selectedRow, 1)));

            service.setPrice(String.valueOf(serviceTable.getValueAt(selectedRow, 3)));

            service.setQuantity(1);
            service.setDiscount(String.valueOf(0));
            service.setTax(String.valueOf(0));

            addServiceToInvoice(service);
            this.isService = true;
            //toggleServiceChargePanel();
            serviceContainer.setVisible(false);
        }
    }

    public void addServiceToInvoice(Service service) {
        //System.out.println(service.getId() + " : " + service.getQuantity() + " : " + service.getDiscount() + " : " + service.getTax());

        if (serviceMap.get(service.getStringId()) == null) {
            serviceMap.put(service.getStringId(), service);
        } else {
            Service currentService = serviceMap.get(service.getStringId());
            int result = JOptionPane.showConfirmDialog(modal, "Do you want to update the Quantity?", "Message", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                double qty = currentService.getQuantity() + service.getQuantity();
                currentService.setQuantity(qty);
                currentService.setDiscount(currentService.getDiscount() + service.getDiscount());
                currentService.setTax(currentService.getTax() + service.getTax());
            }
        }
        loadItems();
        toggleServiceSearch();
        //reset();
    }

    // <editor-fold defaultstate="collapsed" desc="Unused loadServicesToServiceTable">
    /* loadServices to Service Table */
    private void loadServiceItems() {
        DefaultTableModel tModel = (DefaultTableModel) serviceChargeTable.getModel();
        tModel.setRowCount(0);

        double total = 0;
        //double total = totalLabel1.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel1.getText());
        try {
            total = totalLabel1.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel1.getText().replace(getCurrency(), ""));
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " loadServiceItems: " + e.getMessage(), e.getMessage());
        }

        //int count = 0;
        for (Service service : serviceMap.values()) {
            Vector<String> rowData = new Vector<>();

            //rowData.add(String.valueOf(++count));
            rowData.add(service.getStringId());
            rowData.add(service.getName());
            //rowData.add(stock.getCode());
            rowData.add(String.valueOf(service.getPrice()));
            rowData.add("N/A");
            rowData.add(String.valueOf(service.getQuantity()));
            rowData.add(String.valueOf(service.getDiscount()));
            rowData.add(String.valueOf(service.getTax()));

            double itemTotal = (service.getQuantity() * service.getPrice()) - service.getDiscount() + service.getTax();
            total += itemTotal;

            rowData.add(String.valueOf(itemTotal));

            tModel.addRow(rowData);
        }

        totalLabel1.setText(String.valueOf(total));
        calculate();
    }
    // </editor-fold>  

    private void calculate() {
        double subtotal = 0.00;

        for (Stock stock : stockMap.values()) {
            subtotal += ((stock.getStock_cost() * stock.getQuantity()) - stock.getStock_discount()) + stock.getStock_tax();
        }

        if (isService) {
            for (Service service : serviceMap.values()) {
                subtotal += ((service.getPrice() * service.getQuantity()) - service.getDiscount()) + service.getTax();
            }
        }

        double orderTax = 0.00;
        try {
            orderTax = Integer.parseInt(String.valueOf(orderTaxField.getText()));
            //orderTax = Double.valueOf(String.valueOf(orderTaxField.getText().replace(currency, "")));
            int taxAmount = (int) (subtotal * (orderTax / 100));
            taxLabel1.setText(getCurrency() + taxAmount + " (" + orderTax + "%)");
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate tax: " + e.getMessage(), e.getMessage());
        }
        double orderDiscount = 0.00;
        try {
            orderDiscount = Double.parseDouble(String.valueOf(discountField.getText()));
            //orderDiscount = Double.valueOf(String.valueOf(discountField.getText().replace(currency, "")));
            discountLabel.setText(getCurrency() + orderDiscount);
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate discount: " + e.getMessage(), e.getMessage());
        }
        double orderShipping = 0.00;
        try {
            orderShipping = Double.parseDouble(String.valueOf(shippingField.getText()));
            //orderShipping = Double.valueOf(String.valueOf(shippingField.getText().replace(currency, "")));
            shippingLabel.setText(getCurrency() + orderShipping);
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate shipping: " + e.getMessage(), e.getMessage());
        }

        double sub = (subtotal - orderDiscount) + orderShipping;
        int tax = (int) (sub * (orderTax / 100));

        double total = 0.00;
        //total = subtotal - orderDiscount + orderShipping + orderTax;
        total = sub + tax;

        shippingLabel.setText(getCurrency() + orderShipping);
        discountLabel.setText(getCurrency() + orderDiscount);
        taxLabel1.setText(getCurrency() + tax + "(" + orderTax + "%)");
        totalLabel1.setText(getCurrency() + total);

        double payment = 0.00;
        try {
            payment = Double.parseDouble(String.valueOf(paymentField.getText()));
            //payment = Double.valueOf(String.valueOf(paymentField.getText().replace(currency, "")));
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate payment: " + e.getMessage(), e.getMessage());
        }

        double balance = 0.00;
        balance = payment - total;

        paymentLabel1.setText(currency + payment);
        balanceLabel1.setText(currency + balance);
        balanceLabel1.setForeground(balance >= 0 ? paymentLabel1.getForeground() : Color.RED);
    }
    // </editor-fold>

    /*
    *
    * Purchase Submission
     */
    private boolean submitPurchase() {
        Date date;

        String supplierName = supplierField.getText();
        String supplierId = this.supplier;

        String warehouseName = warehouseField.getText();
        String warehouseId = this.warehouse;

        supplierName = supplierName.isBlank() ? "walk-in-customer" : supplierField.getText();
        warehouseName = warehouseName.isBlank() ? "Default" : warehouseField.getText();

        String referenceNo = referenceNoField.getText();

        int itemCount = stockMap.size();
        //int serviceCount = serviceMap.size();

        String payment = paymentField.getText();

        if (referenceNo.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please eneter Reference No!", "Warning", JOptionPane.WARNING_MESSAGE);
            referenceNoField.requestFocus();
        } else if (jDateChooser1.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select Date!", "Warning", JOptionPane.WARNING_MESSAGE);
            jDateChooser1.requestFocus();
        } else if (supplierName.isBlank() && !supplierId.equals("0")) {
            JOptionPane.showMessageDialog(this, "Please select Provider!", "Warning", JOptionPane.WARNING_MESSAGE);
            supplierField.requestFocus();
            this.supplier = "0";
        } else if (warehouseName.isBlank() && !warehouseId.equals("0")) {
            JOptionPane.showMessageDialog(this, "Please select Warehouse!", "Warning", JOptionPane.WARNING_MESSAGE);
            warehouseField.requestFocus();
            this.warehouse = "0";
            //} else if (itemCount == 0 && serviceCount == 0) {
        } else if (itemCount == 0) {
            JOptionPane.showMessageDialog(this, "Please add atleast one Product!", "Warning", JOptionPane.WARNING_MESSAGE);
            productField.requestFocus();
            if (itemCount == 0 && isServicesBox.isSelected()) {
                serviceField.requestFocus();
            }
        } else {

            String status = String.valueOf(statusComboBox.getSelectedItem());
            String paymentStatus = String.valueOf(paymentStatusComboBox.getSelectedItem());

            double paymentAmount = Double.parseDouble(payment);
            double total = Double.parseDouble(totalLabel1.getText().replace(currency, ""));

            boolean isValidPayment = true;
            if (payment.isBlank() || payment.equals("0.00") || total > paymentAmount) {
                if (paymentStatus.equalsIgnoreCase("paid")) {
                    isValidPayment = false;
                    int result = JOptionPane.showConfirmDialog(this, "Are you sure payment been low? Does it need to make partial?", "Payment Warning", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        paymentStatusComboBox.requestFocus();
                    } else {
                        paymentField.requestFocus();
                    }
                } else if (paymentStatus.equalsIgnoreCase("partial")) {
                    int result = JOptionPane.showConfirmDialog(this, "Are you sure payment need to be partial?", "Payment Warning", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.NO_OPTION) {
                        paymentStatusComboBox.requestFocus();
                        isValidPayment = false;
                    }
                }
            }

            if (isValidPayment) {

                try {
                    if (paymentStatus.equalsIgnoreCase("paid") && total > paymentAmount) {
                        JOptionPane.showMessageDialog(this, "Payment is not enough", "Invalid Payment", JOptionPane.WARNING_MESSAGE);
                        paymentField.requestFocus();
                        return false;
                    }
                } catch (NumberFormatException | NullPointerException e) {
                    CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " submit purchase payment|total: " + e.getMessage(), e.getMessage());
                }

                /* GUI Data */
                date = jDateChooser1.getDate();
                SQLDateFormatter dateFormatter = new SQLDateFormatter();
                String stringDate = dateFormatter.getStringDate(date);

                supplierName = supplierName.isBlank() ? "default-supplier" : supplierField.getText();
                warehouseName = warehouseName.isBlank() ? "Default" : warehouseField.getText();

                /* Calculated Data */
                double orderTax = 0.00;
                try {
                    orderTax = Double.parseDouble(String.valueOf(orderTaxField.getText()));
                } catch (NumberFormatException | NullPointerException e) {
                    CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate tax: " + e.getMessage(), e.getMessage());
                }
                double orderDiscount = 0.00;
                try {
                    orderDiscount = Double.parseDouble(String.valueOf(discountField.getText()));
                } catch (NumberFormatException | NullPointerException e) {
                    CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate discount: " + e.getMessage(), e.getMessage());
                }
                double orderShipping = 0.00;
                try {
                    orderShipping = Double.parseDouble(String.valueOf(shippingField.getText()));
                } catch (NumberFormatException | NullPointerException e) {
                    CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate shipping: " + e.getMessage(), e.getMessage());
                }
                double subtotal = 0.00;
                try {
                    subtotal = Double.parseDouble(String.valueOf(totalLabel1.getText().replace(currency, "")));
                } catch (NumberFormatException | NullPointerException e) {
                    CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate shipping: " + e.getMessage(), e.getMessage());
                }

                double sub = (subtotal - orderDiscount) + orderShipping;
                int tax = (int) (sub * (orderTax / 100));

                total = 0.00;
                //total = subtotal - orderDiscount + orderShipping + orderTax;
                total = sub + tax;

                paymentAmount = 0.00;
                try {
                    paymentAmount = Double.parseDouble(String.valueOf(paymentField.getText()));
                    //payment = Double.valueOf(String.valueOf(paymentField.getText().replace(currency, "")));
                } catch (NumberFormatException | NullPointerException e) {
                    CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate payment: " + e.getMessage(), e.getMessage());
                }

                double balance = 0.00;
                balance = paymentAmount - total;

                /* Other Data */
                String note = noteText.getText();

                try {
                    /* Data Insertion */
                    ResultSet refResult = MySQL.execute("SELECT * FROM `purchases` WHERE `Ref`='" + referenceNo + "'");

                    String currentDate = dateFormatter.getStringDate(new Date());
                    String currentDateTime = dateFormatter.getStringDate(new Date(), "yyyy-MM-dd HH:mm:ss");

                    if (refResult.next()) {
                        JOptionPane.showMessageDialog(this, "Reference No. already used!", "Warning", JOptionPane.WARNING_MESSAGE);
                        referenceNoField.requestFocus();
                    } else {

                        boolean isComplete = true;
                        long mil = System.currentTimeMillis();
                        String id = String.valueOf((int) mil).substring(3);
                        //String id = String.valueOf(mil).substring(3);
                        //System.out.println(id);

                        String purchaseQuery = "INSERT INTO `purchases` "
                                + "(`id`, `user_id`, `Ref`, `date`, `provider_id`, `warehouse_id`, `tax_rate`, `TaxNet`, `discount`, `shipping`, "
                                + "`GrandTotal`, `paid_amount`, `statut`, `payment_statut`, `notes`, `created_at`, `updated_at`, `deleted_at`) "
                                //+ "VALUES ('" + id + "', '2', '" + referenceNo + "', '" + stringDate + "', " + (supplierId != "0" ? supplierId : "NULL") + ", '" + warehouseId + "', '" + orderTax + "', '" + tax + "', '" + orderDiscount + "', '" + orderShipping + "', "
                                + "VALUES ('" + id + "', '" + Application.getUser().getStringId() + "', '" + referenceNo + "', '" + stringDate + "', '" + supplierId + "', '" + warehouseId + "', '" + orderTax + "', '" + tax + "', '" + orderDiscount + "', '" + orderShipping + "', "
                                + "'" + total + "', '" + payment + "', '" + status + "', '" + paymentStatus + "', '" + note + "', '" + currentDateTime + "', NULL, NULL)";

                        try {
                            MySQL.execute(purchaseQuery);
                        } catch (SQLException e) {
                            isComplete = false;
                            DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Purchase Submit Purchase row: " + e.getMessage(), e.getMessage());
                        }

                        if (isComplete) {
                            String payQuery = "INSERT INTO `payment_purchases` (`user_id`, `date`, `Ref`, `purchase_id`, `montant`, `change`, `Reglement`, `notes`, `created_at`, `updated_at`, `deleted_at`) VALUES ('" + Application.getUser().getStringId() + "', '" + currentDate + "', '" + (id + 100) + "', '" + id + "', '" + total + "', '" + balance + "', '" + payment + "', NULL, '" + currentDateTime + "', NULL, NULL)";
                            try {
                                MySQL.execute(payQuery);
                            } catch (SQLException e) {
                                isComplete = false;
                                DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Sale Submit Sale payment: " + e.getMessage(), e.getMessage());
                            }
                        }

                        if (isComplete) {
                            for (Stock stock : stockMap.values()) {
                                if (stock.getStock_id() == 0) {

                                    String[] keys = {"id"};
                                    String stockQuery = "INSERT INTO "
                                            + "`stocks` (`name`, `code`, `cost`, `price`, `sale_price`, `is_expire`, `exp_date`, `mfd_date`, `quantity`, `products_id`) "
                                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

                                        ResultSet resultSet = MySQL.executeInsert(preparedStatement);

                                        if (resultSet.next()) {

                                            double itemTotal = stock.getStock_price() * stock.getQuantity() - stock.getStock_discount() + stock.getStock_tax();
                                            String purchaseItemQuery = "INSERT INTO "
                                                    + "`purchase_details` (`cost`, `purchase_unit_id`, `TaxNet`, `discount`, `discount_method`, `purchase_id`, `product_id`, "
                                                    + "`product_variant_id`, `imei_number`, `total`, `quantity`, `created_at`, `updated_at`, `tax_method_id`, `stocks_id`) "
                                                    + "VALUES ('" + stock.getStock_cost() + "', NULL, '" + stock.getStock_tax() + "', '" + stock.getStock_discount() + "', '1', '" + id + "', '" + stock.getStringId() + "', "
                                                    + "NULL, NULL, '" + itemTotal + "', '" + stock.getQuantity() + "', '" + currentDateTime + "', NULL, NULL, '" + resultSet.getString(1) + "')";

                                            try {
                                                MySQL.execute(purchaseItemQuery);
                                            } catch (SQLException e) {
                                                isComplete = false;
                                                DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Purchase Submit Purchase Details row: " + e.getMessage(), e.getMessage());
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(this, "Stock Id Retrieve Failed", "Warning", JOptionPane.WARNING_MESSAGE);
                                        }
                                    } catch (SQLException e) {
                                        isComplete = false;
                                        DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Purchase Submit Stock Detail row: " + e.getMessage(), e.getMessage());
                                    }
                                } else {

                                    double itemTotal = stock.getStock_price() * stock.getQuantity() - stock.getStock_discount() + stock.getStock_tax();
                                    String purchaseItemQuery = "INSERT INTO "
                                            + "`purchase_details` (`cost`, `purchase_unit_id`, `TaxNet`, `discount`, `discount_method`, `purchase_id`, `product_id`, "
                                            + "`product_variant_id`, `imei_number`, `total`, `quantity`, `created_at`, `updated_at`, `tax_method_id`, `stocks_id`) "
                                            + "VALUES ('" + stock.getStock_cost() + "', NULL, '" + stock.getStock_tax() + "', '" + stock.getStock_discount() + "', '1', '" + id + "', '" + stock.getStringId() + "', "
                                            + "NULL, NULL, '" + itemTotal + "', '" + stock.getQuantity() + "', '" + currentDateTime + "', NULL, NULL, '" + stock.getStringStock_id() + "')";
                                    try {
                                        MySQL.execute(purchaseItemQuery);
                                    } catch (SQLException e) {
                                        isComplete = false;
                                        DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Purchase Submit Sale Details row: " + e.getMessage(), e.getMessage());
                                    }

                                    double qty = stock.getStock_quantity() + stock.getQuantity();
                                    String stockQuery = "UPDATE `stocks` SET `quantity`='" + qty + "' WHERE `id`='" + stock.getStock_id() + "';";
                                    //String stockQuery = "UPDATE `stocks` SET `quantity`=`quantity`+'"+stock.getQuantity()+"' WHERE `id`='" + stock.getStock_id() + "';";
                                    try {
                                        MySQL.execute(stockQuery);
                                    } catch (SQLException e) {
                                        isComplete = false;
                                        DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Purchase Submit Stock Update row: " + e.getMessage(), e.getMessage());
                                    }

                                }

                                //stock.getStringStock_id();
                            }
                        }
//                        for (Stock stock : stockMap.values()) {
//                            double itemTotal = stock.getStock_price() * stock.getQuantity() - stock.getStock_discount() + stock.getStock_tax();
//                            String purchaseItemQuery = "INSERT INTO "
//                                    + "`purchase_details` (`date`, `purchase_id`, `product_id`, `product_variant_id`, `imei_number`, `price`, `purchase_unit_id`, "
//                                    + "`TaxNet`, `discount`, `discount_method`, `total`, `quantity`, `created_at`, `updated_at`, `tax_method_id`) "
//                                    + "VALUES ('" + currentDate + "', '" + id + "', '" + stock.getStringId() + "', NULL, NULL, '" + stock.getStock_price() + "', NULL, "
//                                    + "'" + stock.getStock_tax() + "', '" + stock.getStock_discount() + "', '1', '" + itemTotal + "', '" + stock.getQuantity() + "', '" + currentDateTime + "', NULL, 2);";
//                            try {
//                                MySQL.execute(purchaseItemQuery);
//                            } catch (SQLException e) {
//                                isComplete = false;
//                                DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Purchase Submit Purchase Details row: " + e.getMessage(), e.getMessage());
//                            }
//                            //stock.getStringStock_id();
//                        }
//                        for (Service service : serviceMap.values()) {
//                            double itemTotal = service.getPrice() * service.getQuantity() - service.getDiscount() + service.getTax();
//                            String purchaseItemQuery = "INSERT INTO "
//                                    + "`purchase_details` (`date`, `purchase_id`, `product_id`, `product_variant_id`, `imei_number`, `price`, `purchase_unit_id`, "
//                                    + "`TaxNet`, `discount`, `discount_method`, `total`, `quantity`, `created_at`, `updated_at`, `tax_method_id`) "
//                                    + "VALUES ('" + currentDate + "', '" + id + "', '" + service.getStringId() + "', NULL, NULL, '" + service.getPrice() + "', NULL, "
//                                    + "'" + service.getTax() + "', '" + service.getDiscount() + "', '1', '" + itemTotal + "', '" + service.getQuantity() + "', '" + currentDateTime + "', NULL, 2);";
//                            try {
//                                MySQL.execute(purchaseItemQuery);
//                            } catch (SQLException e) {
//                                isComplete = false;
//                                DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Purchase Submit Purchase Details row: " + e.getMessage(), e.getMessage());
//                            }
//                            //service.getStringId();
//                        }

                        if (isComplete) {
                            Shop shop = Application.getShop();

                            HashMap<String, Object> parameters = new HashMap<>();
                            parameters.put("ShopName", shop.getName());
                            parameters.put("Email", shop.getEmail());
                            parameters.put("Mobile", shop.getMobile());
                            parameters.put("Address", shop.getAddress());
                            parameters.put("Logo", shop.getLogo2Path());
                            parameters.put("InvoiceNo", referenceNo);
                            parameters.put("Customer", supplierName);
                            parameters.put("Time", currentDateTime.replace(currentDate, ""));
                            parameters.put("Date", currentDate);
                            parameters.put("Tax", String.valueOf(tax));
                            parameters.put("Discount", String.valueOf(orderDiscount));
                            parameters.put("Method", "Cash");
                            parameters.put("Total", String.valueOf(total));
                            parameters.put("NetAmount ", String.valueOf(total));
                            parameters.put("Payment", String.valueOf(payment));
                            parameters.put("Balance", String.valueOf(balance));
                            new PrintReport().ViewReport("/ewision/sahan/report/jasper/posInvoice3A43.jasper", parameters, new JRTableModelDataSource(productChargeTable.getModel()));

                            JOptionPane.showMessageDialog(this, "Successfully completed!", "Successful", JOptionPane.INFORMATION_MESSAGE);
                            //Application.appService.openCreatePurchase();
                            Application.appService.openPurchaseList();
                        } else {
                            JOptionPane.showMessageDialog(this, "Something went wrong!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }

                } catch (SQLException ex) {
                    DatabaseLogger.logger.log(Level.SEVERE, "SQLException in " + getClass().getName() + " Purchase Submit Ref Search: " + ex.getMessage() + " -- " + ex.getLocalizedMessage(), ex.getMessage());
                }

            }
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        referenceNoField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        bodyScroll = new javax.swing.JScrollPane();
        bodyPanel = new javax.swing.JPanel();
        searchPanelContainer = new javax.swing.JPanel();
        datePanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        dateContainer = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        supplierPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        supplierContainer = new javax.swing.JPanel();
        supplierScroll = new javax.swing.JScrollPane();
        supplierTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        allSuppliersButton = new javax.swing.JButton();
        supplierField = new javax.swing.JTextField();
        warehousePanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        warehouseContainer = new javax.swing.JPanel();
        warehouseScroll = new javax.swing.JScrollPane();
        warehouseTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        warehouseField = new javax.swing.JTextField();
        allWarehousesButton = new javax.swing.JButton();
        productPanel = new javax.swing.JPanel();
        productContainer = new javax.swing.JPanel();
        productScroll = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        allProductsButton = new javax.swing.JButton();
        productField = new javax.swing.JTextField();
        detailPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productChargeTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        orderTaxField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        statusComboBox = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        paymentStatusComboBox = new javax.swing.JComboBox<>();
        discountField = new javax.swing.JFormattedTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        shippingField = new javax.swing.JFormattedTextField();
        jLabel27 = new javax.swing.JLabel();
        paymentField = new javax.swing.JFormattedTextField();
        notePanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        noteText = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        servicePanel = new javax.swing.JPanel();
        serviceContainer = new javax.swing.JPanel();
        serviceScroll1 = new javax.swing.JScrollPane();
        serviceTable = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        allServicesButton = new javax.swing.JButton();
        serviceField = new javax.swing.JTextField();
        isServicesBox = new javax.swing.JCheckBox();
        serviceChargePanel = new javax.swing.JPanel();
        serviceChargeScroll = new javax.swing.JScrollPane();
        serviceChargeTable = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        discountLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        totalLabel1 = new javax.swing.JLabel();
        taxLabel1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        shippingLabel = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel25 = new javax.swing.JLabel();
        paymentLabel1 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        balanceLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        botomPanel = new javax.swing.JPanel();
        submitButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setMinimumSize(new java.awt.Dimension(20, 5));

        headerPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Create Purchase");

        referenceNoField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        referenceNoField.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 5));

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(referenceNoField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(referenceNoField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator1.setMinimumSize(new java.awt.Dimension(20, 5));

        bodyScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        bodyScroll.setMinimumSize(new java.awt.Dimension(20, 5));
        bodyScroll.setVerifyInputWhenFocusTarget(false);

        bodyPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        searchPanelContainer.setMinimumSize(new java.awt.Dimension(20, 5));

        datePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        datePanel.setMaximumSize(new java.awt.Dimension(250, 32767));
        datePanel.setMinimumSize(new java.awt.Dimension(20, 5));

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setText("Date");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(".");

        javax.swing.GroupLayout dateContainerLayout = new javax.swing.GroupLayout(dateContainer);
        dateContainer.setLayout(dateContainerLayout);
        dateContainerLayout.setHorizontalGroup(
            dateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dateContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        dateContainerLayout.setVerticalGroup(
            dateContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dateContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jDateChooser1.setDateFormatString("y-M-d");
        jDateChooser1.setMinSelectableDate(new java.util.Date(1640979067000L));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout datePanelLayout = new javax.swing.GroupLayout(datePanel);
        datePanel.setLayout(datePanelLayout);
        datePanelLayout.setHorizontalGroup(
            datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(datePanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        datePanelLayout.setVerticalGroup(
            datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datePanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(dateContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        supplierPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        supplierPanel.setMaximumSize(new java.awt.Dimension(32767, 250));
        supplierPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setText("Supplier");
        jLabel6.setMinimumSize(new java.awt.Dimension(20, 5));

        supplierContainer.setMinimumSize(new java.awt.Dimension(20, 5));

        supplierScroll.setMinimumSize(new java.awt.Dimension(20, 5));

        supplierTable.setModel(new javax.swing.table.DefaultTableModel(
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
        supplierTable.setMinimumSize(new java.awt.Dimension(20, 5));
        supplierTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supplierTableMouseClicked(evt);
            }
        });
        supplierScroll.setViewportView(supplierTable);
        if (supplierTable.getColumnModel().getColumnCount() > 0) {
            supplierTable.getColumnModel().getColumn(0).setMaxWidth(80);
        }

        javax.swing.GroupLayout supplierContainerLayout = new javax.swing.GroupLayout(supplierContainer);
        supplierContainer.setLayout(supplierContainerLayout);
        supplierContainerLayout.setHorizontalGroup(
            supplierContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(supplierScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        supplierContainerLayout.setVerticalGroup(
            supplierContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(supplierScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel6.setMinimumSize(new java.awt.Dimension(20, 5));

        allSuppliersButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allSuppliersButton.setIconTextGap(2);
        allSuppliersButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allSuppliersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allSuppliersButtonActionPerformed(evt);
            }
        });

        supplierField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supplierFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(supplierField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allSuppliersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allSuppliersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout supplierPanelLayout = new javax.swing.GroupLayout(supplierPanel);
        supplierPanel.setLayout(supplierPanelLayout);
        supplierPanelLayout.setHorizontalGroup(
            supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(supplierPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        supplierPanelLayout.setVerticalGroup(
            supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(supplierContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        warehousePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        warehousePanel.setMaximumSize(new java.awt.Dimension(250, 250));
        warehousePanel.setMinimumSize(new java.awt.Dimension(20, 5));

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel7.setText("Warehouse");

        warehouseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        warehouseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                warehouseTableMouseClicked(evt);
            }
        });
        warehouseScroll.setViewportView(warehouseTable);
        if (warehouseTable.getColumnModel().getColumnCount() > 0) {
            warehouseTable.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        javax.swing.GroupLayout warehouseContainerLayout = new javax.swing.GroupLayout(warehouseContainer);
        warehouseContainer.setLayout(warehouseContainerLayout);
        warehouseContainerLayout.setHorizontalGroup(
            warehouseContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(warehouseContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(warehouseScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        warehouseContainerLayout.setVerticalGroup(
            warehouseContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(warehouseContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(warehouseScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        warehouseField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                warehouseFieldKeyReleased(evt);
            }
        });

        allWarehousesButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allWarehousesButton.setIconTextGap(2);
        allWarehousesButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allWarehousesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allWarehousesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(warehouseField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allWarehousesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(warehouseField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allWarehousesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout warehousePanelLayout = new javax.swing.GroupLayout(warehousePanel);
        warehousePanel.setLayout(warehousePanelLayout);
        warehousePanelLayout.setHorizontalGroup(
            warehousePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(warehousePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(warehousePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(warehouseContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(warehousePanelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        warehousePanelLayout.setVerticalGroup(
            warehousePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(warehousePanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(warehouseContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout searchPanelContainerLayout = new javax.swing.GroupLayout(searchPanelContainer);
        searchPanelContainer.setLayout(searchPanelContainerLayout);
        searchPanelContainerLayout.setHorizontalGroup(
            searchPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelContainerLayout.createSequentialGroup()
                .addComponent(datePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supplierPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(warehousePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        searchPanelContainerLayout.setVerticalGroup(
            searchPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(datePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(supplierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(warehousePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        productPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        productPanel.setMaximumSize(new java.awt.Dimension(32767, 250));
        productPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        productContainer.setMinimumSize(new java.awt.Dimension(20, 5));

        productScroll.setMinimumSize(new java.awt.Dimension(20, 5));

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Code", "Cost", "Brand", "Category"
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
        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productTableMouseClicked(evt);
            }
        });
        productScroll.setViewportView(productTable);
        if (productTable.getColumnModel().getColumnCount() > 0) {
            productTable.getColumnModel().getColumn(0).setMaxWidth(80);
        }

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
                .addComponent(productScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
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
                .addGap(0, 7, Short.MAX_VALUE))
        );

        detailPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        jScrollPane1.setMinimumSize(new java.awt.Dimension(20, 5));

        productChargeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product", "Unit Price", "Stock", "Qty", "Discount", "Tax", "Subtotal", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productChargeTable.setMinimumSize(new java.awt.Dimension(20, 5));
        productChargeTable.setRowHeight(50);
        productChargeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productChargeTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(productChargeTable);
        if (productChargeTable.getColumnModel().getColumnCount() > 0) {
            productChargeTable.getColumnModel().getColumn(0).setMaxWidth(70);
        }

        jPanel3.setMinimumSize(new java.awt.Dimension(20, 5));

        jPanel1.setLayout(new java.awt.GridLayout(1, 0, 2, 2));

        jLabel15.setText("Order Tax");

        orderTaxField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        orderTaxField.setText("0");
        orderTaxField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                orderTaxFieldFocusGained(evt);
            }
        });
        orderTaxField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                orderTaxFieldKeyReleased(evt);
            }
        });

        jLabel18.setText("Status *");

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Completed", "Pending", "Ordered" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(orderTaxField)
                    .addComponent(jLabel18)
                    .addComponent(statusComboBox, 0, 154, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderTaxField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);

        jLabel16.setText("Discount");

        jLabel19.setText("Payment Status *");

        paymentStatusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Paid", "Pending", "Partial" }));

        discountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        discountField.setText("0.00");
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

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(paymentStatusComboBox, 0, 148, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(discountField, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel9);

        jLabel17.setText("Shipping");

        shippingField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        shippingField.setText("0.00");
        shippingField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                shippingFieldFocusGained(evt);
            }
        });
        shippingField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                shippingFieldKeyReleased(evt);
            }
        });

        jLabel27.setText("Payment");

        paymentField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        paymentField.setText("0.00");
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

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(paymentField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(shippingField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                        .addGap(20, 20, 20))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(shippingField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel10);

        noteText.setColumns(20);
        noteText.setRows(5);
        jScrollPane4.setViewportView(noteText);

        jLabel20.setText("Note");

        javax.swing.GroupLayout notePanelLayout = new javax.swing.GroupLayout(notePanel);
        notePanel.setLayout(notePanelLayout);
        notePanelLayout.setHorizontalGroup(
            notePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(notePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addComponent(jLabel20))
                .addContainerGap())
        );
        notePanelLayout.setVerticalGroup(
            notePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                    .addComponent(notePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(notePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setText("Oder Items *");
        jLabel2.setMinimumSize(new java.awt.Dimension(20, 5));

        servicePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        servicePanel.setMaximumSize(new java.awt.Dimension(32767, 250));
        servicePanel.setMinimumSize(new java.awt.Dimension(20, 5));

        serviceContainer.setMinimumSize(new java.awt.Dimension(20, 5));

        serviceScroll1.setMinimumSize(new java.awt.Dimension(20, 5));

        serviceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Code", "Price", "Category", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        serviceTable.setMinimumSize(new java.awt.Dimension(20, 5));
        serviceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                serviceTableMouseClicked(evt);
            }
        });
        serviceScroll1.setViewportView(serviceTable);
        if (serviceTable.getColumnModel().getColumnCount() > 0) {
            serviceTable.getColumnModel().getColumn(0).setMaxWidth(80);
        }

        javax.swing.GroupLayout serviceContainerLayout = new javax.swing.GroupLayout(serviceContainer);
        serviceContainer.setLayout(serviceContainerLayout);
        serviceContainerLayout.setHorizontalGroup(
            serviceContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceContainerLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(serviceScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        serviceContainerLayout.setVerticalGroup(
            serviceContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(serviceScroll1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
        );

        jPanel14.setMinimumSize(new java.awt.Dimension(20, 5));

        allServicesButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allServicesButton.setIconTextGap(2);
        allServicesButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allServicesButton.setMinimumSize(new java.awt.Dimension(20, 5));
        allServicesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allServicesButtonActionPerformed(evt);
            }
        });

        serviceField.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 12, 1, 12));
        serviceField.setMinimumSize(new java.awt.Dimension(20, 5));
        serviceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                serviceFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(serviceField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(allServicesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(allServicesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serviceField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout servicePanelLayout = new javax.swing.GroupLayout(servicePanel);
        servicePanel.setLayout(servicePanelLayout);
        servicePanelLayout.setHorizontalGroup(
            servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(servicePanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(serviceContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        servicePanelLayout.setVerticalGroup(
            servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(servicePanelLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serviceContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        isServicesBox.setText("Add Service Charges");
        isServicesBox.setMinimumSize(new java.awt.Dimension(20, 5));
        isServicesBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isServicesBoxActionPerformed(evt);
            }
        });

        serviceChargePanel.setMinimumSize(new java.awt.Dimension(20, 5));

        serviceChargeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Service", "Serv. Price", "Count", "Discount", "Tax", "Subtotal", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        serviceChargeTable.setRowHeight(30);
        serviceChargeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                serviceChargeTableMouseClicked(evt);
            }
        });
        serviceChargeScroll.setViewportView(serviceChargeTable);

        javax.swing.GroupLayout serviceChargePanelLayout = new javax.swing.GroupLayout(serviceChargePanel);
        serviceChargePanel.setLayout(serviceChargePanelLayout);
        serviceChargePanelLayout.setHorizontalGroup(
            serviceChargePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceChargePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serviceChargeScroll)
                .addContainerGap())
        );
        serviceChargePanelLayout.setVerticalGroup(
            serviceChargePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceChargePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serviceChargeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSeparator3.setMinimumSize(new java.awt.Dimension(20, 5));

        jLabel21.setText(" ");
        jLabel21.setMinimumSize(new java.awt.Dimension(20, 5));

        jPanel2.setMinimumSize(new java.awt.Dimension(20, 5));

        jLabel8.setText("Order Tax");

        discountLabel.setText("Rs.0.00");

        jLabel10.setText("Discount");

        jLabel12.setText("Shipping");

        totalLabel1.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        totalLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalLabel1.setText("Rs.0.00");

        taxLabel1.setText("Rs.0.00 (0%)");

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel14.setText("Grand Total");

        shippingLabel.setText("Rs.0.00");

        jLabel25.setText("Payment");

        paymentLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        paymentLabel1.setText("Rs.0.00");

        jLabel26.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel26.setText("Balance");

        balanceLabel1.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        balanceLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        balanceLabel1.setText("Rs.0.00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(totalLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(taxLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(discountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(shippingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(balanceLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(taxLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(shippingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(totalLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(balanceLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout detailPanelLayout = new javax.swing.GroupLayout(detailPanel);
        detailPanel.setLayout(detailPanelLayout);
        detailPanelLayout.setHorizontalGroup(
            detailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailPanelLayout.createSequentialGroup()
                .addGroup(detailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(detailPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(detailPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(detailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(detailPanelLayout.createSequentialGroup()
                                .addComponent(isServicesBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(servicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(detailPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(serviceChargePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(12, 12, 12))
        );
        detailPanelLayout.setVerticalGroup(
            detailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(detailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(isServicesBox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(servicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serviceChargePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSeparator2.setMinimumSize(new java.awt.Dimension(20, 5));

        botomPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout botomPanelLayout = new javax.swing.GroupLayout(botomPanel);
        botomPanel.setLayout(botomPanelLayout);
        botomPanelLayout.setHorizontalGroup(
            botomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botomPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        botomPanelLayout.setVerticalGroup(
            botomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botomPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(detailPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(productPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
            .addComponent(botomPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addComponent(searchPanelContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(productPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(detailPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        bodyScroll.setViewportView(bodyPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(headerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bodyScroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bodyScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1737, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /*
    *
    * Actions
     */
    // <editor-fold defaultstate="collapsed" desc="Actions & Events">
    private void supplierTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_supplierTableMouseClicked
        // Select Supplier
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = supplierTable.getSelectedRow();

            if (selectedRow != -1) {
                this.supplier = String.valueOf(supplierTable.getValueAt(selectedRow, 0));
                supplierField.setText(String.valueOf(supplierTable.getValueAt(selectedRow, 1)));
                supplierContainer.setVisible(false);
                //loadPassengerTable();
            }
        }
    }//GEN-LAST:event_supplierTableMouseClicked

    private void allSuppliersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allSuppliersButtonActionPerformed
        // Search All Suppliers
        this.supplier = "";
        if (supplierContainer.isVisible()) {
            supplierContainer.setVisible(!supplierContainer.isVisible());
        } else {
            loadSuppliers("");
        }
    }//GEN-LAST:event_allSuppliersButtonActionPerformed

    private void warehouseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_warehouseTableMouseClicked
        // Select Warehouse
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = warehouseTable.getSelectedRow();

            if (selectedRow != -1) {
                this.warehouse = String.valueOf(warehouseTable.getValueAt(selectedRow, 0));
                warehouseField.setText(String.valueOf(warehouseTable.getValueAt(selectedRow, 1)));
                warehouseContainer.setVisible(false);
            }
        }
    }//GEN-LAST:event_warehouseTableMouseClicked

    private void warehouseFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_warehouseFieldKeyReleased
        // Search Warehouse
        String warehouse = warehouseField.getText();
        if (warehouse.isEmpty()) {
            warehouseContainer.setVisible(false);
        } else {
            loadWarehouses(warehouse);
        }
        this.warehouse = "";
    }//GEN-LAST:event_warehouseFieldKeyReleased

    private void allWarehousesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allWarehousesButtonActionPerformed
        // Search All Warehouses
        this.warehouse = "";
        if (warehouseContainer.isVisible()) {
            warehouseContainer.setVisible(!warehouseContainer.isVisible());
        } else {
            loadWarehouses("");
        }
    }//GEN-LAST:event_allWarehousesButtonActionPerformed

    private void supplierFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplierFieldKeyReleased
        // Search Supplier
        String supplier = supplierField.getText();
        if (supplier.isEmpty()) {
            supplierContainer.setVisible(false);
        } else {
            loadSuppliers(supplier);
        }
        this.supplier = "";
    }//GEN-LAST:event_supplierFieldKeyReleased

    private void productTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTableMouseClicked
        // Select Product
//        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
//            int selectedRow = productTable.getSelectedRow();
//
//            if (selectedRow != -1) {
//                this.product = String.valueOf(productTable.getValueAt(selectedRow, 0));
//                productField.setText(String.valueOf(productTable.getValueAt(selectedRow, 1)));
//                productContainer.setVisible(false);
//
//                loadOrderProducts(product);
//            }
//        }
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
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

                modal = new DialogModal(this);
                ////modal.openSelectStock(this, String.valueOf(productTable.getValueAt(selectedRow, 0)), String.valueOf(productTable.getValueAt(selectedRow, 1)));
                modal.openAddStock(this, product);
                modal.setVisible(true);
            }
        }
    }//GEN-LAST:event_productTableMouseClicked

    private void productFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productFieldKeyReleased
        // Search Product
        String product = productField.getText();
        if (product.isEmpty()) {
            productContainer.setVisible(false);
        } else {
            loadProducts(product);
        }
        this.product = "";
    }//GEN-LAST:event_productFieldKeyReleased

    private void serviceTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serviceTableMouseClicked
        // Select Service
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            addService();
        }
    }//GEN-LAST:event_serviceTableMouseClicked

    private void allServicesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allServicesButtonActionPerformed
        // Search All Services
        toggleServiceSearch();
    }//GEN-LAST:event_allServicesButtonActionPerformed

    private void serviceFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_serviceFieldKeyReleased
        // Search Services
        String service = serviceField.getText();
        if (service.isEmpty()) {
            serviceContainer.setVisible(false);
        } else {
            loadServices(service);
        }
    }//GEN-LAST:event_serviceFieldKeyReleased

    private void isServicesBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isServicesBoxActionPerformed
        // Toggle Service Panel
        toggleServicePanel();
    }//GEN-LAST:event_isServicesBoxActionPerformed

    private void allProductsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allProductsButtonActionPerformed
        // Search All Products
        toggleProductSearch();
    }//GEN-LAST:event_allProductsButtonActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        // Submit
        submitPurchase();
    }//GEN-LAST:event_submitButtonActionPerformed

    private void productChargeTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productChargeTableMouseClicked
        // Calculate on Changes of Product Orders
//        if (evt.getClickCount() == 1) {
//            for (int count = 0; count <= productChargeTable.getRowCount(); count++) {
//                //netTotal = Double.parseDouble(productChargeTable.getValueAt(count, 7).toString());
//                calculate();
//            }
//        }
    }//GEN-LAST:event_productChargeTableMouseClicked

    private void serviceChargeTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serviceChargeTableMouseClicked
        // Calculate on Changes of Service Orders
//        if (evt.getClickCount() == 1) {
//            int selectedRow = serviceChargeTable.getSelectedRow();
//            if (selectedRow != -1) {
//                String price = String.valueOf(serviceChargeTable.getValueAt(selectedRow, 2));
//                String qty = String.valueOf(serviceChargeTable.getValueAt(selectedRow, 3));
//
//                try {
//                    double subtotal = Double.parseDouble(price) * Double.parseDouble(qty);
//
//                    serviceChargeTable.setValueAt(subtotal, selectedRow, 6);
//                } catch (NullPointerException e) {
//                    System.out.println("Null: " + e.getMessage());
//                } catch (NumberFormatException e) {
//                    System.out.println("Format: " + e.getMessage());
//                }
//            }
//        }
    }//GEN-LAST:event_serviceChargeTableMouseClicked

    private void orderTaxFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderTaxFieldKeyReleased
        // Order Tax
        String text = orderTaxField.getText();
        orderTaxField.setText(text.isBlank() ? "0" : text);
        if (!orderTaxField.getText().equals("0")) {
            calculate();
        }
    }//GEN-LAST:event_orderTaxFieldKeyReleased

    private void discountFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountFieldKeyReleased
        // Order Discount
        String text = discountField.getText();
        discountField.setText(text.isBlank() ? "0.00" : text);
        if (!discountField.getText().equals("0.00")) {
            calculate();
        }
    }//GEN-LAST:event_discountFieldKeyReleased

    private void shippingFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_shippingFieldKeyReleased
        // Order Shipping
        String text = shippingField.getText();
        shippingField.setText(text.isBlank() ? "0.00" : text);
        if (!shippingField.getText().equals("0.00")) {
            calculate();
        }
    }//GEN-LAST:event_shippingFieldKeyReleased

    private void paymentFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentFieldKeyReleased
        // Payment
        String text = paymentField.getText();
        paymentField.setText(text.isBlank() ? "0.00" : text);
        if (!paymentField.getText().equals("0.00")) {
            calculate();
        }
    }//GEN-LAST:event_paymentFieldKeyReleased

    private void orderTaxFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_orderTaxFieldFocusGained
        // Select on Focus
        orderTaxField.selectAll();
    }//GEN-LAST:event_orderTaxFieldFocusGained

    private void discountFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_discountFieldFocusGained
        // Select on Focus
        discountField.selectAll();
    }//GEN-LAST:event_discountFieldFocusGained

    private void shippingFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_shippingFieldFocusGained
        // Select on Focus
        shippingField.selectAll();
    }//GEN-LAST:event_shippingFieldFocusGained

    private void paymentFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_paymentFieldFocusGained
        // Select on Focus
        paymentField.selectAll();
    }//GEN-LAST:event_paymentFieldFocusGained
    // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allProductsButton;
    private javax.swing.JButton allServicesButton;
    private javax.swing.JButton allSuppliersButton;
    private javax.swing.JButton allWarehousesButton;
    private javax.swing.JLabel balanceLabel;
    private javax.swing.JLabel balanceLabel1;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JScrollPane bodyScroll;
    private javax.swing.JPanel botomPanel;
    private javax.swing.JPanel dateContainer;
    private javax.swing.JPanel datePanel;
    private javax.swing.JPanel detailPanel;
    private javax.swing.JFormattedTextField discountField;
    private javax.swing.JLabel discountLabel;
    private javax.swing.JLabel discountLabel1;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JCheckBox isServicesBox;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JPanel notePanel;
    private javax.swing.JTextArea noteText;
    private javax.swing.JFormattedTextField orderTaxField;
    private javax.swing.JFormattedTextField paymentField;
    private javax.swing.JLabel paymentLabel;
    private javax.swing.JLabel paymentLabel1;
    private javax.swing.JComboBox<String> paymentStatusComboBox;
    private javax.swing.JTable productChargeTable;
    private javax.swing.JPanel productContainer;
    private javax.swing.JTextField productField;
    private javax.swing.JPanel productPanel;
    private javax.swing.JScrollPane productScroll;
    private javax.swing.JTable productTable;
    private javax.swing.JTextField referenceNoField;
    private javax.swing.JPanel searchPanelContainer;
    private javax.swing.JPanel serviceChargePanel;
    private javax.swing.JScrollPane serviceChargeScroll;
    private javax.swing.JTable serviceChargeTable;
    private javax.swing.JPanel serviceContainer;
    private javax.swing.JTextField serviceField;
    private javax.swing.JPanel servicePanel;
    private javax.swing.JScrollPane serviceScroll1;
    private javax.swing.JTable serviceTable;
    private javax.swing.JFormattedTextField shippingField;
    private javax.swing.JLabel shippingLabel;
    private javax.swing.JLabel shippingLabel1;
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JButton submitButton;
    private javax.swing.JPanel supplierContainer;
    private javax.swing.JTextField supplierField;
    private javax.swing.JPanel supplierPanel;
    private javax.swing.JScrollPane supplierScroll;
    private javax.swing.JTable supplierTable;
    private javax.swing.JLabel taxLabel;
    private javax.swing.JLabel taxLabel1;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JLabel totalLabel1;
    private javax.swing.JPanel warehouseContainer;
    private javax.swing.JTextField warehouseField;
    private javax.swing.JPanel warehousePanel;
    private javax.swing.JScrollPane warehouseScroll;
    private javax.swing.JTable warehouseTable;
    // End of variables declaration//GEN-END:variables
}
