package model.user;

/**
 * This class is the user object.
 */

public class User {

    private String password;
    private final String username;
    private final String userType;
    private final String date;
    private final String createdBy;

    public User(String username, String password, String date, String createdBy, String userType) {
        this.username = username;
        this.password = password;
        this.date = date;
        this.createdBy = createdBy;
        this.userType = userType;
    }

    public String getCreatedOn() {
        return date;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }
}
