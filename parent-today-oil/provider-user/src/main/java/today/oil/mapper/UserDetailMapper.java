package today.oil.mapper;

import today.oil.model.UserDetail;

public interface UserDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDetail record);

    int insertSelective(UserDetail record);

    UserDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDetail record);

    int updateByPrimaryKey(UserDetail record);

    /** 查询用户的信息
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: today.oil.model.UserDetail
     */
    UserDetail selectByUid(Long uid);

    /** 删除用户的信息
     * @Description:
     * @date: 2023/11/7
     * @param uid 用户ID
     * @return: int
     */
    int deleteByUid(Long uid);
}