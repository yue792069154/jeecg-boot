package org.jeecg.modules.electrical.tBusElectricDeviceWarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.entity.TBusElectricDeviceWarning;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 告警中心
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
public interface TBusElectricDeviceWarningMapper extends BaseMapper<TBusElectricDeviceWarning> {

    List<TBusElectricDeviceWarning> getDeviceWarningPageList(Page<TBusElectricDeviceWarning> page, String deviceCode, String warningState, String warningHandleState);

}
