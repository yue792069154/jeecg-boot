package org.jeecg.modules.system.sysMenu.service.impl;

import org.jeecg.modules.system.sysMenu.entity.SysMenu;
import org.jeecg.modules.system.sysMenu.mapper.SysMenuMapper;
import org.jeecg.modules.system.sysMenu.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 菜单
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> getMenuListByUserName(String userName) {
        return sysMenuMapper.getMenuListByUserName(userName);
    }
}
