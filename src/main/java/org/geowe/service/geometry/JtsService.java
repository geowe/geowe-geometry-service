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
import javax.validation.executable.ValidateOnExecution;
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
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.annotations.GZIP;

/**
 * Rest end point for JTS resources
 * 
 * @author lotor
 *
 */
@Path("/jts")
public class JtsService {

	@Path("/buffer")
	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateOnExecution
	public Response getBuffer(@NotNull @Valid FlatGeometry flatGeometry, @QueryParam("distance") double distance,
			@QueryParam("segments") @DefaultValue("8") int segments) {

		GeoEngineer geoEngineer = new JTSGeoEngineer();
		FlatGeometry bufferedFlatGeometry = new FlatGeometryBuilder().id(flatGeometry.getId())
				.crs(flatGeometry.getCrs()).wkt(geoEngineer.calculateBuffer(flatGeometry.getWkt(), distance, segments))
				.build();
		return Response.status(Status.CREATED).entity(bufferedFlatGeometry).build();
	}

	@Path("/union")
	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// TODO: must creates a custom validation to verify that the CRS is equals
	// in all entities
	public Response getUnion(@NotNull @NotEmpty @Valid List<FlatGeometry> entities) {

		GeoEngineer geoEngineer = new JTSGeoEngineer();
		FlatGeometry unionFlatGeometry = new FlatGeometryBuilder().crs(entities.get(0).getCrs())
				.wkt(geoEngineer.calculateUnion(entities)).build();

		return Response.status(Status.CREATED).entity(unionFlatGeometry).build();
	}

//	@Path("/intersection")
//	@POST
//	@GZIP
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	// @IntersectionFilter
//	public Response getIntersection(@NotNull @Valid OperationData operationData) {
//		GeoEngineer geoEngineer = new JTSGeoEngineer();
//		FlatGeometry intersectionFlatGeometry = new FlatGeometryBuilder()
//				.wkt(geoEngineer.calculateIntersection(operationData)).build();
//
//		return Response.status(Status.CREATED).entity(intersectionFlatGeometry).build();
//	}

	@Path("/envelope")
	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateOnExecution
	public Response getEnvelope(@NotNull @Valid FlatGeometry flatGeometry) {

		GeoEngineer geoEngineer = new JTSGeoEngineer();
		FlatGeometry envelopeFlatGeometry = new FlatGeometryBuilder().id(flatGeometry.getId())
				.crs(flatGeometry.getCrs()).wkt(geoEngineer.calculateEnvelope(flatGeometry.getWkt())).build();

		return Response.status(Status.CREATED).entity(envelopeFlatGeometry).build();
	}

	@Path("/centroid")
	@POST
	@GZIP
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateOnExecution
	public Response getCentroid(@NotNull @Valid FlatGeometry flatGeometry) {

		GeoEngineer geoEngineer = new JTSGeoEngineer();
		FlatGeometry centroidFlatGeometry = new FlatGeometryBuilder().id(flatGeometry.getId())
				.crs(flatGeometry.getCrs()).wkt(geoEngineer.calculateCentroid(flatGeometry.getWkt())).build();

		return Response.status(Status.CREATED).entity(centroidFlatGeometry).build();
	}
}
