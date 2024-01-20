package xin.admin.service.serviceCharge.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.serviceCharge.SysPosDealServiceCharge;
import xin.admin.domain.serviceCharge.SysSomePosDealServiceCharge;
import xin.admin.mapper.serviceCharge.SysPosDealServiceChargeMapper;
import xin.admin.mapper.serviceCharge.SysSomePosDealServiceChargeMapper;
import xin.admin.service.serviceCharge.SysPosDealServiceChargeService;

import java.util.List;

@Service
public class SysPosDealServiceChargeServiceImpl extends ServiceImpl<SysPosDealServiceChargeMapper, SysPosDealServiceCharge>
        implements SysPosDealServiceChargeService {


    @Autowired
    SysSomePosDealServiceChargeMapper sysSomePosDealServiceChargeMapper;

    @Transactional
    @Override
    public ResponseResult<Void> addSysPosDealServiceCharge(SysPosDealServiceCharge record) {

        if (record.getStartInterval() >= record.getEndInterval()) {
            return new ResponseResult<>(400, "添加失败，结束区间小于等于开始区间");
        }

        List<SysPosDealServiceCharge> list = this.list();

        for (SysPosDealServiceCharge existingRecord : list) {
            // 检查是否存在重叠的区间
            if (!(record.getEndInterval() < existingRecord.getStartInterval() ||
                    record.getStartInterval() > existingRecord.getEndInterval())) {
                return new ResponseResult<>(400, "添加失败，区间与已有区间重复");
            }
        }

        try {
            this.save(record);
            return new ResponseResult<>(200, "添加成功");
        } catch (Exception e) {
            return new ResponseResult<>(400, "添加失败");
        }
    }


    @Transactional
    @Override
    public ResponseResult<Void> deleteSysPosDealServiceCharge(Integer id) {
        try {

            // 删除之前先把绑定与这个阶段费率的pos机自定义费率删掉
            QueryWrapper<SysSomePosDealServiceCharge> qw = new QueryWrapper<>();
            qw.eq("spdsc_id", id);
            sysSomePosDealServiceChargeMapper.delete(qw);

            this.removeById(id);
            return new ResponseResult<>(200, "删除成功");
        } catch (Exception e) {
            return new ResponseResult<>(400, "删除失败");
        }
    }

    @Override
    public ResponseResult<SysPosDealServiceCharge> selectByIdSysPosDealServiceCharge(Integer id) {
        SysPosDealServiceCharge record = this.getById(id);
        if (record != null) {
            return new ResponseResult<>(200, record);
        } else {
            return new ResponseResult<>(400, "未找到数据");
        }
    }

    @Transactional
    @Override
    public ResponseResult<Void> updateSysPosDealServiceCharge(SysPosDealServiceCharge record) {

        if (record.getStartInterval() >= record.getEndInterval()) {
            return new ResponseResult<>(400, "添加失败，结束区间小于等于开始区间");
        }

        List<SysPosDealServiceCharge> list = this.list();

        for (SysPosDealServiceCharge existingRecord : list) {
            // 检查是否存在重叠的区间
            if (!(record.getEndInterval() < existingRecord.getStartInterval() ||
                    record.getStartInterval() > existingRecord.getEndInterval())) {
                return new ResponseResult<>(400, "添加失败，区间与已有区间重复");
            }
        }

        try {
            this.updateById(record);
            return new ResponseResult<>(200, "更新成功");
        } catch (Exception e) {
            return new ResponseResult<>(400, "更新失败");
        }
    }

    @Override
    public ResponseResult<List<SysPosDealServiceCharge>> selectSysPosDealServiceCharge() {
        List<SysPosDealServiceCharge> list = this.list();
        if (list != null && list.size() != 0) {
            return new ResponseResult<>(200, list);
        } else {
            return new ResponseResult<>(400, "查询失败！");
        }
    }
}
