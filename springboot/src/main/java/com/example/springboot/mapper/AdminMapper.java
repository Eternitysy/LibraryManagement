package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.LoginRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    //@Select("select * from admin")
    List<Admin> list();

    List<Admin> listByConditions(BaseRequest baseRequest);

    void save(Admin admin);

    void delete(Integer id);

    void update(Admin admin);

    Admin getById(Integer id);

    Admin getByUsernameAndPassword(LoginRequest request);

    int updatePassword(PasswordRequest request);

    Admin getByUsername(String username);
}
