package com.example.demo.service;

import com.example.demo.entity.EStudent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.pojo.CommonResponseVO;
import com.example.demo.pojo.StudentVO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 学生信息表 服务类
 * </p>
 *
 * @author nickle
 * @since 2020-10-07
 */
public interface IEStudentService extends IService<EStudent> {


    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @param condition
     * @param startBirthday
     * @param endBirthday
     * @return
     */
    StudentVO listByPage(Long currentPage, Long pageSize, EStudent condition, Long startBirthday, Long endBirthday);

    /**
     * insert one(id=null) or update one(id=notNull)
     *
     * @param student
     */
    void saveOrUpdateExecutor(EStudent student);

    void deleteStudent(Integer studentId);

    /**
     * 批量插入 insert xx (),(),()这种形式的批量插入
     *
     * @param students
     */
    void insertListByExcel(List<Object> students);
}
