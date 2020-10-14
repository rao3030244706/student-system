package com.example.demo.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.demo.exception.ManageException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class ReflectCommonUtils {
    /**
     * 构建sql执行条件
     *
     * @param queryWrapper
     * @param obj
     */
    public static void buildCondition(QueryWrapper<?> queryWrapper, Object obj) {
        Class<?> aClass = obj.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                String name = field.getName();
                if (!"serialVersionUID".equals(name) && value != null) {
                    //添加进查询条件
                    //String 采用like处理
                    if (value instanceof String && !"".equals(((String) value).trim())) {
                        queryWrapper.like(name, value);
                    } else {
                        queryWrapper.eq(name, value);
                    }

                }
            }
        } catch (Exception e) {
            log.error("buildCondition 发生异常:{}", e.getMessage());
            throw new ManageException("构建查询条件出错");
        }
    }
}
