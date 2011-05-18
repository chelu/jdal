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
package info.joseluismartin.gui.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Simple TreeNode implementation that use List to hold chidrens.
 * mainly used for RooNodes
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class ListTreeNode implements MutableTreeNode {

	private MutableTreeNode parent;
	private List children = new ArrayList();
	private Object userObject = null;

	public ListTreeNode(String name) {
		this.userObject = name;
	}
	/**
	 * {@inheritDoc}
	 */
	public Enumeration children() {
		return Collections.enumeration(children);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAllowsChildren() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public TreeNode getChildAt(int childIndex) {
		return (TreeNode) children.get(childIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getChildCount() {
		return children.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getIndex(TreeNode node) {
		for (int i = 0; i < children.size(); i++) {
			if (node != null && node.equals(children.get(i)))
				return i;
		}
		// not found
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLeaf() {
		return children.size() == 0;
	}
	
	/**
	 * 
	 */
	public void add(TreeNode node) {
		children.add(node);
	}
	
	public void remove(TreeNode node) {
		children.remove(node);
	}
	
	public String toString() {
		return userObject.toString();
	}
	
	/**
	 * @return
	 */
	public List getList() {
		return children;
	}
	
	public void insert(MutableTreeNode child, int index) {
		children.add(index, child);
		
	}

	public void remove(int index) {
		children.remove(index);
	}
	
	public void remove(MutableTreeNode node) {
		children.remove(node);
	}
	
	public void removeFromParent() {
		if (parent != null) {
			parent.remove(this);
		}
	}

	public void setParent(MutableTreeNode newParent) {
		parent = newParent;
		parent.insert(this, newParent.getChildCount());
	}
	
	public void setUserObject(Object object) {
		
	}
}
