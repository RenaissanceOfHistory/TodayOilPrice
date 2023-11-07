package today.oil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import today.oil.model.User;
import today.oil.model.UserDetail;
import today.oil.service.RemoteFileService;
import today.oil.service.UserService;

/**
 * @create: 2023/10/30
 * @Description:
 * @FileName: UserController
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RemoteFileService fileService;

    @PostMapping("/user-login")
    public String login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/user-enroll")
    public Integer enroll(@RequestBody User user) {
        return userService.enroll(user);
    }

    @GetMapping("/user-detail/uid/{uid}")
    public UserDetail queryUserDetail(@PathVariable("uid") Long uid) {
        return userService.selectDetailByUid(uid);
    }

    @PutMapping("/user-detail/update")
    public Integer updateUserDetail(@RequestBody UserDetail userDetail) {
        return userService.updateUserDetail(userDetail);
    }

    @DeleteMapping("/user-remove/{uid}")
    public Integer removeUser(@PathVariable("uid") Long uid) {
        fileService.uploadAvatar("", userService.queryAvatar(uid));
        return userService.deleteUser(uid);
    }

    @GetMapping("/query/name/uid/{uid}")
    public String queryUserName(@PathVariable("uid") Long uid) {
        return userService.queryNameByUid(uid);
    }

    @PostMapping("/user-avatar/upload/{uid}")
    public Integer uploadAvatar(@RequestBody String avatar,
                                @PathVariable("uid") Long uid) {
        avatar = fileService.uploadAvatar(avatar, userService.queryAvatar(uid));
        return userService.updateUser(User.builder().id(uid).avatar(avatar).build());
    }

    @GetMapping("/user-avatar/download/{uid}")
    public byte[] downloadAvatar(@PathVariable("uid") Long uid) {
        return fileService.downloadAvatar(userService.queryAvatar(uid));
    }
}
