package today.oil.mapper;

import today.oil.model.Fare;

import java.util.List;

public interface FareMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Fare record);

    int insertSelective(Fare record);

    Fare selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Fare record);

    int updateByPrimaryKey(Fare record);

    /** 查询车辆费用集合
     * @Description:
     * @date: 2023/11/7
     * @param cid 车辆ID
     * @return: java.util.List<today.oil.model.Fare>
     */
    List<Fare> selectByCid(Long cid);

    /** 删除车辆费用集合
     * @Description:
     * @date: 2023/11/7
     * @param cid 车辆ID
     * @return: int
     */
    int deleteByCid(Long cid);
}