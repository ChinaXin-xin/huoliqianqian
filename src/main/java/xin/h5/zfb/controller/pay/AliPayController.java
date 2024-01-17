package xin.h5.zfb.controller.pay;

import xin.common.utils.RedisCache;
import xin.h5.zfb.domain.SysH5Goods;
import xin.h5.zfb.entity.R;
import xin.h5.zfb.service.pay.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/h5/alipay")
public class AliPayController {

    @Autowired
    private AliPayService aliPayService;

    /**
     * alipay.trade.wap.pay：H5手机网站支付接口2.0（外部商户创建订单并支付）
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/alipayTradeWapPay")
    @ResponseBody
    public R alipayTradeWapPay(@RequestBody SysH5Goods sysH5Goods) {
    //public R alipayTradeWapPay(@RequestParam Map<String, Object> map) {
        System.out.println(sysH5Goods);
        return aliPayService.alipayTradeWapPay(sysH5Goods);
    }

    @RequestMapping(value = "/verification")
    @ResponseBody
    public String alipayTradeWapPay(@RequestBody String str) {
        System.out.println("前端传入：" + str);
        return "服务器收到" + str;
    }


    /**
     * alipay.trade.page.pay：统一收单下单并支付页面接口（PC场景下单并支付）
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/alipayTradePagePay")
    @ResponseBody
    public R alipayTradePagePay(@RequestParam Map<String, Object> map) {
        return aliPayService.alipayTradePagePay(map);
    }

}
