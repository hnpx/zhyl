

package com.px.modulars.upms.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author pinxun
 * @since 2019/2/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleMenu extends Model<SysRoleMenu> {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@ApiModelProperty(value = "角色id")
	private Integer roleId;

	/**
	 * 菜单ID
	 */
	@ApiModelProperty(value = "菜单id")
	private Integer menuId;

}
