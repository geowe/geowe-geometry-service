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
package org.geowe.service.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.geowe.service.constraints.GeometryValidationGroup;
import org.geowe.service.constraints.SimpleGeometry;
import org.geowe.service.constraints.SimplicityGroup;
import org.geowe.service.constraints.TopologyGroup;
import org.geowe.service.constraints.ValidTopology;
import org.hibernate.validator.constraints.NotBlank;

/**
 * This class represents a Geometry. It is responsible for storing the geometry
 * data
 * 
 * @author rafa@geowe.org
 *
 */
public class FlatGeometry implements Serializable{

	private static final long serialVersionUID = -9132577564797931810L;

	private String id;

	private String crs;

	@NotNull
	@NotBlank
	@ValidTopology(groups={TopologyGroup.class})
	@SimpleGeometry(groups={SimplicityGroup.class})
	private String wkt;

	public FlatGeometry(){
		
	}
	public FlatGeometry(String id, String crs, String wkt) {
		this.id = id;
		this.crs = crs;
		this.wkt = wkt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public String getWkt() {
		return wkt;
	}

	public void setWkt(String wkt) {
		this.wkt = wkt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crs == null) ? 0 : crs.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((wkt == null) ? 0 : wkt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FlatGeometry other = (FlatGeometry) obj;
		if (crs == null) {
			if (other.crs != null) {
				return false;
			}
		} else if (!crs.equals(other.crs)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (wkt == null) {
			if (other.wkt != null) {
				return false;
			}
		} else if (!wkt.equals(other.wkt)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "FlatGeometry [id=" + id + ", crs=" + crs + ", wkt=" + wkt + "]";
	}

}
