package org.jeecg.modules.electrical.tBusElectricDeviceWarning.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.entity.TBusElectricDeviceWarning;
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.mapper.TBusElectricDeviceWarningMapper;
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.service.ITBusElectricDeviceWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 告警中心
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
@Service
public class TBusElectricDeviceWarningServiceImpl extends ServiceImpl<TBusElectricDeviceWarningMapper, TBusElectricDeviceWarning> implements ITBusElectricDeviceWarningService {

    @Autowired
    private TBusElectricDeviceWarningMapper tBusElectricDeviceWarningMapper;

    @Override
    public Page<TBusElectricDeviceWarning> getDeviceWarningPageList(Page<TBusElectricDeviceWarning> page, String deviceCode, String warningState, String warningHandleState) {
        return page.setRecords(tBusElectricDeviceWarningMapper.getDeviceWarningPageList(page,  deviceCode,  warningState,  warningHandleState));
    }

}
