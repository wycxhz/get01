package com.xxxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    /**
     * 方法的返回值类型
     *      视图
     *      json
     *     * 如何判断⽅法的返回类型：
     *  *           如果⽅法级别配置了 @ResponseBody 注解，表示⽅法返回的是JSON；
     *  *            反之，返回的是视图⻚⾯
     *  */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        /**
         * 非法请求拦截
         * */
        /*判断异常类型
           如果是未登录异常，则先执行相关的拦截操作
           */
        if(e instanceof NoLoginException){
            //如果捕获的是未登录异常，则重定向到登录页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;

        }
        //设置默认处理异常
        ModelAndView mv = new ModelAndView();
        mv.setViewName("");
        mv.addObject("code",400);
        mv.addObject("msg","系统异常，请稍后再试....");
        //判断handlerMethod
        if(handler instanceof HandlerMethod){
            //类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上的ResponseBody注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断ResponseBody注解是否存在(如果不存在，返回的是视图，如果存在，返回的是json）
            if(null == responseBody){
                /*方法返回视图*/
                if(e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    mv.addObject("code",pe.getCode());
                    mv.addObject("msg",pe.getMsg());

                }
                return mv;
            }else{
                /*方法返回json*/
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，请重试！");
                //如果捕获的是自定义异常
                if(e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }
                //设置响应类型的编码格式
                response.setContentType("application/json;charset=utf-8");
                //得到输出流
                PrintWriter out = null;
                try{
                    out = response.getWriter();
                    //将对象转换成json格式，通过输出流输出
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                }catch(Exception e1){
                    e1.printStackTrace();
                }finally{
                    if(out != null){
                        out.close();
                    }
                }
                return null;

            }
        }
        return mv;
    }
}
