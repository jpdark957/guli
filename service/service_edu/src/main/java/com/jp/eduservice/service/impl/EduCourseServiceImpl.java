package com.jp.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.eduservice.client.VodClient;
import com.jp.eduservice.entity.EduChapter;
import com.jp.eduservice.entity.EduCourse;
import com.jp.eduservice.entity.EduCourseDescription;
import com.jp.eduservice.entity.EduVideo;
import com.jp.eduservice.entity.frontVo.CourseFrontVo;
import com.jp.eduservice.entity.frontVo.CourseWebVo;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private VodClient vodClient;

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
        //根据课程id查询课程所有的视频id
        QueryWrapper<EduVideo> wrapperVideoId = new QueryWrapper<>();
        wrapperVideoId.eq("course_id", courseId);
        wrapperVideoId.select("video_source_id");
        List<EduVideo> eduVideoList = videoService.list(wrapperVideoId);

        //List<EduVideo> 变成List<String>
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)) {
                videoIds.add(videoSourceId);
            }
        }
        if(videoIds.size() > 0) {
            //删除视频
            vodClient.deleteBatch(videoIds);
        }


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


    //查询热门课程
    @Cacheable(value="course", key="'indexCourse'")
    @Override
    public List<EduCourse> indexCourse() {
        //查询前8条热门课程
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("id");
        courseWrapper.last("LIMIT 8");
        List<EduCourse> courseList = baseMapper.selectList(courseWrapper);
        return courseList;
    }


    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否为空，不为空拼接
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) {  //一级分类
            wrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())) {    //二级分类
            wrapper.eq("subject_id", courseFrontVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())) {    //关注度
            wrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) {   //最新
            wrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {  //价格
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageCourse, wrapper);

        List<EduCourse> records = pageCourse.getRecords();
        long current = pageCourse.getCurrent();
        long pages = pageCourse.getPages();
        long size = pageCourse.getSize();
        long total = pageCourse.getTotal();
        boolean hasNext = pageCourse.hasNext();
        boolean hasPrevious = pageCourse.hasPrevious();

        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    //前台课程详情 根据课程id查询课程信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
