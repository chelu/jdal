package org.jdal.annotation;
/*
 * Copyright 2009-2013 Jose Luis Martin.
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


import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to keep in memory a copy of a non serializable object
 * until deserialization. Useful to avoid {@link java.io.NoSerializableException}
 * when storing in a container sessions objects that references non serializable 
 * objects.
 * 
 * @author Jose Luis Martin 
 */
@Target(FIELD) 
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializableProxy {

}
