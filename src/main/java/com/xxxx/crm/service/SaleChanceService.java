package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.plaf.nimbus.State;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
   @Resource
    private SaleChanceMapper saleChanceMapper;
   /*
   * 多条件分页查询营销机会（BaseService中有对应的方法）
   * */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query){
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(query.getPage(),query.getLimit());
        //得到分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(query));
        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
     }
     /*
     * 添加营销机会
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //1.参数校验
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //2.设置相关属性的默认值
        //isValid是否有效（0=无效，1=有效）设置为有效 1= 有效
        saleChance.setIsValid(1);
        //createDate创建时间 默认是系统当前时间
        saleChance.setCreateDate(new Date());
        //updateDate 默认是系统当前时间
        saleChance.setUpdateDate(new Date());
        //判断是否设置了指派人
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            //如果为空，则表设置指派人
            //state分配状态（0=未分配，1=已分配）
            saleChance.setState(StateStatus.UNSTATE.getType());
            //assignTime指派时间 设置为null
            saleChance.setAssignTime(null);
            //devResult开发状态
            saleChance.setDevResult(DevResult.UNDEV.getStatus());

        }else {
            //如果不为空，则表示设置了指派人
            //state分配状态（0=未分配，1=已分配）
            saleChance.setState(StateStatus.STATED.getType());
            //assignTime指派时间 系统当前时间
            saleChance.setAssignTime(new Date());
            //devResult开发状态
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        //3.执行添加操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)!=1,"添加营销机会失败！");
    }

    /**
     * 营销机会数据更新
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        //1.参数校验
        //通过id查询记录
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断是否为空
        AssertUtil.isTrue(null == temp,"待更新记录不存在！");
        //校验基础参数
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //2.设置相关参数值
        //updateDate更新时间，设置为系统当前时间
        saleChance.setUpdateDate(new Date());
        //assignMan指派人
        //判断原始数据是否存在
        saleChance.setUpdateDate(new Date());
        if (StringUtils.isBlank(temp.getAssignMan())
                && StringUtils.isNotBlank(saleChance.getAssignMan())) {
            // 如果原始记录未分配，修改后改为已分配
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        } else if (StringUtils.isNotBlank(temp.getAssignMan())
                && StringUtils.isBlank(saleChance.getAssignMan())) {
            // 如果原始记录已分配，修改后改为未分配
            saleChance.setAssignMan("");
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }
        //3.执行更新操作，判断受到影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"营销机会数据更新失败！");

    }
/**
 * 参数校验
 *     customerName客户名称   非空
 *     linkMan联系人   非空
 *     linkPhone联系号码   非空
 *     linkPhone联系号码   非空，手机号码格式正确*/
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        //customerName客户名称   非空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空！");
        //linkMan联系人   非空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空！");
        //linkPhone联系号码   非空
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系号码不能为空！");
        //linkPhone联系号码   非空，手机号码格式正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系号码不正确!");
    }

    /**
     * 删除营销机会
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        //判断ID是否为空
        AssertUtil.isTrue(null == ids || ids.length<1,"待删除记录不存在！");
        //执行删除更新操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length,"营销机会数据删除失败！");
    }

    /**
     * 更新营销机会的开发状态
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        //判断ID是否为空
        AssertUtil.isTrue(null == id,"待更新记录不存走!");
        //通过id查询营销机会数据
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        //判断对象是否为空
        AssertUtil.isTrue(null == saleChance,"待更新记录不存在！");
        //设置开发状态
        saleChance.setDevResult(devResult);
        //执行更新操作,判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"开发状态更新失败!");
    }
}
