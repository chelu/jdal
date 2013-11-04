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
package view;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import junit.framework.TestCase;
import model.Author;
import model.Book;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.swing.AbstractView;
import org.jdal.swing.validation.BackgroundErrorProcessor;
import org.jdal.ui.validation.ErrorProcessor;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ViewTest extends TestCase {
	
	private final static Log log = LogFactory.getLog(ViewTest.class);
	
	public void testAbstractView() {
		Author author = new Author("", "The King");
		Book book = new Book(null, author);
		BookView view = new BookView();
		List<ErrorProcessor> errorProcessors = new ArrayList<ErrorProcessor>();
		errorProcessors.add(new BackgroundErrorProcessor());		
		view.setErrorProcessors(errorProcessors);	
		author.setBindFaliure(false);
		view.setModel(book);
		view.autobind();
		view.refresh();
		view.update();
		view.setValidator(createValidator());
		view.validateView();
		log.info(view.getErrorMessage());
	}

	/**
	 * @return
	 */
	private Validator createValidator() {
		LocalValidatorFactoryBean vfb = new LocalValidatorFactoryBean();
		vfb.afterPropertiesSet();
		return vfb;
	}
}

class BookView extends AbstractView<Book> {
	JTextField title = new JTextField();
	AuthorView author = new AuthorView();

	@Override
	protected JComponent buildPanel() {
		return new JPanel();
	}
}

class AuthorView extends AbstractView<Author> {
	JTextField name = new JTextField();
	JTextField surname = new JTextField();
	
	public AuthorView() {
		setModel(new Author("", ""));
		this.autobind();
	}

	@Override
	protected JComponent buildPanel() {
		return new JPanel();
	}
}