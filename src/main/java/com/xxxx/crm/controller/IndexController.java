package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller

public class IndexController extends BaseController {
    @Resource
    private UserService userService;
    /**
     * 系统登录⻚
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    // 系统界⾯欢迎⻚
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 后端管理主⻚⾯
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //通过工具类，从cookie中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用对应service层的方法，通过userId主键查询用户对象
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);

        return "main";
    }
}


