package xin.admin.service.contentManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.contentManagement.SysBanner;
import xin.admin.domain.contentManagement.query.SysBannerRequestQuery;

public interface SysBannerService {
    ResponseResult add(SysBanner sysBanner);

    ResponseResult delete(Integer id);

    ResponseResult<SysBannerRequestQuery> list(SysBannerRequestQuery query);

    ResponseResult alter(SysBanner sysBanner);

    //h5用户查询轮播图
    ResponseResult userList();
}
