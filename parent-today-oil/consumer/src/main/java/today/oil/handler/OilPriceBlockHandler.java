package today.oil.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import today.oil.common.Result;

/**
 * @create: 2023/11/3
 * @Description:
 * @FileName: OilPriceBlockHandler
 */
@Slf4j
public class OilPriceBlockHandler {

    public static Result queryProvincesBlockHandler(BlockException ex) {
        return fallback("queryProvincesBlockHandler");
    }

    public static Result queryOilPriceByProvinceBlockHandler(String province, BlockException ex) {
        return fallback("queryOilPriceByProvinceBlockHandler");
    }

    public static Result queryOilPriceByLabelBlockHandler(String label, BlockException ex) {
        return fallback("queryOilPriceByLabelBlockHandler");
    }

    private static Result fallback(String methodName) {
        log.info("Sentinel：{}", methodName);
        return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "访问频繁");
    }
}
