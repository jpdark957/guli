package com.jp.eduservice.client;

import com.jp.commonutils.R;
import com.jp.commonutils.ordervo.UcenterMemberOrderAndComment;
import com.jp.eduservice.entity.vo.UcenterMemberPay;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UcenterDegradeFeignClient implements UcenterClient {
    @Override
    public UcenterMemberOrderAndComment getInfo(String id) {
        return null;
    }

    @Override
    public R getMemberInfo(HttpServletRequest request) {
        return R.error().message("获取用户信息失败");
    }
}
