package com.jp.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.commonutils.R;
import com.jp.eduservice.entity.EduTeacher;
import com.jp.eduservice.entity.vo.TeacherQuery;
import com.jp.eduservice.service.EduTeacherService;
import com.jp.servicebase.exceptionHandler.JpExceptionHandler;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-27
 */
@Api(tags = {"讲师管理"})
@RestController
@RequestMapping("/eduservice/teacher")
//@CrossOrigin //跨域
public class EduTeacherController {

    //注入service
    @Autowired
    private EduTeacherService teacherService;

    //查询Teacher所有数据
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher() {
        //测试捕获异常
//        try {
//            int a = 10 / 0;
//
//        } catch (Exception e) {
//            throw new JpExceptionHandler(201, "执行了自定义异常处理..");
//        }
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("teacherList", list);
    }

    //逻辑删除Teacher
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R deleteTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        boolean flag = teacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation("分页查询讲师")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "current", value = "当前页", required = true),
//            @ApiImplicitParam(name = "size", value = "每页记录数", required = true)
//    })
    @GetMapping("pageTeacher/{current}/{size}")
    public R pageListTeacher(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,

            @ApiParam(name = "size", value = "每页记录数", required = true)
            @PathVariable Long size) {
        Page<EduTeacher> page = new Page<>(current, size);
        teacherService.page(page, null);
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        Map map = new HashMap();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);
//        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation("多条件分页查询讲师")
    @PostMapping("pageTeacherCondition/{current}/{size}")
    public R pageTeacherCondition(@ApiParam(name = "current", value = "当前页码", required = true)
                                  @PathVariable Long current,
                                  @ApiParam(name = "size", value = "每页记录数", required = true)
                                  @PathVariable Long size,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
        Page<EduTeacher> page = new Page<>(current, size);

        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_modified", end);
        }
        wrapper.orderByDesc("gmt_create");

        teacherService.page(page, wrapper);
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        Map map = new HashMap();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);
    }

    @ApiOperation("添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody(required = true) EduTeacher eduTeacher) {
        boolean flag = teacherService.save(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation("根据ID查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);
    }

    @ApiOperation("修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = teacherService.updateById(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

