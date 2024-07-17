package ewision.sahan.model;

import ewision.sahan.loggers.CommonLogger;
import ewision.sahan.utils.SQLDateFormatter;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author ksoff
 */
public class Stock extends Product {

    protected int stock_id = 0;
    protected String stock_name;
    protected String stock_code;
    protected double stock_cost;
    protected double stock_price;
    protected double stock_discount = 0.00;
    protected double stock_tax = 0.00;
    protected double stock_quantity = 0.00;
    //protected double stock_subtotal;
    protected double quantity;
    protected int is_expire = 0;
    protected Date exp_date;
    protected Date mfd_date;

    public Stock() {
    }

    public Stock(int stock_id, double stock_cost, double stock_price, double quantity, int is_expire) {
        this.stock_id = stock_id;
        this.stock_cost = stock_cost;
        this.stock_price = stock_price;
        this.quantity = quantity;
        this.is_expire = is_expire;
    }

    public void setProduct(Product product) {
        if (product != null) {
            setId(product.getId());
            setName(product.getName());
            setCode(product.getCode());
            setUnit(product.getUnit());
        }
    }

    public int getStock_id() {
        return stock_id;
    }

    public String getStringStock_id() {
        return String.valueOf(stock_id);
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public void setStock_id(String stock_id) {
        try {
            this.setStock_id(Integer.parseInt(stock_id));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Stock Id: " + e.getMessage(), e.getMessage());
        }
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public double getStock_cost() {
        return stock_cost;
    }

    public void setStock_cost(double stock_cost) {
        this.stock_cost = stock_cost;
    }

    public void setStock_cost(String stock_cost) {
        try {
            this.setStock_cost(Double.parseDouble(stock_cost));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Stock Cost: " + e.getMessage(), e.getMessage());
        }
    }

    public double getStock_price() {
        return stock_price;
    }

    public void setStock_price(double stock_price) {
        this.stock_price = stock_price;
    }

    public void setStock_price(String stock_price) {
        try {
            this.setStock_price(Double.parseDouble(stock_price));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Stock Price: " + e.getMessage(), e.getMessage());
        }
    }

    /**
     *
     * @return quantity user is gonna buy
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity of item user has selected quantity
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @param quantity of item user has selected quantity
     */
    public void setQuantity(String quantity) {
        try {
            this.setQuantity(Double.parseDouble(quantity));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Quantity: " + e.getMessage(), e.getMessage());
        }
    }

    public int getIs_expire() {
        return is_expire;
    }

    public void setIs_expire(int is_expire) {
        this.is_expire = is_expire;
    }

    public Date getExp_date() {
        return exp_date;
    }

    public void setExp_date(Date exp_date) {
        this.exp_date = exp_date;
    }

    public void setExp_date(String exp_date) {
        this.setExp_date(new SQLDateFormatter().getDate(exp_date));
    }

    public Date getMfd_date() {
        return mfd_date;
    }

    public void setMfd_date(Date mfd_date) {
        this.mfd_date = mfd_date;
    }

    public void setMfd_date(String mfd_date) {
        this.setMfd_date(new SQLDateFormatter().getDate(mfd_date));
    }

    /**
     *
     * @return discount given to the customer
     */
    public double getStock_discount() {
        return stock_discount;
    }

    /**
     *
     * @param discount given to customer in invoice time as stock_discount
     */
    public void setStock_discount(double stock_discount) {
        this.stock_discount = stock_discount;
    }

    /**
     *
     * @param discount given to customer in invoice time as stock_discount
     */
    public void setStock_discount(String stock_discount) {
        try {
            this.setStock_discount(Double.parseDouble(stock_discount));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Stock Discount: " + e.getMessage(), e.getMessage());
        }
    }

    /**
     *
     * @return tax charged from the customer on invoice time
     */
    public double getStock_tax() {
        return stock_tax;
    }

    /**
     *
     * @param tax charged from customer in invoice time as stock_tax
     */
    public void setStock_tax(double stock_tax) {
        this.stock_tax = stock_tax;
    }

    /**
     *
     * @param tax charged from customer in invoice time as stock_tax
     */
    public void setStock_tax(String stock_tax) {
        try {
            this.setStock_tax(Double.parseDouble(stock_tax));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Stock Tax: " + e.getMessage(), e.getMessage());
        }
    }

//    public double getStock_subtotal() {
//        return stock_subtotal;
//    }
//
//    public void setStock_subtotal(double stock_subtotal) {
//        this.stock_subtotal = stock_subtotal;
//    }
//
//    public void setStock_subtotal(String stock_subtotal) {
//        try {
//            this.setStock_subtotal(Double.parseDouble(stock_subtotal));
//        } catch (NumberFormatException e) {
//            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Stock Tax: " + e.getMessage(), e.getMessage());
//        }
//    }

    /**
     *
     * @return quantity available on stocks
     */
    public double getStock_quantity() {
        return stock_quantity;
    }
    
    /**
     *
     * @param quantity available on stocks as stock_quantity
     */
    public void setStock_quantity(double stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    /**
     *
     * @param quantity available on stocks as stock_quantity
     */
    public void setStock_quantity(String stock_quantity) {
        try {
            this.setStock_quantity(Double.parseDouble(stock_quantity));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Stock Quantity: " + e.getMessage(), e.getMessage());
        }
    }

}
