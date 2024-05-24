package com.abc.aftersale.service.impl;

import com.abc.aftersale.dto.OrderDTO;
import com.abc.aftersale.entity.File;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.entity.User;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.mapper.FileMapper;
import com.abc.aftersale.mapper.OrderMapper;
import com.abc.aftersale.mapper.UserMapper;
import com.abc.aftersale.service.InventoryService;
import com.abc.aftersale.service.OrderService;
import com.abc.aftersale.utils.DateUtil;
import com.abc.aftersale.utils.FileUtils;
import com.abc.aftersale.utils.PhoneNumberValidator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    final Integer USER_CLAIM = 2; //用户确认

    final Integer CLAIM = 3; //工程师确认

    final Integer MAINTENANCE = 4; //维修

    final Integer REINSPECTION = 5; //复检

    final Integer RETURNUSER = 7; //复检

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

    @Autowired
    InventoryService inventoryService;

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
            List<OrderDTO> orderDTOList = new ArrayList<>();
            if (orderDTO.getStatus() != null) {
                // 根据工单状态查询
                if (orderDTO.getStatus() <= 2) {
                    queryWrapper.eq(orderDTO.getStatus() != null, "status", orderDTO.getStatus());
                } else {
                    queryWrapper.eq(orderDTO.getUserId() != null, "engineer_id", orderDTO.getUserId())
                            .eq(orderDTO.getStatus() != null, "status", orderDTO.getStatus());
                }
                Page<Order> orderPage = orderMapper.selectPage(page, queryWrapper);
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
            } else {
                // 查询已取消，已创建，已确认，以及该工程师认领的工单，目前分两次查询，待改进
                queryWrapper.le("status", 2);
                Long totalNum = 0L;
                Page<Order> orderPage = orderMapper.selectPage(page, queryWrapper);
                totalNum += Long.valueOf(orderMapper.selectList(queryWrapper).size());
                Page<Order> pageTwo = new Page<>(orderDTO.getPageNum(), orderDTO.getPageSize());
                QueryWrapper<Order> queryWrapperTwo = new QueryWrapper<>();
                queryWrapperTwo.eq(orderDTO.getUserId() != null, "engineer_id", orderDTO.getUserId())
                        .gt("status", 2);
                totalNum += Long.valueOf(orderMapper.selectList(queryWrapperTwo).size());
                Page<Order> orderPageTwo = orderMapper.selectPage(pageTwo, queryWrapperTwo);
                for (Order order: orderPage.getRecords()) {
                    OrderDTO result = new OrderDTO();
                    BeanUtils.copyProperties(order, result);
                    result.setPageNum(orderDTO.getPageNum());
                    result.setPageSize(orderDTO.getPageSize());
                    result.setTotalNum(totalNum);
                    orderDTOList.add(result);
                }
                for (Order order: orderPageTwo.getRecords()) {
                    OrderDTO result = new OrderDTO();
                    BeanUtils.copyProperties(order, result);
                    result.setPageNum(orderDTO.getPageNum());
                    result.setPageSize(orderDTO.getPageSize());
                    result.setTotalNum(totalNum);
                    orderDTOList.add(result);
                }
                return orderDTOList;
            }
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

    /**
     *
     * @param orderId
     * @param engineerId
     * @return orderDTO
     * 工程师确认工单
     * 工单状态变更："用户已确认--2" ----> "工程师已接单--3"
     */
    @Override
    public OrderDTO accept(Integer orderId, Integer engineerId) {
        Order dbOrder = orderMapper.selectByPrimaryKey(orderId);

        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }


        if (dbOrder.getStatus().equals(USER_CLAIM)){
            // 修改工单状态，添加工程师信息
            dbOrder.setStatus(CLAIM);
            dbOrder.setEngineerId(engineerId);

            orderMapper.updateByPrimaryKeySelective(dbOrder);
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(dbOrder, orderDTO);

            return orderDTO;
        }else{
            throw new ServiceException("当前工单前置工作未确认，无法修改状态。");
        }
    }

    /**
     *  人工检修
     *  状态变更："工程师已接单--3" ----> "设备维修中--4"
     *          "工程师已接单--3" ----> "返还待确认--7"
     * @param orderId
     * @param engineerId
     * @param isFaulty 是否需要维修
     * @param desc 维修：故障检测结果；不维修：返还理由
     * @return
     */
    @Override
    public OrderDTO maintenance(Integer orderId, Integer engineerId, Boolean isFaulty, String desc) {
        Order dbOrder = orderMapper.selectByPrimaryKey(orderId);
        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }
        if (dbOrder.getStatus().equals(CLAIM)){
            if (isFaulty){
                // 修改工单状态为“设备维修中”，添加维修信息
                dbOrder.setStatus(MAINTENANCE);

                dbOrder.setEngineerId(engineerId);
                dbOrder.setEngineerDesc(desc);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }else {
                // 返还待确认
                dbOrder.setStatus(RETURNUSER);

                dbOrder.setEngineerId(engineerId);
                // 没有进行维修，需要返还理由
                dbOrder.setEngineerDesc(desc);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }
        }else{
            throw new ServiceException("当前工单工程师未认领，无法修改状态。");
        }

    }

    /**
     * 申请物料
     * 状态变更：'设备维修中--4"  ----> "人工复检中--5"
     * 申请物料暂时由工程师直接进行物料表的增减
     * @param orderId
     * @param engineerId
     * @param isMaterial 是否需要物料
     * @param inventoryDTO 物料增减表单
     * @return
     */
    @Override
    public OrderDTO apply(Integer orderId, Integer engineerId, Boolean isMaterial, InventoryDTO inventoryDTO) {
        Order dbOrder = orderMapper.selectByPrimaryKey(orderId);
        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }
        if (dbOrder.getStatus().equals(MAINTENANCE)){
            if (isMaterial){
                // 申请物料
                Integer num = -inventoryDTO.getInventoryNumber();
                inventoryDTO.setInventoryNumber(num);
                inventoryService.addInventory(engineerId, inventoryDTO);

                // 修改状态为人工复检中
                dbOrder.setStatus(REINSPECTION);
                dbOrder.setEngineerId(engineerId);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;

            }else{
                // 修改状态为人工复检中
                dbOrder.setStatus(REINSPECTION);
                dbOrder.setEngineerId(engineerId);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }
        }else{
            throw new ServiceException("当前工单未进行维修确认，申请物料失败。");
        }
    }

    /**
     *
     * @param orderId
     * @param engineerId
     * @return orderDTO
     * 工程师确认工单
     * 工单状态变更："用户已确认--2" ----> "工程师已接单--3"
     */
    @Override
    public OrderDTO orderAccept(Integer orderId, Integer engineerId) {
        Order dbOrder = orderMapper.selectByPrimaryKey(orderId);

        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }


        if (dbOrder.getStatus().equals(USER_CLAIM)){
            // 修改工单状态，添加工程师信息
            dbOrder.setStatus(CLAIM);
            dbOrder.setEngineerId(engineerId);

            orderMapper.updateByPrimaryKeySelective(dbOrder);
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(dbOrder, orderDTO);

            return orderDTO;
        }else{
            throw new ServiceException("当前工单前置工作未确认，无法修改状态。");
        }


    }

    /**
     *  人工检修
     *  状态变更："工程师已接单--3" ----> "设备维修中--4"
     *          "工程师已接单--3" ----> "返还待确认--7"
     * @param orderId
     * @param engineerId
     * @param isFaulty 是否需要维修
     * @param desc 维修：故障检测结果；不维修：返还理由
     * @return
     */
    @Override
    public OrderDTO maintenance(Integer orderId, Integer engineerId, Boolean isFaulty, String desc) {
        Order dbOrder = orderMapper.selectByPrimaryKey(orderId);
        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }
        if (dbOrder.getStatus().equals(CLAIM)){
            if (isFaulty){
                // 修改工单状态为“设备维修中”，添加维修信息
                dbOrder.setStatus(MAINTENANCE);

                dbOrder.setEngineerId(engineerId);
                dbOrder.setEngineerDesc(desc);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }else {
                // 返还待确认
                dbOrder.setStatus(RETURNUSER);

                dbOrder.setEngineerId(engineerId);
                // 没有进行维修，需要返还理由
                dbOrder.setEngineerDesc(desc);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }
        }else{
            throw new ServiceException("当前工单工程师未认领，无法修改状态。");
        }

    }

    /**
     * 申请物料
     * 状态变更：'设备维修中--4"  ----> "人工复检中--5"
     * 申请物料暂时由工程师直接进行物料表的增减
     * @param orderId
     * @param engineerId
     * @param isMaterial 是否需要物料
     * @param inventoryDTO 物料增减表单
     * @return
     */
    @Override
    public OrderDTO apply(Integer orderId, Integer engineerId, Boolean isMaterial, InventoryDTO inventoryDTO) {
        Order dbOrder = orderMapper.selectByPrimaryKey(orderId);
        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }
        if (dbOrder.getStatus().equals(MAINTENANCE)){
            if (isMaterial){
                // 申请物料
                Integer num = -inventoryDTO.getInventoryNumber();
                inventoryDTO.setInventoryNumber(num);
                inventoryService.addInventory(engineerId, inventoryDTO);

                // 修改状态为人工复检中
                dbOrder.setStatus(REINSPECTION);
                dbOrder.setEngineerId(engineerId);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;

            }else{
                // 修改状态为人工复检中
                dbOrder.setStatus(REINSPECTION);
                dbOrder.setEngineerId(engineerId);

                orderMapper.updateByPrimaryKeySelective(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }
        }else{
            throw new ServiceException("当前工单未进行维修确认，申请物料失败。");
        }
    }
}
