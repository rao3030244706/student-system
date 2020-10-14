package com.example.demo;

//import org.junit.jupiter.api.Test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.EStudent;
import com.example.demo.mapper.EStudentMapper;
import com.example.demo.excel.create.CreateExcelUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    SqlSessionFactory factory;

    String s = "1";

    @Autowired
    EStudentMapper studentMapper;

    @Test
    public void demoCreateExcel11() {
        QueryWrapper<EStudent> queryWrapper = new QueryWrapper<>();
        List<EStudent> students = studentMapper.selectList(queryWrapper);
        System.out.println(students.size());
        CreateExcelUtils.createEntityExcel(students, EStudent.class, 4);
    }


    @Test
    public void insertList() {
        SqlSession sqlSession = factory.openSession(false);
        EStudentMapper mapper = sqlSession.getMapper(EStudentMapper.class);
        List<EStudent> students = new ArrayList<>();
        for (int i = 9; i < 18; i++) {
            EStudent student = new EStudent();
            student.setId(i + "");
            student.setCnName("嘻嘻嘻");
            student.setEnName("smile");
            student.setProvinceName("上海");
            student.setCityName("上海");
            student.setIsPartyMember(s);
            student.setBirthDate(LocalDateTime.now());
            student.setIsLeagueMember(s);
            student.setIsLeagueMember(s);
            student.setIdCard("100002");
            student.setSex(s);
            student.setGrade("三年级");
            student.setClazz("(1)");
            student.setFatherTelNo("1");
            student.setTelNo("1");
            student.setMotherTelNo("1");
            student.setMotherName("mother");
            student.setFatherName("father");
            student.setQqNo("qq");
            student.setWechatNo("wechat");
            students.add(student);
        }
        mapper.insertList(students);
        sqlSession.commit();
        sqlSession.close();
    }

}
