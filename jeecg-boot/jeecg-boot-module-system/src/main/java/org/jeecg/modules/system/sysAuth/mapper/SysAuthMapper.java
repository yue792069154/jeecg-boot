package org.jeecg.modules.system.sysAuth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.sysAuth.entity.SysAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 功能管理
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
public interface SysAuthMapper extends BaseMapper<SysAuth> {

    List<SysAuth> queryList(@Param("sysAuth") SysAuth sysAuth);

    List<SysAuth> getAuthListByUserName(@Param("userName") String userName);


}
