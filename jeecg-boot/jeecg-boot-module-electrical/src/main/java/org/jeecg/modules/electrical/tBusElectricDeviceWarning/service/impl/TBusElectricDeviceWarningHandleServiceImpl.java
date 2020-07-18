package org.jeecg.modules.electrical.tBusElectricDeviceWarning.service.impl;

import org.jeecg.modules.electrical.tBusElectricDeviceWarning.entity.TBusElectricDeviceWarningHandle;
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.mapper.TBusElectricDeviceWarningHandleMapper;
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.service.ITBusElectricDeviceWarningHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 告警中心
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
@Service
public class TBusElectricDeviceWarningHandleServiceImpl extends ServiceImpl<TBusElectricDeviceWarningHandleMapper, TBusElectricDeviceWarningHandle> implements ITBusElectricDeviceWarningHandleService {


    @Autowired
    private TBusElectricDeviceWarningHandleMapper tBusElectricDeviceWarningHandleMapper;

    @Override
    public List<TBusElectricDeviceWarningHandle> getDeviceWarningHandleList(String warningId) {
        return tBusElectricDeviceWarningHandleMapper.getDeviceWarningHandleList(warningId);
    }

}
