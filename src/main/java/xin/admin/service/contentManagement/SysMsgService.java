package xin.admin.service.contentManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.contentManagement.SysMsg;
import xin.admin.domain.contentManagement.query.SysMsgRequestQuery;

public interface SysMsgService {
    ResponseResult add(SysMsg sysMsg);
    ResponseResult delete(Integer id);
    ResponseResult<SysMsgRequestQuery> list(SysMsgRequestQuery query);
    ResponseResult alter(SysMsg sysMsg);

    //h5用户查询系统公告，不用token
    ResponseResult userList();
}
