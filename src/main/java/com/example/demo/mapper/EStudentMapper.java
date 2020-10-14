package com.example.demo.mapper;

import com.example.demo.entity.EStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 学生信息表 Mapper 接口
 * </p>
 *
 * @author nickle
 * @since 2020-10-07
 */
public interface EStudentMapper extends BaseMapper<EStudent> {
    void insertList(List<EStudent> students);

    /*void insertOne(EStudent student);*/
}
