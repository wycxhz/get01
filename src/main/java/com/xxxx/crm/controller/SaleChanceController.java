package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    /*
    * 多条件分页查询营销机会
    *  查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配⼈参数
    * */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query,Integer flag,HttpServletRequest request){
       //判断flag的值
        if(flag != null && flag == 1){
            //查询客户开发计划
            //设置分配状态
            query.setState(StateStatus.STATED.getType());
            //设置指派人（当前用户登录的ID）
            //从cookie中获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            query.setAssignMan(userId);

        }

        return saleChanceService.querySaleChanceByParams(query);
    }

    /*
    * 进入营销机会页面
    * */
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }
    /**
     * 添加营销机会
     * */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
        //从cookie中获取用户名
        String userName = CookieUtil.getCookieValue(request,"userName");
        //设置营销机会的创建人
        saleChance.setCreateMan(userName);
        //添加营销机会的数据
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功！");
    }

    /**
     * 更新营销机会数据
     * */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){

        //更新营销机会的数据
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }
    /**
     * 进入添加，或者修改营销机会数据页面
     * */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, Model model){
        //判断saleChanceId是否为空
        if(saleChanceId != null){
            //通过ID查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            //将数据设置到请求域中
            model.addAttribute("saleChance",saleChance);

        }
        return "saleChance/add_update";
    }
    /**
     * 删除营销机会数据
     * */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        //调用service层方法，删除营销机会数据
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功！");
    }

    /**
     * 更新营销机会的开发状态
     * */
    @PostMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
       saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功！");
    }
}
