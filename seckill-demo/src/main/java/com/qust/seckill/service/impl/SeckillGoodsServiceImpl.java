package com.qust.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qust.seckill.mapper.SeckillGoodsMapper;
import com.qust.seckill.pojo.SeckillGoods;
import com.qust.seckill.service.ISeckillGoodsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒杀商品表 服务实现类
 * </p>
 *
 * @author qust
 * @since 2023-03-25
 */
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService {

}
