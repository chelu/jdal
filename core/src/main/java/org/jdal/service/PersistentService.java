/*
 * Copyright 2009-2014 Jose Luis Martin.
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
package org.jdal.service;

import java.io.Serializable;

import org.jdal.dao.Dao;

/**
 * Interface for persistence services
 * @author Jose Luis Martin
 * @since 1.0
 * @deprecated Use {@link Dao} instead.
 *
 */
public interface PersistentService<T, PK extends Serializable> extends Dao<T, PK>{

}
