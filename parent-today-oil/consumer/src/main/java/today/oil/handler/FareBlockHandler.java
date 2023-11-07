package today.oil.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import today.oil.common.Result;
import today.oil.model.Fare;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: FareBlockHandler
 */
@Slf4j
public class FareBlockHandler {
    public static Result queryFareByCidBlockHandler(Long cid, BlockException ex) {
        return fallback("queryFareByCidBlockHandler");
    }

    public static Result addFareBlockHandler(Fare fare, BlockException ex) {
        return fallback("addFareBlockHandler");
    }

    public static Result updateFareBlockHandler(Fare fare, BlockException ex) {
        return fallback("updateFareBlockHandler");
    }

    public static Result removeFareBlockHandler(Long id, Long cid, BlockException ex) {
        return fallback("removeFareBlockHandler");
    }

    public static Result removeFareByCidBlockHandler(Long cid, BlockException ex) {
        return fallback("removeFareByCidBlockHandler");
    }

    private static Result fallback(String methodName) {
        log.info("Sentinel：{}", methodName);
        return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "访问频繁");
    }
}
