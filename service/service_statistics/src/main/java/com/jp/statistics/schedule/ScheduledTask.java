package com.jp.statistics.schedule;

import com.jp.statistics.service.DailyService;
import com.jp.statistics.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    private DailyService dailyService;

    //在每天凌晨1点，执行添加统计分析数据
    @Scheduled(cron = "0 0 1 * * ?")
    public void task() {
        dailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }
}
