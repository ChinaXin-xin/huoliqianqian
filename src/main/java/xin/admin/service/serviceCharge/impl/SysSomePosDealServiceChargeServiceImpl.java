package xin.admin.service.serviceCharge.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.query.SysPosTerminalRequestQuery;
import xin.admin.domain.serviceCharge.SysPosDealServiceCharge;
import xin.admin.domain.serviceCharge.SysSomePosDealServiceCharge;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.mapper.serviceCharge.SysPosDealServiceChargeMapper;
import xin.admin.mapper.serviceCharge.SysSomePosDealServiceChargeMapper;
import xin.admin.service.fundsFlowManagement.PosTransferRecordService;
import xin.admin.service.serviceCharge.SysSomePosDealServiceChargeService;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysSomePosDealServiceChargeServiceImpl extends ServiceImpl<SysSomePosDealServiceChargeMapper, SysSomePosDealServiceCharge>
        implements SysSomePosDealServiceChargeService {

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    PosTransferRecordService posTransferRecordService;

    @Autowired
    SysPosDealServiceChargeMapper sysPosDealServiceChargeMapper;

    @Autowired
    SysSomePosDealServiceChargeMapper sysSomePosDealServiceChargeMapper;

    @Override
    public ResponseResult<SysPosTerminalRequestQuery> selectSysSomePosDealServiceCharge(SysPosTerminalRequestQuery query) {
        Page<SysPosTerminal> page = new Page<>(query.getPageNumber(), query.getQuantity()); // 第1页，每页显示10条数据
        Page<SysPosTerminal> resultList = sysPosTerminalMapper.selectSysPosTerminalIsActivate(page, query.getSysPosTerminal());
        query.setResultList(resultList.getRecords());

        // 判断是否是白名单
        for (SysPosTerminal spt : query.getResultList()) {
            boolean isWhiteList = true;

            List<SysPosDealServiceCharge> sysPosDealServiceChargeList = sysPosDealServiceChargeMapper.selectList(null);

            // 判断一下是否是白名单， 如果所有费率为0，就是白名单的
            for (SysPosDealServiceCharge spdsc : sysPosDealServiceChargeList) {
                QueryWrapper<SysSomePosDealServiceCharge> qw = new QueryWrapper<>();
                qw.eq("pos_id", spt.getId());
                qw.eq("spdsc_id", spdsc.getId());
                SysSomePosDealServiceCharge res = sysSomePosDealServiceChargeMapper.selectOne(qw);
                if (res == null) {
                    isWhiteList = false;
                    break;
                }

                if (res.getMoney() != null && res.getMoney() != 0f) {
                    isWhiteList = false;
                    break;
                }
            }

            spt.setIsWhiteList(isWhiteList);
        }

        query.setCount((int) resultList.getTotal());
        return new ResponseResult<>(200, "查询成功！", query);
    }

    @Override
    public ResponseResult<SysPosTerminalRequestQuery> setIsWhiteListStatus(SysPosTerminal spt) {
        if (spt == null) {
            return new ResponseResult<>(400, "设置失败参数为空！");
        }

        List<SysPosDealServiceCharge> sysPosDealServiceChargeList = sysPosDealServiceChargeMapper.selectList(null);

        // 添加到白名单
        if (spt.getIsWhiteList()) {

            // 删除所有的服务费值
            for (SysPosDealServiceCharge spdsc : sysPosDealServiceChargeList) {
                QueryWrapper<SysSomePosDealServiceCharge> qw = new QueryWrapper<>();
                qw.eq("pos_id", spt.getId());
                qw.eq("spdsc_id", spdsc.getId());
                sysSomePosDealServiceChargeMapper.delete(qw);
            }

            // 如果所有费率为0，就是白名单的
            for (SysPosDealServiceCharge spdsc : sysPosDealServiceChargeList) {

                SysSomePosDealServiceCharge sysSomePosDealServiceCharge = new SysSomePosDealServiceCharge();
                sysSomePosDealServiceCharge.setPosId(spt.getId());
                sysSomePosDealServiceCharge.setSpdscId(spdsc.getId());
                sysSomePosDealServiceCharge.setMoney(0f);
                sysSomePosDealServiceChargeMapper.insert(sysSomePosDealServiceCharge);
            }
        } else {
            // 从白名单中删除
            for (SysPosDealServiceCharge spdsc : sysPosDealServiceChargeList) {
                QueryWrapper<SysSomePosDealServiceCharge> qw = new QueryWrapper<>();
                qw.eq("pos_id", spt.getId());
                qw.eq("spdsc_id", spdsc.getId());
                sysSomePosDealServiceChargeMapper.delete(qw);
            }
        }

        return new ResponseResult<>(200, "设置成功！");
    }

    // 参数为pos机的id
    @Override
    public ResponseResult<List<SysPosDealServiceCharge>> selectBySptId(Integer id) {

        ArrayList<SysPosDealServiceCharge> resultList = new ArrayList<>();

        List<SysPosDealServiceCharge> sysPosDealServiceChargeList = sysPosDealServiceChargeMapper.selectList(null);
        // 判断一下是否是白名单， 如果所有费率为0，就是白名单的
        for (SysPosDealServiceCharge spdsc : sysPosDealServiceChargeList) {

            // 返回对应的费率
            SysPosDealServiceCharge result = new SysPosDealServiceCharge();

            result.setStartInterval(spdsc.getStartInterval());
            result.setEndInterval(spdsc.getEndInterval());
            result.setId(spdsc.getId());
            result.setName(spdsc.getName());

            QueryWrapper<SysSomePosDealServiceCharge> qw = new QueryWrapper<>();
            qw.eq("pos_id", id);
            qw.eq("spdsc_id", spdsc.getId());
            SysSomePosDealServiceCharge res = sysSomePosDealServiceChargeMapper.selectOne(qw);
            if (res == null) {
                result.setFeePerTransaction(spdsc.getFeePerTransaction());
            } else {
                result.setFeePerTransaction(res.getMoney());
            }
            resultList.add(result);
        }
        return new ResponseResult<>(200, "查询成功！", resultList);
    }

    @Override
    public ResponseResult setServiceCharge(List<SysSomePosDealServiceCharge> list) {
        if (list == null) {
            return new ResponseResult<>(400, "设置失败参数为空！");
        }

        // 删除所有的服务费值

        for (SysSomePosDealServiceCharge ssp : list) {
            QueryWrapper<SysSomePosDealServiceCharge> qw = new QueryWrapper<>();
            qw.eq("pos_id", ssp.getPosId());
            qw.eq("spdsc_id", ssp.getSpdscId());
            if (sysSomePosDealServiceChargeMapper.update(ssp, qw) == 0) {
                sysSomePosDealServiceChargeMapper.insert(ssp);
            }
        }

        return new ResponseResult<>(200, "设置成功！");
    }

    @Override
    public ResponseResult selectBySomeServiceCharge(Integer id) {
        QueryWrapper<SysSomePosDealServiceCharge> qw = new QueryWrapper<>();
        qw.eq("pos_id", id);
        sysSomePosDealServiceChargeMapper.delete(qw);
        return new ResponseResult<>(200, "重置成功！");
    }
}
