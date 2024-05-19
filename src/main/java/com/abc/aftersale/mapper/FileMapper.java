package com.abc.aftersale.mapper;

import com.abc.aftersale.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/17 20:04
 * @注释
 */
@Repository
@Mapper
public interface FileMapper extends BaseMapper<File> {
}
