package com.jkopay.industry.transport.service.impl;

import com.jkopay.industry.transport.entity.User;
import com.jkopay.industry.transport.repository.UserMapper;
import com.jkopay.industry.transport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUser(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User createUser(User user) {
         userMapper.insert(user);
         return user;
    }
}
