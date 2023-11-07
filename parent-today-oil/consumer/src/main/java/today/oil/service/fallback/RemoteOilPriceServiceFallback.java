package today.oil.service.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import today.oil.service.RemoteOilPriceService;

import java.util.List;

/**
 * @create: 2023/11/1
 * @Description:
 * @FileName: RemoteOilPriceServiceFallback
 */
@Slf4j
@Component
public class RemoteOilPriceServiceFallback implements RemoteOilPriceService {
    @Override
    public List queryProvinces() {
        log.warn("调用服务 queryProvinces()失败");
        return null;
    }

    @Override
    public List queryOilPriceByProvince(String province) {
        log.warn("调用服务 queryOilPriceByProvince({})失败", province);
        return null;
    }

    @Override
    public List queryOilPriceByLabel(String label) {
        log.warn("调用服务 queryOilPriceByLabel({})失败", label);
        return null;
    }
}
