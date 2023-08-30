package com.shop.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.test.constant.RedisConst;
import com.shop.test.exception.BusinessException;
import com.shop.test.mapper.WalletMapper;
import com.shop.test.model.Wallet;
import com.shop.test.model.WalletDetail;
import com.shop.test.request.RefundWalletRequest;
import com.shop.test.request.UpdateWalletRequest;
import com.shop.test.response.ErrorCode;
import com.shop.test.service.WalletService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
* @author songxinming
*/
@Service
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements WalletService {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据用户Id
     * @param userId
     * @return
     */
    @Override
    public Wallet getData(Long userId) {
        QueryWrapper<Wallet> walletQueryWrapper = getWalletQueryWrapper(userId);
        Wallet wallet = baseMapper.selectOne(walletQueryWrapper);
        if (wallet==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return wallet;
    }


    private static QueryWrapper<Wallet> getWalletQueryWrapper(Long userId) {
        QueryWrapper<Wallet> walletQueryWrapper = new QueryWrapper<>();
        walletQueryWrapper.eq("user_id", userId);
        return walletQueryWrapper;
    }

    /**
     * 消费接口
     * @param updateWalletRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMoney(UpdateWalletRequest updateWalletRequest,Long userId) {
        QueryWrapper<Wallet> walletQueryWrapper = getWalletQueryWrapper(userId);
        Wallet wallet = baseMapper.selectOne(walletQueryWrapper);
        // 获取余额
        BigDecimal money = wallet.getMoney();
        // 获取数据
        BigDecimal spendMoney = updateWalletRequest.getSpendMoney();
        String spendDetail = updateWalletRequest.getSpendDetail();
        wallet.setMoney(money.subtract(spendMoney));
        wallet.setSpendDetail(spendDetail);
        wallet.setUpdateTime(new Date());
        int result = baseMapper.updateById(wallet);
        WalletDetail walletDetail=new WalletDetail();
        walletDetail.setUserId(userId);
        walletDetail.setMoney(spendMoney);
        walletDetail.setWalletDetail(spendDetail);
        //设置缓存Key
        String walletDetailKey = RedisConst.updateWalletDetail;
        // 设置相关数据 todo redis持久化 rdb aof
        redisTemplate.opsForHash().put(walletDetailKey,userId.toString(),walletDetail);
        return result>0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean refundMoney(RefundWalletRequest refundWalletRequest, Long userId) {
        QueryWrapper<Wallet> walletQueryWrapper = getWalletQueryWrapper(userId);
        Wallet wallet = baseMapper.selectOne(walletQueryWrapper);
        // 获取余额
        BigDecimal money = wallet.getMoney();
        BigDecimal refundMoney = refundWalletRequest.getRefundMoney();
        String refundDetail = refundWalletRequest.getRefundDetail();

        wallet.setMoney(money.add(refundMoney));
        wallet.setRefundDetail(refundDetail);
        wallet.setUpdateTime(new Date());
        // 将钱包变化情况保存到redis
        int result = baseMapper.updateById(wallet);
        WalletDetail walletDetail=new WalletDetail();
        walletDetail.setUserId(userId);
        walletDetail.setMoney(refundMoney);
        walletDetail.setWalletDetail(refundDetail);
        //设置缓存Key
        String walletDetailKey = RedisConst.refundWalletDetail;
        // 设置相关数据 todo redis持久化
        redisTemplate.opsForHash().put(walletDetailKey,userId.toString(),walletDetail);
        return result>0;
    }

    /**
     * 根据用户Id查询相关数据
     * @param userId 用户Id
     * @return
     */
    @Override
    public String getWalletDetail(Long userId) {
        StringBuilder stringBuilder=new StringBuilder();
        // 花费的钱
        String updateWalletDetail = RedisConst.updateWalletDetail;
        // 退款的
        String refundWalletDetailKey = RedisConst.refundWalletDetail;
        // 获取redis中数据
        WalletDetail updatewalletDetail = (WalletDetail) redisTemplate.opsForHash().get(updateWalletDetail,userId.toString());
        // 退款数据
        WalletDetail refundWalletDetail = (WalletDetail) redisTemplate.opsForHash().get(refundWalletDetailKey,userId.toString());

        if (refundWalletDetail!=null){
            BigDecimal refmoney = refundWalletDetail.getMoney();
            String refwalletDetail = refundWalletDetail.getWalletDetail();
            stringBuilder.append("用户").append(userId).append("退款了").append(refmoney).append("。详情是:").append(refwalletDetail);
        }

        if (updatewalletDetail != null){
            BigDecimal money = updatewalletDetail.getMoney();
            String walletDetail = updatewalletDetail.getWalletDetail();
            stringBuilder.append("用户").append(userId).append("花费了").append(money).append("。详情是:").append(walletDetail).append(",");
        }
        return stringBuilder.toString();
    }
}




