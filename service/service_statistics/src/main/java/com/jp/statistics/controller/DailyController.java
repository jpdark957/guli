package com.jp.statistics.controller;


import com.jp.commonutils.R;
import com.jp.statistics.client.UcenterClient;
import com.jp.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-16
 */
@RestController
@RequestMapping("/staservice/sta")
@CrossOrigin
@Api(tags = "统计分析模块")
public class DailyController {

    @Autowired
    private DailyService dailyService;

    @ApiOperation("统计某一天注册人数,生成统计数据")
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day) {
        dailyService.registerCount(day);
        return R.ok();
    }

    //图表显示，返回两部分数据，日期json数组，数量json数组
    @ApiOperation("图表显示")
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,
                      @PathVariable String begin,
                      @PathVariable String end) {
        Map<String,Object> map = dailyService.getShowData(type, begin, end);
        return R.ok().data(map);
    }



}

