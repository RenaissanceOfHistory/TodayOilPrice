package today.oil.mapper;

import today.oil.model.Station;

/** 加油站服务（暂未使用）
 * @Description:
 * @date: 2023/10/31
 */
public interface StationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Station record);

    int insertSelective(Station record);

    Station selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Station record);

    int updateByPrimaryKey(Station record);
}