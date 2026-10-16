package com.exambaby.uhub.service.code.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exambaby.common.core.domain.BusinessException;
import com.exambaby.common.core.dto.RedisOnlineUser;
import com.exambaby.common.core.utils.JwtUtil;
import com.exambaby.common.core.utils.PasswordUtil;
import com.exambaby.common.core.utils.ThreadLocalContext;
import com.exambaby.uhub.service.code.dto.LoginHistoryDTO;
import com.exambaby.uhub.service.code.dto.UsersDTO;
import com.exambaby.uhub.service.code.entity.SystemInfo;
import com.exambaby.uhub.service.code.entity.Users;
import com.exambaby.uhub.service.code.mapper.LoginMapper;
import com.exambaby.uhub.service.code.mapper.UsersMapper;
import com.exambaby.uhub.service.code.mapstruct.UsersMapStruct;
import com.exambaby.uhub.service.code.service.LoginHistoryService;
import com.exambaby.uhub.service.code.service.LoginService;
import com.exambaby.uhub.service.code.service.OnlineUserService;
import com.exambaby.uhub.service.code.service.SystemInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.exambaby.common.core.utils.ClientTypeUtil.resolveClientType;

@Service
public class LoginServiceImpl implements LoginService {

    private final StringRedisTemplate stringRedisTemplate;
    private final UsersMapper usersMapper;
    private final LoginMapper loginMapper;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final OnlineUserService onlineUserService;
    private final LoginHistoryService loginHistoryService;
    private final SystemInfoService systemInfoService;
    private final UsersMapStruct usersMapStruct;
    public LoginServiceImpl(StringRedisTemplate stringRedisTemplate, UsersMapper usersMapper, LoginMapper loginMapper, JwtUtil jwtUtil, RedisTemplate<String, Object> redisTemplate, OnlineUserService onlineUserService, LoginHistoryService loginHistoryService, SystemInfoService systemInfoService, UsersMapStruct usersMapStruct) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.usersMapper = usersMapper;
        this.loginMapper = loginMapper;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.onlineUserService = onlineUserService;
        this.loginHistoryService = loginHistoryService;
        this.systemInfoService = systemInfoService;
        this.usersMapStruct = usersMapStruct;
    }

    /**
     * 登录
     * @param usersDTO 用户信息
     * @return token
     */
    @Override
    public String login(UsersDTO usersDTO) {
        //判断用户系统代码是否正常
        SystemInfo systemInfo = systemInfoService.getOne(new LambdaQueryWrapper<SystemInfo>().eq(SystemInfo::getCode, usersDTO.getSystemCode()));
        if (systemInfo == null) {
            throw new BusinessException("网站信息错误，请联系管理员及时修复", 444);
        }
        ThreadLocalContext.setSystemId(systemInfo.getId());
        if (systemInfo.getStatus() == 0) {
            throw new BusinessException("网站已被暂时关闭，请联系管理员及时修复",444);
        }

//
        if (usersDTO.getUsername() == null || usersDTO.getPassword() == null) {
            throw new BusinessException("用户名或密码不能为空",444);
        }
        // 1. 根据用户名查询用户
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, usersDTO.getUsername());
        Users dbUser = usersMapper.selectOne(queryWrapper);


        // 判断用户是否存在
        if (dbUser == null) {
            throw new BusinessException("您输入的用户不存在",444);
        }
        //判断用户是否被锁定：0-未锁定，1-已锁定
        if (dbUser.getIsLocked() == 1) {
            throw new BusinessException("您的账号已被禁用",444);
        }
        // 校验密码
        if (!PasswordUtil.matches(usersDTO.getPassword(), dbUser.getPassword())) {
            throw new BusinessException("您输入的密码错误",444);
        }

        //根据系统代码：用户id构建jwt token
        String dbUserId = String.valueOf(dbUser.getId());
        String token = jwtUtil.generateToken(dbUserId, List.of("admin", "user"), String.valueOf(systemInfo.getCode()));

        // Redis 保存用户在线信息
        String ip = ThreadLocalContext.getIp();
        String userAgent = ThreadLocalContext.getUserAgent();
        String clientType = resolveClientType(userAgent); // 可选：解析为 PC / Mobile

        RedisOnlineUser onlineUser = RedisOnlineUser.builder()
                .userId(dbUser.getId())
                .systemCode(systemInfo.getCode())
                .username(usersDTO.getUsername())
                .ip(ip)
                .userAgent(userAgent)
                .clientType(clientType)
                .token(token)
                .loginTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        onlineUserService.save(onlineUser,systemInfo.getCode() ,1, TimeUnit.DAYS);

//        保存登录日志
        LoginHistoryDTO loginHistoryDTO = new LoginHistoryDTO();
        loginHistoryDTO.setUserId(dbUser.getId());
        loginHistoryDTO.setSystemId(systemInfo.getId());
        loginHistoryDTO.setUsername(dbUser.getUsername());
        loginHistoryDTO.setLoginTime(LocalDateTime.now());
        loginHistoryDTO.setIpAddress(ip);
        loginHistoryDTO.setUserAgent(userAgent);
        loginHistoryDTO.setClientType(clientType);
        loginHistoryService.saveLoginHistory(loginHistoryDTO);

        // 返回token
        return token;
    }

    @Override
    public String resetPassword(UsersDTO usersDTO) {
        // 1. 根据用户名查询用户是否存在
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, usersDTO.getUsername());
        Users dbUser = usersMapper.selectOne(queryWrapper);

        if (dbUser == null) {
            throw new BusinessException("用户不存在", 444);
        }

        // 2. 验证邮箱和手机号
        if (!dbUser.getEmail().equals(usersDTO.getEmail()) || !dbUser.getPhoneNumber().equals(usersDTO.getPhoneNumber())) {
            throw new BusinessException("邮箱或手机号不正确", 444);
        }

        // 3. 重置密码为12345678
        dbUser.setPassword("12345678");
        usersMapper.updateById(dbUser);

        return "密码重置成功";
    }

    @Override
    public String changePassword(String oldPassword, String newPassword) {
        String userId = ThreadLocalContext.getUserId();
        
        Users dbUser = usersMapper.selectById((Serializable) userId);
        if (dbUser == null) {
            throw new BusinessException("用户不存在", 444);
        }

        if (!dbUser.getPassword().equals(oldPassword)) {
            throw new BusinessException("原密码错误", 444);
        }

        dbUser.setPassword(newPassword);
        usersMapper.updateById(dbUser);

        return "密码修改成功";
    }

    @Override
    public String register(UsersDTO usersDTO) {
//        1. 设置系统代码
        ThreadLocalContext.setSystemCode(usersDTO.getSystemCode());
//        1. 根据用户名查询用户是否存在
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, usersDTO.getUsername());
        Users dbUser = usersMapper.selectOne(queryWrapper);
        if (dbUser != null) {
            throw new BusinessException("用户名已存在", 444);
        }
//        2. 密码加密
        usersDTO.setPassword(PasswordUtil.encode(usersDTO.getPassword()));
//        3. 保存用户信息
        int i = usersMapper.insert(usersMapStruct.toEntity(usersDTO));
//        4. 返回结果信息
        if (i > 0) {
            return "注册成功";
        }
        throw new BusinessException("注册失败", 444);
    }

}
