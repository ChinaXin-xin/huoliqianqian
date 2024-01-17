package xin.admin.controller.commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.commodity.SysCommodity;
import xin.admin.service.commodity.SysCommodityService;

import java.util.List;

@RestController
@RequestMapping("/admin/SysCommodity")
public class SysCommodityController {

    @Autowired
    SysCommodityService sysCommodityService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody SysCommodity sysCommodity) {
        return sysCommodityService.add(sysCommodity);
    }

    @PostMapping("/listAll")
    public ResponseResult<List<SysCommodity>> listAll() {
        return sysCommodityService.listAll();
    }
}
