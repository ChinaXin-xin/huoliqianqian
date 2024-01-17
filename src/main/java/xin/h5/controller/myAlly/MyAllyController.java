package xin.h5.controller.myAlly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.h5.domain.myselfCommercialTenant.DealAndPosMachineMsg;
import xin.h5.domain.myselfCommercialTenant.MyAllyMsg;
import xin.h5.domain.myselfCommercialTenant.query.DealAndPosMachineMsgRequestQuery;
import xin.h5.domain.myselfCommercialTenant.query.MyAllyMsgRequestQuery;
import xin.h5.service.myAlly.MyAllyService;

/**
 * 我的盟友-》下属的接口
 */
@RestController
@RequestMapping("/h5/myAllyController")
public class MyAllyController {

    @Autowired
    MyAllyService myAllyService;

    /**
     * 查询我的盟友列表
     * @param query
     * @return
     */
    @PostMapping("/queryMyAlly")
    public ResponseResult<MyAllyMsgRequestQuery> queryMyAlly(@RequestBody MyAllyMsgRequestQuery query) {
        return myAllyService.queryMyAlly(query);
    }

    /**
     * 查询的我的盟友个人信息
     * @param allyUserName 要查询的盟友账户号
     * @return
     */
    @PostMapping("queryMyAllyByUserName")
    public ResponseResult<MyAllyMsg> queryMyAllyByUserName(@RequestBody String allyUserName) {
        return myAllyService.queryMyAllyByUserName(allyUserName);
    }

}
