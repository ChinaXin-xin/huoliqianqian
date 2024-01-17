package xin.h5.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.service.contentManagement.SysBannerService;
import xin.admin.service.contentManagement.SysMsgService;

@RestController
@RequestMapping("/h5/common")
public class H5CommonResourcesController {

    @Autowired
    SysBannerService sysBannerService;

    @Autowired
    SysMsgService sysMsgService;

    @PostMapping("/sysBanner")
    public ResponseResult userSysBannerList() {
        return sysBannerService.userList();
    }

    @PostMapping("/sysMsg")
    public ResponseResult userSysMsgList() {
        return sysMsgService.userList();
    }
}
