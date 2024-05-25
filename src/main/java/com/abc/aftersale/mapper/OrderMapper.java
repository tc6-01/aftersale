package com.abc.aftersale.mapper;

import com.abc.aftersale.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/16 19:51
 * @注释
 */
@Repository
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
