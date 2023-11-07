package today.oil.service;

import today.oil.model.User;
import today.oil.model.UserDetail;

import javax.servlet.http.HttpServletResponse;

/**
 * @create: 2023/10/30
 * @Description:
 * @FileName: UserService
 */
public interface UserService {
    /** 登录<br />
     * user 对象 password 属性加密后在数据库进行查询 <br />
     * 如果查询成功，设置 token 缓存并返回 <br />
     * 否则返回 <code>null</code> <br />
     * @Description:
     * @date: 2023/11/7
     * @param user
     * @return: java.lang.String
     */
    String login(User user);

    /** 注册 <br />
     * user 对象 password 属性加密后在数据库进行查询 <br />
     * 如果存在用户，返回 <code>null</code>。
     * 否则，添加用户和用户信息
     * @Description:
     * @date: 2023/11/7
     * @param user
     * @return: java.lang.Integer
     */
    Integer enroll(User user);

    /** 添加用户信息
     * @Description:
     * @date: 2023/11/7
     * @param userDetail
     * @return: java.lang.Integer
     */
    Integer addUserDetail(UserDetail userDetail);

    Integer updateUserDetail(UserDetail userDetail);

    Integer deleteUser(Long id);

    UserDetail selectDetailByUid(Long uid);

    /** 查询用户名
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: java.lang.String
     */
    String queryNameByUid(Long uid);

    int updateUser(User user);

    /** 查询用户头像
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: java.lang.String
     */
    String queryAvatar(Long uid);
}
