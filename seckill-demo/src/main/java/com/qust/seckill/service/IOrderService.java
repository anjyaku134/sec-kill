package com.qust.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qust.seckill.pojo.Order;
import com.qust.seckill.pojo.User;
import com.qust.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qust
 * @since 2023-03-25
 */
public interface IOrderService extends IService<Order> {

    /**
     * 秒杀
     * @param user
     * @param goods
     * @return
     */
    Order seckill(User user, GoodsVo goods);
}
