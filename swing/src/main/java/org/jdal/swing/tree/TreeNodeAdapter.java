/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdal.swing.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * An Adapter from any class to MutableTreeNode.
 *  
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TreeNodeAdapter implements MutableTreeNode  {

	private Node node;
	private MutableTreeNode parent;
	
	
	public TreeNodeAdapter(Node node) {
		this.node = node;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration children() {
		ArrayList<TreeNode> list  = new ArrayList<TreeNode>();
		for (Node n : node.getChildren()) {
			list.add(new TreeNodeAdapter(n));
		}
		return Collections.enumeration(list);
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public TreeNode getChildAt(int childIndex) {
		TreeNodeAdapter child = new TreeNodeAdapter(node.getChildren().get(childIndex));
		child.setParent(this);
		return child;
	}

	public int getChildCount() {
		return node.getChildren().size();
	}

	public int getIndex(TreeNode treeNode) {
		int i = 0;
		for (i = 0;  i < this.node.getChildren().size(); i++) {
			if (this.node.getChildren().get(i).equals(treeNode)) 
				break;
		}
		return i;
	}

	public TreeNode getParent() {
		return parent != null ? parent : 
			(node.getParent() != null ? new TreeNodeAdapter(node.getParent()) : null);
	}

	public boolean isLeaf() {
		return node.getChildren().size() == 0;
	}
	
	public String toString() {
		return node.toString();
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void add(Node node) {
		this.node.addChild(node);
	}

	public void insert(MutableTreeNode child, int index) {
		if (child instanceof TreeNodeAdapter)
			node.addChild(((TreeNodeAdapter)child).getNode());
		
	}

	public void remove(int index) {
		node.removeChild(node.getChildren().get(index));
	}

	public void remove(MutableTreeNode node) {
		if (node instanceof TreeNodeAdapter)
			this.node.removeChild(((TreeNodeAdapter)node).getNode());
	}

	public void removeFromParent() {
		if (parent != null) {
			parent.remove(this);
			parent = null;
		}
	}

	public void setParent(MutableTreeNode newParent) {
		parent = newParent;
	}

	public void setUserObject(Object object) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeNodeAdapter other = (TreeNodeAdapter) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}
}
