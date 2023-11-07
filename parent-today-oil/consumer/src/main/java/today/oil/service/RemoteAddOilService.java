package today.oil.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import today.oil.constant.ServiceConstants;
import today.oil.model.AddOil;
import today.oil.service.fallback.RemoteAddOilServiceFallback;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: RemoteAddOilService
 */
@FeignClient(contextId = "addOilService", name = ServiceConstants.ADD_OIL_SERVICE, fallback = RemoteAddOilServiceFallback.class)
public interface RemoteAddOilService {

    @GetMapping("/add-oil-query/cid/{cid}")
    List<AddOil> queryAddOilByCid(@PathVariable("cid") Long cid);

    @PostMapping("/add-oil-add")
    Integer addAddOil(@RequestBody AddOil addOil);

    @PutMapping("/add-oil-update")
    Integer updateAddOil(@RequestBody AddOil addOil);

    @DeleteMapping("/add-oil-remove/id/{id}/{cid}")
    Integer removeAddOil(@PathVariable("id") Long id,
                         @PathVariable("cid") Long cid);

    @DeleteMapping("/add-oil-remove/cid/{cid}")
    Integer removeAddOilByCid(@PathVariable("cid") Long cid);

    @GetMapping("/add-oil-query/mile/{cid}")
    Double queryMaxMileByCid(@PathVariable("cid") Long cid);
}
