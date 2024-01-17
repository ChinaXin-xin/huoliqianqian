package xin.admin.service.membershipManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;
import xin.admin.domain.membershipManagement.query.UserAuthenticationInfoRequestQuery;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.membershipManagement.UserAuthenticationInfoMapper;
import xin.admin.service.membershipManagement.UserAuthenticationInfoService;
import xin.common.domain.User;

import java.util.Date;
import java.util.List;

@Service
public class UserAuthenticationInfoServiceImpl implements UserAuthenticationInfoService {

    @Autowired
    private UserAuthenticationInfoMapper userAuthenticationInfoMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public ResponseResult<UserAuthenticationInfoRequestQuery> list(UserAuthenticationInfoRequestQuery query) {
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
        List<UserAuthenticationInfo> userTransactions = userAuthenticationInfoMapper.list(
                query.getUserAuthenticationInfo(), offset, quantity);
        query.setResultList(userTransactions);
        if (query.getStartTime() == null || query.getEndTime() == null || query.getResultList() == null) {
            query.setCount(userAuthenticationInfoMapper.listCount(
                    query.getUserAuthenticationInfo(), offset, quantity));
        }
        // 封装到ResponseResult并返回
        return new ResponseResult<>(200, "查询成功", query);
    }

    @Override
    public ResponseResult alter(UserAuthenticationInfo userAuthenticationInfo) {
        userAuthenticationInfoMapper.updateById(userAuthenticationInfo);
        return new ResponseResult(200, "修改成功！");
    }

    @Override
    public ResponseResult delete(Integer id) {
        userAuthenticationInfoMapper.deleteById(id);
        return new ResponseResult(200, "删除成功！");
    }

    /**
     * 提交用户认证信息，从token中读取用户信息，再让认证信息的绑定到用户表中
     *
     * @param userAuthenticationInfo
     * @return
     */
    @Override
    public ResponseResult add(UserAuthenticationInfo userAuthenticationInfo) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (userAuthenticationInfoMapper.selectByUid(curUser.getId()) != null) {
            UserAuthenticationInfo uai = userAuthenticationInfoMapper.selectByUid(curUser.getId());
            if (uai.getStatus() == 0) {
                return new ResponseResult(200, "认证审核中！");
            }

            if (uai.getStatus() == 1) {
                return new ResponseResult(200, "您已通过审核，不能再次实名认证！");
            }
        }

        // 移除字符串字段中的所有空格
        if (userAuthenticationInfo.getMemberPhone() != null) {
            userAuthenticationInfo.setMemberPhone(userAuthenticationInfo.getMemberPhone().replace(" ", ""));
        }
        if (userAuthenticationInfo.getMemberName() != null) {
            userAuthenticationInfo.setMemberName(userAuthenticationInfo.getMemberName().replace(" ", ""));
        }
        if (userAuthenticationInfo.getIdCardNumber() != null) {
            userAuthenticationInfo.setIdCardNumber(userAuthenticationInfo.getIdCardNumber().replace(" ", ""));
        }
        if (userAuthenticationInfo.getIdCardFront() != null) {
            userAuthenticationInfo.setIdCardFront(userAuthenticationInfo.getIdCardFront().replace(" ", ""));
        }
        if (userAuthenticationInfo.getIdCardBack() != null) {
            userAuthenticationInfo.setIdCardBack(userAuthenticationInfo.getIdCardBack().replace(" ", ""));
        }
        if (userAuthenticationInfo.getIdCardHandheld() != null) {
            userAuthenticationInfo.setIdCardHandheld(userAuthenticationInfo.getIdCardHandheld().replace(" ", ""));
        }
        if (userAuthenticationInfo.getBankCard() != null) {
            userAuthenticationInfo.setBankCard(userAuthenticationInfo.getBankCard().replace(" ", ""));
        }
        if (userAuthenticationInfo.getBankBranch() != null) {
            userAuthenticationInfo.setBankBranch(userAuthenticationInfo.getBankBranch().replace(" ", ""));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        userAuthenticationInfo.setUid(loginUser.getUser().getId());
        userAuthenticationInfo.setApplicationDate(new Date());
        userAuthenticationInfo.setStatus(0);
        userAuthenticationInfoMapper.insert(userAuthenticationInfo);
        return new ResponseResult(200, "提交成功！！");
    }
}
