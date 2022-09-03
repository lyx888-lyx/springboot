package com.example.springboot.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboot.entity.User;
import org.springframework.stereotype.Service;

@Service("tokenService")
public class TokenService {

    public String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(String.valueOf(user.getId())).sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
