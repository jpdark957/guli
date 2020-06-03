package com.jp.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoData {

    @ExcelProperty(value = "学生编号", index = 0)
    private int sno;
    @ExcelProperty(value = "学生姓名", index = 1)
    private String sname;
}
