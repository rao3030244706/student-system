package com.example.demo.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageVO /*implements Serializable*/ {
    private  Long currentPage;
    private  Long pageSize;
    private  Long total;
}
