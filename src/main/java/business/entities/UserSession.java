// UserSession.java
package business.entities;

import java.util.HashSet;
import java.util.Set;

public class UserSession {

    private static UserSession instance;
    private String userName;
    private Set<String> privileges;

    private UserSession() {
        // Private constructor to prevent instantiation
        privileges = new HashSet<>();
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
    }

    // Additional methods for user session management if needed
}
