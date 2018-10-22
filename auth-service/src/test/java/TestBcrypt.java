import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;

/**
 * Created by lyl on 2017/3/9.
 */
public class TestBcrypt {
    static MessageDigest md ;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) throws Exception{
        String pwd="123456";
        for(int i=0;i<10;i++) {
            String sha = encodeSHA(pwd.getBytes());

            System.out.println(sha);
        }


    }

    private static String encodeSHA(byte[] data) throws Exception {
        // 初始化MessageDigest,SHA即SHA-1的简称
        if(md==null) {
            md = MessageDigest.getInstance("SHA");
        }
        // 执行摘要方法
        byte[] digest = md.digest(data);
        return new HexBinaryAdapter().marshal(digest).toLowerCase();
    }

    //7c4a8d09ca3762af61e59520943dc26494f8941b
}
