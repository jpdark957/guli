package com.jp.educenter.mapper;

import com.jp.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-06-12
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //统计某一天注册人数
    Integer countRegisterDay(String day);
}
