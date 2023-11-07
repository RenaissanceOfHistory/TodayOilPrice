package today.oil.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import today.oil.common.Result;
import today.oil.constant.BlockHandlerConstants;
import today.oil.handler.FareBlockHandler;
import today.oil.model.Fare;
import today.oil.service.RemoteFareService;

import java.util.List;
import java.util.Objects;

/** 费用记录用户接口
 * @create: 2023/10/31
 * @Description:
 * @FileName: FareController
 */
@RestController
@RequestMapping("/fares")
public class FareController {
    @Autowired
    private RemoteFareService fareService;

    @GetMapping("/query/cid/{cid}")
    @SentinelResource(value = BlockHandlerConstants.BH_QUERY_FARE_BY_CID,
            blockHandlerClass = FareBlockHandler.class, blockHandler = "queryFareByCidBlockHandler")
    public Result queryFareByCid(@PathVariable("cid") Long cid) {
        List<Fare> fareList = fareService.queryFareByCid(cid);
        return Objects.isNull(fareList) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "查询费用失败") :
                Result.ok("查询费用成功", fareList);
    }

    @PostMapping("/add")
    @SentinelResource(value = BlockHandlerConstants.BH_ADD_FARE,
            blockHandlerClass = FareBlockHandler.class, blockHandler = "addFareBlockHandler")
    public Result addFare(@RequestBody Fare fare) {
        Integer res = fareService.addFare(fare);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "添加费用失败") :
                Result.ok("添加费用成功", res);
    }

    @PutMapping("/update")
    @SentinelResource(value = BlockHandlerConstants.BH_UPDATE_FARE,
            blockHandlerClass = FareBlockHandler.class, blockHandler = "updateFareBlockHandler")
    public Result updateFare(@RequestBody Fare fare) {
        Integer res = fareService.updateFare(fare);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "修改费用失败") :
                Result.ok("修改费用成功", res);
    }

    @DeleteMapping("/remove/id/{id}/{cid}")
    @SentinelResource(value = BlockHandlerConstants.BH_REMOVE_FARE,
            blockHandlerClass = FareBlockHandler.class, blockHandler = "removeFareBlockHandler")
    public Result removeFare(@PathVariable("id") Long id,
                             @PathVariable("cid") Long cid) {
        Integer res = fareService.removeFare(id, cid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "删除费用失败") :
                Result.ok("删除费用成功", res);
    }

    @DeleteMapping("/remove/cid/{cid}")
    @SentinelResource(value = BlockHandlerConstants.BH_REMOVE_FARE_BY_CID,
            blockHandlerClass = FareBlockHandler.class, blockHandler = "removeFareByCidBlockHandler")
    public Result removeFareByCid(@PathVariable("cid") Long cid) {
        Integer res = fareService.removeFareByCid(cid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "删除费用集合失败") :
                Result.ok("删除费用集合成功", res);
    }
}
