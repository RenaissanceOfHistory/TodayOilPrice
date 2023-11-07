package today.oil.service;

import today.oil.model.AddOil;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: AddOilService
 */
public interface AddOilService {
    List<AddOil> queryByCid(Long cid);

    Integer addAddOil(AddOil addOil);

    Integer updateAddOil(AddOil addOil);

    Integer removeAddOil(Long id, Long cid);

    Integer removeAddOilByCid(Long cid);

    /** 查询车辆最大行驶里程
     * @Description:
     * @date: 2023/11/7
     * @param cid 车辆ID
     * @return: java.lang.Double
     */
    Double queryMileByCid(Long cid);
}
