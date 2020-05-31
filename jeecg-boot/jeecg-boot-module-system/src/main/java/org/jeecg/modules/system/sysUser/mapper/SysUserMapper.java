package org.jeecg.modules.system.sysUser.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.sysUser.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用户
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("select * from  sys_user  where user_name = #{userName} and status_code = '0'")
    SysUser getUserByUserName(@Param("userName") String userName);


    List<SysUser> queryList();


}
