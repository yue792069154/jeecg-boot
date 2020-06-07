package org.jeecg.modules.system.sysUser.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.sysOrg.entity.SysOrg;
import org.jeecg.modules.system.sysRole.entity.SysRole;
import org.jeecg.modules.system.sysUser.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;


public interface ISysUserService extends IService<SysUser> {

    void addUserRoleAndOrg(SysUser sysUser);

    void editUserRoleAndOrg(SysUser sysUser);

    Boolean changePassword(SysUser sysUser);

    SysUser getUserByUserName(String userName);

    Result checkUserIsEffective(SysUser sysUser);

    Set<String> getUserRoleCodeSet(String userName);

    List<SysUser> queryList(SysUser sysUser);

}
