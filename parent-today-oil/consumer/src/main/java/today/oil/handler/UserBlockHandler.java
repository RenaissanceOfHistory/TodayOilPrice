package today.oil.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import today.oil.common.Result;
import today.oil.model.User;
import today.oil.model.UserDetail;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @create: 2023/10/30
 * @Description:
 * @FileName: UserBlockHandler
 */
@Slf4j
public class UserBlockHandler {
    public static Result loginBlockHandler(User user, String code, BlockException ex) {
        return fallback();
    }

    public static Result enrollBlockHandler(User user, BlockException ex) {
        return fallback();
    }

    public static Result queryUserDetailBlockHandler(Long uid, BlockException ex) {
        return fallback();
    }

    public static Result updateUserDetailBlockHandler(UserDetail userDetail, BlockException ex) {
        return fallback();
    }

    public static Result removeUserBlockHandler(Long uid, BlockException ex) {
        return fallback();
    }

    public static void getCodeBlockHandler(HttpServletResponse response, BlockException ex) {
        try (OutputStream output = response.getOutputStream()) {
            log.info("触发快速失败！");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            output.write("error".getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static Result fallback() {
        return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "访问频繁");
    }
}
