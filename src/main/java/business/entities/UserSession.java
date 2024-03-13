package business.entities;


import java.util.Set;
public class UserSession {

    private static UserSession instance;

    private String userName;
    private Set<String> privileges;

    private UserSession() {
        // Private constructor to prevent instantiation.
    }

    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserSession(String userName, Set<String> privileges) {
        this.userName = userName;
        this.privileges = privileges;
    }

    public String getUserName() {
        return userName;
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public void cleanUserSession() {
        userName = null;
        privileges = null;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                ", privileges=" + privileges +
                '}';
    }

}
