package com.jp.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jp.eduservice.entity.EduSubject;
import com.jp.eduservice.entity.excel.SubjectData;
import com.jp.eduservice.service.EduSubjectService;
import com.jp.servicebase.exceptionHandler.JpExceptionHandler;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    //因为SubjectExcelListener不能交给spring进行管理，需要自己new，不能注入其他对象
    //不能实现数据库操作
    public EduSubjectService subjectService;

    public SubjectExcelListener() {}
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    //读取excel内容，一行一行进行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData == null) {
            throw new JpExceptionHandler(201, "文件数据为空");
        }

        //一行一行读取，每次读取有两个值，第一个值一级分类，第二个值二级分类
        //判断一级分类是否重复
        EduSubject exitstOneSubject = this.exitstOneSubject(subjectService, subjectData.getOneSubjectName());
        if(exitstOneSubject == null) {//没有相同一级分类，进行添加
            exitstOneSubject = new EduSubject();
            exitstOneSubject.setParentId("0");
            exitstOneSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(exitstOneSubject);
        }

        //添加二级分类
        //判断二级分类是否重复
        String pid = exitstOneSubject.getId();
        EduSubject exitstTwoSubject = this.exitstTwoSubject(subjectService, subjectData.getTwoSubject(), pid);
        if(exitstTwoSubject == null) {//没有相同一级分类，进行添加
            exitstTwoSubject = new EduSubject();
            exitstTwoSubject.setParentId(pid);
            exitstTwoSubject.setTitle(subjectData.getTwoSubject());
            subjectService.save(exitstTwoSubject);
        }
    }

    //判断一级分类不能重复添加
    private EduSubject exitstOneSubject(EduSubjectService subjectService, String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }

    //判断二级分类不能重复添加
    private EduSubject exitstTwoSubject(EduSubjectService subjectService, String name, String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        EduSubject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
