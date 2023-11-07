package today.oil.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/** 加油站类
 * @Description:
 * @date: 2023/10/31
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    /** 自增ID */
    private Long id;

    /** 加油站名称 */
    private String staName;

    /** 地址 */
    private String address;

    /** 联系电话 */
    private String phone;

    /** 创建时间 */
    private Date createTime;
}