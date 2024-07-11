package ewision.sahan.model;

import ewision.sahan.loggers.CommonLogger;
import java.util.logging.Level;

/**
 *
 * @author ksoff
 */
public class Product extends Item {

    protected int category_id;
    protected String category;

    protected int brand_id;
    protected String brand;

    protected int unit_id;
    protected String unit;

    protected double tax;
    protected double stock_alert = 0;

    protected int barcode_type;
    protected int is_active = 1;

    public void setProductData(int id, String name, String code, String unit) {
        setId(id);
        setName(name);
        setCode(code);
        setUnit(unit);
    }
    
    public void setProductData(String id, String name, String code, String unit) {
        setId(id);
        setName(name);
        setCode(code);
        setUnit(unit);
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

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setTax(String tax) {
        try {
            this.tax = Double.parseDouble(tax);
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Tax: " + e.getMessage(), e.getMessage());
        }
    }

    public double getStock_alert() {
        return stock_alert;
    }

    public void setStock_alert(double stock_alert) {
        this.stock_alert = stock_alert;
    }

    public void setStock_alert(String stock_alert) {
        try {
            this.setStock_alert(Double.parseDouble(stock_alert));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Stock Alert: " + e.getMessage(), e.getMessage());
        }
    }

    public int getBarcode_type() {
        return barcode_type;
    }

    public void setBarcode_type(int barcode_type) {
        this.barcode_type = barcode_type;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }
}
