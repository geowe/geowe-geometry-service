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

/**
 * This class represents Division Data. It is responsible for storing the data
 * to be operated
 * 
 * @author rafa@geowe.org
 *
 */
public class DivisionData implements Serializable {

	
	private static final long serialVersionUID = -1404198858027181877L;
	
	@NotNull
	private FlatGeometry geomToDivide;
	@NotNull
	private FlatGeometry divisionLine;

	public FlatGeometry getGeomToDivide() {
		return geomToDivide;
	}

	public void setGeomToDivide(FlatGeometry geomToDivide) {
		this.geomToDivide = geomToDivide;
	}

	public FlatGeometry getDivisionLine() {
		return divisionLine;
	}

	public void setDivisionLine(FlatGeometry divisionLine) {
		this.divisionLine = divisionLine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((divisionLine == null) ? 0 : divisionLine.hashCode());
		result = prime * result + ((geomToDivide == null) ? 0 : geomToDivide.hashCode());
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
		DivisionData other = (DivisionData) obj;
		if (divisionLine == null) {
			if (other.divisionLine != null)
				return false;
		} else if (!divisionLine.equals(other.divisionLine))
			return false;
		if (geomToDivide == null) {
			if (other.geomToDivide != null)
				return false;
		} else if (!geomToDivide.equals(other.geomToDivide))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DivisionData [geomToDivide=" + geomToDivide + ", divisionLine=" + divisionLine + "]";
	}

}
