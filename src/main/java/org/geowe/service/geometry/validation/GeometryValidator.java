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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.geowe.service.constraints.GeometryValidationGroupDef;
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

	public Set<ConstraintViolation<FlatGeometry>> validate(FlatGeometry flatGeometry, Set<String> validationTypes) {

		Set<ConstraintViolation<FlatGeometry>> constraintsViolations = validator.validate(flatGeometry,
				getValidationGroups(validationTypes));

		return constraintsViolations;
	}

	private Class<?>[] getValidationGroups(Set<String> validationTypes) {
		final Set<Class<?>> validationGroups = new HashSet<Class<?>>();
		if (validationTypes.contains("all")) {
			validationGroups.addAll(Stream.of(GeometryValidationGroupDef.values())
            .map(GeometryValidationGroupDef::getClazz)
            .collect(Collectors.toSet()));
		}else{
			validationGroups.addAll(Stream.of(GeometryValidationGroupDef.values())
		            .filter(vgd ->  validationTypes.contains(vgd.getName()))
		            .map(GeometryValidationGroupDef::getClazz)
		            .collect(Collectors.toSet()));
		}
		return validationGroups.toArray(new Class<?>[validationGroups.size()]);
	}

}
