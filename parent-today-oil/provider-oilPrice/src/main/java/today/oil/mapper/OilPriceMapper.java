package today.oil.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @create: 2023/11/1
 * @Description:
 * @FileName: OilPriceMapper
 */
public interface OilPriceMapper {

    /** 创建省份表
     * @Description:
     * @date: 2023/11/2
     */
    int CTProvince();

    /** 删除省份表
     * @Description:
     * @date: 2023/11/2
     */
    int dropProvince();

    int dropTableByProvince(@Param("provinceName") String provinceName);

    int dropTableByLabel(@Param("label") String label);

    /** 创建省份油价表
     * @Description:
     * @date: 2023/11/2
     * @param provinceName 省份名
     * @return: int
     */
    int CTProvinceOilPrice(@Param("provinceName") String provinceName);

    /** 创建标号油价表
     * @Description:
     * @date: 2023/11/2
     * @param label 标号（92/95/98/0）
     * @return: int
     */
    int CTLabel(@Param("label") String label);

    /** 查询省份集合
     * @Description:
     * @date: 2023/11/2
     * @param
     * @return: java.util.List<java.lang.String>
     */
    List<String> selectProvinceName();

    /** 批量添加省份
     * @Description:
     * @date: 2023/11/2
     * @param provinceList 省份合集
     * @return: int
     */
    int insertProvince(@Param("provinceList") List<String> provinceList);

    /** 指定provinceName批量添加油价信息
     * @Description:
     * @date: 2023/11/2
     * @param provinceName 省份名
     * @param provinceList 省份油价信息
     * @return: int
     */
    int insertProvinceOilPrice(@Param("provinceName") String provinceName,
                               @Param("provinceList") List<Map<String, String>> provinceList);

    /** 指定label批量添加油价信息
     * @Description:
     * @date: 2023/11/2
     * @param label 标号（92/95/98/0）
     * @param labelList 标号油价合集
     * @return: int
     */
    int insertLabelOilPrice(@Param("label") String label,
                            @Param("labelList") List<Map<String, String>> labelList);

    /** 从指定label指定date查询油价信息
     * @Description:
     * @date: 2023/11/2
     * @param label 标号（92/95/98/0）
     * @param date 日期
     * @return: java.util.List<java.util.Map<java.lang.Object,java.lang.Object>>
     */
    List<Map<Object, Object>> selectByLabel(@Param("label") String label,
                                            @Param("date") Date date);

    /** 从指定province指定date查询油价信息
     * @Description:
     * @date: 2023/11/2
     * @param provinceName 省份名
     * @param date 日期
     * @return: java.util.List<java.util.Map<java.lang.Object,java.lang.Object>>
     */
    List<Map<Object, Object>> selectByProvince(@Param("provinceName") String provinceName,
                                               @Param("date") Date date);
}
