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
package info.joseluismartin.gui.bind;

/**
 * A Factory for ControlAccessors
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 * @see info.joseluismartin.gui.bind.ControlAccessor
 */
public interface ControlAccessorFactory {
	
	/**
	 * Try to find a control accessor for a Class, use super Class if none is configured.
	 * 
	 * @param clazz Class to looking for
	 * @return a ControlAccessor for that class or null if none
	 */
	ControlAccessor getControlAccessor(Object control);

}
