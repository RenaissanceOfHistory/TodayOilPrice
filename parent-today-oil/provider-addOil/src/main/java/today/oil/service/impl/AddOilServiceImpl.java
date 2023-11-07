package today.oil.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import today.oil.mapper.AddOilMapper;
import today.oil.model.AddOil;
import today.oil.service.AddOilService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: AddOilServiceImpl
 */
@Slf4j
@Service
public class AddOilServiceImpl implements AddOilService {
    @Autowired
    private AddOilMapper addOilMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public List<AddOil> queryByCid(Long cid) {
        log.info("查询加油记录集合：cid({})", cid);

        final String KEY = "addOilList::" + cid;
        List addOilList = redisTemplate.opsForList().range(KEY, 0, -1);
        if (Objects.isNull(addOilList) || addOilList.isEmpty()) {
            log.info("加油记录集合为空或大小为0：cid({})", cid);
            synchronized (this) {
                addOilList = redisTemplate.opsForList().range(KEY, 0, -1);
                if (Objects.isNull(addOilList) || addOilList.isEmpty()) {
                    log.info("从数据库查询加油记录集合：cid({})", cid);
                    addOilList = addOilMapper.selectByCid(cid);

                    if (Objects.nonNull(addOilList) && !addOilList.isEmpty()) {
                        log.info("设置加油记录集合缓存及失效时间：cid({})", cid);
                        redisTemplate.opsForList().rightPushAll(KEY, addOilList.toArray());
                        redisTemplate.expire(KEY, 13, TimeUnit.SECONDS);
                    }
                }
            }
        }
        return addOilList;
    }

    @Override
    public Integer addAddOil(AddOil addOil) {
        log.info("添加加油记录：{}", addOil);
        int insert = addOilMapper.insertSelective(addOil);

        log.info("清空加油记录缓存：cid({})", addOil.getCid());
        redisTemplate.expire("addOilList::" + addOil.getCid(), 0, TimeUnit.SECONDS);
        return insert;
    }

    @Override
    public Integer updateAddOil(AddOil addOil) {
        log.info("更新加油记录：{}", addOil);
        int update = addOilMapper.updateByPrimaryKeySelective(addOil);

        log.info("清空加油记录缓存：cid({})", addOil.getCid());
        redisTemplate.expire("addOilList::" + addOil.getCid(), 0, TimeUnit.SECONDS);
        return update;
    }

    @Override
    public Integer removeAddOil(Long id, Long cid) {
        log.info("删除加油记录：{}", id);
        int delete = addOilMapper.deleteByPrimaryKey(id);

        log.info("清空加油记录缓存：cid({})", cid);
        redisTemplate.expire("addOilList::" + cid, 0, TimeUnit.SECONDS);
        return delete;
    }

    @Override
    public Integer removeAddOilByCid(Long cid) {
        log.info("删除加油记录：{}", cid);
        int delete = addOilMapper.deleteByCid(cid);

        log.info("清空加油记录缓存：cid({})", cid);
        redisTemplate.expire("addOilList::" + cid, 0, TimeUnit.SECONDS);
        return delete;
    }

    @Override
    public Double queryMileByCid(Long cid) {
        log.info("查询行驶里程：cid({})", cid);
        Double mile = addOilMapper.selectMaxMileByCid(cid);
        return Objects.isNull(mile) ? 0.0 : mile;
    }
}
