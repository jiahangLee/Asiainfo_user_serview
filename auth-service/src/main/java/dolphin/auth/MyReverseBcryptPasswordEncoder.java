package dolphin.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by lyl on 2017/3/9.
 */
public class MyReverseBcryptPasswordEncoder implements PasswordEncoder {
    private static PasswordEncoder encoder=new BCryptPasswordEncoder();
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encoder.matches(s, charSequence.toString());
    }
}
