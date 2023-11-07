package today.oil.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/** 加油记录类-指定用户车辆的加油记录
 * @Description:
 * @date: 2023/10/31
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddOil {
    /** 自增ID */
    private Long id;

    /** 车辆ID */
    private Long cid;

    /** 加油时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;

    /** 行驶里程 */
    private Double mile;

    /** 余额 */
    private BigDecimal balance;

    /** 油价单价 */
    private BigDecimal unitPrice;

    /** 油量容量 */
    private Double volume;

    /** 剩余油量 */
    private Byte leftover;

    /** 提示 */
    private String note;

    /** 上次是否忘记选择（添加加油记录）（0-否，1-是） */
    private String forgot;

    /** 加油站ID */
    private Long staId;

    /** 评价信息 */
    private String info;

    /** 创建时间 */
    private Date createTime;

}