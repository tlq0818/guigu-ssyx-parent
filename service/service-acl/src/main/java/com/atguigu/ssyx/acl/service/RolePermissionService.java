package com.atguigu.ssyx.acl.service;

import java.util.Map;

import com.atguigu.ssyx.model.acl.Permission;
import com.atguigu.ssyx.model.acl.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description
 * @Author tanlinqing
 * @Date 2023/6/13 10:23
 */

public interface RolePermissionService  extends IService<RolePermission> {
    void saveRolePermission(Long[] permissionIds, Long  roleIds);

}
