package org.jeecg.modules.system.sysAuth.service.impl;

import org.jeecg.modules.system.sysAuth.entity.SysAuth;
import org.jeecg.modules.system.sysAuth.mapper.SysAuthMapper;
import org.jeecg.modules.system.sysAuth.service.ISysAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 功能管理
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
@Service
public class SysAuthServiceImpl extends ServiceImpl<SysAuthMapper, SysAuth> implements ISysAuthService {

    @Autowired
    private SysAuthMapper sysAuthMapper;

    @Override
    public List<SysAuth> queryList(SysAuth sysAuth) {
        List<SysAuth> sysAuthList=sysAuthMapper.queryList(sysAuth);
        return  sysAuthList;
    }

    @Override
    public List<SysAuth> getAuthListByUserName(String userName) {
        return sysAuthMapper.getAuthListByUserName(userName);
    }

}
