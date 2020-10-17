package com.example.demo.validation.sequenceProvider;

import com.example.demo.entity.EStudent;
import com.example.demo.validation.group.Add;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * dynamic group  DefaultGroupSequenceProvider每一个校验都会new新的对象来的
 */

public class EStudentDynamicGroupSequenceProvider implements DefaultGroupSequenceProvider<EStudent> {
    @Override
    public List<Class<?>> getValidationGroups(EStudent student) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        //todo 作用就是保留原先的校验组
        /* @RequestMapping("/addStudent")的   @Validated(value = Add.class) @RequestBody EStudent student
         *defaultGroupSequence就是add(Add.class) */
        defaultGroupSequence.add(EStudent.class);
        //@NotNull==@NotNull(groups=Default.class)  defaultGroupSequence里包含了Default就会触发@NotNull的校验
        /*defaultGroupSequence就是的追加了*/
        if (student != null) {
            //@NotBlank效果就是if(provinceName!=null &&provinceName.trim.len!=0 )
            /* @NotBlank(groups = Add.class)*/
            String provinceName = student.getProvinceName();
            //也就是defaultGroupSequence一开始添加了EStudent.class就是等同于添加Default.class
            //满足了下面的if的情况，就是给EStudent的@NotBlank，@NotNull等等校验注解 的groups动态添加组，
            //todo 猜想一下 就是哪一个校验注解用了分组接口(JiangSu,SiChuan)的选择满足的启用该校验类
            //决定哪一个@Pattern起作用
            if (provinceName.equals("江苏")) {
                defaultGroupSequence.add(EStudent.JiangSu.class);
            } else if (provinceName.equals("四川")) {
                defaultGroupSequence.add(EStudent.SiChuan.class);
            }
        }

        return defaultGroupSequence;
    }
}
