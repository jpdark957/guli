package com.jp.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.eduservice.entity.EduChapter;
import com.jp.eduservice.entity.EduCourse;
import com.jp.eduservice.entity.EduCourseDescription;
import com.jp.eduservice.entity.EduVideo;
import com.jp.eduservice.entity.vo.CourseInfoVo;
import com.jp.eduservice.entity.vo.CoursePublishVo;
import com.jp.eduservice.mapper.EduCourseMapper;
import com.jp.eduservice.service.EduChapterService;
import com.jp.eduservice.service.EduCourseDescriptionService;
import com.jp.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jp.eduservice.service.EduVideoService;
import com.jp.servicebase.exceptionHandler.JpExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程描述注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduChapterService chapterService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1 先添加课程基本信息
        //讲courseInfoVo转换为EduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if(insert == 0) {
            throw new JpExceptionHandler(201, "添加课程信息失败");
        }

        //2 添加课程描述
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(eduCourse.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.save(eduCourseDescription);

        return eduCourse.getId();
    }

    //根据课程id查询信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        EduCourse eduCourse = baseMapper.selectById(courseId);
        BeanUtils.copyProperties(eduCourse, courseInfoVo);

        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);

        if(update == 0) {
            throw new JpExceptionHandler(201, "修改课程信息失败");
        }

        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(eduCourseDescription);
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
//        videoService.removeVideoByCourseId(courseId);

        //删除小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        videoService.remove(wrapperVideo);

        //删除章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        chapterService.remove(wrapperChapter);

        QueryWrapper<EduCourseDescription> wrapperCourseDescription = new QueryWrapper<>();
        wrapperCourseDescription.eq("id", courseId);
        courseDescriptionService.remove(wrapperCourseDescription);


        int result = baseMapper.deleteById(courseId);
        if(result == 0) {
            throw new JpExceptionHandler(201, "删除失败!");
        }
    }

}
