package ewision.sahan.model;

import ewision.sahan.loggers.CommonLogger;
import java.util.logging.Level;

/**
 *
 * @author ksoff
 */
public class Service extends Item {

    protected int category_id;
    protected String category;

    protected double discount = 0.00;
    protected double tax = 0.00;
    //protected double subtotal;
    protected double quantity;

    public void setProductData(int id, String name, String code, double price) {
        setId(id);
        setName(name);
        setCode(code);
        setPrice(price);
    }

    public void setProductData(String id, String name, String code, String price) {
        setId(id);
        setName(name);
        setCode(code);
        setPrice(price);
    }

        /**
     *
     * @return discount given to the customer
     */
    public double getDiscount() {
        return discount;
    }

    /**
     *
     * @param discount given to customer in invoice time as stock_discount
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     *
     * @param discount given to customer in invoice time as stock_discount
     */
    public void setDiscount(String discount) {
        try {
            this.setDiscount(Double.parseDouble(discount));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Discount: " + e.getMessage(), e.getMessage());
        }
    }

    /**
     *
     * @return tax charged from the customer on invoice time
     */
    public double getTax() {
        return tax;
    }

    /**
     *
     * @param tax charged from customer in invoice time as stock_tax
     */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /**
     *
     * @param tax charged from customer in invoice time as stock_tax
     */
    public void setTax(String tax) {
        try {
            this.setTax(Double.parseDouble(tax));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Tax: " + e.getMessage(), e.getMessage());
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
