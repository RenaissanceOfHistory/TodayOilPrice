package today.oil.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import today.oil.constant.ServiceConstants;
import today.oil.service.fallback.RemoteFileServiceFallback;

/** 远程 File 服务
 * @create: 2023/11/4
 * @Description:
 * @FileName: RemoteFileService
 */
@FeignClient(contextId = "fileService", name = ServiceConstants.FILE_SERVICE, fallback = RemoteFileServiceFallback.class)
public interface RemoteFileService {

    /** 上传用户头像
     * @Description:
     * @date: 2023/11/7
     * @param avatar base64 字符串
     * @param filename 用户头像文件名
     * @return: java.lang.String
     */
    @PostMapping("/file-avatar/upload")
    String uploadAvatar(@RequestBody String avatar,
                        @RequestParam String filename);

    /** 下载用户头像
     * @Description:
     * @date: 2023/11/7
     * @param filename 用户头像文件名
     * @return: byte[]
     */
    @GetMapping("/file-avatar/download")
    byte[] downloadAvatar(@RequestParam String filename);
}
