/*******************************************************************************
 * Copyright 2017 geowe.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.geowe.service.geometry;

import org.geowe.service.model.FlatGeometry;

/**
 * This class represents a FlatGeometry builder. It is responsible for build
 * FlatGeomtry objets.
 * 
 * @author lotor
 *
 */
public class FlatGeometryBuilder {

	private String id;
	private String crs;
	private String wkt;

	public FlatGeometryBuilder id(String id) {
		this.id = id;
		return this;
	}

	public FlatGeometryBuilder crs(String crs) {
		this.crs = crs;
		return this;
	}

	public FlatGeometryBuilder wkt(String wkt) {
		this.wkt = wkt;
		return this;
	}

	public FlatGeometry build() {
		return new FlatGeometry(id, crs, wkt);
	}
}
