package xin.admin.mapper.membershipManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xin.admin.domain.membershipManagement.UserWithdrawManagement;
import xin.admin.domain.membershipManagement.UserWithdrawType;

import java.util.List;

@Mapper
public interface UserWithdrawTypeMapper extends BaseMapper<UserWithdrawType> {
    List<UserWithdrawManagement> selectAll();
}
