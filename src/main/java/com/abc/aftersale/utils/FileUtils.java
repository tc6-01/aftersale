package com.abc.aftersale.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Base64;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/17 20:39
 * @注释
 */
@Component
public class FileUtils {
    public byte[] compressAndEncryptImage(byte[] imageBytes) {
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);
        return base64EncodedImage.getBytes();
    }

    public byte[] decryptImage(byte[] encryptedData) {
        byte[] decryptedData = Base64.getDecoder().decode(encryptedData);
        return decryptedData;
    }
}
