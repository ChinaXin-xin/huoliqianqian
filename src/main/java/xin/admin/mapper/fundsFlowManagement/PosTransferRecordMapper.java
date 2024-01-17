package xin.admin.mapper.fundsFlowManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xin.admin.domain.fundsFlowManagement.PosTransferRecord;

@Mapper
public interface PosTransferRecordMapper extends BaseMapper<PosTransferRecord> {
    Page<PosTransferRecord> selectPosTransferRecord(Page<PosTransferRecord> page, @Param("query") PosTransferRecord query);
    Integer selectPosTransferRecordCount(@Param("query") PosTransferRecord query);
}
