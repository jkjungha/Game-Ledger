package com.example.GLServer.repository;

import lombok.Data;
import lombok.Setter;


@Data
@Setter
public class ResponseData {
    private Boolean isSuccess = true;
    private int code = 200;
    private String message = "요청에 성공하였습니다";
    private Object result;
}
