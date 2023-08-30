package com.shop.test.service;

import com.shop.test.model.Wallet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.test.request.RefundWalletRequest;
import com.shop.test.request.UpdateWalletRequest;

/**
* @author songxinming
*/
public interface WalletService extends IService<Wallet> {

    /**
     * 查询钱包数据
     * @param userId
     * @return
     */
    Wallet getData(Long userId);

    /**
     * 消费接口
     * @param updateWalletRequest
     * @return
     */
    Boolean updateMoney(UpdateWalletRequest updateWalletRequest,Long userId);

    /**
     * 退款接口
     * @param refundWalletRequest
     * @param userId
     * @return
     */
    Boolean refundMoney(RefundWalletRequest refundWalletRequest, Long userId);

    /**
     * 根据用户Id查询钱包余额
     * @param userId
     * @return
     */
    String getWalletDetail(Long userId);
}
