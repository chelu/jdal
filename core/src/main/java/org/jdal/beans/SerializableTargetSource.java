/*
 * Copyright 2009-2012 Jose Luis Martin.
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
package org.jdal.beans;

import java.io.Serializable;

import org.springframework.aop.TargetSource;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * SingletonTargetSource that use a SerializableObject to wrap the target
 * to avoid serialization issues in a container.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 */
public class SerializableTargetSource implements TargetSource, Serializable {
	
	private final DefaultSerializableObject target;

	
	public SerializableTargetSource(Object target) {
		Assert.notNull(target, "Target object must not be null");
		this.target = new DefaultSerializableObject(target);
	}


	public Class<?> getTargetClass() {
		return this.target.getSerializedObject().getClass();
	}
	
	public Object getTarget() {
		return this.target.getSerializedObject();
	}
	
	public void releaseTarget(Object target) {
		// nothing to do
	}

	public boolean isStatic() {
		return true;
	}


	/**
	 * Two invoker interceptors are equal if they have the same target or if the
	 * targets or the targets are equal.
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SerializableTargetSource)) {
			return false;
		}
		SerializableTargetSource otherTargetSource = (SerializableTargetSource) other;
		return this.target.getSerializedObject().equals(otherTargetSource.target.getSerializedObject());
	}

	/**
	 * SerializableTargetSource uses the hash code of the target object.
	 */
	@Override
	public int hashCode() {
		return this.target.getSerializedObject().hashCode();
	}

	@Override
	public String toString() {
		return "SerializableTargetSource for target object [" + ObjectUtils.identityToString(this.target.getSerializedObject()) + "]";
	}


}
