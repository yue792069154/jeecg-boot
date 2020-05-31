package org.jeecg.modules.system.sysDict.service.impl;

import org.jeecg.modules.system.sysDict.entity.SysDict;
import org.jeecg.modules.system.dict.mapper.SysDictMapper;
import org.jeecg.modules.system.dict.service.ISysDictService;
import org.jeecg.modules.system.sysDict.entity.SysDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 字典
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {


    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public List<SysDict> getDictByDictTypeCode(String dictTypeCode) {
        return sysDictMapper.getDictByDictTypeCode(dictTypeCode);
    }

}
