package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.dto.UserDto;
import com.example.springboot.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    boolean addUser(UserDto.AddUser user);

    Map<String, Object> checkUserNameAndPassword(UserDto.Login login);
}
