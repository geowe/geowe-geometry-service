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
package org.geowe.service.geometry;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.geowe.service.filter.DivisionFilter;
import org.geowe.service.geometry.engine.GeoEngineer;
import org.geowe.service.geometry.engine.JTSGeoEngineer;
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.OperationData;
import org.geowe.service.model.mapper.FlatGeometryMapper;
import org.jboss.resteasy.annotations.GZIP;

/**
 * Rest end point for JTS Division operantions resources
 * 
 * @author rafa@geowe.org
 *
 */
@Path("/jts/division")
public class JtsDivideService {

	
	@Path("/polygons")
	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@DivisionFilter
	public Response dividePolygons(@NotNull @Valid OperationData operationData) {
		GeoEngineer geoEngineer = new JTSGeoEngineer();
		List<String> dividedPolygons = geoEngineer.dividePolygons(operationData);
		
		return Response.status(Status.CREATED)
				.entity(getFlatGeometries(operationData.getSourceData(), dividedPolygons))
				.build();
	}
	
	@Path("/lines")
	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@DivisionFilter
	public Response divideLines(@NotNull @Valid OperationData operationData) {
		GeoEngineer geoEngineer = new JTSGeoEngineer();
		List<String> dividedLines = geoEngineer.divideLines(operationData);
		return Response.status(Status.CREATED)
				.entity(getFlatGeometries(operationData.getSourceData(), dividedLines))
				.build();
	}
	
	private List<FlatGeometry> getFlatGeometries(Set<FlatGeometry> flatGeometries, List<String> wkts){
		FlatGeometryMapper mapper = new FlatGeometryMapper();
		return mapper.getFilledFlatGeometries(flatGeometries, wkts, 0.000001);
	}
	
}

