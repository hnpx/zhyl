
package com.px.modulars.upms.dto;

import com.px.modulars.upms.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author pinxun
 * @date 2019/2/1 角色Dto
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends SysRole {

	/**
	 * 角色部门Id
	 */
	private Integer roleDeptId;

	/**
	 * 部门名称
	 */
	private String deptName;

}
