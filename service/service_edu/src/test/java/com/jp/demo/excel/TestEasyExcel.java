package com.jp.demo.excel;


import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {

        //写
//        String filename = "E:\\ITRelevant\\projectData\\guli\\write.xlsx";
//        EasyExcel.write(filename, DemoData.class).sheet("写入方法一").doWrite(data());

        //读
        String filename = "E:\\ITRelevant\\projectData\\guli\\write.xlsx";
        EasyExcel.read(filename, DemoData.class, new ExcelListener()).sheet().doRead();
    }

    public static List<DemoData> data() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("张三" + i);
            list.add(data);
        }
        return list;
    }
}
