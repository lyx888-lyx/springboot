package com.example.springboot.controller;

import com.example.springboot.anno.PassToken;
import com.example.springboot.anno.UserLoginToken;
import com.example.springboot.common.ResponseResult;
import com.example.springboot.constant.HttpStatus;
import com.example.springboot.dto.UserDto;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PassToken
    @PostMapping("/addUser")
    public ResponseResult addUser(@RequestBody @Validated UserDto.AddUser user) {
        ResponseResult result = new ResponseResult();
        try {
            userService.addUser(user);
            result.setMessage("注册成功").setCode(HttpStatus.SUCCESS);
        } catch (ServiceException e) {
            result.setMessage(e.getMessage()).setCode(e.getCode());
        }
        return result;
    }

    @PassToken
    @PostMapping("/login")
    public ResponseResult login(@RequestBody @Validated UserDto.Login login) {
        ResponseResult result = new ResponseResult();
        try {
            Map<String, Object> user = userService.checkUserNameAndPassword(login);
            result.setMessage("登陆成功").setData(user).setCode(HttpStatus.SUCCESS);
        } catch (ServiceException e) {
            result.setMessage(e.getMessage()).setCode(e.getCode());
        }
        return result;
    }

    @GetMapping("/test")
    @UserLoginToken
    public String test() {
        return "无权限";
    }
}
