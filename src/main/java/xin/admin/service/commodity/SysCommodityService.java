package xin.admin.service.commodity;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.commodity.SysCommodity;
import xin.admin.domain.commodity.jquery.SysCommodityRequestQuery;

import java.util.List;

public interface SysCommodityService {

    ResponseResult add(SysCommodity sysCommodity);

    ResponseResult<List<SysCommodity>> listAll();
    ResponseResult<SysCommodityRequestQuery> list(SysCommodityRequestQuery query);
}
