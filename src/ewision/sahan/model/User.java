package ewision.sahan.model;

/**
 *
 * @author ksoff
 */
public class User {

    private int id = 0;
    private int statusId = 1;
    private int roleId = 2;
    private String role = "User";
    private String status = "Active";
    private String username = "Default";
    private String email = "default";
    private String mobile = "default";

    public User() {
    }

    public User(int id, int roleId, String username, String email, String mobile) {
        this.id = id;
        this.roleId = roleId;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
    }

    public User(String id, String roleId, String username, String email, String mobile) {
        this.id = Integer.parseInt(id);
        this.roleId = Integer.parseInt(roleId);
        this.username = username;
        this.email = email;
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public String getStringId() {
        return String.valueOf(id);
    }

    public int getStatusId() {
        return statusId;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    
}
