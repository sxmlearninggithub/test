package com.shop.test.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author songxinming
 **/
@Data
public class UpdateWalletRequest {
    private BigDecimal spendMoney;
    private String spendDetail;
}
