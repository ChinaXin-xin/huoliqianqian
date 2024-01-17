package xin.admin.controller.contentManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.contentManagement.SysBanner;
import xin.admin.domain.contentManagement.query.SysBannerRequestQuery;
import xin.admin.service.contentManagement.SysBannerService;

@RestController
@RequestMapping("/admin/sysBanner")
public class SysBannerController {

    @Autowired
    SysBannerService sysBannerService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody SysBanner sysBanner) {
        return sysBannerService.add(sysBanner);
    }

    @PostMapping("/alter")
    public ResponseResult alter(@RequestBody  SysBanner sysBanner) {
        return sysBannerService.alter(sysBanner);
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody SysBannerRequestQuery query) {
        return sysBannerService.list(query);
    }

    @PostMapping("/delete/{id}")
    public ResponseResult list(@PathVariable Integer id) {
        return sysBannerService.delete(id);
    }
}
