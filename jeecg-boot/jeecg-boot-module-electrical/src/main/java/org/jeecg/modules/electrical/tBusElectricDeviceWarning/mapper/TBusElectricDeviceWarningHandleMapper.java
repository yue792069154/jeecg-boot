package org.jeecg.modules.electrical.tBusElectricDeviceWarning.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.entity.TBusElectricDeviceWarningHandle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 告警中心
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
public interface TBusElectricDeviceWarningHandleMapper extends BaseMapper<TBusElectricDeviceWarningHandle> {
    List<TBusElectricDeviceWarningHandle> getDeviceWarningHandleList(@Param("warningId") String warningId);
}
