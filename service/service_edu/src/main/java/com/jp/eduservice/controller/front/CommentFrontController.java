package com.jp.eduservice.controller.front;


import com.alibaba.nacos.client.utils.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jp.commonutils.JwtUtils;
import com.jp.commonutils.R;
import com.jp.commonutils.ordervo.UcenterMemberOrderAndComment;
import com.jp.eduservice.client.UcenterClient;
import com.jp.eduservice.entity.EduComment;
import com.jp.eduservice.entity.vo.UcenterMemberPay;
import com.jp.eduservice.service.EduCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-15
 */
@RestController
@RequestMapping("/eduservice/comment")
@CrossOrigin
@Api(tags = "评论管理")
public class CommentFrontController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UcenterClient ucenterClient;

    @ApiOperation("添加课程")
    @PostMapping("addComment")
    public R addComment(@RequestBody EduComment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(201).message("请先登录!");
        }
        UcenterMemberOrderAndComment info = ucenterClient.getInfo(memberId);
        comment.setMemberId(memberId);

        comment.setNickname(info.getNickname());
        comment.setAvatar(info.getAvatar());

        commentService.save(comment);
        return R.ok();
    }

    @ApiOperation("根据课程id分页查询评论")
    @GetMapping("pageComment/{page}/{size}/{courseId}")
    public R pageComment(
            @ApiParam(name="page", value = "当前页数", required = true)
            @PathVariable Long page,
            @ApiParam(name="size", value = "每页记录数", required = true)
            @PathVariable Long size,
            @ApiParam(name="courseId", value = "课程id", required = true)
            @PathVariable String courseId
            ) {
        Page<EduComment> pageComment = new Page<>(page, size);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        commentService.page(pageComment, wrapper);
        List<EduComment> records = pageComment.getRecords();
        long current = pageComment.getCurrent();
        long pages = pageComment.getPages();
        long size1 = pageComment.getSize();
        long total = pageComment.getTotal();
        boolean hasNext = pageComment.hasNext();
        boolean hasPrevious = pageComment.hasPrevious();
        Map map = new HashMap();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size1);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return R.ok().data(map);
    }
}

