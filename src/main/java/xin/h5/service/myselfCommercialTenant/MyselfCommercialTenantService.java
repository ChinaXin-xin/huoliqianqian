package xin.h5.service.myselfCommercialTenant;

import org.springframework.web.bind.annotation.RequestBody;
import xin.admin.domain.ResponseResult;
import xin.h5.domain.myselfCommercialTenant.query.DealAndPosMachineMsgRequestQuery;
import xin.h5.domain.myselfMachine.MerchantDetails;

public interface MyselfCommercialTenantService {
    /**
     * 我的商户->本月交易额，累计商户中显示的列表
     * @return
     */
    ResponseResult<DealAndPosMachineMsgRequestQuery> thisMonthDealMoney(DealAndPosMachineMsgRequestQuery query);

    /**
     * 我的商户->本月交易额->点击后的商户详情
     * 根据商户号，查询商户详情
     * @return
     */
    ResponseResult<MerchantDetails> queryMerchantDetails(@RequestBody String merchantId);
}
