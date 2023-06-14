package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.PermissionMapper;
import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.acl.service.RolePermissionService;
import com.atguigu.ssyx.acl.utils.PermissionHelper;
import com.atguigu.ssyx.model.acl.Permission;
import com.atguigu.ssyx.model.acl.RolePermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Resource PermissionService permissionService;
    @Resource
    RolePermissionService rolePermissionService;
    //查询所有菜单
    @Override
    public List<Permission> queryAllPermission() {
        //1 查询所有菜单
        List<Permission> allPermissionList =
                baseMapper.selectList(null);

        //2 转换要求数据格式
        List<Permission> result = PermissionHelper.buildPermission(allPermissionList);
        return result;
    }

    //递归删除菜单
    @Override
    public void removeChildById(Long id) {
        List<Long> idList = new ArrayList<>();
        this.getAllPermissionId(id,idList);
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public Map<String, Object> getPermissionByRoleId(Long roleId){
        List<Permission> allPermission = baseMapper.selectList(null);
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId,roleId);
        List<RolePermission> rolePermissionList = rolePermissionService.list(wrapper);
        List<Long> permissionIdsList =
                rolePermissionList.stream()
                        .map(item -> item.getPermissionId())
                        .collect(Collectors.toList());
        List<Permission> assignPermissionList = new ArrayList<>();
        for (Permission permission:allPermission) {
            if(permissionIdsList.contains(permission.getId())) {
                assignPermissionList.add(permission);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("allPermissions",allPermission);
        result.put("assignPermissionList",assignPermissionList);
        return result;
    }
    private void getAllPermissionId(Long id, List<Long> idList) {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid,id);
        List<Permission> childList = baseMapper.selectList(wrapper);
        childList.stream().forEach(item -> {
            idList.add(item.getId());
            this.getAllPermissionId(item.getId(),idList);
        });
    }
}
