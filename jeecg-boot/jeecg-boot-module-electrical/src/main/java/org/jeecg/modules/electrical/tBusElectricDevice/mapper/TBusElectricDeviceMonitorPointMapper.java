package org.jeecg.modules.electrical.tBusElectricDevice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.electrical.tBusElectricDevice.entity.TBusElectricDeviceMonitorPoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 设备监测项目
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
public interface TBusElectricDeviceMonitorPointMapper extends BaseMapper<TBusElectricDeviceMonitorPoint> {

    List<TBusElectricDeviceMonitorPoint> queryList(@Param("tBusElectricDeviceMonitorPoint") TBusElectricDeviceMonitorPoint tBusElectricDeviceMonitorPoint);
}
