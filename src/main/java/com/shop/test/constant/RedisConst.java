package com.shop.test.constant;

import lombok.Data;

/**
 * @author songxinming
 **/
@Data
public class RedisConst {
    // 退款
    public static final String refundWalletDetail="wallet:refund:";
    // 花费
    public static final String updateWalletDetail="wallet:update:";
}
