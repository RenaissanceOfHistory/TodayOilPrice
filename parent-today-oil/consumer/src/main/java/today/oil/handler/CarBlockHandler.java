package today.oil.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import today.oil.common.Result;
import today.oil.model.Car;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: CarBlockHandler
 */
@Slf4j
public class CarBlockHandler {
    public static Result queryCarByUidBlockHandler(Long uid, BlockException ex) {
        return fallback("queryCarByUidBlockHandler");
    }

    public static Result addCarBlockHandler(Car car, Long uid, BlockException ex) {
        return fallback("addCarBlockHandler");
    }

    public static Result updateCarBlockHandler(Car car, BlockException ex) {
        return fallback("updateCarBlockHandler");
    }

    public static Result removeCarBlockHandler(Long id, BlockException ex) {
        return fallback("removeCarBlockHandler");
    }

    public static Result removeCarByUidBlockHandler(Long uid, BlockException ex) {
        return fallback("removeCarByUidBlockHandler");
    }

    private static Result fallback(String methodName) {
        log.info("Sentinel：{}", methodName);
        return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "访问频繁");
    }
}
