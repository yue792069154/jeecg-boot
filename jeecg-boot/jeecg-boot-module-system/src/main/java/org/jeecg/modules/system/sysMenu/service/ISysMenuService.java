package org.jeecg.modules.system.sysMenu.service;

import org.jeecg.modules.system.sysMenu.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 菜单
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> getMenuListByUserName(String userName);
}
