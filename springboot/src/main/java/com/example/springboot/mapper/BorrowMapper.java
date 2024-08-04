package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.Borrow;
import com.example.springboot.entity.Returns;
import com.example.springboot.mapper.po.BorrowReturnsCountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BorrowMapper {
    //@Select("select * from borrow")
    List<Borrow> list();

    List<Borrow> listByConditions(BaseRequest baseRequest);

    List<Returns> listReturnsByConditions(BaseRequest baseRequest);

    void save(Borrow borrow);

    void saveReturns(Returns returns);

    void delete(Integer id);

    void deleteReturns(Integer id);

    void update(Borrow borrow);

    Borrow getById(Integer id);

    List<BorrowReturnsCountPO> getCountByTimeRange(@Param("timeRange") String timeRange, @Param("type") int type);

}
