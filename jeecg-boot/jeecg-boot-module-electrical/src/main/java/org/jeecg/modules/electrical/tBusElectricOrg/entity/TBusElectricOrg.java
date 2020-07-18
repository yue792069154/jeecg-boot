package org.jeecg.modules.electrical.tBusElectricOrg.entity;

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
 * @Description: 机构管理
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Data
@TableName("t_bus_electric_org")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_bus_electric_org对象", description="机构管理")
public class TBusElectricOrg {
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**机构名称*/
	@Excel(name = "机构名称", width = 15)
    @ApiModelProperty(value = "机构名称")
	private java.lang.String orgName;
	/**父级机构*/
	@Excel(name = "父级机构", width = 15)
    @ApiModelProperty(value = "父级机构")
	private java.lang.String orgParentId;
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
