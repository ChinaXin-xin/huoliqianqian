package xin.admin.service.membershipManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserScoreDetail;
import xin.admin.domain.membershipManagement.query.UserScoreDetailRequestQuery;
import xin.admin.mapper.membershipManagement.UserScoreDetailMapper;
import xin.admin.service.membershipManagement.UserScoreDetailService;

import java.util.List;

@Service
public class UserScoreDetailServiceImpl implements UserScoreDetailService {

    @Autowired
    UserScoreDetailMapper userScoreDetailMapper;

    @Override
    public ResponseResult<UserScoreDetailRequestQuery> selectUserScoreDetailPage(UserScoreDetailRequestQuery query) {
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
        List<UserScoreDetail> userScoreDetails = userScoreDetailMapper.queryUserScoreDetailMsgList(
                query.getUserScoreDetail(), offset, quantity, query.getUserScoreDetailId());
        query.setResultList(userScoreDetails);
        if (query.getStartTime() == null || query.getEndTime() == null/* || query.getResultList() == null*/) {
            query.setCount(userScoreDetailMapper.queryUserScoreDetailMsgListCount(
                    query.getUserScoreDetail(), offset, quantity, query.getUserScoreDetailId()));
        }
        // 封装到ResponseResult并返回
        return new ResponseResult<>(200, "查询成功", query);
    }


    @Override
    public ResponseResult add(UserScoreDetail userScoreDetail) {
        userScoreDetailMapper.insert(userScoreDetail);
        return new ResponseResult<>(200, "添加成功");
    }
}
