package ewision.sahan.application.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import ewision.sahan.customer.CreateCustomer;
import ewision.sahan.expenses.ExpenseCategoryRegistration;
import ewision.sahan.model.Product;
import ewision.sahan.pos.POSUI;
import ewision.sahan.product.BrandRegistration;
import ewision.sahan.product.CategoryRegistration;
import ewision.sahan.product.UnitRegistration;
import ewision.sahan.purchase.AddStock;
import ewision.sahan.purchase.CreatePurchase1;
import ewision.sahan.sale.CreateSale1;
import ewision.sahan.sale.SalesReport;
import ewision.sahan.sale.SelectStock1;
import ewision.sahan.supplier.CreateSupplier;
import ewision.sahan.users.CreateUser;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author ksoff
 */
public class DialogModal extends javax.swing.JDialog {
    
    private JPanel parent;

    /**
     * Creates new form SelectProduct
     */
    public DialogModal(javax.swing.JPanel parent) {
        this.parent = parent;
        initComponents();
    }

//    private void init() {
//        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
//        //openSelectProduct();
//        openCreateCustomer();
//        openCreateSupplier();
//    }
    private void resize(JPanel panel) {
        //if (this.getHeight() < 450 || this.getHeight() == this.getMinimumSize().height) {
        this.setSize(this.getWidth(), panel.getPreferredSize().height + jImagePanel1.getHeight() + jSeparator1.getHeight() + 5);
        generateCenter();
        //}
    }
    
    private void generateCenter() {
        //pack(); // set window to preffered size;
        setLocationRelativeTo(null);
    }

//    public void openSelectProduct() {
//        SelectProduct selectProduct = new SelectProduct();
//        jPanel1.add(selectProduct, BorderLayout.CENTER);
//        resize(selectProduct);
//        SwingUtilities.updateComponentTreeUI(jPanel1);
//    }
    public void openPanel(JPanel panel) {
        jPanel1.add(panel, BorderLayout.CENTER);
        resize(panel);
        SwingUtilities.updateComponentTreeUI(jPanel1);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
    }
    
    public void openCreateCustomer() {
        CreateCustomer createCustomer = new CreateCustomer();
        createCustomer.setModal(this);
        openPanel(createCustomer);
    }
    
    public void openCreateSupplier() {
        CreateSupplier createSupplier = new CreateSupplier();
        createSupplier.setModal(this);
        openPanel(createSupplier);
    }
    
    public void openSelectStock(CreateSale1 createSale, String pid, String pname) {
        SelectStock1 selectStock = new SelectStock1(createSale, pid, pname);
        jPanel1.add(selectStock, BorderLayout.CENTER);
        resize(selectStock);
        SwingUtilities.updateComponentTreeUI(jPanel1);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
    }
    
    public void openSelectStock(CreateSale1 createSale, Product product) {
        SelectStock1 selectStock = new SelectStock1(createSale, product);
        openPanel(selectStock);
    }
    
    public void openSelectStock(POSUI posui, Product product) {
        SelectStock1 selectStock = new SelectStock1(posui, product);
        openPanel(selectStock);
    }
    
    public void openAddStock(CreatePurchase1 createPurchase, Product product) {
        AddStock addStock = new AddStock(createPurchase, product);
        openPanel(addStock);
    }
    
    public void openBrandReg() {
        BrandRegistration brandReg = new BrandRegistration();
        brandReg.setModal(this);
        openPanel(brandReg);
    }
    
    public void openUnitReg() {
        UnitRegistration unitReg = new UnitRegistration();
        unitReg.setModal(this);
        openPanel(unitReg);
    }
    
    public void openCategoryReg() {
        CategoryRegistration categoryReg = new CategoryRegistration();
        categoryReg.setModal(this);
        openPanel(categoryReg);
    }
    
    public void openSalesReport() {
        SalesReport saleReport = new SalesReport();
        saleReport.setModal(this);
        openPanel(saleReport);
    }
    
    public void openExpenseCategoryReg() {
        ExpenseCategoryRegistration expenseCategoryReg = new ExpenseCategoryRegistration();
        expenseCategoryReg.setModal(this);
        openPanel(expenseCategoryReg);
    }
    
    public void openCreateUser() {
        CreateUser createUser = new CreateUser();
        createUser.setModal(this);
        openPanel(createUser);
    }
    
    public void closeModal() {
        this.dispose();
        System.gc();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jImagePanel1 = new main.JImagePanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel1.setMinimumSize(new java.awt.Dimension(720, 250));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jImagePanel1.setCenterImage(true);
        jImagePanel1.setFitToPanel(true);
        jImagePanel1.setImageIcon(new javax.swing.ImageIcon(getClass().getResource("/ewision/sahan/icon/whiteLogo.png"))); // NOI18N
        jImagePanel1.setSmoothRendering(true);

        javax.swing.GroupLayout jImagePanel1Layout = new javax.swing.GroupLayout(jImagePanel1);
        jImagePanel1.setLayout(jImagePanel1Layout);
        jImagePanel1Layout.setHorizontalGroup(
            jImagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jImagePanel1Layout.setVerticalGroup(
            jImagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 201, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jImagePanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jImagePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacDarkLaf.setup();

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            DialogModal dialog = new DialogModal(new javax.swing.JPanel());
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private main.JImagePanel jImagePanel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
