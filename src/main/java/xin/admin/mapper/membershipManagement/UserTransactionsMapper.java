package xin.admin.mapper.membershipManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xin.admin.domain.membershipManagement.UserTransactions;

import java.util.List;

@Mapper
public interface UserTransactionsMapper extends BaseMapper<UserTransactions> {
    List<UserTransactions> selectPaging(@Param("userTransactions")  UserTransactions  userTransactions,
                                        @Param("offset") Integer offset,
                                        @Param("quantity") Integer quantity);
    Integer selectPagingCount(@Param("userTransactions")  UserTransactions  userTransactions,
                                        @Param("offset") Integer offset,
                                        @Param("quantity") Integer quantity);
}
