package com.jp.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.commonutils.R;
import com.jp.eduservice.entity.EduCourse;
import com.jp.eduservice.entity.EduTeacher;
import com.jp.eduservice.service.EduCourseService;
import com.jp.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/eduservice/teacherfront")
@Api(tags = "前台讲师模块")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @ApiOperation("分页查询讲师的方法")
    @PostMapping("getTeacherFrontList/{page}/{size}")
    public R getTeacherFrontList(@PathVariable long page, @PathVariable long size) {
        Page<EduTeacher> pageTeacher = new Page<>(page, size);
        Map<String, Object> map = teacherService.getTeacherFrontList(pageTeacher);
        return R.ok().data(map);
    }

    @ApiOperation("讲师基本信息")
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId) {
        //根据讲师id查询讲师基本信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);
        //根据讲师id查询所讲课程
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.eq("teacher_id", teacherId);
        List<EduCourse> eduList = courseService.list(courseWrapper);
//        Map map = new HashMap<>();
//        map.put("teacher", eduTeacher);
//        map.put("courseList", eduList);
//        return R.ok().data("list", map);
        return R.ok().data("teacher", eduTeacher).data("courseList", eduList);
    }


}
