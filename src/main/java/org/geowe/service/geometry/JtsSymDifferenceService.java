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
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.OperationData;
import org.jboss.resteasy.annotations.GZIP;

/**
 * Rest end point for JTS Symetric difference operantions resources
 * 
 * @author rafa@geowe.org
 *
 */
@Path("/jts/sym-difference")
public class JtsSymDifferenceService {

	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDifference(@NotNull @Valid OperationData operationData,
			@QueryParam("tolerance") @DefaultValue("-0.00001") double tolerance) {
		GeoEngineer geoEngineer = new JTSGeoEngineer();
		FlatGeometry intersectionFlatGeometry = new FlatGeometryBuilder()
				.wkt(geoEngineer.calculateSymDifference(operationData, tolerance)).build();

		return Response.status(Status.CREATED).entity(intersectionFlatGeometry).build();
	}

	@Path("/elements")
	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDifferenceElements(@NotNull @Valid OperationData operationData,
			@QueryParam("tolerance") @DefaultValue("-0.00001") double tolerance) {
		GeoEngineer geoEngineer = new JTSGeoEngineer();

		List<String> elements = geoEngineer.calculateSymDifferenceElements(operationData, tolerance);

		return Response.status(Status.CREATED).entity(elements).build();
	}

}
