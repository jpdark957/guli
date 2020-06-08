package com.jp.eduservice.client;

import com.jp.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 删除多个视频出错了
 */
@Component
public class VodFileDegradeFeignClient implements VodClient{
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }
}
