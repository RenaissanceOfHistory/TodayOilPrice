package today.oil.service.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import today.oil.model.User;
import today.oil.model.UserDetail;
import today.oil.service.RemoteUserService;

/**
 * @create: 2023/10/30
 * @Description:
 * @FileName: RemoteUserServiceFallBack
 */
@Slf4j
@Component
public class RemoteUserServiceFallBack implements RemoteUserService {
    @Override
    public String login(User user) {
        log.warn("服务login()调用失败：{}", user);
        return null;
    }

    @Override
    public Integer enroll(User user) {
        log.warn("服务enroll()调用失败：{}", user);
        return null;
    }

    @Override
    public UserDetail queryUserDetail(Long uid) {
        log.warn("服务queryUserDetail()调用失败：{}", uid);
        return null;
    }

    @Override
    public Integer updateUserDetail(UserDetail userDetail) {
        log.warn("服务updateUserDetail()调用失败：{}", userDetail);
        return null;
    }

    @Override
    public Integer removeUser(Long uid) {
        log.warn("服务removeUser()调用失败：{}", uid);
        return null;
    }

    @Override
    public String queryUserName(Long uid) {
        log.warn("服务queryUserName()调用失败：{}", uid);
        return null;
    }

    @Override
    public Integer uploadAvatar(String avatar, Long uid) {
        log.warn("服务 uploadAvatar({}, {})调用失败", avatar, uid);
        return null;
    }

    @Override
    public byte[] downloadAvatar(Long uid) {
        log.warn("服务 downloadAvatar({})调用失败", uid);
        return null;
    }
}
