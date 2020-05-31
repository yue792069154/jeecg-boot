package org.jeecg.modules.system.sysUser.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.sysUser.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用户角色
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("select role_code from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where user_name=#{userName}))")
    List<String> getRoleCodeListByUserName(@Param("username") String userName);

    @Select("select id from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where user_name=#{userName}))")
    List<String> getRoleIdByUserName(@Param("username") String userName);

}
