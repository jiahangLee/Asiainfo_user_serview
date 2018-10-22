package dolphin.account.domain;

/**
 * Created by lyl on 2017/3/3.
 */
public class AuthUser {

    private int id;



    private String username;



    private String password;
    private String authoritiesStr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthoritiesStr() {
        return authoritiesStr;
    }

    public void setAuthoritiesStr(String authoritiesStr) {
        this.authoritiesStr = authoritiesStr;
    }
}
