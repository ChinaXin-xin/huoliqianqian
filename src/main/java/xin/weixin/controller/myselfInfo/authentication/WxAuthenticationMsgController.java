package xin.weixin.controller.myselfInfo.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.common.domain.CommonalityQuery;
import xin.weixin.domain.myselfInfo.authentication.WxAuthenticationMsg;
import xin.weixin.service.myselfInfo.authentication.WxAuthenticationMsgService;

/**
 * 微信小程序用户的实名信息请求处理
 */
@RestController
@RequestMapping("/pro/wxAuthenticationMsg")
public class WxAuthenticationMsgController {

    @Autowired
    WxAuthenticationMsgService wxAuthenticationMsgService;

    /**
     * 获取实名
     *
     * @return
     */
    @PostMapping("/list")
    public ResponseResult mySelectList(@RequestBody CommonalityQuery<WxAuthenticationMsg> query) {
        return wxAuthenticationMsgService.mySelectList(query);
    }

    /**
     * 添加实名认证
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody WxAuthenticationMsg wxAuthenticationMsg) {
        return wxAuthenticationMsgService.myAdd(wxAuthenticationMsg);
    }
}
