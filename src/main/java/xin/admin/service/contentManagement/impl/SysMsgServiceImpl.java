package xin.admin.service.contentManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.contentManagement.SysMsg;
import xin.admin.domain.contentManagement.query.SysMsgRequestQuery;
import xin.admin.mapper.contentManagement.SysMsgMapper;
import xin.admin.service.contentManagement.SysMsgService;

import java.util.List;

@Service
public class SysMsgServiceImpl implements SysMsgService {

    @Autowired
    SysMsgMapper sysMsgMapper;

    @Override
    public ResponseResult add(SysMsg sysMsg) {
        sysMsgMapper.insert(sysMsg);
        return new ResponseResult(200, "添加成功");
    }

    @Override
    public ResponseResult delete(Integer id) {
        sysMsgMapper.deleteById(id);
        return new ResponseResult(200, "删除成功");
    }

    @Override
    public ResponseResult<SysMsgRequestQuery> list(SysMsgRequestQuery query) {
        if (query == null) {
            return new ResponseResult<>(400, "请求参数不能为空", null);
        }

        // 设置默认的 pageNumber 和 quantity，例如 pageNumber=1，quantity=10
        Integer pageNumber = query.getPageNumber() == null ? 1 : query.getPageNumber();
        Integer quantity = query.getQuantity() == null ? 10 : query.getQuantity();

        // 修正 pageNumber 和 quantity 的值
        pageNumber = Math.max(pageNumber, 1);
        quantity = Math.max(quantity, 1);

        // 计算 OFFSET
        int offset = (pageNumber - 1) * quantity;

        System.out.println(query);
        List<SysMsg> usd = sysMsgMapper.selectPaging(
                query.getSysMsg(), offset, quantity);
        query.setResultList(usd);
        query.setCount(sysMsgMapper.selectPagingCount(
                query.getSysMsg(), offset, quantity));
        // 封装到ResponseResult并返回
        return new ResponseResult<>(200, "查询成功", query);
    }

    @Override
    public ResponseResult alter(SysMsg sysMsg) {
        sysMsgMapper.updateById(sysMsg);
        return new ResponseResult(200, "修改成功");
    }

    @Override
    public ResponseResult userList() {
        List<SysMsg> sysMsgList = sysMsgMapper.userList();
        return new ResponseResult(200, "查询成功！", sysMsgList);
    }
}
