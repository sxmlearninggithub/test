package com.shop.test.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @TableName wallet
 */
@TableName(value ="wallet")
@Data
public class Wallet implements Serializable {
    private Long id;

    private BigDecimal money;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "spend_detail")
    private String spendDetail;

    @TableField(value = "refund_detail")
    private String refundDetail;

    @TableField(value = "bank_id")
    private Long bankId;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}