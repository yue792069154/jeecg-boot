package org.jeecg.modules.system.sysMenu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.sysMenu.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 菜单
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> getMenuListByUserName(@Param("userName") String userName);
}
