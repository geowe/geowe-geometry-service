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
package org.geowe.service.constraints.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.geowe.service.constraints.DuplicateVertex;
import org.geowe.service.geometry.engine.JTSGeoEngineerHelper;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.valid.RepeatedPointTester;

/**
 * Tests whether this Geometry has repeated vertex.
 * 
 * @author rafa@geowe.org
 *
 */
public class DuplicateVertexValidator implements ConstraintValidator<DuplicateVertex, String> {

	private JTSGeoEngineerHelper helper;
	private RepeatedPointTester repeatedPointTester;

	@Override
	public void initialize(DuplicateVertex constraintAnnotation) {
		helper = new JTSGeoEngineerHelper();
		repeatedPointTester = new RepeatedPointTester();
		
	}

	@Override
	public boolean isValid(String wkt, ConstraintValidatorContext context) {

		boolean hasRepeatedPoint = repeatedPointTester.hasRepeatedPoint(helper.getGeom(wkt));
		if (hasRepeatedPoint) {
			context.disableDefaultConstraintViolation();
			Point repeatedPoint = new GeometryFactory().createPoint(repeatedPointTester.getCoordinate());
			context.buildConstraintViolationWithTemplate(
					context.getDefaultConstraintMessageTemplate() + ": " + repeatedPoint.toText())
					.addConstraintViolation();
		}

		return !hasRepeatedPoint;
	}

}
