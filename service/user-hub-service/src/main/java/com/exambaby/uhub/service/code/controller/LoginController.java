package com.exambaby.uhub.service.code.controller;

import com.exambaby.common.core.domain.BusinessException;
import com.exambaby.common.core.dto.RedisOnlineUser;
import com.exambaby.common.core.utils.RedisKeyUtils;
import com.exambaby.common.core.utils.ThreadLocalContext;
import com.exambaby.uhub.service.code.dto.UsersDTO;
import com.exambaby.uhub.service.code.service.LoginService;
import com.exambaby.uhub.service.code.service.UsersService;
import com.exambaby.uhub.service.code.service.serviceImpl.OnlineUserServiceImpl;
import com.exambaby.uhub.service.code.service.serviceImpl.UsersServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 沐
 * @description: 登录控制器
 */
@RestController
@RequestMapping("/auth")
public class LoginController {
    /**
     * 注入服务
     */
    private final LoginService loginService;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UsersService usersService;
    private final OnlineUserServiceImpl onlineUserService;

    public LoginController(LoginService loginService, RedisTemplate<String, Object> redisTemplateObject, StringRedisTemplate redisTemplate, UsersServiceImpl usersService, OnlineUserServiceImpl onlineUserService) {
        this.loginService = loginService;
        this.redisTemplate = redisTemplateObject;
        this.stringRedisTemplate = redisTemplate;
        this.usersService = usersService;
        this.onlineUserService = onlineUserService;
    }

    /**
     * 验证码
     *
     * @return object
     */
    @GetMapping("/captcha")
    public Object captcha() {
        ThreadLocalContext.setSystemCode("userHubTest");
        return "http://dummyimage.com/100x40/dcdfe6/000000.png&text=V3Admin";
    }

    /**
     * Login API
     *
     * @param usersDTO 用户实体类
     * @return object
     */
    @PostMapping("/login")
    public Object login(@Valid @RequestBody UsersDTO usersDTO) {
        Map<String, Object> token = new HashMap<>();
//        判断系统ID是否存在
        if (usersDTO.getSystemCode() == null) {
            throw new BusinessException("网站信息缺失，请联系管理员修复", 444);
        }
        ThreadLocalContext.setSystemCode(usersDTO.getSystemCode());
        token.put("token", loginService.login(usersDTO));
        return token;
    }

    @GetMapping("/getUserInfo")
    public Map<String, Object> getUser() {
        String userId = ThreadLocalContext.getUserId();
        return usersService.getUserInfo(userId);
    }

    @GetMapping("/getPersonalInfo")
    public UsersDTO getPersonalInfo() {
        String userId = ThreadLocalContext.getUserId();
        if (userId == null) {
            throw new BusinessException("请您先登录再查看个人信息", 444);
        }
        return usersService.getPersonalInfo(userId);
    }

    @Operation(summary = "修改个人信息", description = "修改个人信息")
    @PostMapping("/modifyPersonalInfo")
    public boolean modifyPersonalInfo(@Valid @RequestBody UsersDTO usersDTO) {
        String userId = ThreadLocalContext.getUserId();
        if (userId == null) {
            throw new BusinessException("请您先登录再修改个人信息", 444);
        }
        return usersService.modifyPersonalInfo(usersDTO);
    }


    /**
     * 重置密码 API
     * @param usersDTO 用户实体类
     * @return String
     */
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody UsersDTO usersDTO) {
        return loginService.resetPassword(usersDTO);
    }

    /**
     * 注册 API
     * @param usersDTO  用户实体类
     * @return String
     */
    @PostMapping("/register")
    public String register(@RequestBody UsersDTO usersDTO) {
        return loginService.register(usersDTO);
    }

    /**
     * 修改密码 API
     * @param oldPassword 源密码
     * @param newPassword 新密码
     * @return String
     */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        return loginService.changePassword(oldPassword, newPassword);
    }


    @GetMapping("/OutLogin")
    public Object OutLogin() {
        String userId = ThreadLocalContext.getUserId();
        String systemCode = ThreadLocalContext.getSystemCode();
        String key = RedisKeyUtils.buildOnlineUserKey(systemCode, userId);

        String token = stringRedisTemplate.opsForValue().get(key);
        if (token != null) {
            stringRedisTemplate.delete(key);
        }
        return "退出登录成功";
    }



    /**
     * 获取在线用户
     * @return List<RedisOnlineUser>
     */
    @GetMapping("/online-users")
    public List<RedisOnlineUser> getOnlineUsers() {
        return onlineUserService.getAllOnlineUsers();
    }

    /**
     * 踢出登录
     * @param userId token
     * @return String
     */
    @GetMapping("/logout")
    public String logout(@RequestParam String userId) {
        onlineUserService.kickOut(userId);
        return "退出登录成功";
    }

}
