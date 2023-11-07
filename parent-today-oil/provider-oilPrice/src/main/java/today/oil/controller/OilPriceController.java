package today.oil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import today.oil.service.OilPriceService;

import java.util.List;

/**
 * @create: 2023/11/1
 * @Description:
 * @FileName: OilPriceController
 */
@RestController
public class OilPriceController {

    @Autowired
    private OilPriceService oilPriceService;

    @GetMapping("/oil-price-province")
    public List queryProvinces() {
        return oilPriceService.queryAllProvinces();
    }

    @GetMapping("/oil-price-province/{province}")
    public List queryOilPriceByProvince(@PathVariable("province") String province) {
        return oilPriceService.queryOilPriceByProvince(province);
    }

    @GetMapping("/oil-price-label/{label}")
    public List queryOilPriceByLabel(@PathVariable("label") String label) {
        return oilPriceService.queryOilPriceByLabel(label);
    }
}
