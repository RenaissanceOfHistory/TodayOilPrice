package today.oil.utils;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 加密工具类
 * @create: 2023/10/30
 * @Description:
 * @FileName: EncryptUtil
 */
@Slf4j
public class EncryptUtil {
    private static final HexBinaryAdapter adapter = new HexBinaryAdapter();

    private EncryptUtil() {}

    /** 对text进行加密
     * 加密成功，返回加密后text；
     * 否则，返回原text
     * @Description:
     * @date: 2023/10/30
     * @param text
     * @param type
     * @return: java.lang.String
     */
    public static String encode(String text, EncryptType type) {
        try {
            MessageDigest instance = MessageDigest.getInstance(type.value);
            instance.update(text.getBytes());
            return adapter.marshal(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            return text;
        }
    }

    /** 加密类型
     * @Description:
     * @date: 2023/10/30
     */
    public enum EncryptType {
        MD5("MD5"),
        SHA256("SHA-256");

        private String value;

        EncryptType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
