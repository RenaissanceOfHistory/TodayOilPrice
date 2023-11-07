package today.oil.service;

import today.oil.model.Fare;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: FareService
 */
public interface FareService {
    /** 查询车辆费用集合
     * @Description:
     * @date: 2023/11/7
     * @param cid 车辆ID
     * @return: java.util.List<today.oil.model.Fare>
     */
    List<Fare> queryByCid(Long cid);

    Integer addFare(Fare fare);

    Integer updateFare(Fare fare);

    Integer removeFare(Long id, Long cid);

    /** 删除车辆费用集合
     * @Description:
     * @date: 2023/11/7
     * @param cid 车辆ID
     * @return: java.lang.Integer
     */
    Integer removeFareByCid(Long cid);
}
