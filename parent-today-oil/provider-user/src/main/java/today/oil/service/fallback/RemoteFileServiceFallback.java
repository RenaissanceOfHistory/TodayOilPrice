package today.oil.service.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import today.oil.service.RemoteFileService;

/**
 * @create: 2023/11/4
 * @Description:
 * @FileName: RemoteFileServiceFallback
 */
@Slf4j
@Component
public class RemoteFileServiceFallback implements RemoteFileService {
    @Override
    public String uploadAvatar(String avatar, String filename) {
        log.warn("调用服务 uploadAvatar({}, {})失败", avatar, filename);
        return null;
    }

    @Override
    public byte[] downloadAvatar(String filename) {
        log.warn("调用服务 downloadAvatar({})失败", filename);
        return null;
    }
}
