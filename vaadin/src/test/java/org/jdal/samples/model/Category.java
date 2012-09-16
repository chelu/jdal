package org.jdal.samples.model;

import info.joseluismartin.model.Entity;

import javax.persistence.Table;

@javax.persistence.Entity
@Table(name="categories")
public class Category extends Entity {
	
	private static final long serialVersionUID = 1L;
	
	public String toString() {
		return getName();
	}

}
