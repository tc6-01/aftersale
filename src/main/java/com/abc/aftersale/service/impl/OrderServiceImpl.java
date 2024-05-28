package com.abc.aftersale.service.impl;

import com.abc.aftersale.dto.InventoryDTO;
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
import com.abc.aftersale.utils.HttpUtils;
import com.abc.aftersale.utils.PhoneNumberValidator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.http.HttpResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.*;

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

    final Integer PAY = 6; //复检

    final Integer RETURNUSER = 7; //设备返还

    private final String REDIS_KEY = "key:";

    private final Integer REDIS_EXPIRE = 5 * 60;

    // 手工费
    private final Integer SERVICE_CHARGES = 50;

    // 物料rate
    private final String RATE = "1.2";

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

    @Autowired
    private Jedis jedis;

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
        if (orderDTO.getEngineerId() != null) {
            User engineer = userMapper.selectById(orderDTO.getEngineerId());
            orderDTO.setEngineerName(engineer.getName());
        }
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

        long isExist = jedis.setnx(REDIS_KEY + orderId, "");
        if (isExist == 0) {

            jedis.expire(REDIS_KEY + orderId, REDIS_EXPIRE);

            // 更新
            Order dbOrder = orderMapper.selectById(orderId);

            if (dbOrder == null) {
                throw new ServiceException("当前工单不存在！");
            }


            if (dbOrder.getStatus().equals(USER_CLAIM)){
                // 修改工单状态，添加工程师信息
                dbOrder.setStatus(CLAIM);
                dbOrder.setEngineerId(engineerId);

            orderMapper.updateById(dbOrder);
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(dbOrder, orderDTO);

                jedis.del(REDIS_KEY + orderId);

                return orderDTO;
            }else{
                jedis.del(REDIS_KEY + orderId);
                throw new ServiceException("当前工单前置工作未确认，无法修改状态。");
            }


        } else {
            // 跳过
            throw new ServiceException("其他工程师在处理当前订单！");
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
        Order dbOrder = orderMapper.selectById(orderId);
        if (dbOrder == null) {
            throw new ServiceException("当前工单不存在！");
        }
        if (dbOrder.getStatus().equals(CLAIM)){
            if (isFaulty){
                // 修改工单状态为“设备维修中”，添加维修信息
                dbOrder.setStatus(MAINTENANCE);

                dbOrder.setEngineerId(engineerId);
                dbOrder.setEngineerDesc(desc);

                orderMapper.updateById(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }else {
                // 返还待确认
                dbOrder.setStatus(RETURNUSER);

                dbOrder.setEngineerId(engineerId);
                // 没有进行维修，需要返还理由
                dbOrder.setEngineerDesc(desc);

                orderMapper.updateById(dbOrder);
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
        Order dbOrder = orderMapper.selectById(orderId);
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

                orderMapper.updateById(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;

            }else{
                // 修改状态为人工复检中
                dbOrder.setStatus(REINSPECTION);
                dbOrder.setEngineerId(engineerId);

                orderMapper.updateById(dbOrder);
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(dbOrder, orderDTO);

                return orderDTO;
            }
        }else{
            throw new ServiceException("当前工单未进行维修确认，申请物料失败。");
        }
    }

    @Override
    public void smsSend(String mobile, String templateId, String smsSignId) {


        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "200d16ddf8c54fd0b1a73255caec3d9b";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("param", "");

//smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html

        // 由于没有资质，smsSignId和templateId暂时不设置
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

//    @Override
//    public OrderDTO costPush(Integer orderId, Integer engineerId, Boolean isMaterial, InventoryDTO inventoryDTO) {
//        Order dbOrder = orderMapper.selectById(orderId);
//        if (dbOrder == null) {
//            throw new ServiceException("当前工单不存在！");
//        }
//        if (dbOrder.getStatus().equals(REINSPECTION)){
//            if (isMaterial){
//                // 计算费用, price * num * 1.2 + 手工费SERVICE_CHARGES
//                BigDecimal price = inventoryDTO.getInventoryPrice();
//                BigDecimal num = BigDecimal.valueOf(inventoryDTO.getInventoryNumber());
//                BigDecimal rate = new BigDecimal(RATE);
//                BigDecimal fee = price.multiply(num).multiply(rate);
//                BigDecimal finalPrice = fee.add(new BigDecimal(SERVICE_CHARGES));
//
//                dbOrder.setPredCost(finalPrice);
//
//                // 修改状态为费用待支付
//                dbOrder.setStatus(PAY);
//                dbOrder.setEngineerId(engineerId);
//
//                orderMapper.updateById(dbOrder);
//                OrderDTO orderDTO = new OrderDTO();
//                BeanUtils.copyProperties(dbOrder, orderDTO);
//
//                return orderDTO;
//
//            }else{
//
//                dbOrder.setPredCost(new BigDecimal(SERVICE_CHARGES));
//
//                // 修改状态为费用待支付
//                dbOrder.setStatus(PAY);
//                dbOrder.setEngineerId(engineerId);
//
//                orderMapper.updateById(dbOrder);
//                OrderDTO orderDTO = new OrderDTO();
//                BeanUtils.copyProperties(dbOrder, orderDTO);
//
//                return orderDTO;
//            }
//        }else{
//            throw new ServiceException("当前工单未进行复检确认，费用推送失败。");
//        }
//    }
}
