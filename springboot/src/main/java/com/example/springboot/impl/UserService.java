package com.example.springboot.impl;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    public PageInfo<User> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNumber(), baseRequest.getPageSize());
        List<User> users = userMapper.listByConditions(baseRequest);
        return new PageInfo<>(users);
    }

    @Override
    public void save(User user) {
//        //自动生成卡号(日期+编号)
//        Date date = new Date();
//        user.setCardID(DateUtil.format(date,"yyyMMdd")+Math.abs(IdUtil.fastSimpleUUID().hashCode()));
        userMapper.save(user);
    }

    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }

    @Override
    public void update(User user) {
        user.setUpdatetime(new Date());
        userMapper.update(user);
    }

    @Override
    public User getById(Integer id) {
        return userMapper.getById(id);
    }

    @Override
    public void account(User user) {
        Integer score = user.getScore();
        if (score == null) {
            return;
        }
        Integer id = user.getId();
        User user1 = userMapper.getById(id);
        user1.setAccount(user1.getAccount() + score);
        userMapper.update(user1);

    }
}
