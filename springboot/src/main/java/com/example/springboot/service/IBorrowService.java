package com.example.springboot.service;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.Borrow;
import com.example.springboot.entity.Returns;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IBorrowService {
    List<Borrow> list();

    PageInfo<Borrow> page(BaseRequest baseRequest);

    PageInfo<Returns> pageReturns(BaseRequest baseRequest);

    void save(Borrow obj);

    void saveReturns(Returns obj);

    void delete(Integer id);

    void deleteReturns(Integer id);

    void update(Borrow obj);

    Borrow getById(Integer id);

    Object getCountByTimeRange(String timeRange);
}
