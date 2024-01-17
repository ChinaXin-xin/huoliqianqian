package xin.admin.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xin.common.enmu.SmsType;
import xin.common.utils.SecurityCodeUtilsUtils;
import xin.h5.exception.SmsSendingFailedException;

import java.util.Scanner;

@SpringBootTest
public class MapperTest {

    @Autowired
    SecurityCodeUtilsUtils securityCodeUtilsUtils;

    @Test
    public void TestUserMapper() throws SmsSendingFailedException {
        securityCodeUtilsUtils.generate("13223950610", SmsType.LOGIN_CONFIRMATION);
        System.out.println("------------------");
        Scanner scanner = new Scanner(System.in);
        //System.out.println("结果：" + securityCodeUtilsUtils.codeAndMobileExist("13223950610", phone, SmsType.LOGIN_CONFIRMATION));
    }
}
