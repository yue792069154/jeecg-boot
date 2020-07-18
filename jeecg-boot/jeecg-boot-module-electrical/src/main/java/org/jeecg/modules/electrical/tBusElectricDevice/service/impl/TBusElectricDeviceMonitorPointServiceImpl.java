package org.jeecg.modules.electrical.tBusElectricDevice.service.impl;

import org.jeecg.modules.electrical.tBusElectricDevice.entity.TBusElectricDeviceMonitorPoint;
import org.jeecg.modules.electrical.tBusElectricDevice.mapper.TBusElectricDeviceMonitorPointMapper;
import org.jeecg.modules.electrical.tBusElectricDevice.service.ITBusElectricDeviceMonitorPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 设备监测项目
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Service
public class TBusElectricDeviceMonitorPointServiceImpl extends ServiceImpl<TBusElectricDeviceMonitorPointMapper, TBusElectricDeviceMonitorPoint> implements ITBusElectricDeviceMonitorPointService {


    @Autowired
    private TBusElectricDeviceMonitorPointMapper tBusElectricDeviceMonitorPointMapper;

    @Override
    public List<TBusElectricDeviceMonitorPoint> queryList(TBusElectricDeviceMonitorPoint tBusElectricDeviceMonitorPoint) {
        List<TBusElectricDeviceMonitorPoint> tBusElectricDeviceMonitorPointList=tBusElectricDeviceMonitorPointMapper.queryList(tBusElectricDeviceMonitorPoint);
        return  tBusElectricDeviceMonitorPointList;
    }


}
