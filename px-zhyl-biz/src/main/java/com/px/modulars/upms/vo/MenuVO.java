

package com.px.modulars.upms.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author pinxun
 * @since 2017-11-08
 */
@Data
public class MenuVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	private Integer menuId;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单权限标识
	 */
	private String permission;

	/**
	 * 父菜单ID
	 */
	private Integer parentId;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 前端路由标识路径
	 */
	private String path;

	/**
	 * 排序值
	 */
	private Integer sort;

	/**
	 * 菜单类型 （0菜单 1按钮）
	 */
	private String type;

	/**
	 * 是否缓冲
	 */
	private String keepAlive;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 0--正常 1--删除
	 */
	private String delFlag;

	@Override
	public int hashCode() {
		return menuId.hashCode();
	}

	/**
	 * menuId 相同则相同
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof com.px.modulars.upms.vo.MenuVO) {
			Integer targetMenuId = ((com.px.modulars.upms.vo.MenuVO) obj).getMenuId();
			return menuId.equals(targetMenuId);
		}
		return super.equals(obj);
	}

}
