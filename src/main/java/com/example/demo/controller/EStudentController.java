package com.example.demo.controller;


import com.example.demo.entity.EStudent;
import com.example.demo.excel.create.CreateExcelUtils;
import com.example.demo.validation.MustIsExcel;
import com.example.demo.pojo.CommonResponseVO;
import com.example.demo.pojo.StudentVO;
import com.example.demo.service.IEStudentService;
import com.example.demo.validation.group.Add;
import com.example.demo.validation.group.Query;
import com.example.demo.validation.group.Update;
//import com.example.demo.excel.parse.ParseExcelOneThreadUtils;
import com.example.demo.excel.parse.ParseExcelOneThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 学生信息表 前端控制器
 * </p>
 *
 * @author nickle
 * @since 2020-10-07
 */
@RestController
@RequestMapping("/e-student")
@Validated
public class EStudentController {
    @Autowired
    private IEStudentService studentService;

    //http://localhost:8888/e-student/studentList?currentPage=1&pageSize=5
    @RequestMapping("/studentList")
    public CommonResponseVO studentList(@NotNull @Min(1) Long currentPage
            , @NotNull @Min(1) @Max(50) Long pageSize,
            /*@Valid的作用使用EStudent的Field的@NotNull，@NotBlank起作用*/
                                        @Validated({Query.class}) EStudent condition,
                                        Long startBirthday,
                                        Long endBirthday) {
        StudentVO studentVO = studentService.listByPage(currentPage, pageSize, condition, startBirthday, endBirthday);
        return CommonResponseVO.success(studentVO);
    }

    @RequestMapping("/addStudent")
    public CommonResponseVO addStudent(@Validated(value = Add.class) @RequestBody EStudent student) throws Exception {
        studentService.saveOrUpdateExecutor(student);
        return CommonResponseVO.SUCCESS;
    }


    /**
     * 解析传递的Excel文件，加入到数据库中
     * todo 使用upload.html去上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addStudents", method = RequestMethod.POST)
    public CommonResponseVO addStudents(@MustIsExcel @RequestParam(name = "fileUpload") MultipartFile file) {
        //使用fork-join or one thread 看情况，看数量
        List<Object> objects = ParseExcelOneThreadUtils.parseExcelFile(file, EStudent.class);
        studentService.insertListByExcel(objects);//>0 success <0 fail 后面再改
        return CommonResponseVO.SUCCESS;
    }

    @RequestMapping("/updateStudent")
    public CommonResponseVO updateStudent(@Validated(value = Update.class) @RequestBody EStudent student) throws Exception {
        studentService.saveOrUpdateExecutor(student);
        return CommonResponseVO.SUCCESS;
    }

    @RequestMapping("/deleteStudent")
    public CommonResponseVO deleteStudent(@NotNull Integer stuId) throws Exception {
        studentService.deleteStudent(stuId);
        return CommonResponseVO.SUCCESS;
    }

    /**
     * 导出学生excel
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/exportStudentExcel")
    public CommonResponseVO exportStudentExcel(/*@NotNull Integer stuId*/) throws Exception {
        CreateExcelUtils.createEntityExcel(null,EStudent.class,10);
        return CommonResponseVO.SUCCESS;
    }

}
