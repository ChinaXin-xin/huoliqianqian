package xin.admin.service.membershipManagement.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.UserLevel;
import xin.admin.domain.membershipManagement.query.UserLevelRequestQuery;
import xin.admin.mapper.membershipManagement.UserLevelMapper;
import xin.admin.service.membershipManagement.UserLevelService;

import java.util.Collections;
import java.util.List;

@Service
public class UserLevelServiceImpl implements UserLevelService {

    @Autowired
    private UserLevelMapper userLevelMapper;

    @Override
    public void addUserLevel(UserLevel userLevel) {
        userLevel.setUpgradeVolume(userLevel.getUpgradeVolume() * 1000000);
        userLevel.setProfitSplit(userLevel.getProfitSplit() * 100);
        userLevelMapper.insert(userLevel);
    }

    @Override
    public void deleteUserLevelById(Long id) {
        userLevelMapper.deleteById(id);
    }

    @Override
    public UserLevelRequestQuery getAllUserLevels(UserLevelRequestQuery userLevelRequestQuery) {

        //如果要查询单个用户等级信息
        if (userLevelRequestQuery.getQueryUserLevelId() != null) {
            UserLevel userLevel = userLevelMapper.selectById(userLevelRequestQuery.getQueryUserLevelId());
            if (userLevel == null) {
                userLevelRequestQuery.setCount(-1);
            } else {
                userLevel.setUpgradeVolume(userLevel.getUpgradeVolume() / 1000000);
                userLevel.setProfitSplit(userLevel.getProfitSplit() / 100);
            }
            userLevelRequestQuery.setCount(1);
            userLevelRequestQuery.setUserList(Collections.singletonList(userLevel));
            return userLevelRequestQuery;
        }
        System.out.println(userLevelRequestQuery);
        // 创建分页对象
        IPage<UserLevel> page = new Page<>(userLevelRequestQuery.getPageNumber(), userLevelRequestQuery.getQuantity());

        // 执行分页查询
        userLevelMapper.selectPage(page, null);

        // 获取分页查询结果
        List<UserLevel> userLevels = page.getRecords();

        for (UserLevel userLevel : userLevels) {
            userLevel.setUpgradeVolume(userLevel.getUpgradeVolume() / 1000000);
            userLevel.setProfitSplit(userLevel.getProfitSplit() / 100);
        }

        // 获取总记录数并转换为 int 类型
        int totalCount = (int) page.getTotal();

        // 设置总记录数到 userLevelRequestQuery 对象中
        userLevelRequestQuery.setCount(totalCount);
        userLevelRequestQuery.setUserList(userLevels);

        // 返回 userLevelRequestQuery 对象
        return userLevelRequestQuery;
    }


    @Override
    public void updateUserLevel(UserLevel userLevel) {
        userLevel.setUpgradeVolume(userLevel.getUpgradeVolume() * 1000000);
        userLevel.setProfitSplit(userLevel.getProfitSplit() * 100);
        userLevelMapper.updateById(userLevel);
    }
}
