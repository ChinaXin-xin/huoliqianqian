package xin.admin.controller.contentManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.contentManagement.SysMsg;
import xin.admin.domain.contentManagement.query.SysMsgRequestQuery;
import xin.admin.service.contentManagement.SysMsgService;

@RestController
@RequestMapping("/admin/sysMsg")
public class SysMsgController {
    @Autowired
    SysMsgService sysMsgService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody SysMsg sysMsg) {
        return sysMsgService.add(sysMsg);
    }

    @PostMapping("/alter")
    public ResponseResult alter(@RequestBody  SysMsg sysMsg) {
        return sysMsgService.alter(sysMsg);
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody SysMsgRequestQuery query) {
        return sysMsgService.list(query);
    }

    @PostMapping("/delete/{id}")
    public ResponseResult list(@PathVariable Integer id) {
        return sysMsgService.delete(id);
    }
}
