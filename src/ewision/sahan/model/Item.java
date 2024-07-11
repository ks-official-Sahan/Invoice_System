package ewision.sahan.model;

import ewision.sahan.loggers.CommonLogger;
import java.util.logging.Level;

/**
 *
 * @author ksoff
 */
public class Item {

    protected int id;
    protected String name;
    protected String code;
    protected String description;
    protected double cost;
    protected double price;
    
    public int getId() {
        return id;
    }

    public String getStringId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(String id) {
        try {
            this.setId(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set id: " + e.getMessage(), e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setCost(String cost) {
        try {
            this.cost = Double.parseDouble(cost);
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Cost: " + e.getMessage(), e.getMessage());
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPrice(String price) {
        try {
            this.price = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            CommonLogger.logger.log(Level.SEVERE, "ParseException in " + getClass().getName() + " set Price: " + e.getMessage(), e.getMessage());
        }
    }

}
