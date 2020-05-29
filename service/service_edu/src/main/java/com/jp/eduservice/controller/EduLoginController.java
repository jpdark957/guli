package com.jp.eduservice.controller;

import com.jp.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "后台登录")
@RestController
@RequestMapping("/eduservice/user")
public class EduLoginController {

    //后台登录功能
    @ApiOperation(value = "后台登录功能")
    @PostMapping("login")
    public R login() {
        return R.ok().data("token", "admin");
    }

    //后台获取登录用户信息
    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("info")
    public R info() {
        return R.ok().data("roles", "[admin]").data("name", "admin").data("avater", "https://lzhdeblog.oss-cn-shenzhen.aliyuncs.com/img/1589963596595.jpeg");
    }
}
