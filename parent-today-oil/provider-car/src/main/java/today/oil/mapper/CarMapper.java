package today.oil.mapper;

import org.apache.ibatis.annotations.Param;
import today.oil.model.Car;

import java.util.List;

public interface CarMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Car record);

    int insertSelective(Car record);

    Car selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);

    /** 查询用户的车辆集合
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: java.util.List<today.oil.model.Car>
     */
    List<Car> selectByUid(Long uid);

    /** 删除用户车辆
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: int
     */
    int deleteByUid(Long uid);

    /** 查询车辆ID集合
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: java.util.List<java.lang.Long>
     */
    List<Long> selectPrimaryKeyByUid(Long uid);

    /** 查询用户 carName 的数量
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @param carName 车辆名
     * @return: int
     */
    int selectByUidAndName(@Param("uid") Long uid,
                           @Param("carName") String carName,
                           @Param("id") Long id);
}