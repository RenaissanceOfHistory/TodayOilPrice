package today.oil.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import today.oil.common.Result;
import today.oil.constant.BlockHandlerConstants;
import today.oil.handler.OilPriceBlockHandler;
import today.oil.service.RemoteOilPriceService;

import java.util.List;
import java.util.Objects;

/**
 * @create: 2023/11/1
 * @Description:
 * @FileName: OilPriceController
 */
@RestController
@RequestMapping("/oil-prices")
public class OilPriceController {
    @Autowired
    private RemoteOilPriceService oilPriceService;

    @GetMapping("/provinces")
    @SentinelResource(value = BlockHandlerConstants.BH_QUERY_PROVINCES,
            blockHandlerClass = OilPriceBlockHandler.class, blockHandler = "queryProvincesBlockHandler")
    public Result queryProvinces() {
        List res = oilPriceService.queryProvinces();
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "查询省份失败") :
                Result.ok("查询省份成功", res);
    }

    @GetMapping("/prov/{province}")
    @SentinelResource(value = BlockHandlerConstants.BH_QUERY_OIL_PRICE_BY_PROVINCE,
            blockHandlerClass = OilPriceBlockHandler.class, blockHandler = "queryOilPriceByProvinceBlockHandler")
    public Result queryOilPriceByProvince(@PathVariable("province") String province) {
        List res = oilPriceService.queryOilPriceByProvince(province);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "查询油价信息失败") :
                Result.ok("查询油价信息成功", res);
    }

    @GetMapping("/label/{label}")
    @SentinelResource(value = BlockHandlerConstants.BH_QUERY_OIL_PRICE_BY_LABEL,
            blockHandlerClass = OilPriceBlockHandler.class, blockHandler = "queryOilPriceByLabelBlockHandler")
    public Result queryOilPriceByLabel(@PathVariable("label") String label) {
        List res = oilPriceService.queryOilPriceByLabel(label);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "查询油价信息失败") :
                Result.ok("查询油价信息成功", res);
    }
}
