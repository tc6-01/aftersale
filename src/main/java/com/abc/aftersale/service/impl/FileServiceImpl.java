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
import java.util.ArrayList;
import java.util.List;

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
    public List<File> upload(MultipartFile[] files, Integer orderId) {
        List<File> uploadedFiles = new ArrayList<>();
        try {
            boolean flag = false; // 用于判断是用户确认，还是工程师自检视频上传
            Order dbOrder = orderMapper.selectById(orderId);
            if (dbOrder == null) {
                throw new ServiceException("该订单不存在！");
            }
            if (!dbOrder.getStatus().equals(1) && !dbOrder.getStatus().equals(5)) {
                throw new ServiceException("请选择正确状态的订单进行操作！");
            }
            for (MultipartFile file : files) {
                if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")) {
                    File dbFile = new File();
                    dbFile.setFileName(file.getOriginalFilename());
                    byte[] compressedImage = fileUtils.compressAndEncryptImage(file.getBytes());
                    dbFile.setFileData(compressedImage);
                    dbFile.setOrderId(orderId);
                    dbFile.setFileType(0);
                    dbFile.setCreateTime(dateUtil.getCurrentTimestamp());
                    dbFile.setUpdateTime(dateUtil.getCurrentTimestamp());
                    fileMapper.insert(dbFile);
                    uploadedFiles.add(dbFile);
                } else if (file.getContentType().equals("video/mp4")) {
                    flag = true;
                    File dbFile = new File();
                    dbFile.setFileName(file.getOriginalFilename());
                    byte[] compressedVideo = fileUtils.compressAndEncryptImage(file.getBytes());
                    dbFile.setFileData(compressedVideo);
                    dbFile.setOrderId(orderId);
                    dbFile.setFileType(1);
                    dbFile.setCreateTime(dateUtil.getCurrentTimestamp());
                    dbFile.setUpdateTime(dateUtil.getCurrentTimestamp());
                    int i = fileMapper.insert(dbFile);
                    if (i != 1) {
                        throw new ServiceException("数据库插入失败，请联系开发人员！");
                    }
                    uploadedFiles.add(dbFile);
                }
            }
            if (!flag) {
                dbOrder.setStatus(2);
            } else if (flag) {
                dbOrder.setStatus(6);
                // 补充消息推送功能
                dbOrder.setPayStatus(1);
            }
            orderMapper.updateById(dbOrder);
            return uploadedFiles;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }
}
