package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    //@Select("select * from book")
    List<Book> list();

    List<Book> listByConditions(BaseRequest baseRequest);

    void save(Book book);

    void delete(Integer id);

    void update(Book book);

    Book getById(Integer id);

    Book getByNo(String bookNo);

    void updateNum(String bookNo);

}
