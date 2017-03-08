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
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class represents OperationData. It is responsible for storing the data
 * to be operated
 * 
 * @author lotor
 *
 */
public class OperationData implements Serializable {

	private static final long serialVersionUID = -2030495979309359704L;
	
	@NotNull
	@NotEmpty
	private Set<FlatGeometry> sourceData;
	@NotNull
	@NotEmpty
	private Set<FlatGeometry> overlayData;

	public Set<FlatGeometry> getSourceData() {
		return sourceData;
	}

	public void setSourceData(Set<FlatGeometry> sourceData) {
		this.sourceData = sourceData;
	}

	public Set<FlatGeometry> getOverlayData() {
		return overlayData;
	}

	public void setOverlayData(Set<FlatGeometry> overlayData) {
		this.overlayData = overlayData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((overlayData == null) ? 0 : overlayData.hashCode());
		result = prime * result
				+ ((sourceData == null) ? 0 : sourceData.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperationData other = (OperationData) obj;
		if (overlayData == null) {
			if (other.overlayData != null)
				return false;
		} else if (!overlayData.equals(other.overlayData))
			return false;
		if (sourceData == null) {
			if (other.sourceData != null)
				return false;
		} else if (!sourceData.equals(other.sourceData))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OperationData [sourceData=" + sourceData + ", overlayData="
				+ overlayData + "]";
	}

}
