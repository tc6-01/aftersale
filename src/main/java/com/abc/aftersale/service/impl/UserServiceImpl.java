package com.abc.aftersale.service.impl;

import com.abc.aftersale.entity.User;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.mapper.UserMapper;
import com.abc.aftersale.service.UserService;
import com.abc.aftersale.utils.DateUtil;
import com.abc.aftersale.utils.MichatIdGenerator;
import com.abc.aftersale.utils.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/14 23:35
 * @注释
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    MichatIdGenerator michatIdGenerator;

    @Autowired
    PhoneNumberValidator phoneNumberValidator;

    @Autowired
    DateUtil dateUtil;

    @Override
    public User loginService(User user) {
        User dbUser = userMapper.searchByLoginName(user.getLoginName());
        if (dbUser == null) {
            throw new ServiceException("账号不存在！");
        }
        if (!user.getPassword().equals(dbUser.getPassword()) || !user.getIdentity().equals(dbUser.getIdentity())) {
            throw new ServiceException("密码或身份错误！");
        }
        return dbUser;
    }

    @Override
    public User registerService(User user) {
        User dbUser = userMapper.searchByLoginName(user.getLoginName());
        if (dbUser != null) {
            throw new ServiceException("用户名已存在！");
        }
        // 校验电话格式
        if (!phoneNumberValidator.validatePhoneNumber(user.getPhone(), 11)) {
            throw new ServiceException("请输入正确的电话号码！");
        }

        // 只开放用户的注册
        user.setIdentity(0);

        // 随机生成十位米聊号
        List<User> resultList = userMapper.searchMichatIds();
        List<String> michatIds = new ArrayList<>();
        for (User result : resultList) {
            michatIds.add(result.getMichatId());
        }
        System.out.print(michatIds);
        String michatId = michatIdGenerator.generatorMichatId(michatIds);
        user.setMichatId(michatId);

        // 添加创建时间和更新时间
        user.setCreateTime(dateUtil.getCurrentTimestamp());
        user.setUpdateTime(dateUtil.getCurrentTimestamp());

        user.setStatus(1);

        userMapper.insert(user);
        return user;
    }
}
