package today.oil.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/** 车辆类
 * @Description:
 * @date: 2023/10/31
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    /** 自增ID */
    private Long id;

    /** 用户ID */
    private Long uid;

    /** 车辆名称 */
    private String carName;

    /** 油量容量 */
    private Double volume;

    /** 创建时间 */
    private Date createTime;

}