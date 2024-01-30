package xin.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysFeeDeductionRecord;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.mapper.fundsFlowManagement.SysFeeDeductionRecordMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.service.fundsFlowManagement.impl.SysFeeDeductionRecordServiceImpl;
import xin.zhongFu.model.resp.MerchActivityAmtResp;
import xin.zhongFu.model.resp.MerchActivityQueryResp;

@SpringBootTest
public class SpringbootTest {

    @Autowired
    SysFeeDeductionRecordMapper sysFeeDeductionRecordMapper;

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Test
    public ResponseResult testExistsByRrn() {

        QueryWrapper<SysPosTerminal> sptQw = new QueryWrapper<>();

        SysFeeDeductionRecord record = sysFeeDeductionRecordMapper.queryRecentlyRecord("00005702883072010418");

        sptQw.eq("machine_no", "00005702883072010418");
        SysPosTerminal spt = sysPosTerminalMapper.selectOne(sptQw);

        MerchActivityQueryResp merchActivityQueryResp = SysFeeDeductionRecordServiceImpl.queryPayOrNot(spt);
/*        if (merchActivityQueryResp == null) {
            return new ResponseResult(400, "设置错误，未查询到历史记录！");
        }*/

        for (MerchActivityAmtResp maa : merchActivityQueryResp.getAmtList()) {

            // 判断流水号与操作号是否相同
            if (record.getSerialNumber().equals(maa.getTraceNo())
                    && record.getOperatorNumber().equals(maa.getOptNo())) {
                // 已经缴费了
                if (maa.getMerchPayStatus().equals("0")) {
                    return new ResponseResult(400, "该用户上一笔未付款");
                }
            }
        }

        System.out.println("");
        return null;
    }
}
