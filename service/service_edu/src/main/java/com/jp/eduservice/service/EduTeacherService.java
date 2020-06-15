package com.jp.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-27
 */
public interface EduTeacherService extends IService<EduTeacher> {
    //查询名师
    public List<EduTeacher> indexTeacher();

    //分页查询讲师
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);
}
