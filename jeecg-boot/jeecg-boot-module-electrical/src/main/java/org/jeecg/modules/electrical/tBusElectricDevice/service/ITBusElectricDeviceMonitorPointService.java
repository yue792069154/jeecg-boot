package org.jeecg.modules.electrical.tBusElectricDevice.service;

import org.jeecg.modules.electrical.tBusElectricDevice.entity.TBusElectricDeviceMonitorPoint;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 设备监测项目
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
public interface ITBusElectricDeviceMonitorPointService extends IService<TBusElectricDeviceMonitorPoint> {


    List<TBusElectricDeviceMonitorPoint> queryList(TBusElectricDeviceMonitorPoint tBusElectricDeviceMonitorPoint);

}
