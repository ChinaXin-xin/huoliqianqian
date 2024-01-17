package xin.h5.controller.myselfCommercialTenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.h5.domain.myselfCommercialTenant.query.DealAndPosMachineMsgRequestQuery;
import xin.h5.domain.myselfMachine.MerchantDetails;
import xin.h5.service.myselfCommercialTenant.MyselfCommercialTenantService;

/**
 * 我的商户功能
 */
@RestController
@RequestMapping("/h5/myselfCommercialTenant")
public class MyselfCommercialTenantController {

    @Autowired
    MyselfCommercialTenantService myselfCommercialTenantService;
    /**
     * 我的商户->本月交易额，累计商户中显示的列表
     * @return
     */
    @PostMapping("/thisMonthDealMoney")
    public ResponseResult<DealAndPosMachineMsgRequestQuery> thisMonthDealMoney(@RequestBody DealAndPosMachineMsgRequestQuery query) {
        return myselfCommercialTenantService.thisMonthDealMoney(query);
    }

    /**
     * 我的商户->本月交易额->点击后的商户详情
     * 根据商户号，查询商户详情
     * @return
     */
    @PostMapping("/queryMerchantDetails")
    public ResponseResult<MerchantDetails> queryMerchantDetails(@RequestBody String merchantId) {
        return myselfCommercialTenantService.queryMerchantDetails(merchantId);
    }

}
