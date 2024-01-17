package xin.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class HelloController {
    @PostMapping("/hello")
    @PreAuthorize("hasAuthority('system:test:list')")
    public String Hello() {
        return "hello";
    }

    @PostMapping("/ping/{id}")
    public String ping(@PathVariable String id) {
        return "ping" + id;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('system:admin:list')")
    public String test() {
        return "admin";
    }
}
