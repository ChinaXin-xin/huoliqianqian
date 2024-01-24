package xin.h5.zfb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Temp {

    @RequestMapping("/admin/hi")
    public String sayHello() {
        return "index";
    }

    @RequestMapping("/admin/h2")
    public String sayHello2() {
        return "index2";
    }
}