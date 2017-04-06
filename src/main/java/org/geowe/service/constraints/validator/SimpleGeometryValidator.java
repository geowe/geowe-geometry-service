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

import org.geowe.service.constraints.SimpleGeometry;
import org.geowe.service.geometry.engine.JTSGeoEngineerHelper;

/**
 * Tests whether this Geometry is simple. The SFS definition of simplicity
 * follows the general rule that a Geometry is simple if it has no points of
 * self-tangency, self-intersection or other anomalous points. Simplicity is
 *
 * See JTS geometry.isSimple javadoc
 *  
 * @author rafa@geowe.org
 *
 */
public class SimpleGeometryValidator implements ConstraintValidator<SimpleGeometry, String> {

	private JTSGeoEngineerHelper helper;

	@Override
	public void initialize(SimpleGeometry constraintAnnotation) {
		helper = new JTSGeoEngineerHelper();
	}

	@Override
	public boolean isValid(String wkt, ConstraintValidatorContext context) {
		return helper.getGeom(wkt).isSimple();
	}

}
