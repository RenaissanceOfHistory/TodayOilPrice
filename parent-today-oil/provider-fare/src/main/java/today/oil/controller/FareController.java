package today.oil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import today.oil.model.Fare;
import today.oil.service.FareService;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: FareController
 */
@RestController
public class FareController {

    @Autowired
    private FareService fareService;

    @GetMapping("/fare-query/cid/{cid}")
    public List<Fare> queryFareByCid(@PathVariable("cid") Long cid) {
        return fareService.queryByCid(cid);
    }

    @PostMapping("/fare-add")
    public Integer addFare(@RequestBody Fare fare) {
        return fareService.addFare(fare);
    }

    @PutMapping("/fare-update")
    public Integer updateFare(@RequestBody Fare fare) {
        return fareService.updateFare(fare);
    }

    @DeleteMapping("/fare-remove/id/{id}/{cid}")
    public Integer removeFare(@PathVariable("id") Long id,
                              @PathVariable("cid") Long cid) {
        return fareService.removeFare(id, cid);
    }

    @DeleteMapping("/fare-remove/cid/{cid}")
    public Integer removeFareByCid(@PathVariable("cid") Long cid) {
        return fareService.removeFareByCid(cid);
    }
}
