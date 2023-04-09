package com.qust.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qust.seckill.pojo.User;
import com.qust.seckill.vo.LoginVo;
import com.qust.seckill.vo.RespBean;
import com.qust.seckill.vo.RespBeanEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qust
 * @since 2023-01-30
 */
public interface IUserService extends IService<User> {

    /**
     * 登录
     *
     * @param loginVo
     * @param request
     * @param response
     * @return
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);


}
