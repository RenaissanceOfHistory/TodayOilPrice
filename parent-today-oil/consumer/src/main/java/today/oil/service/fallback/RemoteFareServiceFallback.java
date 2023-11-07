package today.oil.service.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import today.oil.model.Fare;
import today.oil.service.RemoteFareService;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: RemoteFareServiceFallback
 */
@Slf4j
@Component
public class RemoteFareServiceFallback implements RemoteFareService {
    @Override
    public List<Fare> queryFareByCid(Long cid) {
        log.warn("调用服务 queryFareByCid({})失败", cid);
        return null;
    }

    @Override
    public Integer addFare(Fare fare) {
        log.warn("调用服务 addFare({})失败", fare);
        return null;
    }

    @Override
    public Integer updateFare(Fare fare) {
        log.warn("调用服务 updateFare({})失败", fare);
        return null;
    }

    @Override
    public Integer removeFare(Long id, Long cid) {
        log.warn("调用服务 removeFare({}, {})失败", id, cid);
        return null;
    }

    @Override
    public Integer removeFareByCid(Long cid) {
        log.warn("调用服务 removeFareByCid({})失败", cid);
        return null;
    }
}
