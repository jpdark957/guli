package com.jp.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jp.commonutils.JwtUtils;
import com.jp.commonutils.MD5;
import com.jp.educenter.entity.UcenterMember;
import com.jp.educenter.entity.vo.RegisterVo;
import com.jp.educenter.mapper.UcenterMemberMapper;
import com.jp.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jp.servicebase.exceptionHandler.JpExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-12
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //用户登录
    @Override
    public String login(UcenterMember member) {
        //获取登录手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        //手机号和密码非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new JpExceptionHandler(201, "登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);

        //判断查询对象是否为空
        if (mobileMember == null) {
            throw new JpExceptionHandler(201, "登录失败!");
        }

        //判断密码
        //把输入的密码进行加密，再和数据库密码进行比较
        //加密方式, MD5
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new JpExceptionHandler(201, "密码不正确!");
        }
        //判断用户是否金庸
        if (mobileMember.getIsDisabled()) {
            throw new JpExceptionHandler(201, "账号被禁用!");
        }

        //登录成功
        //生成token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }


    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode();//验证码
        String mobile = registerVo.getMobile();//手机号
        String nickname = registerVo.getNickname();//昵称
        String password = registerVo.getPassword();//密码

        //非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(code)) {
            throw new JpExceptionHandler(201, "注册失败!");
        }

        //判断验证码
        //获取redis中的验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new JpExceptionHandler(201, "验证码错误!");
        }

        //判断手机号是否重复，表里面存在相同手机号不进行添加
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new JpExceptionHandler(201, "该手机号已经注册!");
        }

        //数据添加数据库中
        UcenterMember member = new UcenterMember();
        registerVo.setPassword(MD5.encrypt(password));
        BeanUtils.copyProperties(registerVo, member);
        member.setIsDisabled(false);//用户不禁用
        member.setAvatar("https://jpdark.cn/random/11.jpg");
        baseMapper.insert(member);

    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }


}
