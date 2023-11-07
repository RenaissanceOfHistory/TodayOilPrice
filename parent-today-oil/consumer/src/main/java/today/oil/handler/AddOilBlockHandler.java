package today.oil.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import today.oil.common.Result;
import today.oil.model.AddOil;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: AddOilBlockHandler
 */
@Slf4j
public class AddOilBlockHandler {

    public static Result queryAddOilByCidBlockHandler(Long cid, BlockException ex) {
        return fallback("queryAddOilByCidBlockHandler");
    }

    public static Result addAddOilBlockHandler(AddOil addOil, BlockException ex) {
        return fallback("addAddOilBlockHandler");
    }

    public static Result updateAddOilBlockHandler(AddOil addOil, BlockException ex) {
        return fallback("updateAddOilBlockHandler");
    }

    public static Result removeAddOilByIdBlockHandler(Long id, Long cid, BlockException ex) {
        return fallback("removeAddOilByIdBlockHandler");
    }

    public static Result removeAddOilByCidBlockHandler(Long cid, BlockException ex) {
        return fallback("removeAddOilByCidBlockHandler");
    }

    public static Result queryMaxMileByCidBlockHandler(Long cid, BlockException ex) {
        return fallback("queryMaxMileByCidBlockHandler");
    }

    private static Result fallback(String methodName) {
        log.info("Sentinel：{}", methodName);
        return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "访问频繁");
    }
}
