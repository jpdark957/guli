package com.jp.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.commonutils.R;
import com.jp.eduservice.client.VodClient;
import com.jp.eduservice.entity.EduCourse;
import com.jp.eduservice.entity.vo.CourseInfoVo;
import com.jp.eduservice.entity.vo.CoursePublishVo;
import com.jp.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
@RestController
@RequestMapping("/eduservice/course")
@Api(tags = "课程信息")
//@CrossOrigin
public class EduCourseController {

    //注入Service层
    @Autowired
    private EduCourseService courseService;

    @PostMapping("addCourseInfo")
    @ApiOperation("添加课程基本信息以及描述")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    @GetMapping("getCourseInfo/{courseId}")
    @ApiOperation("根据课程id查询信息")
    public R getCourseInfo(@PathVariable String courseId) {
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo", courseInfoVo);
    }

    @ApiOperation("修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    @ApiOperation("根据课程id查询课程确认信息")
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    @ApiOperation("课程最终发布")
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    @ApiOperation("删除课程")
    @DeleteMapping("deleteCourse/{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        courseService.removeCourse(courseId);
        return R.ok();
    }


    @ApiOperation("课程列表")
    @GetMapping("courseList")
    public R getCourseList() {
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list", list);
    }

    @ApiOperation("分页查询课程")
    @GetMapping("pageCourse/{current}/{size}")
    public R pageCourse(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,

            @ApiParam(name = "size", value = "每页记录数", required = true)
            @PathVariable Long size
    ) {
        Page<EduCourse> page = new Page<>(current, size);
        courseService.page(page, null);
        long total = page.getTotal();
        List<EduCourse> records = page.getRecords();
        Map map = new HashMap();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);
    }

    @ApiOperation("多条件分页查询课程")
    @PostMapping("pageCourseCondition/{current}/{size}")
    public R pageCourseCondition(@ApiParam(name = "current", value = "当前页码", required = true)
                                 @PathVariable Long current,
                                 @ApiParam(name = "size", value = "每页记录数", required = true)
                                 @PathVariable Long size,
                                 @RequestBody(required = false) EduCourse eduCourse) {
        Page<EduCourse> page = new Page<>(current, size);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        String title = eduCourse.getTitle();
        String status = eduCourse.getStatus();

        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("gmt_create");
        courseService.page(page, wrapper);
        long total = page.getTotal();
        List<EduCourse> records = page.getRecords();
        Map map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);
    }

}

