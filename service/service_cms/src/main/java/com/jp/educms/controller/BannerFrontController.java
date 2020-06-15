package com.jp.educms.controller;


import com.jp.commonutils.R;
import com.jp.educms.entity.CrmBanner;
import com.jp.educms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端banner显示接口
 * </p>
 *
 * @author testjava
 * @since 2020-06-11
 */
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
@Api(tags = "用户轮播图")
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    @ApiOperation("获取全部Banner")
    @GetMapping("getAllBanner")
    public R getAllBanner() {
        List<CrmBanner> list = bannerService.selectAllBanner();
        return R.ok().data("list", list);
    }

}

