package com.jp.statistics.client;

import com.jp.commonutils.R;
import org.springframework.stereotype.Component;

@Component
public class UcenterDegradeFeignClient implements UcenterClient {
    @Override
    public R countRegister(String day) {
        return R.error().message("获取当天注册人数失败!");
    }
}
