package com.shop.test.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author songxinming
 **/
@Data
public class RefundWalletRequest {
    private BigDecimal refundMoney;
    private String refundDetail;

}
