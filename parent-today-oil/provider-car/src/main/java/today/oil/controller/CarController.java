package today.oil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import today.oil.model.Car;
import today.oil.service.CarService;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: CarController
 */
@RestController
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping("/car-query/uid/{uid}")
    public List<Car> queryCarByUid(@PathVariable("uid") Long uid) {
        return carService.queryByUid(uid);
    }

    @PostMapping("/car-add")
    public Integer addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @PutMapping("/car-update")
    public Integer updateCar(@RequestBody Car car) {
        return carService.updateCar(car);
    }

    @DeleteMapping("/car-remove/id/{id}/{uid}")
    public Integer removeCar(@PathVariable("id") Long id,
                             @PathVariable("uid") Long uid) {
        return carService.removeByPrimaryKey(id, uid);
    }

    @DeleteMapping("/car-remove/uid/{uid}")
    public Integer removeCarByUid(@PathVariable("uid") Long uid) {
        return carService.removeByUid(uid);
    }

    @DeleteMapping("/car-query-id/uid/{uid}")
    public List<Long> queryIdByUid(@PathVariable("uid") Long uid) {
        return carService.queryIdByUid(uid);
    }

    @PostMapping("/cac-query-has-car")
    public Boolean hasCar(@RequestBody Car car) {
        return carService.hasCar(car);
    }
}
