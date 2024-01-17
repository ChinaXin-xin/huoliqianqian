package xin.h5.service.myInformaation;

import xin.admin.domain.ResponseResult;
import xin.common.domain.User;
import xin.h5.domain.myInformaation.PersonalInformation;

public interface MyInformationService {
    ResponseResult<PersonalInformation> getInfo();

    ResponseResult signIn();

    ResponseResult<User> getMyselfBonus();
}
