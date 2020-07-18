package org.jeecg.modules.electrical.tBusElectricDeviceDatReal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.entity.TBusElectricDeviceDatReal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 实时数据
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
public interface TBusElectricDeviceDatRealMapper extends BaseMapper<TBusElectricDeviceDatReal> {

    List<TBusElectricDeviceDatReal> getDeviceDatRealPageList(Page<TBusElectricDeviceDatReal> page, String keyword, String deviceCode);

    List<TBusElectricDeviceDatReal> getDeviceHistoryPageList(Page<TBusElectricDeviceDatReal> page, String keyword, String deviceCode,String monitorStartTime,String monitorEndTime);
}
