package xin.admin.controller.fundsFlowManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.controller.fundsFlowManagement.push.ZFInformPush;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.CommercialTenantOrderZF;
import xin.admin.domain.fundsFlowManagement.query.CommercialTenantOrderZFRequestQuery;
import xin.admin.service.fundsFlowManagement.CommercialTenantOrderZFService;

@RestController
@RequestMapping("/admin/CommercialTenantOrderZF")
public class CommercialTenantOrderZFController {

    @Autowired
    CommercialTenantOrderZFService commercialTenantOrderZFService;

    /**
     * 中付推送到这个接口上，对应文档：服务商交易通知API文档-V1->机构交易流水通知内容
     * @param commercialTenantOrderZFPush，中付推送的：”机构交易流水流水通知“
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody ZFInformPush<CommercialTenantOrderZF> commercialTenantOrderZFPush) {
        return commercialTenantOrderZFService.add(commercialTenantOrderZFPush);
    }


    @PostMapping("/list")
    public ResponseResult<CommercialTenantOrderZFRequestQuery>  list(@RequestBody CommercialTenantOrderZFRequestQuery query) {
        return commercialTenantOrderZFService.list(query);
    }
}
