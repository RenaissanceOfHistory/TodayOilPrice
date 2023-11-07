package today.oil.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import today.oil.service.FileService;
import today.oil.utils.ResourceUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @create: 2023/11/4
 * @Description:
 * @FileName: FileServiceImpl
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    /** 文件存储目录 */
    private static final String LOCAL_PATH =  System.getProperty("user.dir") + "/static/upload/img/";

    // 初始化目录
    static {
        File dir = new File(LOCAL_PATH);
        if (!dir.exists()) //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
    }

    /** 如果 filename 存在，删除原有用户头像。
     * 如果 base64 存在，转换为字节并保存到本地，
     * 成功时返回保存文件名，
     * 否则返回<code>null</code>
     * @Description:
     * @date: 2023/11/7
     * @param base64
     * @param filename
     * @return: java.lang.String
     */
    @SneakyThrows
    @Override
    public String uploadAvatar(String base64, String filename) {
        if (hasAvatar(filename)) {
            log.info("存在用户头像");
            removeAvatar(filename);
        }

        if (StringUtils.isEmpty(base64)) return null;

        log.info("上传用户头像");
        filename = System.currentTimeMillis() + ResourceUtil.getFileSuffix(base64);
        File saveFile = new File(LOCAL_PATH + filename);
        if (!saveFile.exists()) //noinspection ResultOfMethodCallIgnored
            saveFile.createNewFile();

        log.info("保存头像位置：{}", saveFile.getPath());
        log.info("将base64转换为字节");
        byte[] bytes = Base64Utils.decodeFromString(ResourceUtil.trimBase64(base64));
        log.info("base64转换字节成功，保存头像到本地");
        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(saveFile))) {
            output.write(bytes);
            output.flush();
            return filename;
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public byte[] downloadAvatar(String filename) {
        log.info("请求用户头像：{}", filename);
        if (StringUtils.isEmpty(filename)) return null;

        File avatar = new File(LOCAL_PATH + filename);
        return ResourceUtil.toByte(avatar);
    }

    /** filename 是否有文本
     * @Description:
     * @date: 2023/11/7
     * @param filename 文件名
     * @return: boolean
     */
    private static boolean hasAvatar(String filename) {
        return StringUtils.hasText(filename);
    }

    /** 删除用户头像
     * @Description:
     * @date: 2023/11/7
     * @param filename
     * @return: void
     */
    private static void removeAvatar(String filename) {
        File avatar = new File(LOCAL_PATH + filename);
        if (avatar.exists()) {
            log.info("删除用户头像：{}，删除状态：{}", filename, avatar.delete());
        }
    }
}
