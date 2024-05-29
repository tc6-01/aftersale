package com.abc.aftersale.service.impl;

import com.abc.aftersale.dto.UserDTO;
import com.abc.aftersale.entity.User;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.mapper.UserMapper;
import com.abc.aftersale.service.UserService;
import com.abc.aftersale.utils.DateUtil;
import com.abc.aftersale.utils.MichatIdGenerator;
import com.abc.aftersale.utils.PhoneNumberValidator;
import com.abc.aftersale.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public UserDTO loginService(UserDTO userDTO) {
        User dbUser = userMapper.searchByLoginName(userDTO.getLoginName());
        if (dbUser == null) {
            throw new ServiceException("账号不存在！");
        }
        if (!userDTO.getPassword().equals(dbUser.getPassword()) || !userDTO.getIdentity().equals(dbUser.getIdentity())) {
            throw new ServiceException("密码或身份错误！");
        }
        // 生成token
        String token = TokenUtils.createToken(dbUser.getId().toString(), dbUser.getPassword());
        BeanUtils.copyProperties(dbUser, userDTO);
        userDTO.setToken(token);
        return userDTO;
    }

    @Override
    public UserDTO registerService(UserDTO userDTO) {
        User dbUser = userMapper.searchByLoginName(userDTO.getLoginName());
        if (dbUser != null) {
            throw new ServiceException("用户名已存在！");
        }
        // 校验电话格式
        if (!phoneNumberValidator.validatePhoneNumber(userDTO.getPhone(), 11)) {
            throw new ServiceException("请输入正确的电话号码！");
        }

        // 只开放用户的注册
        userDTO.setIdentity(0);

        // 随机生成十位米聊号
        List<User> resultList = userMapper.searchMichatIds();
        List<String> michatIds = new ArrayList<>();
        for (User result : resultList) {
            michatIds.add(result.getMichatId());
        }
        System.out.print(michatIds);
        String michatId = michatIdGenerator.generatorMichatId(michatIds);
        userDTO.setMichatId(michatId);

        // 添加创建时间和更新时间
        userDTO.setCreateTime(dateUtil.getCurrentTimestamp());
        userDTO.setUpdateTime(dateUtil.getCurrentTimestamp());

        userDTO.setStatus(1);

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        try {
            userMapper.insert(user);
        } catch (Exception e){
            throw new ServiceException("数据库插入失败，请联系开发人员");
        }
        // 生成token
        dbUser = userMapper.searchByLoginName(userDTO.getLoginName());
        String token = TokenUtils.createToken(dbUser.getId().toString(), user.getPassword());
        userDTO.setId(dbUser.getId());
        userDTO.setToken(token);
        return userDTO;
    }
}
