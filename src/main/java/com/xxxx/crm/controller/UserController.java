package com.xxxx.crm.controller;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
@Resource
    private UserService userService;
/**
 * 登录功能
 * */

/*@PostMapping("/login")
@ResponseBody //表示当前返回一个json格式给客户端
public ResultInfo userLogin(String userName,String userPwd){
    ResultInfo resultInfo = new ResultInfo();
    //调用service层的方法
    UserModel userModel =  userService.userLogin(userName, userPwd);
    //设置resultInfo的result值（将数据返回给请求）
    resultInfo.setResult(userModel);
   *//* 通过 try catch 捕获 Service 层抛出的异常
    try{
    调用service层的方法
       UserModel userModel =  userService.userLogin(userName, userPwd);
       设置resultInfo的result值（将数据返回给请求）
        resultInfo.setResult(userModel);
    }catch(ParamsException p){
        resultInfo.setCode(p.getCode());
        resultInfo.setMsg(p.getMsg());
        p.printStackTrace();
    }catch(Exception e){
        resultInfo.setCode(500);
        resultInfo.setMsg("操作失败！");
        e.printStackTrace();
    }*//*

    return  resultInfo;
}*/
   /* @PostMapping("/login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.userLogin(userName,userPwd);
        resultInfo.setResult(userModel);
        return resultInfo;
    }*/
    @PostMapping("/login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        //创建ResultInfo对象
        ResultInfo resultInfo = new ResultInfo();
        //调用service层的方法
        UserModel userModel = userService.userLogin(userName, userPwd);
        //设置resultInfo的result值（将数据返回给请求）
        resultInfo.setResult(userModel);
        return resultInfo;
    }
/**
 * 用户修改密码
 * */
/*@PostMapping("updatePwd")
@ResponseBody
public ResultInfo updateUserPassword(HttpServletRequest request,String oldPassword,String newPassword,String confirmPassword){
    ResultInfo resultInfo = new ResultInfo();
    //获取userId
    Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
    //调用service层的密码修改方法
    userService.updateUserPassword(userId,oldPassword,newPassword,confirmPassword);
    *//* 通过 try catch 捕获 Service 层抛出的异常
    try{
        //获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用service层的密码修改方法
        userService.updateUserPassword(userId,oldPassword,newPassword,confirmPassword);
    }catch(ParamsException p){
        p.printStackTrace();
        resultInfo.setCode(p.getCode());
        resultInfo.setMsg(p.getMsg());
    }catch(Exception e){
        e.printStackTrace();
        resultInfo.setCode(500);
        resultInfo.setMsg("操作失败！");
    }*//*
    return resultInfo;
  }*/

    /*
    * 用户修改密码
    * */
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,String oldPassword,String newPassword,String confirmPassword){
        ResultInfo resultInfo = new ResultInfo();
        //获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用service层的密码修改方法
        userService.updateUserPassword(userId,oldPassword,newPassword,confirmPassword);
        return resultInfo;
    }
  @RequestMapping("toPasswordPage")
  public String toPasswordPage(){
    return "user/password";
  }
/**
 * 查询所有的销售人员
 * */
/*@RequestMapping("queryAllSales")
@ResponseBody
public List<Map<String,Object>> queryAllSales(){
    return userService.queryAllSales();
    }*/
    @RequestMapping("queryAllSales")
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }
/**
 * 分页多条件查询用户列表
 * */
/*@RequestMapping("list")
@ResponseBody
public Map<String,Object> selectByParams(UserQuery userQuery){
    return userService.queryByParamsForTable(userQuery);
    }*/
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 进入用户列表页面
     * */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }
    /**
     * 添加用户
     * */
/*@PostMapping("add")
@ResponseBody
    public ResultInfo addUser(User user){
    userService.addUser(user);
        return success("用户添加成功！");
    }*/
  /*  @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功！");
    }*/
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功！");
    }

    /**
     * 更新用户
     * */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功！");
    }
/**
 * 打开添加/修改用户的对话框
 * */
@RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id,HttpServletRequest request){
    //判断ID是否为空，不为空表示更新操作，查询用户对象
    if(id != null){
        //通过ID查询用户对象
        User user = userService.selectByPrimaryKey(id);
        //将数据设置到请求域中
        request.setAttribute("userInfo",user);
    }
    return "user/add_update";
    }
    /**
     * 用户删除
     * */
  /*  @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteByIds(ids);
        return success("用户删除成功！");
    }*/

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteByIds(ids);
        return success("用户删除成功！");
    }
}
