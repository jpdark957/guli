package com.jp.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jp.commonutils.R;
import com.jp.eduservice.entity.EduCourse;
import com.jp.eduservice.entity.EduTeacher;
import com.jp.eduservice.service.EduCourseService;
import com.jp.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@Api(tags = "前台主页接口")
//@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation("查询前八条热门课程，前四条名师")
    @GetMapping("index")
    public R index() {
        //查询前8条热门课程
        List<EduCourse> eduCourses = courseService.indexCourse();

        //查询前4条热门课程
        List<EduTeacher> teacherList = teacherService.indexTeacher();
        return R.ok().data("eduList", eduCourses).data("teacherList", teacherList);
    }

}
