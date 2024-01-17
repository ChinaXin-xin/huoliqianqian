package xin.admin.service.contentManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.contentManagement.SysBanner;
import xin.admin.domain.contentManagement.query.SysBannerRequestQuery;
import xin.admin.mapper.contentManagement.SysBannerMapper;
import xin.admin.service.contentManagement.SysBannerService;

import java.util.List;

@Service
public class SysBannerServiceImpl implements SysBannerService {

    @Autowired
    SysBannerMapper sysBannerMapper;

    @Override
    public ResponseResult add(SysBanner sysBanner) {
        sysBannerMapper.insert(sysBanner);
        return new ResponseResult(200, "添加成功");
    }

    @Override
    public ResponseResult delete(Integer id) {
        sysBannerMapper.deleteById(id);
        return new ResponseResult(200, "删除成功");
    }

    @Override
    public ResponseResult<SysBannerRequestQuery> list(SysBannerRequestQuery query) {

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
        List<SysBanner> usd = sysBannerMapper.selectPaging(
                query.getSysBanner(), offset, quantity);
        query.setResultList(usd);
        query.setCount(sysBannerMapper.selectPagingCount(
                query.getSysBanner(), offset, quantity));
        // 封装到ResponseResult并返回
        return new ResponseResult<>(200, "查询成功", query);
    }

    @Override
    public ResponseResult alter(SysBanner sysBanner) {
        sysBannerMapper.updateById(sysBanner);
        return new ResponseResult(200, "修改成功");
    }

    //H5用户查询轮播图
    @Override
    public ResponseResult userList() {
        List<SysBanner> sysBanners = sysBannerMapper.userList();
        return new ResponseResult(200, "查询成功！", sysBanners);
    }
}
