package xin.admin.service.earningsManagenment.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.UserScore;
import xin.admin.domain.earningsManagenment.query.UserScoreRequestQuery;
import xin.admin.mapper.earningsManagenment.UserScoreMapper;
import xin.admin.service.earningsManagenment.UserScoreService;

@Service
public class UserScoreServiceImpl implements UserScoreService {

    @Autowired
    UserScoreMapper userScoreMapper;

    @Override
    public ResponseResult<UserScoreRequestQuery> list(UserScoreRequestQuery query) {
        query.setCount(0);
        Page<UserScore> page = new Page<>(query.getPageNumber(), query.getQuantity()); // 第1页，每页显示10条数据
        Page<UserScore> result = userScoreMapper.selectUserScoreWithUserInfo(page, query.getUserScore(),query);
        query.setResultList(result.getRecords());
        query.setCount(userScoreMapper.selectUserScoreWithUserInfoCount(query.getUserScore(),query));
        return new ResponseResult<>(200, "查询成功！", query);
    }
}
