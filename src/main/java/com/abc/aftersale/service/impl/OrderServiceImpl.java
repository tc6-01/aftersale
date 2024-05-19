package com.abc.aftersale.service.impl;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.File;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.entity.User;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.mapper.FileMapper;
import com.abc.aftersale.mapper.OrderMapper;
import com.abc.aftersale.mapper.UserMapper;
import com.abc.aftersale.service.OrderService;
import com.abc.aftersale.utils.DateUtil;
import com.abc.aftersale.utils.FileUtils;
import com.abc.aftersale.utils.PhoneNumberValidator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.javassist.bytecode.ByteArray;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/16 20:01
 * @注释
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    PhoneNumberValidator phoneNumberValidator;

    @Autowired
    DateUtil dateUtil;

    @Autowired
    FileUtils fileUtils;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        // 校验电话格式
        if (!phoneNumberValidator.validatePhoneNumber(orderDTO.getUserPhone(), 11)) {
            throw new ServiceException("请输入正确的电话号码！");
        }
        orderDTO.setStatus(1);
        // 添加创建时间和更新时间
        orderDTO.setCreateTime(dateUtil.getCurrentTimestamp());
        orderDTO.setUpdateTime(dateUtil.getCurrentTimestamp());
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        orderMapper.insert(order);
        return orderDTO;
    }

    @Override
    public List<OrderDTO> list(OrderDTO orderDTO) {
        if (Objects.isNull(orderDTO.getPageNum()) || Objects.isNull(orderDTO.getPageSize())) {
            throw new ServiceException("请传入当前分页以及分页大小！");
        }
        Page<Order> page = new Page<>(orderDTO.getPageNum(), orderDTO.getPageSize());
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        // 判断用户身份
        User dbUser = userMapper.selectById(orderDTO.userId);
        if (dbUser == null) {
            throw new ServiceException("当前用户不存在！");
        }
        if (dbUser.getIdentity().equals(0)) {
            // 用户查询操作
            queryWrapper.eq(orderDTO.getUserId() != null, "user_id", orderDTO.getUserId())
                    .eq(orderDTO.getStatus() != null, "status", orderDTO.getStatus());
             Page<Order> orderPage = orderMapper.selectPage(page, queryWrapper);
             List<OrderDTO> orderDTOList = new ArrayList<>();
             Long totalNum = Long.valueOf(orderMapper.selectList(queryWrapper).size());
             for (Order order: orderPage.getRecords()) {
                 OrderDTO result = new OrderDTO();
                 BeanUtils.copyProperties(order, result);
                 result.setPageNum(orderDTO.getPageNum());
                 result.setPageSize(orderDTO.getPageSize());
                 result.setTotalNum(totalNum);
                 orderDTOList.add(result);
             }
             return orderDTOList;
        } else if (dbUser.getIdentity().equals(1)) {
            // 工程师查询操作
            return null;
        }
        return null;
    }

    @Override
    public OrderDTO cancel(OrderDTO orderDTO) {
        if (Objects.isNull(orderDTO.getId())) {
            throw new ServiceException("请输入工单号！");
        }
        Order dbOrder = orderMapper.selectById(orderDTO.getId());
        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }
        if (dbOrder.getStatus().equals(1) || dbOrder.getStatus().equals(2)) {
            dbOrder.setStatus(0);
            orderDTO.setStatus(0);
            orderMapper.updateById(dbOrder);
            BeanUtils.copyProperties(dbOrder, orderDTO);
            return orderDTO;
        } else {
            throw new ServiceException("工程师已认领的工单不允许用户取消！");
        }
    }

    @Override
    public OrderDTO processDetails(OrderDTO orderDTO) {
        if (Objects.isNull(orderDTO.getId())) {
            throw new ServiceException("请输入工单号！");
        }
        Order dbOrder = orderMapper.selectById(orderDTO.getId());
        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }
        BeanUtils.copyProperties(dbOrder, orderDTO);
        // 查找file表中用户上传的故障照片
        List<byte[]> imageFileList = new ArrayList<>();
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(orderDTO.getId() != null, "order_id", orderDTO.getId())
                        .eq("file_type", 0);
        List<File> fileList = fileMapper.selectList(queryWrapper);
        for (File file : fileList) {
            // String fileName = file.getFileName();
            byte[] encryptedData = file.getFileData();
            byte[] decryptedData = fileUtils.decryptImage(encryptedData);
            // 创建 MultipartFile 对象并添加到列表中
            imageFileList.add(decryptedData);
        }
        orderDTO.setImageFileList(imageFileList);
        return orderDTO;
    }
}
