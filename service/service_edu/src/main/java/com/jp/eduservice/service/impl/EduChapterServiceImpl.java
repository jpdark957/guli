package com.jp.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jp.eduservice.entity.EduChapter;
import com.jp.eduservice.entity.EduVideo;
import com.jp.eduservice.entity.chapter.ChapterVo;
import com.jp.eduservice.entity.chapter.VideoVo;
import com.jp.eduservice.mapper.EduChapterMapper;
import com.jp.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jp.eduservice.service.EduVideoService;
import com.jp.servicebase.exceptionHandler.JpExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    //根据课程id查询章节以及小节
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1 先查询出所有的章节
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapper);

        //2 查询出所有的小节
        QueryWrapper<EduVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id", courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapper1);

        //3 遍历查询章节list集合进行封装
        List<ChapterVo> finalList = new ArrayList<>();
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            finalList.add(chapterVo);

            List<VideoVo> finalVideoList = new ArrayList<>();
            for (int m = 0; m < eduVideoList.size(); m++) {
                EduVideo eduVideo = eduVideoList.get(m);
                if (eduVideo.getChapterId().equals(eduChapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    finalVideoList.add(videoVo);
                }
            }
            chapterVo.setChildren(finalVideoList);
        }

        //4 遍历查询小节list集合，进行封装
        return finalList;
    }

    //删除章节
    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        int count = videoService.count(wrapper);
        if(count > 0) {
            throw new JpExceptionHandler(201, "不能删除!");
        } else {
            int result = baseMapper.deleteById(chapterId);
            //result>0等于成功 =0等于失败
            return result>0;
        }

    }
}
