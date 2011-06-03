/*
 * Copyright 2009-2011 the original author or authors.
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
package info.joseluismartin.vaadin.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User model for table example
 * .
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@Entity
@Table(name="users")
public class User  {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	@Column(name="usuario")
	private String username;
	@Column(name="nombre")
	private String name;
	@Column(name="apellido")
	private String surname;
	@Column(name="edad")
	private int age;
	@Column(name="password")
	private String password;
	@Column(name="admin")
	private boolean admin;
	
	
	public User() {}
	
	
	/**
	 * Create new User
	 * @param username
	 * @param name
	 * @param surname
	 * @param age
	 * @param password
	 * @param admin
	 */
	public User(String username, String name, String surname, int age,
			String password, boolean admin) {
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.password = password;
		this.admin = admin;
	}


	/**
	 * Create new User by prototype
	 * @param user
	 */
	public User(User user) {
		this(user.getUsername(), user.getName(), user.getSurname(), user.getAge(),
				user.getPassword(), user.isAdmin());
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}
	
	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
