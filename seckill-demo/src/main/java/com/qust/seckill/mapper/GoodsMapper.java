package com.qust.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qust.seckill.pojo.Goods;
import com.qust.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author qust
 * @since 2023-03-25
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 获取商品详情
     * @return
     */
    GoodsVo findGoodsVoById(Long goodsId);
}
