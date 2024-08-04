package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    //@Select("select * from user")
    List<User> list();

    List<User> listByConditions(BaseRequest baseRequest);

    void save(User user);

    void delete(Integer id);

    void update(User user);

    User getById(Integer id);

    User getByNo(String userId);

}
