package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.model.TreeModule;
import com.xxxx.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;


    /**
     * 查询所有的资源列表
     * */
    public List<TreeModule> queryAllModules(Integer roleId){
        //查询所有的资源列表
        List<TreeModule> treeModuleList = moduleMapper.queryAllModules();
        //查询指定角色已经授权过的列表(查询角色拥有的资源ID)
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);
        //判断角色是否拥有资源ID
        if(permissionIds != null && permissionIds.size() > 0){
            //循环所有的资源列表，判断用户拥有的资源ID中是否有匹配的，如果有，则设置checked属性为true
            treeModuleList.forEach(treeModule -> {
                //判断角色拥有的资源ID中是否有当前遍历的资源ID
                if(permissionIds.contains(treeModule.getId())){
                    //如果包含，则说明角色授权过，设置check为true
                    treeModule.setChecked(true);
                }
            });
        }
        return treeModuleList;
    }

}
