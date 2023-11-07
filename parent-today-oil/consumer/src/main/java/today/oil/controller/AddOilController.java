package today.oil.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import today.oil.common.Result;
import today.oil.constant.BlockHandlerConstants;
import today.oil.handler.AddOilBlockHandler;
import today.oil.model.AddOil;
import today.oil.service.RemoteAddOilService;

import java.util.List;
import java.util.Objects;

/** 加油记录用户接口
 * @create: 2023/10/31
 * @Description:
 * @FileName: AddOilController
 */
@RestController
@RequestMapping("/add-oils")
public class AddOilController {
    @Autowired
    private RemoteAddOilService addOilService;

    @GetMapping("/query/cid/{cid}")
    @SentinelResource(value = BlockHandlerConstants.BH_QUERY_ADD_OIL_BY_CID,
            blockHandlerClass = AddOilBlockHandler.class, blockHandler = "queryAddOilByCidBlockHandler")
    public Result queryAddOilByCid(@PathVariable("cid") Long cid) {
        List<AddOil> res = addOilService.queryAddOilByCid(cid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "查询加油记录失败") :
                Result.ok("查询加油记录成功", res);
    }

    @PostMapping("/add")
    @SentinelResource(value = BlockHandlerConstants.BH_ADD_ADD_OIL,
            blockHandlerClass = AddOilBlockHandler.class, blockHandler = "addAddOilBlockHandler")
    public Result addAddOil(@RequestBody AddOil addOil) {
        Integer res = addOilService.addAddOil(addOil);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "添加加油记录失败") :
                Result.ok("添加加油记录成功", res);
    }

    @PutMapping("/update")
    @SentinelResource(value = BlockHandlerConstants.BH_UPDATE_ADD_OIL,
            blockHandlerClass = AddOilBlockHandler.class, blockHandler = "updateAddOilBlockHandler")
    public Result updateAddOil(@RequestBody AddOil addOil) {
        Integer res = addOilService.updateAddOil(addOil);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "修改加油记录失败") :
                Result.ok("修改加油记录成功", res);
    }

    @DeleteMapping("/remove/id/{id}/{cid}")
    @SentinelResource(value = BlockHandlerConstants.BH_REMOVE_ADD_OIL_BY_ID,
            blockHandlerClass = AddOilBlockHandler.class, blockHandler = "removeAddOilByIdBlockHandler")
    public Result removeAddOilById(@PathVariable("id") Long id,
                                   @PathVariable("cid") Long cid) {
        Integer res = addOilService.removeAddOil(id, cid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "删除加油记录失败") :
                Result.ok("删除加油记录成功", res);
    }

    @DeleteMapping("/remove/cid/{cid}")
    @SentinelResource(value = BlockHandlerConstants.BH_REMOVE_ADD_OIL_BY_CID,
            blockHandlerClass = AddOilBlockHandler.class, blockHandler = "removeAddOilByCidBlockHandler")
    public Result removeAddOilByCid(@PathVariable("cid") Long cid) {
        Integer res = addOilService.removeAddOilByCid(cid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "删除加油记录失败") :
                Result.ok("删除加油记录成功", res);
    }

    @GetMapping("/query/mile/{cid}")
    @SentinelResource(value = BlockHandlerConstants.BH_QUERY_MAX_MILE_BY_CID,
            blockHandlerClass = AddOilBlockHandler.class, blockHandler = "queryMaxMileByCidBlockHandler")
    public Result queryMaxMileByCid(@PathVariable("cid") Long cid) {
        Double res = addOilService.queryMaxMileByCid(cid);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "查询车辆里程失败") :
                Result.ok("查询车辆里程成功", res);
    }
}
