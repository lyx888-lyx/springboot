package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.dto.UserDto;
import com.example.springboot.entity.User;
import com.example.springboot.error.ErrorCode;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.jwt.TokenService;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.OrikaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;
    private final TokenService tokenService;

    @Override
    public boolean addUser(UserDto.AddUser addUser) {
        User one = userMapper.selectOne(new QueryWrapper<User>().eq("username", addUser.getUsername()));
        if (Objects.isNull(one)) {
            addUser.setPassword(DigestUtils.md5DigestAsHex(addUser.getPassword().getBytes()));
            User user = OrikaUtil.convert(addUser, User.class);
            return userMapper.insert(user) > 0;
        }
        throw new ServiceException(ErrorCode.USER_ALREADY_EXISTS.getCode(), ErrorCode.USER_ALREADY_EXISTS.getMessage());
    }

    @Override
    public Map<String, Object> checkUserNameAndPassword(UserDto.Login login) {
        Map<String, Object> map = new HashMap<>(1);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", login.getUsername()));
        if (Objects.isNull(user)) {
            throw new ServiceException(ErrorCode.USER_NOT_EXISTS.getCode(), ErrorCode.USER_NOT_EXISTS.getMessage());
        } else {
            if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(login.getPassword().getBytes()))) {
                throw new ServiceException(ErrorCode.USER_PASSWORD_ERROR.getCode(), ErrorCode.USER_PASSWORD_ERROR.getMessage());
            } else {
                UserDto.UserVo userVo = OrikaUtil.convert(user, UserDto.UserVo.class);
                map.put("token", tokenService.getToken(user));
                map.put("userInfo", userVo);
                return map;
            }
        }
    }
}
