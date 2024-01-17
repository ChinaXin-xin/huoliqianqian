package xin.weixin.service.myselfInfo.authentication.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.mapper.UserMapper;
import xin.common.domain.CommonalityQuery;
import xin.common.domain.User;
import xin.weixin.domain.myselfInfo.authentication.WxAuthenticationMsg;
import xin.weixin.mapper.myselfInfo.authentication.WxAuthenticationMsgMapper;
import xin.weixin.service.myselfInfo.authentication.WxAuthenticationMsgService;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class WxAuthenticationMsgServiceImpl extends ServiceImpl<WxAuthenticationMsgMapper, WxAuthenticationMsg> implements WxAuthenticationMsgService {

    @Autowired
    UserMapper userMapper;

    @Override
    public ResponseResult<CommonalityQuery<WxAuthenticationMsg>> mySelectList(CommonalityQuery<WxAuthenticationMsg> query) {
        if (query == null) {
            return new ResponseResult<>(400, "请求参数不能为空", null);
        }

        WxAuthenticationMsg queryConditions = query.getQuery();
        QueryWrapper<WxAuthenticationMsg> queryWrapper = new QueryWrapper<>();

        // 只有当queryConditions不为null时才进行处理
        if (queryConditions != null) {
            // 反射遍历所有字段
            Field[] fields = queryConditions.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    // 使用反射访问 queryConditions 对象的私有字段
                    field.setAccessible(true);  // 设置字段可访问，忽略访问权限控制

                    // 获取当前 queryConditions 对象的字段值
                    Object value = field.get(queryConditions);

                    if (value != null) {
                        // 如果字段是字符串类型，则进行模糊查询
                        if (value instanceof String) {
                            queryWrapper.like(field.getName(), value);
                        } else {
                            // 其他类型字段进行精确查询
                            queryWrapper.eq(field.getName(), value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    return new ResponseResult<>(400, "查询失败！");
                }
            }
        }

        Page<WxAuthenticationMsg> page = new Page<>(query.getPageNumber(), query.getQuantity());
        Page<WxAuthenticationMsg> resultPage = this.page(page, queryWrapper);
        List<WxAuthenticationMsg> resultList = resultPage.getRecords();
        query.setResultList(resultList);
        query.setCount(resultPage.getTotal());

        // 封装到ResponseResult并返回
        return new ResponseResult<>(200, "查询成功", query);
    }

    @Override
    public ResponseResult myAlter(WxAuthenticationMsg wxAuthenticationMsg) {
        return new ResponseResult(200, "修改成功！");
    }

    @Override
    public ResponseResult myDelete(Integer id) {

        return new ResponseResult(200, "删除成功！");
    }

    /**
     * 提交用户认证信息，从token中读取用户信息，再让认证信息的绑定到用户表中
     *
     * @param wxAuthenticationMsg
     * @return
     */
    @Override
    public ResponseResult myAdd(WxAuthenticationMsg wxAuthenticationMsg) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (curUser.getIsAuthentication() != null && curUser.getIsAuthentication()) {
            return new ResponseResult(400, "提交失败，您已经实名！");
        }

        // 移除字符串字段中的所有空格
        if (wxAuthenticationMsg.getMemberPhone() != null) {
            wxAuthenticationMsg.setMemberPhone(wxAuthenticationMsg.getMemberPhone().replace(" ", ""));
        }
        if (wxAuthenticationMsg.getMemberName() != null) {
            wxAuthenticationMsg.setMemberName(wxAuthenticationMsg.getMemberName().replace(" ", ""));
        }
        if (wxAuthenticationMsg.getIdCardNumber() != null) {
            wxAuthenticationMsg.setIdCardNumber(wxAuthenticationMsg.getIdCardNumber().replace(" ", ""));
        }
        if (wxAuthenticationMsg.getIdCardFront() != null) {
            wxAuthenticationMsg.setIdCardFront(wxAuthenticationMsg.getIdCardFront().replace(" ", ""));
        }
        if (wxAuthenticationMsg.getIdCardBack() != null) {
            wxAuthenticationMsg.setIdCardBack(wxAuthenticationMsg.getIdCardBack().replace(" ", ""));
        }
        if (wxAuthenticationMsg.getIdCardHandheld() != null) {
            wxAuthenticationMsg.setIdCardHandheld(wxAuthenticationMsg.getIdCardHandheld().replace(" ", ""));
        }
        if (wxAuthenticationMsg.getBankCard() != null) {
            wxAuthenticationMsg.setBankCard(wxAuthenticationMsg.getBankCard().replace(" ", ""));
        }
        if (wxAuthenticationMsg.getBankBranch() != null) {
            wxAuthenticationMsg.setBankBranch(wxAuthenticationMsg.getBankBranch().replace(" ", ""));
        }

        wxAuthenticationMsg.setUid(curUser.getId());
        wxAuthenticationMsg.setApplicationDate(new Date());
        wxAuthenticationMsg.setStatus(1);

        // 状态设置为已实名
        curUser.setIsAuthentication(true);
        userMapper.updateById(curUser);

        this.baseMapper.insert(wxAuthenticationMsg);
        return new ResponseResult(200, "提交成功！");
    }
}
