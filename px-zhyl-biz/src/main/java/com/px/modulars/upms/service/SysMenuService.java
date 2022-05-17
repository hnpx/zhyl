

package com.px.modulars.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.modulars.upms.dto.MenuTree;
import com.px.modulars.upms.entity.SysMenu;
import com.px.modulars.upms.vo.MenuVO;
import com.pig4cloud.pig.common.core.util.R;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author pinxun
 * @since 2019/2/1
 */
public interface SysMenuService extends IService<SysMenu> {

	/**
	 * 通过角色编号查询URL 权限
	 * @param roleId 角色ID
	 * @return 菜单列表
	 */
	List<MenuVO> findMenuByRoleId(Integer roleId);

	/**
	 * 级联删除菜单
	 * @param id 菜单ID
	 * @return true成功,false失败
	 */
	R removeMenuById(Integer id);

	/**
	 * 更新菜单信息
	 * @param sysMenu 菜单信息
	 * @return 成功、失败
	 */
	Boolean updateMenuById(SysMenu sysMenu);

	/**
	 * 构建树
	 * @param lazy 是否是懒加载
	 * @param parentId 父节点ID
	 * @return
	 */
	List<MenuTree> treeMenu(boolean lazy, Integer parentId);

	/**
	 * 查询菜单
	 * @param menuSet
	 * @param parentId
	 * @return
	 */
	List<MenuTree> filterMenu(Set<MenuVO> menuSet, Integer parentId);

}
