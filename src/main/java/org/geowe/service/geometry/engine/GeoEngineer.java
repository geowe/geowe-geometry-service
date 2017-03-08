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
 * @author lotor
 *
 */
public interface GeoEngineer {

	String calculateBuffer(String wkt, double distance, int segments);

	String calculateUnion(Collection<FlatGeometry> entities);

	String calculateIntersection(OperationData entities);
	
	List<String> calculateIntersectionElements(OperationData entities);

	String calculateEnvelope(String wkt);

	String calculateCentroid(String wkt);

}