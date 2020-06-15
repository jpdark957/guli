package com.jp.eduservice.mapper;

import com.jp.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jp.eduservice.entity.frontVo.CourseWebVo;
import com.jp.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getPublishCourseInfo(String courseId);


    //前台课程详情 根据课程id查询课程信息
    public CourseWebVo getBaseCourseInfo(String courseId);
}
