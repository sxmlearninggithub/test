package com.shop.test.controller;

import com.shop.test.exception.BusinessException;
import com.shop.test.model.Wallet;
import com.shop.test.request.RefundWalletRequest;
import com.shop.test.request.UpdateWalletRequest;
import com.shop.test.response.BaseResponse;
import com.shop.test.response.ErrorCode;
import com.shop.test.response.ResultUtils;
import com.shop.test.service.WalletService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author songxinming
 **/
@RestController
@RequestMapping("/test/wallet")
public class WalletController {
    @Resource
    private WalletService walletService;


    /**
     * 根据用户Id查询钱包变动数据
     * @param userId
     * @return
     */
    @GetMapping("/get/detail/{userId}")
    public BaseResponse<String> getWalletDetail(@PathVariable Long userId){
        if (userId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        String walletDetail=walletService.getWalletDetail(userId);
        return ResultUtils.success(walletDetail);
    }

    /**
     * 根据用户Id查询钱包数据
     * @param userId
     * @return
     */
    @GetMapping("/get/{userId}")
    public BaseResponse<BigDecimal> getWallet(@PathVariable Long userId){
        if (userId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        Wallet wallet=walletService.getData(userId);
        return ResultUtils.success(wallet.getMoney());
    }

    /**
     * 根据用户Id消费余额
     * @param updateWalletRequest
     * @return
     */
    @PutMapping("/update/{userId}")
    public BaseResponse<Boolean> updateWallet(@RequestBody UpdateWalletRequest updateWalletRequest, @PathVariable Long userId){
        if (userId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        Boolean flag=walletService.updateMoney(updateWalletRequest,userId);
        return ResultUtils.success(flag);
    }

    /**
     * 根据用户Id退款余额
     * @param refundWalletRequest
     * @return
     */
    @PostMapping("/refund/{userId}")
    public BaseResponse<Boolean> refWallet(@RequestBody RefundWalletRequest refundWalletRequest, @PathVariable Long userId){
        if (userId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        Boolean flag=walletService.refundMoney(refundWalletRequest,userId);
        return ResultUtils.success(flag);
    }
}
