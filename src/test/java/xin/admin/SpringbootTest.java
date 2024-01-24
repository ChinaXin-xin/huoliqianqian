package xin.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xin.admin.mapper.fundsFlowManagement.CommercialTenantOrderZFMapper;

@SpringBootTest
public class SpringbootTest {

    @Autowired
    CommercialTenantOrderZFMapper commercialTenantOrderZFMapper;

    @Test
    public void testExistsByRrn() {
        System.out.println(commercialTenantOrderZFMapper.sumAmountToday());
    }
}
