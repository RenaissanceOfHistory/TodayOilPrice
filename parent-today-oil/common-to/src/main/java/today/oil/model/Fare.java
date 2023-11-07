package today.oil.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/** 费用记录类-指定车辆费用记录
 * @Description:
 * @date: 2023/10/31
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fare {
    /** 自增ID */
    private Long id;

    /** 车辆ID */
    private Long cid;

    /** 费用类型（0-支出，1-收入） */
    private String fareType;

    /** 添加时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;

    /** 支出类型 */
    private Byte outType;

    /** 支出费用 */
    private BigDecimal outFare;

    /** 收入类型 */
    private Byte inType;

    /** 收入费用 */
    private BigDecimal inFare;

    /** 评价信息 */
    private String info;

    /** 创建时间 */
    private Date createTime;
}