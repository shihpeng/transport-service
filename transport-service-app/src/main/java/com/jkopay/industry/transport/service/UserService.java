package com.jkopay.industry.transport.service;

import com.jkopay.industry.transport.entity.User;

public interface UserService {

    User getUser(Long id);

    User createUser(User user);
}
