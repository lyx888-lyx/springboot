package com.example.springboot.dto;

import com.example.springboot.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.DigestUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserDto implements Serializable {

    @Data
    public static class AddUser {
        @NotBlank(message = "账号不能为空")
        private String username;
        @NotBlank(message = "密码不能为空")
        private String password;
    }

    @Data
    public static class Login {
        @NotBlank(message = "账号不能为空")
        private String username;
        @NotBlank(message = "密码不能为空")
        private String password;
    }

    @Data
    public static class UserVo {
        private String username;
    }

}
