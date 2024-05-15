package com.abc.aftersale.mapper;

import com.abc.aftersale.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/14 23:18
 * @注释
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User searchByLoginName(String loginName);

    List<User> searchMichatIds();
}
