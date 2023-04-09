package com.qust.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qust.seckill.pojo.Goods;
import com.qust.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author qust
 * @since 2023-03-25
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();


    /**
     * 获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoById(Long goodsId);
}
