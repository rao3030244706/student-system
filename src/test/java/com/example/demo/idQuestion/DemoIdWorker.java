package com.example.demo.idQuestion;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

public class DemoIdWorker {
    public static void main(String[] args) {
//        IdWorker idWorker = new IdWorker();
        System.out.println(IdWorker.getId());
        for (int i = 0; i < 10; i++) {
            System.out.println(IdWorker.getId());
        }

    }
}
