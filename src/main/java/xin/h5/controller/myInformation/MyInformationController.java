package xin.h5.controller.myInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.common.domain.User;
import xin.h5.domain.myInformaation.PersonalInformation;
import xin.h5.service.myInformaation.MyInformationService;

/**
 * 我的信息查询接口
 */
@RestController
@RequestMapping("/h5/myInformation")
public class MyInformationController {

    @Autowired
    MyInformationService myInformationService;

    @PostMapping("/getInfo")
    public ResponseResult<PersonalInformation> getInfo() {
        return myInformationService.getInfo();
    }

    @PostMapping("/signIn")
    public ResponseResult signIn() {
        return myInformationService.signIn();
    }

    @PostMapping("/getMyselfBonus")
    public ResponseResult<User> getMyselfBonus() {
        return myInformationService.getMyselfBonus();
    }
}
