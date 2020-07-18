package org.jeecg.modules.electrical.tBusElectricDeviceDatReal.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.entity.TBusElectricDeviceDatReal;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.entity.TBusElectricDeviceDatRealVO;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.mapper.TBusElectricDeviceDatRealMapper;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.service.ITBusElectricDeviceDatRealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 实时数据
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Service
public class TBusElectricDeviceDatRealServiceImpl extends ServiceImpl<TBusElectricDeviceDatRealMapper, TBusElectricDeviceDatReal> implements ITBusElectricDeviceDatRealService {

    @Autowired
    private TBusElectricDeviceDatRealMapper tBusElectricDeviceDatRealMapper;

    @Override
    public Page<TBusElectricDeviceDatReal> getDeviceDatRealPageList(Page<TBusElectricDeviceDatReal> page, String keyword, String deviceCode) {
        return page.setRecords(tBusElectricDeviceDatRealMapper.getDeviceDatRealPageList(page, keyword,deviceCode));
    }


    @Override
    public Page<TBusElectricDeviceDatReal> getDeviceHistoryPageList(Page<TBusElectricDeviceDatReal> page, String keyword, String deviceCode,String monitorStartTime,String monitorEndTime) {
        return page.setRecords(tBusElectricDeviceDatRealMapper.getDeviceHistoryPageList(page, keyword,deviceCode, monitorStartTime, monitorEndTime));
    }
}


