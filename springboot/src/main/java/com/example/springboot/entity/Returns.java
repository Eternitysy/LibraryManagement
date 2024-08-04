package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Returns {
    private Integer id;
    private String bookName;
    private String bookNo;
    private String userId;
    private String userName;
    private String userPhone;
    private String status;
    private Integer score;
    private Integer days;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updatetime;
    private LocalDate returnDate;
    private LocalDate realDate;
}
