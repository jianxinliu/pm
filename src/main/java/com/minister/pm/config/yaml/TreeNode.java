package com.minister.pm.config.yaml;

import com.minister.pm.util.StringUtil;

/**
 * 配置树节点定义
 * 
 * @date 2020年2月9日 下午10:37:12
 * @author jianxinliu
 */
public class TreeNode {

	/**
	 * <code>
	 * 节点类型：
	 *	1 # 开头的注释行
	 *	2 - 开头的列表
	 *	3  其他则是正常的节点
	 * </code>
	 */
	private int nodeType;
	
	/**
	 * 当前节点所处的层数
	 */
	private int level;

	/**
	 * 子配置项
	 */
	private TreeNode subNode;

	/**
	 * 配置值。若存在配置值则为叶结点
	 */
	private String value;

	/**
	 * 若存在配置值则为叶结点
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		return StringUtil.isNotEmpty(this.value);
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public TreeNode getSubNode() {
		return subNode;
	}

	public void setSubNode(TreeNode subNode) {
		this.subNode = subNode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TreeNode [nodeType=").append(nodeType).append(", level=").append(level).append(", subNode=")
				.append(subNode).append(", value=").append(value).append("]");
		return builder.toString();
	}
}
