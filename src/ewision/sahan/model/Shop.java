package ewision.sahan.model;

import java.io.Serializable;

/**
 *
 * @author ksoff
 */
public class Shop implements Serializable {

    private int id = 0;
    private String name = "default";
    private String address = "default";
    private String mobile = "default";
    private String email = "default";
    private String logoPath = "logo//logo.png";
    private String logo2Path = "logo//logo2.png";

    public int getId() {
        return id;
    }

    public String getStringId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath==null?"logo//logo2.png":logoPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo2Path() {
        return logo2Path;
    }

    public void setLogo2Path(String logo2Path) {
        this.logo2Path = logo2Path==null?"logo//logo2.png":logo2Path;
    }
    
    

}
