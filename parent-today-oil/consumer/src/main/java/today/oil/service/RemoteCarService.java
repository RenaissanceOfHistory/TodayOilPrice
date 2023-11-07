package today.oil.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import today.oil.constant.ServiceConstants;
import today.oil.model.Car;
import today.oil.service.fallback.RemoteCarServiceFallback;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: RemoteCarService
 */
@FeignClient(contextId = "carService", name = ServiceConstants.CAR_SERVICE, fallback = RemoteCarServiceFallback.class)
public interface RemoteCarService {

    @GetMapping("/car-query/uid/{uid}")
    List<Car> queryCarByUid(@PathVariable("uid") Long uid);

    @PostMapping("/car-add")
    Integer addCar(@RequestBody Car car);

    @PutMapping("/car-update")
    Integer updateCar(@RequestBody Car car);

    @DeleteMapping("/car-remove/id/{id}/{uid}")
    Integer removeCar(@PathVariable("id") Long id,
                      @PathVariable("uid") Long uid);

    @DeleteMapping("/car-remove/uid/{uid}")
    Integer removeCarByUid(@PathVariable("uid") Long uid);

    @DeleteMapping("/car-query-id/uid/{uid}")
    List<Long> queryIdByUid(@PathVariable("uid") Long uid);

    @PostMapping("/cac-query-has-car")
    Boolean hasCar(@RequestBody Car car);
}
