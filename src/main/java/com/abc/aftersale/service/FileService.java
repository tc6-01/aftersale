package com.abc.aftersale.service;

import com.abc.aftersale.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/17 20:05
 * @注释
 */
public interface FileService {
    List<File> upload(MultipartFile[] files, Integer orderId);
}
