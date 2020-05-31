package org.jeecg.common.system.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginUser {

	/**主键id*/
	private java.lang.String id;

	/**登录账号*/
	private java.lang.String userName;

	/**真实姓名*/
	private java.lang.String realName;

	/**密码*/
	private java.lang.String password;

	/**md5密码盐*/
	private java.lang.String salt;

	/**头像*/
	private java.lang.String avatar;

	/**生日*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date birthday;

	/**性别(0-默认未知,1-男,2-女)*/
	private java.lang.Integer sex;

	/**电子邮件*/
	private java.lang.String email;

	/**电话*/
	private java.lang.String phone;

	/**机构编码*/
	private java.lang.String orgCode;

	/**座机号*/
	private java.lang.String telephone;

	/**顺序*/
	private java.lang.Integer sort;

	/**备注*/
	private java.lang.String rem;

	/**状态*/
	private java.lang.String statusCode;

	/**更新人*/
	private java.lang.String updaterId;

	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;

	/**创建人*/
	private java.lang.String createrId;

	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;

}
