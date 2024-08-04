package com.example.springboot.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.Borrow;
import com.example.springboot.entity.Returns;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.BookMapper;
import com.example.springboot.mapper.BorrowMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.mapper.po.BorrowReturnsCountPO;
import com.example.springboot.service.IBorrowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class BorrowService implements IBorrowService {

    @Autowired
    BorrowMapper borrowMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    BookMapper bookMapper;

    @Override
    public List<Borrow> list() {
        return borrowMapper.list();
    }

    @Override
    public PageInfo<Borrow> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNumber(), baseRequest.getPageSize());
        List<Borrow> borrows = borrowMapper.listByConditions(baseRequest);
        LocalDate now = LocalDate.now();
        for (Borrow borrow : borrows) {
            LocalDate returnDate = borrow.getReturnDate();
            // 判断借阅状态
            if (returnDate != null) {
                if (now.plusDays(1).isEqual(returnDate)) {
                    borrow.setNote("即将到期");
                } else if (now.isEqual(returnDate)) {
                    borrow.setNote("已到期");
                } else if (now.isAfter(returnDate)) {
                    borrow.setNote("已过期");
                } else {
                    borrow.setNote("正常");
                }
            } else {
                borrow.setNote("无还书日期");
            }
        }
        return new PageInfo<>(borrows);
    }

    @Override
    public PageInfo<Returns> pageReturns(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNumber(), baseRequest.getPageSize());
        return new PageInfo<>(borrowMapper.listReturnsByConditions(baseRequest));
    }

    @Override
    @Transactional
    public void save(Borrow obj) {
        String userId = obj.getUserId();
        User user = userMapper.getByNo(userId);
        if (Objects.isNull(user)) {
            throw new ServiceException("用户不存在");
        }
        Book book = bookMapper.getByNo(obj.getBookNo());
        if (Objects.isNull(book)) {
            throw new ServiceException("所借图书不存在");
        }
        //1.检验用户积分是否足够
        Integer account = user.getAccount();
        Integer score = book.getScore() * obj.getDays(); //score=借一本的积分*天数
        if (account < score) {
            throw new ServiceException("用户积分不足");
        }
        //2.检验图书数量是否足够
        Integer nums = book.getNums();
        if (nums < 1) {
            throw new ServiceException("图书数量不足");
        }
        //3.更新账户余额
        user.setAccount(account - score);
        userMapper.update(user);
        //4.更新图书数量
        book.setNums(nums - 1);
        bookMapper.update(book);
        //5.修改归还日期
        obj.setReturnDate(LocalDate.now().plus(obj.getDays(), ChronoUnit.DAYS));
        obj.setScore(score);
        //6.新增借书记录
        borrowMapper.save(obj);
    }

    @Transactional
    @Override
    public void saveReturns(Returns obj) {
        //1.修改状态
        obj.setStatus("已归还");
        Borrow borrow = borrowMapper.getById(obj.getId());
        borrow.setStatus("已归还");
        //2.增加图书数量
        obj.setId(null); //新数据
        bookMapper.updateNum(obj.getBookNo());
        //3.更新实际还书日期
        obj.setRealDate(LocalDate.now());
        //4.返回和扣除用户积分
        Book book = bookMapper.getByNo(obj.getBookNo());
        if (book != null) {
            long until = 0;
            if (obj.getRealDate().isBefore(obj.getReturnDate())) {
                until = obj.getRealDate().until(obj.getReturnDate(), ChronoUnit.DAYS);
            } else if (obj.getRealDate().isAfter(obj.getReturnDate())) {
                until = -obj.getReturnDate().until(obj.getRealDate(), ChronoUnit.DAYS);
            }
            int score = book.getScore() * (int) until;
            //更新借书列表
            borrow.setScore(obj.getScore() - score);
            borrowMapper.update(borrow);
            //更新还书列表
            obj.setScore(obj.getScore() - score);
            borrowMapper.saveReturns(obj);
            User user = userMapper.getByNo(obj.getUserId());
            int account = user.getAccount() + score;
            user.setAccount(account);
            if (account < 0) {
                //禁用账号
                user.setStatus(false);
            }
            userMapper.update(user);
        }


    }

    @Override
    public void delete(Integer id) {
        borrowMapper.delete(id);
    }

    public void deleteReturns(Integer id) {
        borrowMapper.delete(id);
    }

    @Override
    public void update(Borrow obj) {
        obj.setUpdatetime(new Date());
        borrowMapper.update(obj);
    }

    @Override
    public Borrow getById(Integer id) {
        return borrowMapper.getById(id);
    }

    @Override
    public Map<String, Object> getCountByTimeRange(String timeRange) {
        Map<String, Object> map = new HashMap<>();
        Date today = new Date();
        List<DateTime> dateRange;
        switch (timeRange) {
            case "week":
                //offsetDay 计算时间的一个工具方法
                //rangeToList 返回从开始时间到结束时间的一个时间范围
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -6), today, DateField.DAY_OF_WEEK);
                break;
            case "month":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -29), today, DateField.DAY_OF_MONTH);
                break;
            case "month2":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -59), today, DateField.DAY_OF_MONTH);
                break;
            case "month3":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -89), today, DateField.DAY_OF_MONTH);
                break;
            default:
                dateRange = new ArrayList<>();
                break;
        }
        //datetimeToDateStr 把Datetime类型的List转换成一个String的List
        List<String> dateStrRange = datetimeToDateStr(dateRange);
        map.put("date", dateStrRange);
        List<BorrowReturnsCountPO> borrowCount = borrowMapper.getCountByTimeRange(timeRange, 1);
        List<BorrowReturnsCountPO> returnsCount = borrowMapper.getCountByTimeRange(timeRange, 2);
        map.put("borrow", countList(borrowCount, dateStrRange));
        map.put("returns", countList(returnsCount, dateStrRange));
        return map;
    }

    private List<String> datetimeToDateStr(List<DateTime> dateTimeList) {
        List<String> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(dateTimeList)) {
            return list;
        }
        for (DateTime dateTime : dateTimeList) {
            String date = DateUtil.formatDate(dateTime);
            list.add(date);
        }
        return list;
    }

    //对数据库未统计的时间进行处理
    private Object countList(List<BorrowReturnsCountPO> countPOList, List<String> dateRange) {
        List<Integer> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(countPOList)) {
            return list;
        }
        for (String date : dateRange) {
            Integer count = countPOList.stream().filter(countPO -> date.equals(countPO.getDate())).map(BorrowReturnsCountPO::getCount).findFirst().orElse(0);
            list.add(count);
        }
        return list;
    }

}
