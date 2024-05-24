package com.abc.aftersale.mapper;

import com.abc.aftersale.entity.Inventory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Inventory record);

    int insertSelective(Inventory record);


    Inventory selectByPrimaryKey(Integer id);



    int updateByPrimaryKeySelective(Inventory record);

    int updateByPrimaryKey(Inventory record);
}