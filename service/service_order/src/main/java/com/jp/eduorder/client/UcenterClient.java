package com.jp.eduorder.client;

import com.jp.commonutils.ordervo.UcenterMemberOrderAndComment;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    //根据用户id获取用户信息
    @PostMapping("/educenter/member/getInfoUc/{id}")
    public UcenterMemberOrderAndComment getInfo(@PathVariable("id") String id);

}
