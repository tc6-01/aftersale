package com.abc.aftersale.mapper;

import com.abc.aftersale.entity.Inventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

    int insertSelective(Inventory record);

    Inventory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Inventory record);

    Inventory findByInventoryNameAndClass(@Param("inventoryName") String inventoryName, @Param("inventoryClass") String inventoryClass);
}