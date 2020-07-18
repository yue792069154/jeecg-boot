package org.jeecg.modules.electrical.tBusElectricDevice.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 设备中心
 * @Author: jeecg-boot
 * @Date:   2020-07-05
 * @Version: V1.0
 */
@Data
@TableName("t_bus_electric_device")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_bus_electric_device对象", description="设备中心")
public class TBusElectricDevice {
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
	private java.lang.String deviceName;
	/**制造厂*/
	@Excel(name = "制造厂", width = 15)
    @ApiModelProperty(value = "制造厂")
	private java.lang.String deviceManufacturer;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
	private java.lang.String deviceModel;
	/**电源*/
	@Excel(name = "电源", width = 15)
    @ApiModelProperty(value = "电源")
	private java.lang.String devicePower;
	/**功率*/
	@Excel(name = "功率", width = 15)
    @ApiModelProperty(value = "功率")
	private java.lang.String deviceRate;
	/**出厂编号*/
	@Excel(name = "出厂编号", width = 15)
    @ApiModelProperty(value = "出厂编号")
	private java.lang.String deviceCode;
	/**出厂日期*/
	@Excel(name = "出厂日期", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出厂日期")
	private java.util.Date deviceFactoryDate;
	/**设备类别*/
	@Excel(name = "设备类别", width = 15)
    @ApiModelProperty(value = "设备类别")
	private java.lang.String deviceType;
	/**设备安装线路*/
	@Excel(name = "设备安装线路", width = 15)
    @ApiModelProperty(value = "设备安装线路")
	private java.lang.String deviceLine;
	/**设备安装用户*/
	@Excel(name = "设备安装用户", width = 15)
    @ApiModelProperty(value = "设备安装用户")
	private java.lang.String deviceInstall;
	/**设备安装日期*/
	@Excel(name = "设备安装日期", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "设备安装日期")
	private java.util.Date deviceInstallDate;
	/**设备安装经度*/
	@Excel(name = "设备安装经度", width = 15)
    @ApiModelProperty(value = "设备安装经度")
	private java.math.BigDecimal longitude;
	/**设备安装纬度*/
	@Excel(name = "设备安装纬度", width = 15)
    @ApiModelProperty(value = "设备安装纬度")
	private java.math.BigDecimal latitude;
	/**所属机构*/
	@Excel(name = "所属机构", width = 15)
    @ApiModelProperty(value = "所属机构")
	private java.lang.String orgId;
	/**顺序*/
	@Excel(name = "顺序", width = 15)
    @ApiModelProperty(value = "顺序")
	private java.lang.Integer sort;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String rem;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
	private java.lang.String updaterId;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createrId;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**删除状态（0正常 1删除）*/
	@Excel(name = "删除状态（0正常 1删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0正常 1删除）")
	@TableLogic
	private java.lang.String deleteCode;
	/**启用状态（0正常 1停用）*/
	@Excel(name = "启用状态（0正常 1停用）", width = 15)
    @ApiModelProperty(value = "启用状态（0正常 1停用）")
	private java.lang.String statusCode;

	@TableField(exist = false)
    private String keyword;
}
