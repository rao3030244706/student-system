package com.example.demo;

import com.example.demo.entity.EStudent;
import com.example.demo.excel.parse.ParseExcelOneThreadUtils;

import java.io.File;
import java.util.List;

public class TestExcel {
    public static void main(String[] args) {
        String s = "C:\\Users\\gain\\Documents\\Tencent Files\\1839139886\\FileRecv\\student-system\\src\\main\\resources\\upload\\excel\\studentExcel.xls";
//        String s = "C:\\Users\\gain\\Documents\\Tencent Files\\1839139886\\FileRecv\\student-system\\src\\main\\resources\\demoUserJiShu.xls";
        List<Object> objects = ParseExcelOneThreadUtils.parseExcelFile(new File(s), EStudent.class);
        objects.forEach(System.out::println);
    }
}
