package com.jp.servicebase.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JpExceptionHandler extends RuntimeException {
    private Integer code;//状态码
    private String msg;//异常信息

    @Override
    public String toString() {
        return "JpExceptionHandler{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
