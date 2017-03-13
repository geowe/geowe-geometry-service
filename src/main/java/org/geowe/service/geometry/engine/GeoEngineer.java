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
package org.geowe.service.geometry.engine;

import java.util.Collection;
import java.util.List;

import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.OperationData;

/**
 * This class represents the Geometry expert. It is responsible for performing
 * the geometric operations
 * 
 * @author rafa@geowe.org
 *
 */
public interface GeoEngineer {
	/**
	 * calculates the geometry buffer
	 * 
	 * @param wkt:
	 *            Well Known Text
	 * @param distance:
	 *            distance to generate buffer in meters. Beware of the buffer in
	 *            geometries with geographic coordinate System like WGS84
	 * @param segments:
	 *            Number of quadrants segments to build arcs.
	 * @return WKT string
	 */
	String calculateBuffer(String wkt, double distance, int segments);

	/**
	 * calculates the geometry resulting from the union of the geometries in
	 * param.
	 * 
	 * @param entities:
	 *            Collection of FlatGeometry to calculate Union.
	 * @return WKT string
	 */
	String calculateUnion(Collection<FlatGeometry> entities);

	/**
	 * calculates the envelope of the geometry
	 * 
	 * @param wkt:
	 *            Well Known text
	 * @return WKT string
	 */
	String calculateEnvelope(String wkt);

	/**
	 * Calculates the centroid of the geometry
	 * 
	 * @param wkt:
	 *            Well Known text
	 * @return WKT string
	 */
	String calculateCentroid(String wkt);

	/**
	 * calculates the outline geometry resulting from the intersection of source
	 * and overlay data in entities.
	 * 
	 * @param entities:
	 *            Operation data to calculate intersection
	 * @param tolerance:
	 *            tolerance to apply.
	 * @return WKT string
	 */
	String calculateIntersection(OperationData entities, double tolerance);

	/**
	 * calculates an array of geometries resulting from the intersection of
	 * source and overlay data in entities.
	 * 
	 * @param entities:
	 *            Operation data to calculate intersection
	 * @param tolerance:
	 *            tolerance to apply.
	 * @return A Collection of WKT strings
	 */
	List<FlatGeometry> calculateIntersectionElements(OperationData entities, double tolerance);

	/**
	 * Calculates the outline geometry resulting from the Difference of source
	 * and overlay data in entities.
	 * 
	 * @param entities:
	 *            Operation data to calculate intersection
	 * @param tolerance:
	 *            tolerance to apply.
	 * @return WKT string
	 */
	String calculateDifference(OperationData operationData, double tolerance);

	/**
	 * calculates an array of geometries resulting from the Difference of source
	 * and overlay data in entities.
	 * 
	 * @param entities:
	 *            Operation data to calculate intersection
	 * @param tolerance:
	 *            tolerance to apply.
	 * @return A Collection of WKT strings
	 */
	List<FlatGeometry> calculateDifferenceElements(OperationData operationData, double tolerance);

	/**
	 * Calculates the outline geometry resulting from the Symmetric Difference
	 * of source and overlay data in entities.
	 * 
	 * @param entities:
	 *            Operation data to calculate intersection
	 * @param tolerance:
	 *            tolerance to apply.
	 * @return WKT string
	 */
	String calculateSymDifference(OperationData operationData, double tolerance);

	/**
	 * calculates an array of geometries resulting from the Symmetric Difference
	 * of source and overlay data in entities.
	 * 
	 * @param entities:
	 *            Operation data to calculate intersection
	 * @param tolerance:
	 *            tolerance to apply.
	 * @return A Collection of WKT strings
	 */
	List<FlatGeometry> calculateSymDifferenceElements(OperationData operationData, double tolerance);

	/**
	 * Calculates the geometries in source that intersects with overlay data,
	 * sent in the body
	 * 
	 * @param operationData
	 *            Operation data to calculate intersection
	 * @param tolerance:
	 *            tolerance to apply.
	 * @return A Collection of WKT strings
	 */
	List<FlatGeometry> calculateIntersectedElements(OperationData operationData, double tolerance);

	/**
	 * Combine a collection of FlatGeometry into a new FlatGeometry
	 * 
	 * @param entities
	 * @return
	 */
	String combine(Collection<FlatGeometry> entities);

	/**
	 * Calculate the geometric union of source and overlay data in
	 * OperantionData
	 * 
	 * @param operationData
	 *            Operation data to calculate Union
	 * @return
	 */
	List<FlatGeometry> calculateOverlapedUnion(OperationData operationData);

}