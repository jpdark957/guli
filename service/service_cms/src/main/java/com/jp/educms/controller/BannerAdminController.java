package com.jp.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.commonutils.R;
import com.jp.educms.entity.CrmBanner;
import com.jp.educms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台banner管理接口
 * </p>
 *
 * @author testjava
 * @since 2020-06-11
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
@Api(tags = "管理员管理轮播图")
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    @ApiOperation("分页查询Banner")
    @GetMapping("pageBanner/{page}/{size}")
    public R pageBanner(
            @ApiParam(name = "page", value = "当前页数", required = true)
            @PathVariable Long page,
            @ApiParam(name = "size", value = "每页记录数", required = true)
            @PathVariable Long size) {
        Page<CrmBanner> pageParam = new Page<>(page, size);
        bannerService.page(pageParam, null);
        List<CrmBanner> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        Map map = new HashMap<>();
        map.put("items", records);
        map.put("total", total);
        return R.ok().data("map", map);
    }

    @ApiOperation("根据id查询Banner")
    @GetMapping("get/{id}")
    public R get(@PathVariable Long id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @ApiOperation("添加Banner")
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner banner) {
        bannerService.save(banner);
        return R.ok();
    }

    @ApiOperation("修改Banner")
    @PutMapping("updateBanner")
    public R updateBanner(@RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }

    @ApiOperation("删除Banner")
    @DeleteMapping("deleteBanner/{id}")
    public R deleteBanner(@PathVariable Long id) {
        bannerService.removeById(id);
        return R.ok();
    }

}

