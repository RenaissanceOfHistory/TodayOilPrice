package today.oil.mapper;

import org.apache.ibatis.annotations.Param;
import today.oil.model.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /** 根据 username 和 password 查询用户
     * @Description:
     * @date: 2023/11/7
     * @param username 用户名
     * @param password 密码
     * @return: today.oil.model.User
     */
    User selectByNameAndPwd(@Param("username") String username,
                            @Param("password") String password);

    /** 查询用户名
     * @Description:
     * @date: 2023/11/7
     * @param id 用户ID
     * @return: java.lang.String
     */
    String selectNameByPrimaryKey(Long id);

    /** 查询用户头像
     * @Description:
     * @date: 2023/11/7
     * @param id 用户ID
     * @return: java.lang.String
     */
    String selectAvatarByPrimaryKey(Long id);
}