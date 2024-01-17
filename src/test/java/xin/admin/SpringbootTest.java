package xin.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.mapper.fundsFlowManagement.CommercialTenantOrderZFMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.h5.domain.performance.Performance;
import xin.h5.mapper.performance.PerformanceMapper;
import xin.h5.service.invitation.PersonalInformationService;
import xin.h5.service.invitation.impl.PersonalInformationServiceImpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class SpringbootTest {

    @Autowired
    PersonalInformationService personalInformationService;

    @Autowired
    CommercialTenantOrderZFMapper commercialTenantOrderZFMapper;

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    PerformanceMapper performanceMapper;

    @Test
    public void testExistsByRrn() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.APRIL); // 月份从0开始，所以4月是3
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yy年MM月dd日");

        Date today = new Date();
        List<Date> datesBetweenDay = PersonalInformationServiceImpl.getDatesBetweenDay(date, today);

        Collections.sort(datesBetweenDay, new Comparator<Date>() {
            @Override
            public int compare(Date date1, Date date2) {
                // 降序排列
                return date2.compareTo(date1);
            }
        });

        QueryWrapper<SysPosTerminal> qw = new QueryWrapper<>();
        qw.eq("type", "3");
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectList(qw);

        int totalDates = datesBetweenDay.size();
        int currentDateIndex = 0;

        for (Date assign : datesBetweenDay) {
            currentDateIndex++; // 更新当前日期的索引

            int totalTerminals = sysPosTerminalList.size();
            int currentTerminalIndex = 0;

            for (SysPosTerminal spt : sysPosTerminalList) {
                currentTerminalIndex++; // 更新当前终端的索引

                BigDecimal money = commercialTenantOrderZFMapper.selectBySnTodayMoney(spt.getMachineNo(), spt.getClazz(), assign);
                if (money != null) {
                    Performance performance = new Performance();
                    performance.setCreateTime(assign);
                    performance.setTermModel(spt.getClazz());
                    performance.setTodayMoney(money);
                    performance.setTermSn(spt.getMachineNo());
                    performanceMapper.insert(performance);
                }

                // 在循环的每次迭代中打印进度
                System.out.println("日期进度：" + currentDateIndex + "/" + totalDates + "，当前日期共有记录：" + currentTerminalIndex);
            }

            String formattedDate = dateFormat.format(assign);
            System.out.println("处理完日期：" + formattedDate + "，共处理了终端数：" + totalTerminals);
        }
    }
}
