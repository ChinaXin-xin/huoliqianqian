package xin.admin.network;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "myselfFeign",url = "http://localhost")
public interface MyFeignUtils {
    @PostMapping("/admin/ping/{id}")
    String findById(@PathVariable("id") String id);
}