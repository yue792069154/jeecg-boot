package org.jeecg.modules.system.sysAuth.service;

import org.jeecg.modules.system.sysAuth.entity.SysAuth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 功能管理
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
public interface ISysAuthService extends IService<SysAuth> {



    List<SysAuth> queryList(SysAuth sysAuth);

    List<SysAuth> getAuthListByUserName(String userName);

}
