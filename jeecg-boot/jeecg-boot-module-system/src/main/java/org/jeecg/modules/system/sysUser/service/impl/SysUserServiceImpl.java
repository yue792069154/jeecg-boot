package org.jeecg.modules.system.sysUser.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.sysMenu.entity.SysMenu;
import org.jeecg.modules.system.sysMenu.mapper.SysMenuMapper;
import org.jeecg.modules.system.sysOrg.entity.SysOrg;
import org.jeecg.modules.system.sysRole.entity.SysRole;
import org.jeecg.modules.system.sysUser.entity.SysUser;
import org.jeecg.modules.system.sysUser.entity.SysUserRole;
import org.jeecg.modules.system.sysUser.entity.sysUserOrg;
import org.jeecg.modules.system.sysUser.mapper.SysUserMapper;
import org.jeecg.modules.system.sysUser.mapper.SysUserRoleMapper;
import org.jeecg.modules.system.sysUser.mapper.sysUserOrgMapper;
import org.jeecg.modules.system.sysUser.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 用户
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private sysUserOrgMapper sysUserOrgMapper;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public void addUserRoleAndOrg(SysUser sysUser) {

        List<String> sysRoleIdList=sysUser.getSysRoleIdList();
        if(sysRoleIdList!=null&&sysRoleIdList.size()>0){
            for (String roleId : sysRoleIdList) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getId());
                userRole.setRoleId(roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }


    }

    @Override
    public void editUserRoleAndOrg(SysUser sysUser) {

        QueryWrapper<SysUserRole> sysUserRoleQueryWrapper =new QueryWrapper<>();
        sysUserRoleQueryWrapper.lambda().eq(SysUserRole::getUserId,sysUser.getId());
        sysUserRoleMapper.delete(sysUserRoleQueryWrapper);

        this.addUserRoleAndOrg(sysUser);

    }

    @Override
    public Boolean changePassword(SysUser sysUser) {
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String password = sysUser.getPassword();
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUserName(), password, salt);
        sysUser.setPassword(passwordEncode);
        int record=sysUserMapper.updateById(sysUser);
        if(record>0){
            return true;
        }else
        {
            return false;
        }
    }

    @Override
    public SysUser getUserByUserName(String userName) {
        return sysUserMapper.getUserByUserName(userName);
    }

    @Override
    public Result<?> checkUserIsEffective(SysUser sysUser) {
        Result<?> result = new Result<Object>();
        //情况1：根据用户信息查询，该用户不存在
        if (sysUser == null) {
            result.error500("该用户不存在，请注册");
            sysBaseAPI.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        }
        //情况2：根据用户信息查询，该用户已注销
        if (CommonConstant.DEL_FLAG_1.toString().equals(sysUser.getStatusCode())) {
            sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUserName() + "已注销！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已注销");
            return result;
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.STATUS_DISABLE.equals(sysUser.getStatusCode())) {
            sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUserName() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已冻结");
            return result;
        }
        return result;
    }

    @Override
    public Set<String> getUserRoleCodeSet(String userName) {
        List<String> roleCodeList = sysUserRoleMapper.getRoleCodeListByUserName(userName);
        return new HashSet<>(roleCodeList);
    }

    @Override
    public List<SysUser> queryList(SysUser sysUser) {
        List<SysUser> sysUserList=sysUserMapper.queryList(sysUser);
        return  sysUserList;
    }
}
