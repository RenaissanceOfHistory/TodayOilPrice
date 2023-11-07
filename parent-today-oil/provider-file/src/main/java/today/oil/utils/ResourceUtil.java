package today.oil.utils;

import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @create: 2023/11/4
 * @Description:
 * @FileName: ResourceUtil
 */
public class ResourceUtil {
    private static final String EMPTY = "";

    private ResourceUtil() {}

    /** 获取在类路径下的文件
     * @Description:
     * @date: 2023/11/7
     * @param relativePath 文件相对路径
     * @return: java.io.File
     */
    @SneakyThrows
    public static File getFileInClassPath(String relativePath) {
        return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + relativePath);
    }

    /** 将 file 转换为字节数组
     * @Description:
     * @date: 2023/11/7
     * @param file
     * @return: byte[]
     */
    @SneakyThrows
    public static byte[] toByte(File file) {
        final byte[] buff = new byte[(int) file.length()];
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))) {
            //noinspection ResultOfMethodCallIgnored
            input.read(buff);
        }
        return buff;
    }

    /** 从 base64 中获取文件后缀。
     * 成功时返回文件后缀，
     * 否则返回 ""
     * @Description:
     * @date: 2023/11/7
     * @param base64
     * @return: java.lang.String
     */
    public static String getFileSuffix(String base64) {
        int index = base64.indexOf("base64,");
        return (-1 == index) ? EMPTY :
                "." + base64.substring(base64.indexOf("/") + 1, base64.indexOf(";"));
    }

    /** 将 base64 串的 data前缀去除。
     * 成功时返回去除后的串，
     * 否则返回原串
     * @Description:
     * @date: 2023/11/7
     * @param base64
     * @return: java.lang.String
     */
    public static String trimBase64(String base64) {
        int index = base64.indexOf("base64,");
        return (-1 == index) ? base64 :
                base64.substring(index + "base64,".length());
    }
}
