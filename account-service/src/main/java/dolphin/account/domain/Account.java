package dolphin.account.domain;

import java.util.Collection;

/**
 * Created by lyl on 2017/3/3.
 */
public class Account {
    private String name;
    private Collection<String> authorities;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<String> authorities) {
        this.authorities = authorities;
    }
}
