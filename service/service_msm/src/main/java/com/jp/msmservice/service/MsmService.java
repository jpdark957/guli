package com.jp.msmservice.service;

import java.util.Map;

public interface MsmService {
    //发送短信
    boolean send(Map<String, Object> param, String phone);
}
