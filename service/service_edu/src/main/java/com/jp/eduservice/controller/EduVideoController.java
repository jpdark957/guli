package com.jp.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jp.commonutils.R;
import com.jp.eduservice.client.VodClient;
import com.jp.eduservice.entity.EduVideo;
import com.jp.eduservice.service.EduVideoService;
import com.jp.servicebase.exceptionHandler.JpExceptionHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
@Api(tags = "小节管理")
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    //注入vodClient
    @Autowired
    private VodClient vodClient;

    @ApiOperation("添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        videoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation("删除视频id和name")
    @PostMapping("deleteVidAndName/{id}")
    public R deleteVidAndName(@PathVariable String id) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        EduVideo eduVideo = videoService.getById(id);
        eduVideo.setId(id);
        eduVideo.setVideoSourceId(null);
        eduVideo.setVideoOriginalName(null);
        videoService.update(eduVideo, wrapper);
        return R.ok();
    }

    @ApiOperation("删除小节删除对应的视频")
    @DeleteMapping("deleteVideo/{id}")
    public R deleteVideo(@PathVariable String id) {
        //根据小节id获取视频id，调用方法实现视频删除
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        //根据视频id，远程调用实现视频删除
        if(!StringUtils.isEmpty(videoSourceId)) {
            R result = vodClient.removeAlyVideo(videoSourceId);
            if(result.getCode() == 201) {
                throw new JpExceptionHandler(201, "删除视频失败，熔断器");
            }
        }
        videoService.removeById(id);
        return R.ok();
    }

    @ApiOperation("修改小节")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        videoService.updateById(eduVideo);
        return R.ok();
    }

    @ApiOperation("根据小节ID查询数据")
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId) {
        EduVideo video = videoService.getById(videoId);
        return R.ok().data("video", video);
    }

}

