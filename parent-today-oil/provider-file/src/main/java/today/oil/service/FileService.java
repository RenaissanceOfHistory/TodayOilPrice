package today.oil.service;

/**
 * @create: 2023/11/4
 * @Description:
 * @FileName: FileService
 */
public interface FileService {

    /** 上传用户头像
     * @Description:
     * @date: 2023/11/7
     * @param base64 Base64字符串
     * @param filename 文件名
     * @return: java.lang.String
     */
    String uploadAvatar(String base64, String filename);

    /** 下载用户头像
     * @Description:
     * @date: 2023/11/7
     * @param filename
     * @return: byte[]
     */
    byte[] downloadAvatar(String filename);
}
