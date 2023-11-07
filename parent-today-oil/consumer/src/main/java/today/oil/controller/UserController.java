package today.oil.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import today.oil.common.Result;
import today.oil.constant.BlockHandlerConstants;
import today.oil.handler.UserBlockHandler;
import today.oil.model.User;
import today.oil.model.UserDetail;
import today.oil.service.RemoteUserService;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/** 用户相关接口
 * @create: 2023/10/30
 * @Description:
 * @FileName: UserController
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final LineCaptcha captcha = CaptchaUtil.createLineCaptcha(220, 120);

    @Autowired
    private RemoteUserService userService;

    @Autowired
    private CarController carController;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/login/{code}")
    @SentinelResource(value = BlockHandlerConstants.BH_LOGIN,
            blockHandlerClass = UserBlockHandler.class, blockHandler = "loginBlockHandler")
    public Result login(@RequestBody User user, @PathVariable("code") String code) {
        log.info("用户登录：code({}), user({})", code, user);
        if (!captcha.getCode().equals(code)) {
            log.info("验证码错误：code({})-({})", code, captcha.getCode());
            return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "验证码错误");
        }

        log.info("验证码验证成功");
        String token = userService.login(user);
        return Objects.isNull(token) ?
                Result.error(HttpStatus.NO_CONTENT.value(), "用户不存在") :
                Result.ok("登录成功", token);
    }

    @PostMapping("/enroll/{code}")
    @SentinelResource(value = BlockHandlerConstants.BH_ENROLL,
            blockHandlerClass = UserBlockHandler.class, blockHandler = "enrollBlockHandler")
    public Result enroll(@RequestBody User user, @PathVariable("code") String code) {
        log.info("用户注册：code({}), user({})", code, user);
        if (!captcha.getCode().equals(code)) {
            log.info("验证码错误：code({})-({})", code, captcha.getCode());
            return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "验证码错误");
        }

        log.info("验证码验证成功");
        Integer res = userService.enroll(user);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "用户已存在") :
                Result.ok("注册成功", res);
    }

    @GetMapping("/detail/uid")
    @SentinelResource(value = BlockHandlerConstants.BH_QUERY_USER_DETAIL,
            blockHandlerClass = UserBlockHandler.class, blockHandler = "queryUserDetailBlockHandler")
    public Result queryUserDetail(@RequestHeader("uid") Long uid) {
        UserDetail userDetail = userService.queryUserDetail(uid);
        return Objects.isNull(userDetail) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户信息不存在") :
                Result.ok("用户信息查询成功", userDetail);
    }

    @PutMapping("/detail")
    @SentinelResource(value = BlockHandlerConstants.BH_UPDATE_USER_DETAIL,
            blockHandlerClass = UserBlockHandler.class, blockHandler = "updateUserDetailBlockHandler")
    public Result updateUserDetail(@RequestBody UserDetail userDetail) {
        Integer res = userService.updateUserDetail(userDetail);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "更新用户信息失败") :
                Result.ok("更新用户信息成功", res);
    }

    @Transactional
    @DeleteMapping("/remove/uid")
    @SentinelResource(value = BlockHandlerConstants.BH_REMOVE_USER,
            blockHandlerClass = UserBlockHandler.class, blockHandler = "removeUserBlockHandler")
    public Result removeUser(@RequestHeader("uid") Long uid) {
        carController.removeCarByUid(uid);
        Integer res = userService.removeUser(uid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "注销失败") :
                Result.ok("注销成功", res);
    }

    @GetMapping("/code")
    @SentinelResource(value = BlockHandlerConstants.BH_CODE,
            blockHandlerClass = UserBlockHandler.class, blockHandler = "getCodeBlockHandler")
    public void getCode(HttpServletResponse response) {
        try (OutputStream output = response.getOutputStream()) {
            if (Objects.isNull(redisTemplate.opsForValue().get("code::" + captcha.getCode()))) {
                log.info("缓存验证码失效，重新生成验证码");
                captcha.createCode();
                log.info("设置验证码缓存（5s）：{}", captcha.getCode());
                redisTemplate.opsForValue().set("code::" + captcha.getCode(), "", 5, TimeUnit.SECONDS);
            }

            log.info("使用缓存验证码：{}", captcha.getCode());
            captcha.write(output);
        } catch (IOException e) {
            log.info("获取验证码失败", e);
        }
    }

    @GetMapping("/query/name/uid")
    public Result queryName(@RequestHeader("uid") Long uid) {
        String userName = userService.queryUserName(uid);
        return Objects.isNull(userName) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "查询用户名失败") :
                Result.ok("查询用户名成功", userName);
    }

    @PostMapping("/image/upload")
    public Result uploadAvatar(@RequestBody User user, @RequestHeader("uid") Long uid) {
        Integer res = userService.uploadAvatar(user.getAvatar(), uid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "图片保存失败") :
                Result.ok("图片保存成功", res);
    }

    @GetMapping("/image/download")
    public void downloadAvatar(@RequestHeader("uid") Long uid,
                               HttpServletResponse response) {
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            output.write(userService.downloadAvatar(uid));
            output.flush();
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
