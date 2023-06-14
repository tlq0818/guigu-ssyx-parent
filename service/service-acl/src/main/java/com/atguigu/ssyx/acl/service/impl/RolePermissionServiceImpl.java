package com.atguigu.ssyx.acl.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.atguigu.ssyx.acl.mapper.RolePermissionMapper;
import com.atguigu.ssyx.acl.service.RolePermissionService;
import com.atguigu.ssyx.model.acl.AdminRole;
import com.atguigu.ssyx.model.acl.Permission;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.model.acl.RolePermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description
 * @Author tanlinqing
 * @Date 2023/6/13 10:24
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Resource RolePermissionService rolePermissionService;
    @Override
    public void saveRolePermission(Long[] permissionIds, Long roleId){
        //1 删除用户已经分配过的角色权限
        //根据用户id删除role_permission表里面对应数据
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId,roleId);
        rolePermissionService.remove(wrapper);
        //2 重新分配
        //adminId:1   roleId: 2 3
        //遍历多个角色id，得到每个角色id，拿着每个角色id + 用户id添加用户角色关系表
        //        for (Long roleId:roleIds) {
        List<RolePermission> list = new ArrayList<>();
        for (Long permissionId: permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(roleId);
            //放到list集合
            list.add(rolePermission);
        }
        //调用方法添加
        rolePermissionService.saveBatch(list);
    }

}
