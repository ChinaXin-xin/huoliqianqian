package xin.h5.service.myAlly;

import org.springframework.web.bind.annotation.RequestBody;
import xin.admin.domain.ResponseResult;
import xin.h5.domain.myselfCommercialTenant.DealAndPosMachineMsg;
import xin.h5.domain.myselfCommercialTenant.MyAllyMsg;
import xin.h5.domain.myselfCommercialTenant.query.DealAndPosMachineMsgRequestQuery;
import xin.h5.domain.myselfCommercialTenant.query.MyAllyMsgRequestQuery;

public interface MyAllyService {
    /**
     * 查询我的盟友列表
     * @param query
     * @return
     */
    ResponseResult<MyAllyMsgRequestQuery> queryMyAlly(MyAllyMsgRequestQuery query);

    ResponseResult<MyAllyMsg> queryMyAllyByUserName(String allyUserName);
}
