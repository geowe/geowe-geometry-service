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
package org.geowe.service.model.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.geowe.service.geometry.FlatGeometryBuilder;
import org.geowe.service.geometry.engine.JTSGeoEngineerHelper;
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.OperationData;

/**
 * Obtain Flat geometries to fill operations results
 * 
 * @author rafa@geowe.org
 *
 */
public class FlatGeometryMapper {

	private JTSGeoEngineerHelper helper = new JTSGeoEngineerHelper();

	public List<FlatGeometry> getFilledFlatGeometries(Set<FlatGeometry> sourceFlatGeometries,
			List<String> intersectedWkts, double tolerance) {
		List<FlatGeometry> fGeoms = new ArrayList<FlatGeometry>();

		for (String intersectedWkt : intersectedWkts) {
			fGeoms.add(getIntersectedFlatGeomtetry(intersectedWkt, sourceFlatGeometries, tolerance));
		}
		return fGeoms;
	}

	public FlatGeometry getIntersectedFlatGeomtetry(String wkt, Set<FlatGeometry> sourceFlatGeometries,
			double tolerance) {
		FlatGeometry intersectedFlatGeom = new FlatGeometryBuilder().wkt(wkt).build();

		for (FlatGeometry flatGeometry : sourceFlatGeometries) {
			if (helper.getGeom(wkt).intersects(helper.getGeom(flatGeometry.getWkt()).buffer(tolerance))) {
				intersectedFlatGeom.setCrs(flatGeometry.getCrs());
				intersectedFlatGeom.setId(flatGeometry.getId());
				break;
			}
		}
		return intersectedFlatGeom;
	}

	public List<FlatGeometry> getOverlapedUnionFlatGeometries(List<String> overlapedUnionWkts,
			OperationData operationData) {
		List<FlatGeometry> overlapedUnionFlatGeometries = getFilledFlatGeometries(operationData.getSourceData(),
				overlapedUnionWkts, 0.00001);
		if (overlapedUnionFlatGeometries.size() != overlapedUnionWkts.size()) {
			overlapedUnionWkts.remove(helper.getWkts(overlapedUnionFlatGeometries));
			overlapedUnionFlatGeometries
					.addAll(getFilledFlatGeometries(operationData.getOverlayData(), overlapedUnionWkts, 0.00001));
		}

		return overlapedUnionFlatGeometries;
	}
}
