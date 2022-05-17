
package com.px.modulars.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.modulars.upms.entity.SysDict;

/**
 * 字典表
 *
 * @author pinxun
 * @date 2019/03/19
 */
public interface SysDictService extends IService<SysDict> {

	/**
	 * 根据ID 删除字典
	 * @param id
	 * @return
	 */
	void removeDict(Integer id);

	/**
	 * 更新字典
	 * @param sysDict 字典
	 * @return
	 */
	void updateDict(SysDict sysDict);

}
