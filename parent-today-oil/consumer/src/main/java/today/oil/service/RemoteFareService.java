package today.oil.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import today.oil.constant.ServiceConstants;
import today.oil.model.Fare;
import today.oil.service.fallback.RemoteFareServiceFallback;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: RemoteFareService
 */
@FeignClient(contextId = "fareService", name = ServiceConstants.FARE_SERVICE, fallback = RemoteFareServiceFallback.class)
public interface RemoteFareService {

    @GetMapping("/fare-query/cid/{cid}")
    List<Fare> queryFareByCid(@PathVariable("cid") Long cid);

    @PostMapping("/fare-add")
    Integer addFare(@RequestBody Fare fare);

    @PutMapping("/fare-update")
    Integer updateFare(@RequestBody Fare fare);

    @DeleteMapping("/fare-remove/id/{id}/{cid}")
    Integer removeFare(@PathVariable("id") Long id,
                       @PathVariable("cid") Long cid);

    @DeleteMapping("/fare-remove/cid/{cid}")
    Integer removeFareByCid(@PathVariable("cid") Long cid);
}
