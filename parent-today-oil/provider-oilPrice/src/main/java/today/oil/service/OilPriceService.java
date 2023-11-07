package today.oil.service;

import java.util.List;

/**
 * @create: 2023/11/1
 * @Description:
 * @FileName: OilPriceService
 */
public interface OilPriceService {

    /** 查询省份合集
     * @Description:
     * @date: 2023/11/2
     * @param
     * @return: java.util.List
     */
    List queryAllProvinces();

    /** 查询指定province油价信息
     * @Description:
     * @date: 2023/11/2
     * @param province 省份名
     * @return: java.util.List
     */
    List queryOilPriceByProvince(String province);

    /** 查询指定label油价信息
     * @Description:
     * @date: 2023/11/2
     * @param label 标号（92/95/98/0）
     * @return: java.util.List
     */
    List queryOilPriceByLabel(String label);
}
