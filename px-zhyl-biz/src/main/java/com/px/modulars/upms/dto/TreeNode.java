
/*
 *
 * 此类来自 https://gitee.com/geek_qi/cloud-platform/blob/master/ace-common/src/main/java/com/github/wxiaoqi/security/common/vo/TreeNode.java
 * @ Apache-2.0
 */

package com.px.modulars.upms.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ace
 * @author pinxun
 * @date 2017年11月9日23:33:45
 */
@Data
public class TreeNode {

	protected int id;

	protected int parentId;

	protected List<com.px.modulars.upms.dto.TreeNode> children = new ArrayList<com.px.modulars.upms.dto.TreeNode>();

	public void add(com.px.modulars.upms.dto.TreeNode node) {
		children.add(node);
	}

}
