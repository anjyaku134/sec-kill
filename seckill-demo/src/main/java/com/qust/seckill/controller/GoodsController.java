package com.qust.seckill.controller;

import com.qust.seckill.controller.interceptor.UserInterceptor;
import com.qust.seckill.pojo.User;
import com.qust.seckill.service.IGoodsService;
import com.qust.seckill.vo.GoodsVo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 商品
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;



    /**
     * 跳转商品列表页
     * 优化前QPS:2213
     * @param session
     * @param model
     * @param ticket
     * @return
     */
    @RequestMapping(value = "/toList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model
                         ,@CookieValue("userTicket") String ticket){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ticket);
        model.addAttribute("user",user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        //如果为空，手动渲染，存入Redis并返回
        WebContext context = new WebContext(request,response,request.getServletContext()
                ,request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
         }

        //return "goodsList";
        return html;
    }

    /**
     * 跳转商品详情页
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toDetail(HttpServletRequest request, HttpServletResponse response, Model model
                           ,@CookieValue("userTicket") String ticket, @PathVariable Long goodsId){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail"+goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ticket);
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        int remainSeconds = 0;
        //秒杀未开始
        if(nowDate.before(startDate)){
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime()) / 1000);
        }else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        }else {
            //秒杀中
            secKillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("goods",goodsVo);
        //return "goodsDetail";
        WebContext context = new WebContext(request,response,request.getServletContext()
                ,request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail",context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail"+goodsId,html,60, TimeUnit.SECONDS);
        }
        return html;
    }

}
