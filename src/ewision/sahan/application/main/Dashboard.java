package ewision.sahan.application.main;

import com.formdev.flatlaf.FlatClientProperties;
import ewision.sahan.chart.ModelChart;
import ewision.sahan.loggers.DatabaseLogger;
import ewision.sahan.model.MySQL;
import ewision.sahan.utils.SQLDateFormatter;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ksoff
 */
public class Dashboard extends javax.swing.JPanel {

    /**
     * Creates new form Form1
     */
    public Dashboard() {
        initComponents();
        init();
    }

    private void init() {
        chart.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:30;"
                + "background:rgba(240,240,240,10)");
        chart1.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:30;"
                + "background:rgba(240,240,240,10)");
        loadSales("");
        loadExpenses("");
        loadData();
    }

    private void loadData() {
        int iValue = (int) value;
        int iTValue = (int) tValue;

        chart.addLegend("Total Sale Count", new Color(245, 189, 135));
        chart.addLegend("Today Sale Count", new Color(189, 135, 245));

        chart1.addLegend("Total Sale Income", new Color(135, 189, 245));
        chart1.addLegend("Today Sale Income", new Color(139, 229, 222));

        chart.addData(new ModelChart("Today", new double[]{count, todayCount}));
        chart1.addData(new ModelChart("Total", new double[]{iValue, iTValue}));

//        chart.addData(new ModelChart("February", new double[]{600, 750, 300, 150}));
//        chart.addData(new ModelChart("March", new double[]{200, 350, 1000, 900}));
//        chart.addData(new ModelChart("April", new double[]{480, 150, 750, 700}));
//        chart.addData(new ModelChart("May", new double[]{350, 540, 300, 150}));
//        chart.addData(new ModelChart("June", new double[]{190, 500, 700, 1000}));
    }

    int count = 0;
    double value = 0;

    int todayCount = 0;
    double tValue = 0;

    int todayEpenseCount = 0;
    double tExpenseValue = 0;

    int expenseCount = 0;
    double expenseValue = 0;

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

            Date today = new Date();
            String todayString = new SQLDateFormatter().getStringDate(today);

            while (resultSet.next()) {
                Vector row = new Vector();

                double due = 0.00;
                //due = (Double.parseDouble(resultSet.getString("sales.GrandTotal")) - Double.parseDouble(resultSet.getString("sales.discount")) - Double.parseDouble(resultSet.getString("sales.paid_amount")));                
                double total = Double.parseDouble(resultSet.getString("sales.GrandTotal"));
                double payment = Double.parseDouble(resultSet.getString("sales.paid_amount"));
                due = (total - payment);
                due = (due >= 0 ? due : 0.00);
                payment = (due >= 0 ? payment : total);

                String date = resultSet.getString("date");
                row.add(date);

                if (date.equalsIgnoreCase(todayString)) {
                    todayCount++;
                    tValue += total;
                }
                count++;
                value += total;

                totalSales.setText(String.valueOf(count));
                totalValue.setText(String.valueOf(value));
                todaySales.setText(String.valueOf(todayCount));
                todayValue.setText(String.valueOf(tValue));
            }
        } catch (SQLException ex) {
            //ex.printStackTrace();
            DatabaseLogger.logger.log(Level.SEVERE, "Sale loading error: " + ex.getMessage(), ex.getMessage());
        }
    }

    private void loadExpenses(String txt) {
        try {
            String query = "SELECT * FROM `expenses` "
                    + "INNER JOIN `users` ON `users`.`id`=`expenses`.`user_id` "
                    + "INNER JOIN `clients` ON `clients`.`id`=`expenses`.`clientsId` "
                    + "WHERE (`expenses`.`Ref` LIKE '%" + txt + "%' "
                    + "OR `users`.`username` LIKE '%" + txt + "%' "
                    + "OR `clients`.`name` LIKE '%" + txt + "%') AND `expenses`.`status`<>'Inactive' "
                    + "ORDER BY `date` DESC";
            ResultSet resultSet = MySQL.execute(query);

            Date today = new Date();
            String todayString = new SQLDateFormatter().getStringDate(today);

            while (resultSet.next()) {
                Vector row = new Vector();

                double payment = Double.parseDouble(resultSet.getString("expenses.amount"));

                row.add(resultSet.getString("expenses.Ref"));

                String date = resultSet.getString("date");
                row.add(payment);

                if (date.equalsIgnoreCase(todayString)) {
                    todayEpenseCount++;
                    tExpenseValue += payment;
                }
                expenseCount++;
                expenseValue += payment;

                totalExpenses.setText(String.valueOf(expenseCount));
                totalExpenseValue.setText(String.valueOf(expenseValue));
                todayExpenses.setText(String.valueOf(todayEpenseCount));
                todayExpenseValue.setText(String.valueOf(tExpenseValue));
            }
        } catch (SQLException ex) {
            //ex.printStackTrace();
            DatabaseLogger.logger.log(Level.SEVERE, "Expense loading error: " + ex.getMessage(), ex.getMessage());
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

        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        totalSales = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        totalValue = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        todaySales = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        todayValue = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        totalExpenses = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        totalExpenseValue = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        todayExpenses = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        todayExpenseValue = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        chart = new ewision.sahan.chart.Chart();
        chart1 = new ewision.sahan.chart.Chart();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel2.setText("Dashboard");

        jPanel1.setLayout(new java.awt.GridLayout(1, 0, 10, 10));

        jPanel2.setBackground(new java.awt.Color(102, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel1.setText("All Time Sales");

        totalSales.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        totalSales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalSales.setText("0");

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Value");

        totalValue.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        totalValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalValue.setText("0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalSales)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(totalValue))
                .addGap(25, 25, 25))
        );

        jPanel1.add(jPanel2);

        jPanel3.setBackground(new java.awt.Color(102, 102, 255));

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel4.setText("Today Sales");

        todaySales.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        todaySales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        todaySales.setText("0");

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Value");

        todayValue.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        todayValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        todayValue.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(todaySales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 50, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(todayValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(todaySales)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(todayValue))
                .addGap(25, 25, 25))
        );

        jPanel1.add(jPanel3);

        jPanel5.setBackground(new java.awt.Color(102, 255, 255));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel3.setText("Expenses");

        totalExpenses.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        totalExpenses.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalExpenses.setText("0");

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Value");

        totalExpenseValue.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        totalExpenseValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalExpenseValue.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalExpenses, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 79, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalExpenseValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalExpenses)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(totalExpenseValue))
                .addGap(25, 25, 25))
        );

        jPanel1.add(jPanel5);

        jPanel6.setBackground(new java.awt.Color(102, 102, 255));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel5.setText("Today Expenses");

        todayExpenses.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        todayExpenses.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        todayExpenses.setText("0");

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Value");

        todayExpenseValue.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        todayExpenseValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        todayExpenseValue.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(todayExpenses, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(todayExpenseValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(todayExpenses)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(todayExpenseValue))
                .addGap(25, 25, 25))
        );

        jPanel1.add(jPanel6);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0, 30, 20));
        jPanel4.add(chart);
        jPanel4.add(chart1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ewision.sahan.chart.Chart chart;
    private ewision.sahan.chart.Chart chart1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel todayExpenseValue;
    private javax.swing.JLabel todayExpenses;
    private javax.swing.JLabel todaySales;
    private javax.swing.JLabel todayValue;
    private javax.swing.JLabel totalExpenseValue;
    private javax.swing.JLabel totalExpenses;
    private javax.swing.JLabel totalSales;
    private javax.swing.JLabel totalValue;
    // End of variables declaration//GEN-END:variables
}
