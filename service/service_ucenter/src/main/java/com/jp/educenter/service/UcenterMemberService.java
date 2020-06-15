package com.jp.educenter.service;

import com.jp.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jp.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-12
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //用户登录
    String login(UcenterMember member);

    //注册
    void register(RegisterVo registerVo);

    //根据openid判断
    UcenterMember getOpenIdMember(String openid);
}
