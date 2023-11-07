package today.oil.service.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import today.oil.model.Car;
import today.oil.service.RemoteCarService;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: RemoteCarServiceFallback
 */
@Slf4j
@Component
public class RemoteCarServiceFallback implements RemoteCarService {
    @Override
    public List<Car> queryCarByUid(Long uid) {
        log.warn("调用服务queryCarByUid({})失败", uid);
        return null;
    }

    @Override
    public Integer addCar(Car car) {
        log.warn("调用服务addCar({})失败", car);
        return null;
    }

    @Override
    public Integer updateCar(Car car) {
        log.warn("调用服务updateCar({})失败", car);
        return null;
    }

    @Override
    public Integer removeCar(Long id, Long uid) {
        log.warn("调用服务removeCar({}, {})失败", id, uid);
        return null;
    }

    @Override
    public Integer removeCarByUid(Long uid) {
        log.warn("调用服务removeCarByUid({})失败", uid);
        return null;
    }

    @Override
    public List<Long> queryIdByUid(Long uid) {
        log.warn("调用服务 queryIdByUid({})失败", uid);
        return null;
    }

    @Override
    public Boolean hasCar(Car car) {
        log.warn("调用服务 hasCar({})失败", car);
        return null;
    }
}
