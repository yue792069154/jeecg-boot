package org.jeecg.modules.electrical.tBusElectricDeviceDatReal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.entity.TBusElectricDeviceDatReal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.entity.TBusElectricDeviceDatRealVO;

/**
 * @Description: 实时数据
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
public interface ITBusElectricDeviceDatRealService extends IService<TBusElectricDeviceDatReal> {

    Page<TBusElectricDeviceDatReal> getDeviceDatRealPageList(Page<TBusElectricDeviceDatReal> page, String keyword,String deviceCode);

    Page<TBusElectricDeviceDatReal> getDeviceHistoryPageList(Page<TBusElectricDeviceDatReal> page, String keyword,String deviceCode,String monitorStartTime,String monitorEndTime);

}
