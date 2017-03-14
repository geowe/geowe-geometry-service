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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.geowe.service.geometry.engine.GeoEngineer;
import org.geowe.service.geometry.engine.JTSGeoEngineer;
import org.geowe.service.geometry.engine.JTSGeoEngineerHelper;
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.OperationData;
import org.geowe.service.model.mapper.FlatGeometryMapper;
import org.jboss.resteasy.annotations.GZIP;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Rest end point for JTS Intersection operations resources
 * 
 * @author lotor
 *
 */
@Path("/jts/intersection")
public class JtsIntersectionService {

	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// @IntersectionFilter
	public Response getIntersection(@NotNull @Valid OperationData operationData,
			@QueryParam("tolerance") @DefaultValue("-0.00001") double tolerance) {
		GeoEngineer geoEngineer = new JTSGeoEngineer();
		FlatGeometry intersectionFlatGeometry = new FlatGeometryBuilder()
				.wkt(geoEngineer.calculateIntersection(operationData, tolerance)).build();

		return Response.status(Status.CREATED).entity(intersectionFlatGeometry).build();
	}

	@Path("/elements")
	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getIntersectionElements(@NotNull @Valid OperationData operationData,
			@QueryParam("tolerance") @DefaultValue("-0.00001") double tolerance) {
		GeoEngineer geoEngineer = new JTSGeoEngineer();

		List<String> elements = geoEngineer.calculateIntersectionElements(operationData, tolerance);
		return Response.status(Status.CREATED)
				.entity(getFlatGeometries(operationData.getSourceData(), elements))
				.build();
	}
	
	private List<FlatGeometry> getFlatGeometries(Set<FlatGeometry> flatGeometries, List<String> wkts){
		FlatGeometryMapper mapper = new FlatGeometryMapper();
		return mapper.getFilledFlatGeometries(flatGeometries, wkts, 0.000001);
	}

}
