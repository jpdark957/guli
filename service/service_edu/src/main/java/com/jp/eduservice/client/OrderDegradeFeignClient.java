package com.jp.eduservice.client;

import org.springframework.stereotype.Component;

@Component
public class OrderDegradeFeignClient implements OrderClient {
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        return false;
    }
}
