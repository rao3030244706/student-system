package com.example.demo.validation.sequenceProvider;

import com.example.demo.entity.EStudent;
import com.example.demo.validation.group.Add;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * dynamic group
 */

public class EStudentDynamicGroupSequenceProvider implements DefaultGroupSequenceProvider<EStudent> {
    @Override
    public List<Class<?>> getValidationGroups(EStudent student) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(EStudent.class);//  相当于添加了默认组 javax.validation.groups.Default

        if (student != null) {
            //@NotBlank效果就是if(provinceName!=null &&provinceName.trim.len!=0 )
           /* @NotBlank(groups = Add.class)*/ String provinceName = student.getProvinceName();
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
