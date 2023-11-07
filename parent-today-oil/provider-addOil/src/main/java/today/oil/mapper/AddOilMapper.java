package today.oil.mapper;

import today.oil.model.AddOil;

import java.util.List;

public interface AddOilMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AddOil record);

    int insertSelective(AddOil record);

    AddOil selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AddOil record);

    int updateByPrimaryKey(AddOil record);

    /** 查询车辆的加油记录集合
     * @Description:
     * @date: 2023/11/7
     * @param cid 车辆ID
     * @return: java.util.List<today.oil.model.AddOil>
     */
    List<AddOil> selectByCid(Long cid);

    /** 删除车辆的加油记录集合
     * @Description:
     * @date: 2023/11/7
     * @param cid 车辆ID
     * @return: int
     */
    int deleteByCid(Long cid);

    /** 查询车辆加油记录集合中最大行驶里程
     * @Description:
     * @date: 2023/11/7
     * @param cid
     * @return: java.lang.Double
     */
    Double selectMaxMileByCid(Long cid);
}