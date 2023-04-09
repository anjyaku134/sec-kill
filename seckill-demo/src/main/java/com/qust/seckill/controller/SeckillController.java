package com.qust.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qust.seckill.pojo.Order;
import com.qust.seckill.pojo.SeckillOrder;
import com.qust.seckill.pojo.User;
import com.qust.seckill.service.IGoodsService;
import com.qust.seckill.service.IOrderService;
import com.qust.seckill.service.ISeckillOrderService;
import com.qust.seckill.vo.GoodsVo;
import com.qust.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 秒杀
     * 优化前QPS：1265
     * @param request
     * @param response
     * @param model
     * @param ticket
     * @param goodsId
     * @return
     */

    @RequestMapping("/doSeckill2")
    public String doSeckill2(HttpServletRequest request, HttpServletResponse response, Model model
                            ,@CookieValue("userTicket") String ticket,Long goodsId){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ticket);
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoById(goodsId);
        if(goods.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(seckillOrder!=null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        Order order = orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }

    /**
     * 秒杀
     * @param request
     * @param response
     * @param model
     * @param ticket
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSeckill")
    public String doSeckill(HttpServletRequest request, HttpServletResponse response, Model model
            ,@CookieValue("userTicket") String ticket,Long goodsId){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ticket);
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoById(goodsId);

        //判断库存
        if(goods.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        System.out.println(user == null);
        System.out.println(goodsId);
        //判断是否重复抢购
        /*SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId()).eq("goods_id", goodsId));*/
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        System.out.println("111");
        if(seckillOrder!=null){
            System.out.println("222");
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }

        Order order = orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }


}
