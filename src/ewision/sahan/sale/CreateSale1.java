package ewision.sahan.sale;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import ewision.sahan.application.main.DialogModal;
import ewision.sahan.components.action_button.ActionButton;
import ewision.sahan.components.action_button.ActionButtonEvent;
import ewision.sahan.loggers.CommonLogger;
import ewision.sahan.loggers.DatabaseLogger;
import ewision.sahan.model.MySQL;
import ewision.sahan.model.Product;
import ewision.sahan.model.Service;
import ewision.sahan.model.Stock;
import ewision.sahan.table.button.TableActionPanelCellRenderer;
import ewision.sahan.table.TableCenterCellRenderer;
import ewision.sahan.table.spinner.SpinnerChangeEvent;
import ewision.sahan.table.spinner.TableSpinnerCellEditor;
import ewision.sahan.utils.ImageScaler;
import ewision.sahan.utils.SQLDateFormatter;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ksoff
 */
public class CreateSale1 extends javax.swing.JPanel {

    /**
     * Creates new form CreateSale
     */
    public CreateSale1() {
        initComponents();
        init();
    }

    private void init() {
        render();
        toggleServicePanel();
        styleComponents();
        renderTables();
        initData();
    }

    private void initData() {
        jDateChooser1.setDate(new Date());
        //loadOrderProducts("");
        loadStatus();
        loadPaymentStatus();
    }

    private void render() {
        FlatSVGIcon refreshSvgIcon = new ImageScaler().getSvgIcon("refresh", 28);
        allCustomersButton.setIcon(refreshSvgIcon);
        allWarehousesButton.setIcon(refreshSvgIcon);
        allProductsButton.setIcon(refreshSvgIcon);
        allServicesButton.setIcon(refreshSvgIcon);
    }

    private void styleComponents() {
        productField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search products by keywords");

        JPanel[] containers = {productContainer, serviceContainer, servicePanel, jPanel14, serviceChargePanel, dateContainer, customerContainer, warehouseContainer};
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
        tableCenterCellRenderer.renderTables(customerTable);
        tableCenterCellRenderer.renderTables(productChargeTable);
        tableCenterCellRenderer.renderTables(productTable);
        tableCenterCellRenderer.renderTables(serviceChargeTable);
        tableCenterCellRenderer.renderTables(serviceTable);
        tableCenterCellRenderer.renderTables(warehouseTable);

        setupProductChargeTable();
        setupServiceChargeTable();
    }

    /*
    *
    * Table Operations Setup
     */
    private void setupProductChargeTable() {
        SpinnerChangeEvent event = (int row, JSpinner spinner) -> {
            try {
                double qty = Double.parseDouble(String.valueOf(spinner.getValue()));

                double price = Double.parseDouble(String.valueOf(productChargeTable.getValueAt(row, 2)));

                double newTotal = qty * price;
                productChargeTable.setValueAt(newTotal, row, 7);

                Stock currentStock = stockMap.get(String.valueOf(productChargeTable.getValueAt(row, 0)));
                currentStock.setQuantity(qty);

                calculate();
            } catch (NumberFormatException | NullPointerException e) {
                CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " Product Spinner Event: " + e.getMessage(), e.getMessage());
            }
        };

        TableSpinnerCellEditor tableSpinnerCellEditor = new TableSpinnerCellEditor(event, 3);
        //TableSpinnerCellEditor tableSpinnerCellEditor = new TableSpinnerCellEditor(2, 7, 3);
        productChargeTable.getColumnModel().getColumn(4).setCellEditor(tableSpinnerCellEditor);

        HashMap<String, ActionButtonEvent> actionButtonEventMap = new HashMap<>();
        actionButtonEventMap.put("delete", (ActionButtonEvent) (int row) -> {
            System.out.println("Delete: " + row);
            System.out.println("Delete: " + String.valueOf(productChargeTable.getValueAt(row, 1)));
        });
        actionButtonEventMap.put("edit", (ActionButtonEvent) (int row) -> {
            System.out.println("Edit: " + row);
        });
        TableActionPanelCellRenderer tableActionPanelCellRenderer = new TableActionPanelCellRenderer(ActionButton.EDIT_DELETE_BUTTON, actionButtonEventMap);
        productChargeTable.getColumnModel().getColumn(8).setCellRenderer(tableActionPanelCellRenderer);
    }

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
            System.out.println("Delete: " + row);
            System.out.println("Delete: " + String.valueOf(serviceChargeTable.getValueAt(row, 1)));
        });
        actionButtonEventMap.put("edit", (ActionButtonEvent) (int row) -> {
            System.out.println("Edit: " + row);
        });
        TableActionPanelCellRenderer tableActionPanelCellRenderer = new TableActionPanelCellRenderer(ActionButton.EDIT_DELETE_BUTTON, actionButtonEventMap);
        serviceChargeTable.getColumnModel().getColumn(7).setCellRenderer(tableActionPanelCellRenderer);
    }

    /*
    *
    * Service UI Actions
     */
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

    private void toggleServiceChargePanel() {
        serviceChargePanel.setVisible(isService);
    }

    /*
    *
    * Data Loading
    * &
    * Selection
     */
    private String customer = "";
    private String warehouse = "";
    private String product = "";
    private String currency = "Rs.";

    private void loadProducts(String product) {
        productContainer.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);

        model.addRow(new Object[]{0, "Apple", "1025", "150.00", "-", "Fruit"});

        //String query = "SELECT * FROM `products` INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` INNER JOIN `brands` ON `brands`.`id`=`products`.`brand_id` INNER JOIN `units` ON `units`.`id`=`products`.`unit_id` ";
        String query = "SELECT * FROM `products` INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` INNER JOIN `brands` ON `brands`.`id`=`products`.`brand_id` INNER JOIN `units` ON `units`.`id`=`products`.`unit_id` WHERE `product_type`='product' ";
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
        String query = "SELECT * FROM `products` INNER JOIN `categories` ON `categories`.`id`=`products`.`category_id` WHERE `product_type`='service' ";
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

    private void loadCustomers(String customer) {
        customerContainer.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0);

        model.addRow(new Object[]{0, "walk-in-customer", "default", "default"});

        String query = "SELECT * FROM `clients` ";
        if (!customer.isEmpty()) {
            query += "  WHERE `name` LIKE '%" + customer + "%' "
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

    private void reset() {
        this.customer = "";
        this.warehouse = "";
        JPanel[] containers = {dateContainer, customerContainer, warehouseContainer};
        for (JPanel container : containers) {
            container.setVisible(false);
        }
        JTextField[] fields = {customerField, warehouseField};
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

    /*
    *
    * Stock Selection
     */
    //public void addProductToInvoice(String stock_id, String quantity, String discount, String tax) {
    //    System.out.println(stock_id + " : " + quantity + " : " + discount + " : " + tax);
    //}
    private DialogModal modal;

    
    private void loadItems() {
        DefaultTableModel tModel = (DefaultTableModel) serviceChargeTable.getModel();
        tModel.setRowCount(0);

        double total = 0;
        //double total = totalLabel.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel.getText());
        try {
            total = totalLabel.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel.getText().replace(getCurrency(), ""));
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " loadServiceItems: " + e.getMessage(), e.getMessage());
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

        totalLabel.setText(String.valueOf(total));
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

    private void loadOrderItems() {
        DefaultTableModel tModel = (DefaultTableModel) productChargeTable.getModel();
        tModel.setRowCount(0);

        double total = 0;
        //double total = totalLabel.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel.getText());
        try {
            total = totalLabel.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel.getText().replace(getCurrency(), ""));
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

        totalLabel.setText(String.valueOf(total));
        calculate();
    }

    private HashMap<String, Service> serviceMap = new HashMap<>();

    public void addServiceeToInvoice(Service service) {
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

    private void loadServiceItems() {
        DefaultTableModel tModel = (DefaultTableModel) serviceChargeTable.getModel();
        tModel.setRowCount(0);

        double total = 0;
        //double total = totalLabel.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel.getText());
        try {
            total = totalLabel.getText().isBlank() ? 0.00 : Double.parseDouble(totalLabel.getText().replace(getCurrency(), ""));
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

        totalLabel.setText(String.valueOf(total));
        calculate();
    }

    private void calculate() {
        double subtotal = 0.00;

        for (Stock stock : stockMap.values()) {
            subtotal += ((stock.getStock_price() * stock.getQuantity()) - stock.getStock_discount()) + stock.getStock_tax();
        }

        if (isService) {
            for (Service service : serviceMap.values()) {
                subtotal += ((service.getPrice() * service.getQuantity()) - service.getDiscount()) + service.getTax();
            }
        }

        double orderTax = 0.00;
        try {
            orderTax = Double.parseDouble(String.valueOf(orderTaxField.getText()));
            //orderTax = Double.valueOf(String.valueOf(orderTaxField.getText().replace(currency, "")));
            int taxAmount = (int) (subtotal * (orderTax / 100));
            taxLabel.setText(getCurrency() + taxAmount + "(" + orderTax + "%)");
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
        taxLabel.setText(getCurrency() + tax + "(" + orderTax + "%)");
        totalLabel.setText(getCurrency() + total);

        double payment = 0.00;
        try {
            payment = Double.parseDouble(String.valueOf(paymentField.getText()));
            //payment = Double.valueOf(String.valueOf(paymentField.getText().replace(currency, "")));
        } catch (NumberFormatException | NullPointerException e) {
            CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " calculate payment: " + e.getMessage(), e.getMessage());
        }

        double balance = 0.00;
        balance = payment - total;

        paymentLabel.setText(currency + payment);
        balanceLabel.setText(currency + balance);
        balanceLabel.setForeground(balance >= 0 ? paymentLabel.getForeground() : Color.RED);
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
        customerPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        customerContainer = new javax.swing.JPanel();
        customerScroll = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        allCustomersButton = new javax.swing.JButton();
        customerField = new javax.swing.JTextField();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        discountLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        taxLabel = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        shippingLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        paymentLabel = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel23 = new javax.swing.JLabel();
        balanceLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        orderTaxField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        statusComboBox = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        discountField = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        paymentStatusComboBox = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        shippingField = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
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
        jSeparator2 = new javax.swing.JSeparator();
        botomPanel = new javax.swing.JPanel();
        submitButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setMinimumSize(new java.awt.Dimension(20, 5));

        headerPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Create Sale");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
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
        customerTable.setNextFocusableComponent(warehouseField);
        customerTable.setShowHorizontalLines(true);
        customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerTableMouseClicked(evt);
            }
        });
        customerScroll.setViewportView(customerTable);
        if (customerTable.getColumnModel().getColumnCount() > 0) {
            customerTable.getColumnModel().getColumn(0).setMaxWidth(80);
        }

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
                .addComponent(customerScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel6.setMinimumSize(new java.awt.Dimension(20, 5));

        allCustomersButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allCustomersButton.setIconTextGap(2);
        allCustomersButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allCustomersButton.setNextFocusableComponent(customerTable);
        allCustomersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allCustomersButtonActionPerformed(evt);
            }
        });

        customerField.setFocusCycleRoot(true);
        customerField.setNextFocusableComponent(allCustomersButton);
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
        warehouseTable.setNextFocusableComponent(productField);
        warehouseTable.setShowHorizontalLines(true);
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

        warehouseField.setNextFocusableComponent(allWarehousesButton);
        warehouseField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                warehouseFieldKeyReleased(evt);
            }
        });

        allWarehousesButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allWarehousesButton.setIconTextGap(2);
        allWarehousesButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allWarehousesButton.setNextFocusableComponent(warehouseTable);
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
                .addComponent(customerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(warehousePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        searchPanelContainerLayout.setVerticalGroup(
            searchPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(datePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(customerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        productTable.setNextFocusableComponent(productChargeTable);
        productTable.setShowHorizontalLines(true);
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
                .addComponent(productScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel8.setMinimumSize(new java.awt.Dimension(20, 5));

        allProductsButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allProductsButton.setIconTextGap(2);
        allProductsButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allProductsButton.setMinimumSize(new java.awt.Dimension(20, 5));
        allProductsButton.setNextFocusableComponent(productTable);
        allProductsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allProductsButtonActionPerformed(evt);
            }
        });

        productField.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 5, 1));
        productField.setMinimumSize(new java.awt.Dimension(20, 5));
        productField.setNextFocusableComponent(allProductsButton);
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
                .addGap(0, 12, Short.MAX_VALUE))
        );

        detailPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        jScrollPane1.setMinimumSize(new java.awt.Dimension(20, 5));

        productChargeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Description", "Unit Price", "Stock", "Qty", "Discount", "Tax", "Subtotal", "Action"
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
        productChargeTable.setNextFocusableComponent(isServicesBox);
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

        jPanel2.setMinimumSize(new java.awt.Dimension(20, 5));

        jLabel8.setText("Order Tax");

        discountLabel.setText("Rs.0.00");

        jLabel10.setText("Discount");

        jLabel12.setText("Shipping");

        totalLabel.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        totalLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalLabel.setText("Rs.0.00");

        taxLabel.setText("Rs.0.00 (0%)");

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel14.setText("Grand Total");

        shippingLabel.setText("Rs.0.00");

        jLabel13.setText("Payment");

        paymentLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        paymentLabel.setText("Rs.0.00");

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel23.setText("Balance");

        balanceLabel.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        balanceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        balanceLabel.setText("Rs.0.00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(totalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(taxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(discountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(shippingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(balanceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(taxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(totalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(balanceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jPanel3.setMinimumSize(new java.awt.Dimension(20, 5));

        jPanel1.setLayout(new java.awt.GridLayout(1, 0, 2, 2));

        jLabel15.setText("Order Tax");

        orderTaxField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        orderTaxField.setText("0");
        orderTaxField.setNextFocusableComponent(discountField);
        orderTaxField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                orderTaxFieldKeyReleased(evt);
            }
        });

        jLabel18.setText("Status *");

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Completed", "Pending", "Ordered" }));
        statusComboBox.setNextFocusableComponent(paymentStatusComboBox);

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
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);

        jLabel16.setText("Discount");

        discountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        discountField.setText("0.00");
        discountField.setNextFocusableComponent(shippingField);
        discountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discountFieldKeyReleased(evt);
            }
        });

        jLabel19.setText("Payment Status *");

        paymentStatusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Paid", "Pending", "Partial" }));
        paymentStatusComboBox.setNextFocusableComponent(noteText);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paymentStatusComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discountField, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(20, 20, 20))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel9);

        jLabel17.setText("Shipping");

        shippingField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        shippingField.setText("0.00");
        shippingField.setNextFocusableComponent(paymentField);
        shippingField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                shippingFieldKeyReleased(evt);
            }
        });

        jLabel22.setText("Payment");

        paymentField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        paymentField.setText("0.00");
        paymentField.setNextFocusableComponent(submitButton);
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
                        .addComponent(jLabel22)
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
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel10);

        noteText.setColumns(20);
        noteText.setRows(5);
        noteText.setNextFocusableComponent(submitButton);
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
        serviceTable.setNextFocusableComponent(serviceChargeTable);
        serviceTable.setShowHorizontalLines(true);
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
            .addComponent(serviceScroll1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );

        jPanel14.setMinimumSize(new java.awt.Dimension(20, 5));

        allServicesButton.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        allServicesButton.setIconTextGap(2);
        allServicesButton.setMargin(new java.awt.Insets(2, 2, 3, 3));
        allServicesButton.setMinimumSize(new java.awt.Dimension(20, 5));
        allServicesButton.setNextFocusableComponent(serviceTable);
        allServicesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allServicesButtonActionPerformed(evt);
            }
        });

        serviceField.setMinimumSize(new java.awt.Dimension(20, 5));
        serviceField.setNextFocusableComponent(allServicesButton);
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
        isServicesBox.setNextFocusableComponent(serviceField);
        isServicesBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isServicesBoxActionPerformed(evt);
            }
        });

        serviceChargePanel.setMinimumSize(new java.awt.Dimension(20, 5));

        serviceChargeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

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
        serviceChargeTable.setNextFocusableComponent(orderTaxField);
        serviceChargeTable.setRowHeight(50);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(servicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serviceChargePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSeparator2.setMinimumSize(new java.awt.Dimension(20, 5));

        botomPanel.setMinimumSize(new java.awt.Dimension(20, 5));

        submitButton.setText("Submit");
        submitButton.setNextFocusableComponent(statusComboBox);
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
                .addComponent(bodyScroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /*
    *
    * Actions
     */

    private void customerTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerTableMouseClicked
        // Select Customer
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = customerTable.getSelectedRow();

            if (selectedRow != -1) {
                this.customer = String.valueOf(customerTable.getValueAt(selectedRow, 0));
                customerField.setText(String.valueOf(customerTable.getValueAt(selectedRow, 1)));
                customerContainer.setVisible(false);
                //loadPassengerTable();
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
        // loadPassengerTable();
    }//GEN-LAST:event_allCustomersButtonActionPerformed

    private void warehouseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_warehouseTableMouseClicked
        // Select Warehouse
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            int selectedRow = warehouseTable.getSelectedRow();

            if (selectedRow != -1) {
                this.warehouse = String.valueOf(warehouseTable.getValueAt(selectedRow, 0));
                warehouseField.setText(String.valueOf(warehouseTable.getValueAt(selectedRow, 1)));
                warehouseContainer.setVisible(false);
                //loadPassengerTable();
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
        // loadPassengerTable        
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

    private void productTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTableMouseClicked
        // Select Product
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
                //modal.openSelectStock(this, String.valueOf(productTable.getValueAt(selectedRow, 0)), String.valueOf(productTable.getValueAt(selectedRow, 1)));
                modal.openSelectStock(this, product);
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
//            int selectedRow = serviceTable.getSelectedRow();
//
//            if (selectedRow != -1) {
//                //this.service = String.valueOf(serviceTable.getValueAt(selectedRow, 0));
//                this.isService = true;
//                toggleServiceChargePanel();
//                serviceField.setText(String.valueOf(serviceTable.getValueAt(selectedRow, 1)));
//                serviceContainer.setVisible(false);
//            }
            addService();
        }
    }//GEN-LAST:event_serviceTableMouseClicked

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

            addServiceeToInvoice(service);
            this.isService = true;
            toggleServiceChargePanel();
            serviceContainer.setVisible(false);
        }
    }

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
        submitSale();
    }//GEN-LAST:event_submitButtonActionPerformed

    private boolean submitSale() {
        Date date;

        String customerName = customerField.getText();
        String customerId = this.customer;

        String warehouseName = warehouseField.getText();
        String warehouseId = this.warehouse;

        int itemCount = stockMap.size();
        int serviceCount = serviceMap.size();

        String payment = paymentField.getText();

        if (jDateChooser1.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select Date!", "Warning", JOptionPane.WARNING_MESSAGE);
            jDateChooser1.requestFocus();
        } else if (customerName.isBlank() && !customerId.equals("0")) {
            JOptionPane.showMessageDialog(this, "Please select Customer!", "Warning", JOptionPane.WARNING_MESSAGE);
            customerField.requestFocus();
            this.customer = "0";
        } else if (warehouseName.isBlank() && !warehouseId.equals("0")) {
            JOptionPane.showMessageDialog(this, "Please select Warehouse!", "Warning", JOptionPane.WARNING_MESSAGE);
            warehouseField.requestFocus();
            this.warehouse = "0";
        } else if (itemCount == 0 && serviceCount == 0) {
            JOptionPane.showMessageDialog(this, "Please add atlease one Product or a Service!", "Warning", JOptionPane.WARNING_MESSAGE);
            productField.requestFocus();
            if (itemCount == 0 && isServicesBox.isSelected()) {
                serviceField.requestFocus();
            }
        } else {

            String status = String.valueOf(statusComboBox.getSelectedItem());
            String paymentStatus = String.valueOf(paymentStatusComboBox.getSelectedItem());

            boolean isValidPayment = true;
            if (payment.isBlank() || payment.equals("0.00")) {
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
                    double paymentAmount = Double.parseDouble(payment);
                    double total = Double.parseDouble(totalLabel.getText().replace(currency, ""));
                    if (paymentStatus.equalsIgnoreCase("paid") && total > paymentAmount) {
                        JOptionPane.showMessageDialog(this, "Payment is not enough", "Invalid Payment", JOptionPane.WARNING_MESSAGE);
                        paymentField.requestFocus();
                        return false;
                    }
                } catch (NumberFormatException | NullPointerException e) {
                    CommonLogger.logger.log(Level.SEVERE, "Exception in " + getClass().getName() + " submit sale payment|total: " + e.getMessage(), e.getMessage());
                }

                date = jDateChooser1.getDate();
                String stringDate = new SQLDateFormatter().getStringDate(date);

                customerName = customerName.isBlank() ? "walk-in-customer" : customerField.getText();
                warehouseName = warehouseName.isBlank() ? "Default" : warehouseField.getText();

            }
        }
        return true;
    }


    private void productChargeTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productChargeTableMouseClicked
        // Calculate on Changes of Product Orders
//        if (evt.getClickCount() == 1) {
//            int selectedRow = productChargeTable.getSelectedRow();
//            if (selectedRow != -1) {
//                String price = String.valueOf(productChargeTable.getValueAt(selectedRow, 2));
//                String qty = String.valueOf(productChargeTable.getValueAt(selectedRow, 4));
//
//                try {
//                    double subtotal = Double.parseDouble(price) * Double.parseDouble(qty);
//
//                    productChargeTable.setValueAt(subtotal, selectedRow, 7);
//                } catch (NullPointerException e) {
//                    System.out.println("Null: " + e.getMessage());
//                } catch (NumberFormatException e) {
//                    System.out.println("Format: " + e.getMessage());
//                }
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
        orderTaxField.setText(orderTaxField.getText().isBlank() ? "0" : orderTaxField.getText());
        calculate();
    }//GEN-LAST:event_orderTaxFieldKeyReleased

    private void discountFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountFieldKeyReleased
        // Order Discount
        discountField.setText(discountField.getText().isBlank() ? "0.00" : discountField.getText());
        calculate();
    }//GEN-LAST:event_discountFieldKeyReleased

    private void shippingFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_shippingFieldKeyReleased
        // Order Shipping
        shippingField.setText(shippingField.getText().isBlank() ? "0.00" : shippingField.getText());
        calculate();
    }//GEN-LAST:event_shippingFieldKeyReleased

    private void paymentFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentFieldKeyReleased
        // Payment
        paymentField.setText(paymentField.getText().isBlank() ? "0.00" : paymentField.getText());
        calculate();
    }//GEN-LAST:event_paymentFieldKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allCustomersButton;
    private javax.swing.JButton allProductsButton;
    private javax.swing.JButton allServicesButton;
    private javax.swing.JButton allWarehousesButton;
    private javax.swing.JLabel balanceLabel;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JScrollPane bodyScroll;
    private javax.swing.JPanel botomPanel;
    private javax.swing.JPanel customerContainer;
    private javax.swing.JTextField customerField;
    private javax.swing.JPanel customerPanel;
    private javax.swing.JScrollPane customerScroll;
    private javax.swing.JTable customerTable;
    private javax.swing.JPanel dateContainer;
    private javax.swing.JPanel datePanel;
    private javax.swing.JPanel detailPanel;
    private javax.swing.JFormattedTextField discountField;
    private javax.swing.JLabel discountLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JCheckBox isServicesBox;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
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
    private javax.swing.JPanel notePanel;
    private javax.swing.JTextArea noteText;
    private javax.swing.JFormattedTextField orderTaxField;
    private javax.swing.JFormattedTextField paymentField;
    private javax.swing.JLabel paymentLabel;
    private javax.swing.JComboBox<String> paymentStatusComboBox;
    private javax.swing.JTable productChargeTable;
    private javax.swing.JPanel productContainer;
    private javax.swing.JTextField productField;
    private javax.swing.JPanel productPanel;
    private javax.swing.JScrollPane productScroll;
    private javax.swing.JTable productTable;
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
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel taxLabel;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JPanel warehouseContainer;
    private javax.swing.JTextField warehouseField;
    private javax.swing.JPanel warehousePanel;
    private javax.swing.JScrollPane warehouseScroll;
    private javax.swing.JTable warehouseTable;
    // End of variables declaration//GEN-END:variables

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
