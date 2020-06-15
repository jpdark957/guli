package com.jp.educenter.controller;


import com.jp.commonutils.JwtUtils;
import com.jp.commonutils.R;
import com.jp.educenter.entity.UcenterMember;
import com.jp.educenter.entity.vo.RegisterVo;
import com.jp.educenter.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-12
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
@Api(tags = "用户相关")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation("登录")
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member) {
        //member对象封装手机号和密码
        //返回token值，使用jwt生成
        String token = memberService.login(member);
        return R.ok().data("token", token);
    }

    @ApiOperation("注册")
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation("根据token获取用户信息")
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        //调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo", member);
    }

    @GetMapping("aa")
    public R getaa() {
        return R.ok().data("asdasd","asasdassdasda");
    }

}

