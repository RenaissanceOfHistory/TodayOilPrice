package today.oil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import today.oil.model.AddOil;
import today.oil.service.AddOilService;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: AddOilController
 */
@RestController
public class AddOilController {
    @Autowired
    private AddOilService addOilService;

    @GetMapping("/add-oil-query/cid/{cid}")
    public List<AddOil> queryAddOilByCid(@PathVariable("cid") Long cid) {
        return addOilService.queryByCid(cid);
    }

    @PostMapping("/add-oil-add")
    public Integer addAddOil(@RequestBody AddOil addOil) {
        return addOilService.addAddOil(addOil);
    }

    @PutMapping("/add-oil-update")
    public Integer updateAddOil(@RequestBody AddOil addOil) {
        return addOilService.updateAddOil(addOil);
    }

    @DeleteMapping("/add-oil-remove/id/{id}/{cid}")
    public Integer removeAddOil(@PathVariable("id") Long id,
                                @PathVariable("cid") Long cid) {
        return addOilService.removeAddOil(id, cid);
    }

    @DeleteMapping("/add-oil-remove/cid/{cid}")
    public Integer removeAddOilByCid(@PathVariable("cid") Long cid) {
        return addOilService.removeAddOilByCid(cid);
    }

    @GetMapping("/add-oil-query/mile/{cid}")
    public Double queryMaxMileByCid(@PathVariable("cid") Long cid) {
        return addOilService.queryMileByCid(cid);
    }
}
