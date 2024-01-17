package xin.admin.controller.membershipManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserDealType;
import xin.admin.service.membershipManagement.UserDealTypeService;

import java.util.List;

@RestController
@RequestMapping("/admin/deal")
public class UserDealTypeController {

    @Autowired
    private UserDealTypeService userDealTypeService;

    @PostMapping("/list")
    public ResponseResult<List<UserDealType>> selectAll() {
        return new ResponseResult<>(200, "查询成功", userDealTypeService.selectAll());
    }
}
