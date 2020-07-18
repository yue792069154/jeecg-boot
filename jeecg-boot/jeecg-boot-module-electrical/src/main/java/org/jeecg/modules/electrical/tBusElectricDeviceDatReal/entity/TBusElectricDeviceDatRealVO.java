package org.jeecg.modules.electrical.tBusElectricDeviceDatReal.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TBusElectricDeviceDatRealVO {




    private String pointName;

    private String pointCode;

    private String monitorValue;

    private String pointUnit;




}
