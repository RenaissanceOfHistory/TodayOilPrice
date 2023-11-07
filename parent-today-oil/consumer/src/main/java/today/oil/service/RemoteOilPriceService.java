package today.oil.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import today.oil.constant.ServiceConstants;
import today.oil.service.fallback.RemoteOilPriceServiceFallback;

import java.util.List;

/**
 * @create: 2023/11/1
 * @Description:
 * @FileName: RemoteOilPriceService
 */
@FeignClient(contextId = "oilPriceService",
        name = ServiceConstants.OIL_PRICE_SERVICE, fallback = RemoteOilPriceServiceFallback.class)
public interface RemoteOilPriceService {

    @GetMapping("/oil-price-province")
    List queryProvinces();

    @GetMapping("/oil-price-province/{province}")
    List queryOilPriceByProvince(@PathVariable("province") String province);

    @GetMapping("/oil-price-label/{label}")
    List queryOilPriceByLabel(@PathVariable("label") String label);
}
