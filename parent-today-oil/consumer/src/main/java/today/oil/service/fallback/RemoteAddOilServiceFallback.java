package today.oil.service.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import today.oil.model.AddOil;
import today.oil.service.RemoteAddOilService;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: RemoteAddOilServiceFallback
 */
@Slf4j
@Component
public class RemoteAddOilServiceFallback implements RemoteAddOilService {
    @Override
    public List<AddOil> queryAddOilByCid(Long cid) {
        log.warn("调用服务 queryAddOilByCid({})失败", cid);
        return null;
    }

    @Override
    public Integer addAddOil(AddOil addOil) {
        log.warn("调用服务 addAddOil({})失败", addOil);
        return null;
    }

    @Override
    public Integer updateAddOil(AddOil addOil) {
        log.warn("调用服务 updateAddOil({})失败", addOil);
        return null;
    }

    @Override
    public Integer removeAddOil(Long id, Long cid) {
        log.warn("调用服务 removeAddOil({}, {})失败", id, cid);
        return null;
    }

    @Override
    public Integer removeAddOilByCid(Long cid) {
        log.warn("调用服务 removeAddOilByCid({})失败", cid);
        return null;
    }

    @Override
    public Double queryMaxMileByCid(Long cid) {
        log.warn("调用服务 queryMaxMileByCid({})失败", cid);
        return null;
    }
}
