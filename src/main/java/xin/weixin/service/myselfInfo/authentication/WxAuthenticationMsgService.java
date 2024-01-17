package xin.weixin.service.myselfInfo.authentication;

import com.baomidou.mybatisplus.extension.service.IService;
import xin.admin.domain.ResponseResult;
import xin.common.domain.CommonalityQuery;
import xin.weixin.domain.myselfInfo.authentication.WxAuthenticationMsg;

public interface WxAuthenticationMsgService extends IService<WxAuthenticationMsg> {
    ResponseResult<CommonalityQuery<WxAuthenticationMsg>> mySelectList(CommonalityQuery<WxAuthenticationMsg> query);

    ResponseResult myAlter(WxAuthenticationMsg wxAuthenticationMsg);

    ResponseResult myDelete(Integer id);

    ResponseResult myAdd(WxAuthenticationMsg wxAuthenticationMsg);
}