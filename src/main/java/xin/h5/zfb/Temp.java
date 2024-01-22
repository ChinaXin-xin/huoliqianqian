package xin.h5.zfb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Temp {

    @RequestMapping("/admin/hi")
    public String sayHello() {
        return "index";
    }
}