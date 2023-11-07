package today.oil.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import today.oil.mapper.FareMapper;
import today.oil.model.Fare;
import today.oil.service.FareService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: FareServiceImpl
 */
@Slf4j
@Service
public class FareServiceImpl implements FareService {
    @Autowired
    private FareMapper fareMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public List<Fare> queryByCid(Long cid) {
        log.info("查询费用集合：cid({})", cid);

        final String KEY = "fareList::" + cid;
        List fareList = redisTemplate.opsForList().range(KEY, 0, -1);
        if (Objects.isNull(fareList) || fareList.isEmpty()) {
            log.info("费用集合为空或大小为0：cid({})", cid);
            synchronized (this) {
                log.info("再次查询费用集合：cid({})", cid);
                fareList = redisTemplate.opsForList().range(KEY, 0, -1);
                if (Objects.isNull(fareList) || fareList.isEmpty()) {
                    log.info("从数据库查询费用集合：cid({})", cid);
                    fareList = fareMapper.selectByCid(cid);

                    if (Objects.nonNull(fareList) && !fareList.isEmpty()) {
                        log.info("设置费用集合缓存及失效时间：cid({})", cid);
                        redisTemplate.opsForList().rightPushAll(KEY, fareList.toArray());
                        redisTemplate.expire(KEY, 12, TimeUnit.SECONDS);
                    }
                }
            }
        }

        return fareList;
    }

    @Override
    public Integer addFare(Fare fare) {
        log.info("添加费用：{}", fare);
        int insert = fareMapper.insertSelective(fare);

        log.info("清空费用缓存：cid({})", fare.getCid());
        redisTemplate.expire("fareList::" + fare.getCid(), 0, TimeUnit.SECONDS);
        return insert;
    }

    @Override
    public Integer updateFare(Fare fare) {
        log.info("更新费用：{}", fare);
        int update = fareMapper.updateByPrimaryKeySelective(fare);

        log.info("清空费用缓存：cid({})", fare.getCid());
        redisTemplate.expire("fareList::" + fare.getCid(), 0, TimeUnit.SECONDS);
        return update;
    }

    @Override
    public Integer removeFare(Long id, Long cid) {
        log.info("删除费用：id({})", id);
        int delete = fareMapper.deleteByPrimaryKey(id);

        log.info("清空费用缓存：cid({})", cid);
        redisTemplate.expire("fareList::" + cid, 0, TimeUnit.SECONDS);
        return delete;
    }

    @Override
    public Integer removeFareByCid(Long cid) {
        log.info("删除费用：cid({})", cid);
        int delete = fareMapper.deleteByCid(cid);

        log.info("清空费用缓存：cid({})", cid);
        redisTemplate.expire("fareList::" + cid, 0, TimeUnit.SECONDS);
        return delete;
    }
}
