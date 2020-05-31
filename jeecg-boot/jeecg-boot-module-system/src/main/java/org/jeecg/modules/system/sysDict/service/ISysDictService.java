package org.jeecg.modules.system.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.sysDict.entity.SysDict;

import java.util.List;

/**
 * @Description: 字典
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
public interface ISysDictService extends IService<SysDict> {

    List<SysDict> getDictByDictTypeCode(String dictTypeCode);

}
