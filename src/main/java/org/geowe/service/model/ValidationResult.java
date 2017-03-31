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
import java.util.ArrayList;
import java.util.List;

/**
 * Represent the validation result
 * @author rafa@geowe.org
 *
 */
public class ValidationResult implements Serializable {


	private static final long serialVersionUID = -7546966370178181715L;
	private boolean valid;
	private FlatGeometry validatedFlatGeometry;
	
	private List<ValidationErrorData> errors = new ArrayList<ValidationErrorData>();

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean isValid) {
		this.valid = isValid;
	}

	public FlatGeometry getValidatedFlatGeometry() {
		return validatedFlatGeometry;
	}

	public void setValidatedFlatGeometry(FlatGeometry validatedFlatGeometry) {
		this.validatedFlatGeometry = validatedFlatGeometry;
	}

	public List<ValidationErrorData> getErrors() {
		return errors;
	}

	public void setErrors(List<ValidationErrorData> errors) {
		this.errors = errors;
	}

	public void addValidationError(ValidationErrorData error){
		errors.add(error);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errors == null) ? 0 : errors.hashCode());
		result = prime * result + (valid ? 1231 : 1237);
		result = prime * result + ((validatedFlatGeometry == null) ? 0 : validatedFlatGeometry.hashCode());
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
		ValidationResult other = (ValidationResult) obj;
		if (errors == null) {
			if (other.errors != null)
				return false;
		} else if (!errors.equals(other.errors))
			return false;
		if (valid != other.valid)
			return false;
		if (validatedFlatGeometry == null) {
			if (other.validatedFlatGeometry != null)
				return false;
		} else if (!validatedFlatGeometry.equals(other.validatedFlatGeometry))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ValidationResult [isValid=" + valid + ", validatedFlatGeometry=" + validatedFlatGeometry + ", errors="
				+ errors + "]";
	}

}
