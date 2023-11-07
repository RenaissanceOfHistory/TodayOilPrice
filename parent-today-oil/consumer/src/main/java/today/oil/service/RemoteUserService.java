package today.oil.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import today.oil.constant.ServiceConstants;
import today.oil.model.User;
import today.oil.model.UserDetail;
import today.oil.service.fallback.RemoteUserServiceFallBack;

/**
 * @create: 2023/10/30
 * @Description:
 * @FileName: RemoteUserService
 */
@FeignClient(contextId = "userService", name = ServiceConstants.USER_SERVICE, fallback = RemoteUserServiceFallBack.class)
public interface RemoteUserService {

    @PostMapping("/user-login")
    String login(@RequestBody User user);

    @PostMapping("/user-enroll")
    Integer enroll(@RequestBody User user);

    @GetMapping("/user-detail/uid/{uid}")
    UserDetail queryUserDetail(@PathVariable("uid") Long uid);

    @PutMapping("/user-detail/update")
    Integer updateUserDetail(@RequestBody UserDetail userDetail);

    @DeleteMapping("/user-remove/{uid}")
    Integer removeUser(@PathVariable("uid") Long uid);

    @GetMapping("/query/name/uid/{uid}")
    String queryUserName(@PathVariable("uid") Long uid);

    @PostMapping("/user-avatar/upload/{uid}")
    Integer uploadAvatar(@RequestBody String avatar,
                         @PathVariable("uid") Long uid);

    @GetMapping("/user-avatar/download/{uid}")
    byte[] downloadAvatar(@PathVariable("uid") Long uid);
}
