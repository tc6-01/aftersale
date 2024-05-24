package com.abc.aftersale.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Inventory implements Serializable {
    private Integer id;

    private String inventoryName;

    private String inventoryClass;

    private Integer inventoryNumber;

    private BigDecimal inventoryPrice;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName == null ? null : inventoryName.trim();
    }

    public String getInventoryClass() {
        return inventoryClass;
    }

    public void setInventoryClass(String inventoryClass) {
        this.inventoryClass = inventoryClass == null ? null : inventoryClass.trim();
    }

    public Integer getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(Integer inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public BigDecimal getInventoryPrice() {
        return inventoryPrice;
    }

    public void setInventoryPrice(BigDecimal inventoryPrice) {
        this.inventoryPrice = inventoryPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", inventoryName=").append(inventoryName);
        sb.append(", inventoryClass=").append(inventoryClass);
        sb.append(", inventoryNumber=").append(inventoryNumber);
        sb.append(", inventoryPrice=").append(inventoryPrice);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Inventory other = (Inventory) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getInventoryName() == null ? other.getInventoryName() == null : this.getInventoryName().equals(other.getInventoryName()))
                && (this.getInventoryClass() == null ? other.getInventoryClass() == null : this.getInventoryClass().equals(other.getInventoryClass()))
                && (this.getInventoryNumber() == null ? other.getInventoryNumber() == null : this.getInventoryNumber().equals(other.getInventoryNumber()))
                && (this.getInventoryPrice() == null ? other.getInventoryPrice() == null : this.getInventoryPrice().equals(other.getInventoryPrice()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInventoryName() == null) ? 0 : getInventoryName().hashCode());
        result = prime * result + ((getInventoryClass() == null) ? 0 : getInventoryClass().hashCode());
        result = prime * result + ((getInventoryNumber() == null) ? 0 : getInventoryNumber().hashCode());
        result = prime * result + ((getInventoryPrice() == null) ? 0 : getInventoryPrice().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
