package org.jdal.ui.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeViewModel {
	List<TreeModelBuilder> builders = new ArrayList<TreeModelBuilder>();

	/**
	 * @return the builders
	 */
	public List<TreeModelBuilder> getBuilders() {
		return builders;
	}

	/**
	 * @param builders the builders to set
	 */
	public void setBuilders(List<TreeModelBuilder> builders) {
		this.builders = builders;
	}
}
