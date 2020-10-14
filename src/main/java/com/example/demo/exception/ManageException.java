package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManageException extends RuntimeException {
    private String msg;
    private Integer status = 500;

    public ManageException(Integer status, String msg) {
        super(msg);
        this.msg = msg;
        this.status = status;
    }

    public ManageException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
