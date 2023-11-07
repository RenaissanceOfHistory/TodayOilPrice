package today.oil.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import today.oil.mapper.CarMapper;
import today.oil.model.Car;
import today.oil.service.CarService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: CarServiceImpl
 */
@Slf4j
@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarMapper carMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /** 从缓存查询车辆集合，如果不存在，从数据库查询。
     * 存在数据时将数据添加进缓存并设置失效时间
     * @Description:
     * @date: 2023/11/7
     * @param uid
     * @return: java.util.List<today.oil.model.Car>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Car> queryByUid(Long uid) {
        log.info("查询uid({})的车辆集合", uid);

        final String KEY = "carList::" + uid;
        List carList = redisTemplate.opsForList().range(KEY, 0, -1);
        if (Objects.isNull(carList) || carList.isEmpty()) {
            log.info("uid({})车辆集合为空或大小为0", uid);
            synchronized (this) {
                log.info("uid({})再次查询车辆集合", uid);
                carList = redisTemplate.opsForList().range(KEY, 0, -1);
                if (Objects.isNull(carList) || carList.isEmpty()) {
                    log.info("从数据库查询uid({})车辆集合", uid);
                    carList = carMapper.selectByUid(uid);

                    if (Objects.nonNull(carList) && !carList.isEmpty()) {
                        log.info("为uid({})车辆集合设置缓存及失效时间", uid);
                        redisTemplate.opsForList().rightPushAll(KEY, carList.toArray());
                        redisTemplate.expire(KEY, 10, TimeUnit.SECONDS);
                    }
                }
            }
        }
        return carList;
    }

    /** 实行添加车辆操作，清空车辆缓存
     * @Description:
     * @date: 2023/11/7
     * @param car
     * @return: java.lang.Integer
     */
    @Override
    public Integer addCar(Car car) {
        log.info("添加车辆：{}", car);
        int insert = carMapper.insertSelective(car);

        log.info("清空车辆缓存：uid({})", car.getUid());
        redisTemplate.expire("carList::" + car.getUid(), 0, TimeUnit.SECONDS);
        return insert;
    }

    @Override
    public Integer updateCar(Car car) {
        log.info("更新车辆：{}", car);
        int update = carMapper.updateByPrimaryKeySelective(car);

        log.info("清空车辆缓存：uid({})", car.getUid());
        redisTemplate.expire("carList::" + car.getUid(), 0, TimeUnit.SECONDS);
        return update;
    }

    @Override
    public Integer removeByPrimaryKey(Long id, Long uid) {
        log.info("删除指定车辆：id({})", id);
        int delete = carMapper.deleteByPrimaryKey(id);

        log.info("清空车辆缓存：uid({})", uid);
        redisTemplate.expire("carList::" + uid, 0, TimeUnit.SECONDS);
        return delete;
    }

    @Override
    public Integer removeByUid(Long uid) {
        log.info("删除用户所有车辆：uid({})", uid);
        int delete = carMapper.deleteByUid(uid);

        log.info("清空车辆缓存：uid({})", uid);
        redisTemplate.expire("carList::" + uid, 0, TimeUnit.SECONDS);
        return delete;
    }

    @Override
    public List<Long> queryIdByUid(Long uid) {
        log.info("查询车辆集合：uid({})", uid);
        return carMapper.selectPrimaryKeyByUid(uid);
    }

    @Override
    public Boolean hasCar(Car car) {
        log.info("是否存在同名车辆：{}", car);
        return carMapper.selectByUidAndName(car.getUid(), car.getCarName(), car.getId()) > 0;
    }
}
