package com.example.demo.pojo;

import com.example.demo.entity.EStudent;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StudentVO implements Serializable {
    private PageVO pageVO;
    private List<EStudent> students;
}
