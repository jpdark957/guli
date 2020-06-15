package com.jp.eduservice.client;

import com.jp.commonutils.R;
import com.jp.commonutils.ordervo.UcenterMemberOrderAndComment;
import com.jp.eduservice.entity.vo.UcenterMemberPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "service-ucenter", fallback = UcenterDegradeFeignClient.class)//调用服务名称
@Component
public interface UcenterClient {
    //根据token获取用户信息
    @GetMapping("/educenter/member/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request);

    //根据用户id获取用户信息
    @PostMapping("/educenter/member/getInfoUc/{id}")
    public UcenterMemberOrderAndComment getInfo(@PathVariable("id") String id);

}
