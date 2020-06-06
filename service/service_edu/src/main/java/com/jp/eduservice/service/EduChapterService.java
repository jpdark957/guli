package com.jp.eduservice.service;

import com.jp.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jp.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
public interface EduChapterService extends IService<EduChapter> {

    //根据课程id查询章节以及小节
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    //删除章节
    boolean deleteChapter(String chapterId);
}
