package com.shop.test.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author songxinming
 * 钱包明细
 **/
@Data
public class WalletDetail {
    private Long userId;
    private BigDecimal money;
    private String walletDetail;
}
