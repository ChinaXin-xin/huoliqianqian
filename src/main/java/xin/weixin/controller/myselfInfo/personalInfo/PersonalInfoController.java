package xin.weixin.controller.myselfInfo.personalInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.common.domain.User;
import xin.weixin.service.myselfInfo.personalInfo.PersonalInfoService;

@RestController
@RequestMapping("/pro/personalInfo")
public class PersonalInfoController {

    @Autowired
    PersonalInfoService personalInfoService;

    @PostMapping("/getMyselfInfo")
    public ResponseResult<User> getMyselfInfo(){
        return personalInfoService.getMyselfInfo();
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody User user){
        return personalInfoService.updateInfo(user);
    }
}
