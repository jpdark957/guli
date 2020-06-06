package com.jp.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jp.eduservice.entity.vo.CourseInfoVo;
import com.jp.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程基本信息以及描述
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询数据
    CourseInfoVo getCourseInfo(String courseId);

    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程确认信息
    CoursePublishVo publishCourseInfo(String id);

    //删除课程
    void removeCourse(String courseId);

}
