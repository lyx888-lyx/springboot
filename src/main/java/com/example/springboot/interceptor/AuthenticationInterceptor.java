package com.example.springboot.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springboot.anno.PassToken;
import com.example.springboot.anno.UserLoginToken;
import com.example.springboot.entity.User;
import com.example.springboot.error.ErrorCode;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 拦截器 获取 token 并验证 token
 * @author liyuxin
 */
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        // 不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 检查是否有passToken注释
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        // 检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (Objects.isNull(token)) {
                    throw new ServiceException(ErrorCode.USER_TOKEN_NOT_EXISTS.getCode(),
                            ErrorCode.USER_TOKEN_NOT_EXISTS.getMessage());
                }
                // 获取 token 中的 userId
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException e) {
                    throw new RuntimeException("401");
                } catch (ServiceException e) {
                    throw new ServiceException(ErrorCode.USER_ROLE_REFUSED.getCode(),
                            ErrorCode.USER_ROLE_REFUSED.getMessage());
                }
                User user = userService.getById(userId);
                if (Objects.isNull(user)) {
                    throw new ServiceException(ErrorCode.USER_NOT_EXISTS.getCode(),
                            ErrorCode.USER_NOT_EXISTS.getMessage());
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException(e);
                } catch (ServiceException e) {
                    throw new ServiceException(ErrorCode.USER_ROLE_REFUSED.getCode(),
                            ErrorCode.USER_ROLE_REFUSED.getMessage());
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
