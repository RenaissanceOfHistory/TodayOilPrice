package today.oil.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/** 用户信息类
 * @Description:
 * @date: 2023/10/31
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {
    /** 自增ID */
    private Long id;

    /** 用户ID */
    private Long uid;

    /** 电子邮件 */
    private String email;

    /** 性别（0-男，1-女） */
    private String gender;

    /** 居住城市 */
    private String city;

    /** 居住省份 */
    private String province;

    /** 使用语言 */
    private String language;

    /** 自我评价 */
    private String commentary;

    /** 创建时间 */
    private Date createTime;
}