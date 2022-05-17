

package com.px.modulars.upms.dto;

import com.px.modulars.upms.dto.TreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author pinxun
 * @date 2019/2/1 部门树
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends TreeNode {

	private String name;

}
