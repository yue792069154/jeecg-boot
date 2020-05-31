package org.jeecg.modules.system.dict.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.sysDict.entity.SysDict;

public interface SysDictMapper extends BaseMapper<SysDict> {

    @Select("SELECT * FROM sys_dict WHERE status_code=0 and dict_type_code = '${dictTypeCode}' order by sort asc")
    List<SysDict> getDictByDictTypeCode(@Param("dictTypeCode") String dictTypeCode);

}
