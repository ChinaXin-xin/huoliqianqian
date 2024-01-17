package xin.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xin.admin.mapper.other.SysPosTypeMapper;

@SpringBootTest
public class SpringbootTest {

    @Autowired
    SysPosTypeMapper sysPosTypeMapper;

    @Test
    public void testExistsByRrn() {
        System.out.println(sysPosTypeMapper.selectList(null));
    }
}
