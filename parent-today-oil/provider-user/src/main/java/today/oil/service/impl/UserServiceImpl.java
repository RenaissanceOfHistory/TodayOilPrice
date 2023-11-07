package today.oil.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.oil.mapper.UserDetailMapper;
import today.oil.mapper.UserMapper;
import today.oil.model.User;
import today.oil.model.UserDetail;
import today.oil.service.UserService;
import today.oil.utils.EncryptUtil;
import today.oil.utils.JWTUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @create: 2023/10/30
 * @Description:
 * @FileName: UserServiceImpl
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String login(User user) {
        log.info("用户登录：{}", user);
        user.setPassword(EncryptUtil.encode(user.getPassword(), EncryptUtil.EncryptType.MD5));
        user = userMapper.selectByNameAndPwd(user.getUsername(), user.getPassword());

        if (Objects.nonNull(user)) {
            log.info("查询成功，设置token：{}", user);
            String token = JWTUtil.token(user);
            redisTemplate.opsForValue().set(token, "", 30, TimeUnit.MINUTES);
            return token;
        }
        return null;
    }

    @Transactional
    @Override
    public Integer enroll(User user) {
        log.info("用户注册：{}", user);
        user.setPassword(EncryptUtil.encode(user.getPassword(), EncryptUtil.EncryptType.MD5));
        User select = userMapper.selectByNameAndPwd(user.getUsername(), user.getPassword());
        if (Objects.nonNull(select)) {
            log.info("用户已存在");
            return null;
        }

        log.info("添加用户：{}", user);
        userMapper.insertSelective(user);
        return addUserDetail(UserDetail.builder().uid(user.getId()).build());
    }

    @Override
    public Integer addUserDetail(UserDetail userDetail) {
        log.info("添加用户信息：{}", userDetail);
        return userDetailMapper.insertSelective(userDetail);
    }

    @Override
    public Integer updateUserDetail(UserDetail userDetail) {
        log.info("更新用户信息：{}", userDetail);
        return userDetailMapper.updateByPrimaryKeySelective(userDetail);
    }

    @Transactional
    @Override
    public Integer deleteUser(Long id) {
        log.info("删除用户：id({})", id);
        userDetailMapper.deleteByUid(id);
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public UserDetail selectDetailByUid(Long uid) {
        log.info("查询用户信息：uid({})", uid);
        return userDetailMapper.selectByUid(uid);
    }

    @Override
    public String queryNameByUid(Long uid) {
        return userMapper.selectNameByPrimaryKey(uid);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public String queryAvatar(Long uid) {
        return userMapper.selectAvatarByPrimaryKey(uid);
    }
}
