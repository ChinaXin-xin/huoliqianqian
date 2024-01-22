package xin.h5.zfb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	

	/**
	 * 返回到主页
	 * @return
	 */
	@RequestMapping("/toIndex")
	public String toIndex() {
		return "支付宝下单index";
	}


}
