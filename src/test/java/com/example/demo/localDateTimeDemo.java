package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j
public class localDateTimeDemo {
    public static void main(String[] args) {
        //log.debug与printf用法差不多---C语言
        log.debug("{}", LocalDateTime.now());
    }
}
