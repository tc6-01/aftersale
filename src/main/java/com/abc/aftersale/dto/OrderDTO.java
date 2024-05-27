package com.abc.aftersale.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @version 1.0
 * @Author wzh
 * @Date 2024/5/16 19:53
 * @注释
 */
@Data
public class OrderDTO extends CommonQueryDTO implements Serializable {
    public Integer id;

    public Integer userId;

    public String userName;

    public String userPhone;

    public String userAddress;

    public String productInfo;

    public String snInfo;

    public Integer status;

    public String userDesc;

    public String invoiceInfo;

    public Integer engineerId;

    public String engineerDesc;

    public BigDecimal predCost;

    public BigDecimal realCost;

    public Timestamp createTime;

    public Timestamp updateTime;

    public List<byte[]> imageFileList;

    public String engineerName;

    public byte[] videoFile;
}
