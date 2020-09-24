package kgc.cn.role.sys.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptUtil {

    public static String encrypt(String original){
        try {
//            获取MD5加密算法实例
            MessageDigest md = MessageDigest.getInstance("MD5");
//            取出加密的字符串的字节数据
            byte[] data = original.getBytes();
//            使用MD5算法对字节数据加密
            byte[] result = md.digest(data);
//            返回一个使用Base64对加密后的数据进行编码的字符串
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
