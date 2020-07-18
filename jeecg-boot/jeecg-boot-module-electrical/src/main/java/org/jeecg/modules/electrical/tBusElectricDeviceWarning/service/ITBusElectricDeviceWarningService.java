package org.jeecg.modules.electrical.tBusElectricDeviceWarning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.entity.TBusElectricDeviceWarning;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 告警中心
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
public interface ITBusElectricDeviceWarningService extends IService<TBusElectricDeviceWarning> {


    Page<TBusElectricDeviceWarning> getDeviceWarningPageList(Page<TBusElectricDeviceWarning> page, String deviceCode, String warningState, String warningHandleState);



}
