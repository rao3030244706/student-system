package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.EStudent;
import com.example.demo.mapper.EStudentMapper;
import com.example.demo.pojo.PageVO;
import com.example.demo.pojo.StudentVO;
import com.example.demo.service.IEStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.util.ReflectCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 学生信息表 服务实现类
 * </p>
 *
 * @author nickle
 * @since 2020-10-07
 */
@Service
@Slf4j
public class EStudentServiceImpl extends ServiceImpl<EStudentMapper, EStudent> implements IEStudentService {

    @Autowired
    private EStudentMapper studentMapper;

    @Override
    public StudentVO listByPage(Long currentPage, Long pageSize, EStudent condition, Long startBirthday, Long endBirthday) {
        StudentVO studentVO = new StudentVO();
        Page<EStudent> studentPage = new Page<>(currentPage, pageSize);
        QueryWrapper<EStudent> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<EStudent> lambda = queryWrapper.lambda();
        //日期时间段
        if (null != startBirthday) {
            lambda.ge(EStudent::getBirthDate, new Date(startBirthday));
        }
        if (null != endBirthday) {
            lambda.le(EStudent::getBirthDate, new Date(endBirthday));
        }
        //其他sql条件
        ReflectCommonUtils.buildCondition(queryWrapper, condition);
        IPage<EStudent> page = page(studentPage, queryWrapper);
        PageVO pageVO = new PageVO();
        pageVO.setCurrentPage(currentPage);
        pageVO.setPageSize(pageSize);
        pageVO.setTotal(page.getTotal());
        List<EStudent> records = page.getRecords();
        studentVO.setPageVO(pageVO);
        studentVO.setStudents(records);
        return studentVO;
    }


    @Transactional
    @Override
    public void saveOrUpdateExecutor(EStudent student) {
        String executorId = student.getId();
        if (executorId != null) {//update
            student.setUpdatedTime(LocalDateTime.now());
            student.setUpdatedBy("admin");
            this.updateById(student);
            return;
        }
        //insert todo 这个createTime也可以借助MySQL触发器完成
        student.setCreatedTime(LocalDateTime.now());
        student.setCreatedBy("admin");
        save(student);
    }

    @Transactional
    @Override
    public void deleteStudent(Integer studentId) {
        removeById(studentId);
    }

    @Transactional
    @Override
    public void insertListByExcel(List<Object> students) {
        List<EStudent> collect = students.parallelStream().map(o -> {
            EStudent student = (EStudent) o;
            student.setCreatedTime(LocalDateTime.now());
            student.setCreatedBy("admin");
            student.setId(IdWorker.getId() + "");
            return student;
        }).collect(Collectors.toList());
        studentMapper.insertList(collect);
    }
}
