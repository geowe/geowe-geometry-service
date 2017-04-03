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
package org.geowe.service.geometry.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.geowe.service.constraints.TopologyGroup;
import org.geowe.service.model.FlatGeometry;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

/**
 * Represents a geometry validator. Is responsible for the validation of
 * geometries
 * 
 * @author rafa@geowe.org
 *
 */
public class GeometryValidator {

	private Validator validator;

	public GeometryValidator() {
		super();
		ValidatorFactory factory = Validation.byDefaultProvider().configure()
				.messageInterpolator(
						new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator("ErrorMessages")))
				.buildValidatorFactory();
		validator = factory.getValidator();
	}

	public Set<ConstraintViolation<FlatGeometry>> hasTopologyErros(FlatGeometry flatGeometry) {

		Set<ConstraintViolation<FlatGeometry>> constraintsViolations = validator.validate(flatGeometry, TopologyGroup.class);

		return constraintsViolations;
	}

}
