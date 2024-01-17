package xin.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xin.common.domain.CommonPrefix;
import xin.common.utils.RedisCache;

import java.util.Collection;

@SpringBootTest
class SecurityQuickStartApplicationTests {

    @Autowired
    RedisCache redisCache;

    @Test
    void contextLoads() {
        Collection<String> keys = redisCache.keys(CommonPrefix.wxOrderFormPrefix_);
        for (String key : keys) {
            System.out.println(key);
        }
    }
}
