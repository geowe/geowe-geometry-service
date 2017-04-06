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

import org.geowe.service.constraints.ValidTopology;
import org.geowe.service.geometry.engine.JTSGeoEngineerHelper;

/**
 * Topology validator. Tests whether the Geometry for an WKT is topologically
 * valid, according to the OGC SFS specification.
 * 
 * @author rafa@geowe.org
 *
 */
public class TopologyValidator implements ConstraintValidator<ValidTopology, String> {

	private JTSGeoEngineerHelper helper;

	@Override
	public void initialize(ValidTopology constraintAnnotation) {
		helper = new JTSGeoEngineerHelper();
	}

	@Override
	public boolean isValid(String wkt, ConstraintValidatorContext context) {
		return helper.getGeom(wkt).isValid();
	}

}
