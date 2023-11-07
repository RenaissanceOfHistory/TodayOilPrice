package today.oil.service;

import today.oil.model.Car;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: CarService
 */
public interface CarService {

    /** 查询用户车辆集合
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: java.util.List<today.oil.model.Car>
     */
    List<Car> queryByUid(Long uid);

    Integer addCar(Car car);

    Integer updateCar(Car car);

    Integer removeByPrimaryKey(Long id, Long uid);

    /** 删除用户车辆
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: java.lang.Integer
     */
    Integer removeByUid(Long uid);

    /** 查询用户车辆ID集合
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: java.util.List<java.lang.Long>
     */
    List<Long> queryIdByUid(Long uid);

    /** 是否存在车辆
     * @Description:
     * @date: 2023/11/7
     * @param car
     * @return: java.lang.Boolean
     */
    Boolean hasCar(Car car);
}
