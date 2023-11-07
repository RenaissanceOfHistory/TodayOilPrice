package today.oil.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;
import today.oil.common.Result;
import today.oil.constant.BlockHandlerConstants;
import today.oil.handler.CarBlockHandler;
import today.oil.model.Car;
import today.oil.service.RemoteAddOilService;
import today.oil.service.RemoteCarService;
import today.oil.service.RemoteFareService;

import java.util.List;
import java.util.Objects;

/** 车辆管理用户接口
 * @create: 2023/10/31
 * @Description:
 * @FileName: CarController
 */
@Slf4j
@RestController
@RequestMapping("/cars")
public class CarController {
    @Autowired
    private RemoteCarService carService;

    @Autowired
    private RemoteFareService fareService;

    @Autowired
    private RemoteAddOilService addOilService;

    @GetMapping("/query/uid")
    @SentinelResource(value = BlockHandlerConstants.BH_QUERY_CAR_BY_UID,
            blockHandlerClass = CarBlockHandler.class, blockHandler = "queryCarByUidBlockHandler")
    public Result queryCarByUid(@RequestHeader("uid") Long uid) {
        List<Car> carList = carService.queryCarByUid(uid);
        return Objects.isNull(carList) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "查询车辆失败") :
                Result.ok("查询车辆成功", carList);
    }

    @PostMapping("/add")
    @SentinelResource(value = BlockHandlerConstants.BH_ADD_CAR,
            blockHandlerClass = CarBlockHandler.class, blockHandler = "addCarBlockHandler")
    public Result addCar(@RequestBody Car car, @RequestHeader("uid") Long uid) {
        car.setUid(uid);
        Boolean hasCar = carService.hasCar(car);
        if (Objects.nonNull(hasCar) && hasCar) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "已存在同名车辆");
        }

        Integer res = carService.addCar(car);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "添加车辆失败") :
                Result.ok("添加车辆成功", res);
    }

    @PutMapping("/update")
    @SentinelResource(value = BlockHandlerConstants.BH_UPDATE_CAR,
            blockHandlerClass = CarBlockHandler.class, blockHandler = "updateCarBlockHandler")
    public Result updateCar(@RequestBody Car car, @RequestHeader("uid") Long uid) {
        car.setUid(uid);
        Boolean hasCar = carService.hasCar(car);
        if (Objects.nonNull(hasCar) && hasCar) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "已存在同名车辆");
        }

        Integer res = carService.updateCar(car);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "修改车辆失败") :
                Result.ok("修改车辆成功", res);
    }

    @DeleteMapping("/remove/id/{id}")
    @SentinelResource(value = BlockHandlerConstants.BH_REMOVE_CAR,
            blockHandlerClass = CarBlockHandler.class, blockHandler = "removeCarBlockHandler")
    public Result removeCar(@PathVariable("id") Long id, @RequestHeader("uid") Long uid) {
        // 删除关联数据
        addOilService.removeAddOilByCid(id);
        fareService.removeFareByCid(id);

        Integer res = carService.removeCar(id, uid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "删除车辆失败") :
                Result.ok("删除车辆成功", res);
    }

    @Transactional
    @DeleteMapping("/remove/uid")
    @SentinelResource(value = BlockHandlerConstants.BH_REMOVE_CAR_BY_UID,
            blockHandlerClass = CarBlockHandler.class, blockHandler = "removeCarByUidBlockHandler")
    public Result removeCarByUid(@RequestHeader("uid") Long uid) {
        // 删除关联数据
        carService.queryIdByUid(uid).forEach(cid -> {
            fareService.removeFareByCid(cid);
            addOilService.removeAddOilByCid(cid);
        });

        Integer res = carService.removeCarByUid(uid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "删除用户车辆集合失败") :
                Result.ok("删除用户车辆集合成功", res);
    }
}
