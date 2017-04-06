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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.geowe.service.constraints.VertexOrientation;
import org.geowe.service.geometry.engine.JTSGeoEngineerHelper;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Check whether the orientation of the vertices conforms to the Simple Feature
 * Specification.
 * 06-103r4_Implementation_Specification_for_Geographic_Information_
 * -_Simple_feature_access_-_Part_1_Common_Architecture_v1.2.1
 * 
 * @author rafa@geowe.org
 *
 */
public class VertexOrientationValidator implements ConstraintValidator<VertexOrientation, String> {

	private JTSGeoEngineerHelper helper;

	@Override
	public void initialize(VertexOrientation constraintAnnotation) {
		helper = new JTSGeoEngineerHelper();

	}

	@Override
	public boolean isValid(String wkt, ConstraintValidatorContext context) {
		boolean isValid = true;
		Geometry geom = helper.getGeom(wkt);
		if ("Polygon".equals(geom.getGeometryType())) {
			Polygon polygon = (Polygon) geom;
			isValid = CGAlgorithms.isCCW(polygon.getExteriorRing().getCoordinates());
			if (isValid) {
				isValid = isValidInteriorRings(polygon, context);
			}
		}
		return isValid;
	}

	private boolean isValidInteriorRings(Polygon polygon, ConstraintValidatorContext context) {
		boolean isValid = true;
		Set<Polygon> badInteriorRings = getNoValidInteriorRings(polygon);
		if (!badInteriorRings.isEmpty()) {
			isValid = false;
			context.disableDefaultConstraintViolation();
			String s = badInteriorRings.stream().map(Polygon::toText).collect(Collectors.joining(" | "));
			context.buildConstraintViolationWithTemplate(
					"{interiorring.vertex.orientarion.error}"+": [" + s+"]")
					.addConstraintViolation();
		}
		return isValid;

	}

	private Set<Polygon> getNoValidInteriorRings(Polygon polygon) {
		Set<Polygon> noValidInteriorRings = new HashSet<Polygon>();

		int numInteriorRings = polygon.getNumInteriorRing();
		if (numInteriorRings > 0) {
			for (int numRing = 0; numRing < numInteriorRings; numRing++) {
				LineString interiorRing = polygon.getInteriorRingN(numRing);
				if (CGAlgorithms.isCCW(interiorRing.getCoordinates())) {
					noValidInteriorRings.add(new GeometryFactory()
							.createPolygon(interiorRing.getCoordinates()));
				}
			}
		}
		return noValidInteriorRings;
	}

}
