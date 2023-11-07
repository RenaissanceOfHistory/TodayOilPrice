package today.oil.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/** 用户类
 * @Description:
 * @date: 2023/10/31
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /** 自增ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 联系电话 */
    private String phone;

    /** 用户头像 */
    private String avatar;

    /** 创建时间 */
    private Date createTime;
}