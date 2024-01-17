package xin.admin.service.fundsFlowManagement;

import xin.admin.controller.fundsFlowManagement.push.ZFInformPush;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.CommercialTenantOrderZF;
import xin.admin.domain.fundsFlowManagement.query.CommercialTenantOrderZFRequestQuery;

public interface CommercialTenantOrderZFService {
    ResponseResult add(ZFInformPush<CommercialTenantOrderZF> commercialTenantOrderZFPush);

    ResponseResult<CommercialTenantOrderZFRequestQuery> list(CommercialTenantOrderZFRequestQuery query);

}
