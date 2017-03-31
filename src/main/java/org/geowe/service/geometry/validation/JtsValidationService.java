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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.ValidationErrorData;
import org.geowe.service.model.ValidationResult;
import org.jboss.resteasy.annotations.GZIP;

/**
 * Rest end point for JTS validation geometries resources
 * 
 * @author rafa@geowe.org
 *
 */
@Path("/jts/validation")
public class JtsValidationService {

	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateOnExecution
	public Response validate(@NotNull @Valid FlatGeometry flatGeometry) {
		Set<ConstraintViolation<FlatGeometry>> errors = new GeometryValidator().hasTopologyErros(flatGeometry);

		return Response.status(Status.CREATED).entity(buildValidationResult(errors, flatGeometry)).build();
	}

	private ValidationResult buildValidationResult(Set<ConstraintViolation<FlatGeometry>> constraintViolations,
			FlatGeometry flatGeometry) {

		ValidationResult result = new ValidationResult();
		
		result.setValid(constraintViolations.isEmpty());
		result.setValidatedFlatGeometry(flatGeometry);
		result.setErrors(getValidationErrorData(constraintViolations));

		return result;
	}
	
	private List<ValidationErrorData> getValidationErrorData(Set<ConstraintViolation<FlatGeometry>> constraintViolations){
		return constraintViolations.stream()
				.map(constraintViolation -> new ValidationErrorData(
						(int) constraintViolation.getConstraintDescriptor().getAttributes().get("errorCode"),
						constraintViolation.getMessage()))
				.collect(Collectors.toList());
	}

}
