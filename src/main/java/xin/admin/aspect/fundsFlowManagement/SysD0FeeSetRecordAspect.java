package xin.admin.aspect.fundsFlowManagement;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysFeeDeductionRecord;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.SysServiceChargeHistory;
import xin.admin.mapper.fundsFlowManagement.SysFeeDeductionRecordMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.zhongFu.model.resp.MerchFeeQueryResp;

import java.util.Date;

/**
 * 用于记录设置无感押金
 */
@Aspect
@Component
public class SysD0FeeSetRecordAspect {

    SysFeeDeductionRecord sysFeeDeductionRecord;

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    SysFeeDeductionRecordMapper sysFeeDeductionRecordMapper;

    // 定义切入点，指向 SetupPosRateService 的 set 方法
    @Pointcut("execution(* xin.admin.service.fundsFlowManagement.UnifiedChargingService.setAssignPosD0SingleCashDrawal(..))")
    public void setPointcut() {
        // 切入点定义
    }

    // 前置通知：在目标方法执行前调用
    @Before("setPointcut()")
    public void beforeSetMethod(JoinPoint joinPoint) {

        sysFeeDeductionRecord = new SysFeeDeductionRecord();

        Object[] args = joinPoint.getArgs(); // 获取所有原始参数
        if (args != null && args.length > 0 && args[0] instanceof MerchFeeQueryResp) {
            // 原始参数
            MerchFeeQueryResp merchFeeQueryResp = (MerchFeeQueryResp) args[0];
            Boolean status = (Boolean) args[1];

            System.out.println("前置通知：SetupPosRate 参数为：" + merchFeeQueryResp + "  " + status);

            if (status) {
                sysFeeDeductionRecord.setType("D0手续费费率(%)");
                sysFeeDeductionRecord.setAmount(merchFeeQueryResp.getD0FeeRate());
            }else {
                sysFeeDeductionRecord.setType("D0单笔提现(元)");
                sysFeeDeductionRecord.setAmount(merchFeeQueryResp.getD0SingleCashDrawal());
            }

            SysPosTerminal sysPosTerminal = sysPosTerminalMapper.selectById(merchFeeQueryResp.getPosId());
            if (sysPosTerminal == null) {
                sysFeeDeductionRecord.setPos("未找到");
            } else {
                sysFeeDeductionRecord.setPos(sysPosTerminal.getMachineNo());
            }
        }
    }

    // 后置通知：在目标方法正常结束后调用
    @AfterReturning(pointcut = "setPointcut()", returning = "result")
    public void afterReturningSetMethod(Object result) {
        ResponseResult resResult = (ResponseResult) result;
        if (resResult.getCode() == 200) {
            SysServiceChargeHistory sysServiceChargeHistory = (SysServiceChargeHistory) resResult.getData();
            sysFeeDeductionRecord.setStatus("成功");
            sysFeeDeductionRecord.setTransactionTime(new Date());
        } else {
            sysFeeDeductionRecord.setStatus("失败");
            sysFeeDeductionRecord.setTransactionTime(new Date());
            sysFeeDeductionRecord.setRemark(resResult.getMsg());
        }
        sysFeeDeductionRecordMapper.insert(sysFeeDeductionRecord);
        System.out.println("后置通知：方法返回值为：" + result);
    }

    // 环绕通知：在目标方法执行前后调用
    @Around("setPointcut()")
    public Object aroundSetMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕通知：方法执行前");
        Object result;
        try {
            result = joinPoint.proceed(); // 执行目标方法
        } finally {
            System.out.println("环绕通知：方法执行后");
        }
        return result;
    }

    // 异常通知：在目标方法抛出异常时调用
    @AfterThrowing(pointcut = "setPointcut()", throwing = "ex")
    public void afterThrowingSetMethod(JoinPoint joinPoint, Throwable ex) {
        sysFeeDeductionRecord.setStatus("失败");
        sysFeeDeductionRecord.setTransactionTime(new Date());
        sysFeeDeductionRecord.setRemark("执行异常！");
        sysFeeDeductionRecordMapper.insert(sysFeeDeductionRecord);
        System.out.println("异常通知：方法抛出异常：" + ex.getMessage());
    }
}
