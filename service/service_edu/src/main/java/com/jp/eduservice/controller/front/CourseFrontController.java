package com.jp.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.commonutils.R;
import com.jp.eduservice.entity.EduCourse;
import com.jp.eduservice.entity.chapter.ChapterVo;
import com.jp.eduservice.entity.frontVo.CourseFrontVo;
import com.jp.eduservice.entity.frontVo.CourseWebVo;
import com.jp.eduservice.service.EduChapterService;
import com.jp.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/eduservice/coursefront")
@Api(tags = "前台课程模块")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @PostMapping("getFrontCourseList/{page}/{size}")
    @ApiOperation("条件查询带分页查询课程")
    public R getFrontCourseList(@PathVariable Long page, @PathVariable Long size, @RequestBody(required = false) CourseFrontVo courseFrontVo) {
        Page<EduCourse> pageCourse = new Page<>(page, size);
        Map<String, Object> map = courseService.getCourseFrontList(pageCourse, courseFrontVo);
        //返回分页所有数据
        return R.ok().data(map);
    }

    @ApiOperation("课程详情")
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId) {
        //根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("courseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList);
    }

}
