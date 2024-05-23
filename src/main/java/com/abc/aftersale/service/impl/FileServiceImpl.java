package com.abc.aftersale.service.impl;

import com.abc.aftersale.entity.File;
import com.abc.aftersale.entity.Order;
import com.abc.aftersale.exception.ServiceException;
import com.abc.aftersale.mapper.FileMapper;
import com.abc.aftersale.mapper.OrderMapper;
import com.abc.aftersale.service.FileService;
import com.abc.aftersale.utils.DateUtil;
import com.abc.aftersale.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/17 20:22
 * @注释
 */
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileMapper fileMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    DateUtil dateUtil;

    @Autowired
    FileUtils fileUtils;

    @Override
    public File upload(MultipartFile file, Integer orderId) {
        File dbFile = new File();
        try {
            if (file.getContentType().equals("image/png")) {
                dbFile.setFileName(file.getOriginalFilename());
                byte[] compressedImage = fileUtils.compressAndEncryptImage(file.getBytes());
                dbFile.setFileData(compressedImage);
                dbFile.setOrderId(orderId);
                dbFile.setFileType(0);
                dbFile.setCreateTime(dateUtil.getCurrentTimestamp());
                dbFile.setUpdateTime(dateUtil.getCurrentTimestamp());
                Order dbOrder =  orderMapper.selectById(orderId);
                if (dbOrder == null) {
                    throw new ServiceException("该订单不存在！");
                }
                if (!dbOrder.getStatus().equals(1)) {
                    throw new ServiceException("请选择用户已创建状态的订单进行确认！");
                }
                dbOrder.setStatus(2);
                orderMapper.updateById(dbOrder);
                fileMapper.insert(dbFile);
                return dbFile;
            } else {
                return dbFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }
}
