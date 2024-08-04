package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    //@Select("select * from category")
    List<Category> list();

    List<Category> listByConditions(BaseRequest baseRequest);

    void save(Category category);

    void delete(Integer id);

    void update(Category category);

    Category getById(Integer id);

}
