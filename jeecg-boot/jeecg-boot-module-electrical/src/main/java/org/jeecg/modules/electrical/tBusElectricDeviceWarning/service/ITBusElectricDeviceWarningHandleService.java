package org.jeecg.modules.electrical.tBusElectricDeviceWarning.service;

import org.jeecg.modules.electrical.tBusElectricDeviceWarning.entity.TBusElectricDeviceWarningHandle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 告警中心
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
public interface ITBusElectricDeviceWarningHandleService extends IService<TBusElectricDeviceWarningHandle> {


    List<TBusElectricDeviceWarningHandle> getDeviceWarningHandleList(String warningId);

}
