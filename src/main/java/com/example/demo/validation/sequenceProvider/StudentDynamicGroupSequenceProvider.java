package com.example.demo.validation.sequenceProvider;

import com.example.demo.entity.EStudent;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * dynamic group
 */
public class StudentDynamicGroupSequenceProvider implements DefaultGroupSequenceProvider<EStudent> {
    @Override
    public List<Class<?>> getValidationGroups(EStudent student) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(EStudent.class);//  相当于添加了默认组 javax.validation.groups.Default

        if (student != null) {
            /*if (20 < student.getAge() && student.getAge() <= 25) {
                defaultGroupSequence.add(student.TitleJunior.class);
            } else if (25 < student.getAge() && student.getAge() <= 30) {
                defaultGroupSequence.add(student.TitleMiddle.class);
            }*/
        }

        return defaultGroupSequence;
    }
}
