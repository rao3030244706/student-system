package com.example.demo.util;

import com.example.demo.entity.EStudent;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * foreach tag---cn_name--->#{item.cnName}
 * cn_name-->cnName
 * todo 其实这个只是用来写mapper批量插入的那个一大堆#{..}#{..}的没啥意思
 */
public class SqlUtils {

    public static void main(String[] args) {
        System.out.println(getSql());
        System.out.println();
        System.out.println(getSqlItem());
    }

    /**
     * 构建无item的sql
     */
    public static String getSql() {
        StringBuilder builder = new StringBuilder();
        Class<EStudent> eStudentClass = EStudent.class;
        Field[] declaredFields = eStudentClass.getDeclaredFields();
        builder.append("(");
        Arrays.stream(declaredFields).
                filter(field -> !field.getName().equals("serialVersionUID")).
                forEach(field -> builder.append("#{").
                        append(field.getName()).
                        append("},"));
        String substring = builder.substring(0, builder.lastIndexOf(","));
        substring = substring + ")";
        return substring;
    }

    /**
     * 构建有item的foreach的sql
     */
    public static String getSqlItem() {
        StringBuilder builder = new StringBuilder();
        Class<EStudent> eStudentClass = EStudent.class;
        Field[] declaredFields = eStudentClass.getDeclaredFields();
        builder.append("(");
        Arrays.stream(declaredFields).
                filter(field -> !field.getName().equals("serialVersionUID")).
                forEach(field -> builder.append("#{item.").
                        append(field.getName()).
                        append("},"));
        String substring = builder.substring(0, builder.lastIndexOf(","));
        substring = substring + ")";
        return substring;
    }
}
