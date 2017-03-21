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
package org.geowe.service.geometry.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.geowe.service.geometry.JtsUnionService;
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.OperationData;
import org.geowe.service.model.error.ErrorEntity;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

public class UnionTest {

	private final Logger log = Logger.getLogger(UnionTest.class);
	
	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/union";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;
	private static final String UNION_WKT = "POLYGON ((-4.783172607421875 37.60084982441606, -4.7687530517578125 37.633483617951576, -4.758783727534563 37.6342538428435, -4.7577667236328125 37.636202454188854, -4.720504757959569 37.63721125645988, -4.7124481201171875 37.63783370818002, -4.7123962183792205 37.63743078092659, -4.67742919921875 37.63837745155179, -4.654335748019889 37.61249215194633, -4.634513854980469 37.63147161675051, -4.6039581298828125 37.57516784429852, -4.623527526855469 37.54441399087736, -4.631445845568974 37.55342804406164, -4.725494384765625 37.54316179356405, -4.78729248046875 37.579630178849854, -4.776905736240289 37.599531506278424, -4.783172607421875 37.60084982441606))";
	private static final String COMBINED_WKT= "MULTIPOLYGON (((-4.7687530517578125 37.633483617951576, -4.783172607421875 37.60084982441606, -4.7055816650390625 37.584527557100245, -4.7124481201171875 37.63783370818002, -4.7687530517578125 37.633483617951576)), ((-4.7577667236328125 37.636202454188854, -4.78729248046875 37.579630178849854, -4.725494384765625 37.54316179356405, -4.6307373046875 37.55350538772098, -4.6410369873046875 37.59758565735052, -4.67742919921875 37.63837745155179, -4.7577667236328125 37.636202454188854)), ((-4.634513854980469 37.63147161675051, -4.6657562255859375 37.60155704339839, -4.6369171142578125 37.55965642520303, -4.623527526855469 37.54441399087736, -4.6039581298828125 37.57516784429852, -4.634513854980469 37.63147161675051)))"; 
	
	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}

	@Test
	public void unionServiceTest() {
		JtsUnionService service = new JtsUnionService();
		Response response = service.getUnion(DataTestProvider
				.getThreeEntities());
		FlatGeometry unionGeometry = (FlatGeometry) response.getEntity();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(UNION_WKT.equals(unionGeometry.getWkt()));
	}

	@Test
	public void unionPostRequestTest() {

		Collection<FlatGeometry> sourceGeoms = DataTestProvider
				.getThreeEntities();
		Response response = target.request().post(
				Entity.entity(sourceGeoms, "application/json;charset=UTF-8"));

		FlatGeometry unionGeometry = response.readEntity(FlatGeometry.class);
		response.close();
		log.info(unionGeometry);
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(UNION_WKT.equals(unionGeometry.getWkt()));
	}

	@Test
	public void unionPostRequestNullBodyTest() {

		Response response = target.request().post(
				Entity.entity(null, "application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(
				response.getStatus() == Status.BAD_REQUEST.getStatusCode(),
				error.toString());

	}

	@Test
	public void unionPostRequestEmptyGeometriesTest() {

		Collection<FlatGeometry> sourceGeoms = new ArrayList<FlatGeometry>();

		Response response = target.request().post(
				Entity.entity(sourceGeoms, "application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(
				response.getStatus() == Status.BAD_REQUEST.getStatusCode(),
				error.toString());

	}

	@Test
	public void unionPostRequestNullGeometryTest() {

		FlatGeometry nullGeom = DataTestProvider.createFlatGeometry(null);
		Collection<FlatGeometry> sourceGeoms = DataTestProvider
				.getThreeEntities();
		sourceGeoms.add(nullGeom);
		Response response = target.request().post(
				Entity.entity(sourceGeoms, "application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(
				response.getStatus() == Status.BAD_REQUEST.getStatusCode(),
				error.toString());
	}
	
	@Test
	public void combinePostRequestNullGeometryTest() {

		
		Collection<FlatGeometry> sourceGeoms = DataTestProvider
				.getThreeEntities();
		target = restClient.target(SERVICE_URL+"/combined");
		Response response = target.request().post(
				Entity.entity(sourceGeoms, "application/json;charset=UTF-8"));
		FlatGeometry combinedFlatGeometry = response.readEntity(FlatGeometry.class);
		response.close();
		Assert.isTrue(
				response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(COMBINED_WKT.equals(combinedFlatGeometry.getWkt()));
	}
	
	@Test
	public void overlapedUnionPolygonsFeatureCollectionElements(){
		OperationData opData = DataTestProvider.get2PolygonsOverlayFCData();
		target = restClient.target(SERVICE_URL+"/overlaped");
		Response response = target.request().post(
				Entity.entity(opData,"application/json;charset=UTF-8"));

		List<FlatGeometry> overlapedUnionGeometries = response.readEntity(new GenericType<List<FlatGeometry>>(){});
		response.close();
		log.info(overlapedUnionGeometries.size());
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(overlapedUnionGeometries.size() == 21);
	}
	
	@Test
	public void overlapedUnionLinesFeatureCollectionElements(){
		OperationData opData = DataTestProvider.getLinesFCIntersectionData();
		target = restClient.target(SERVICE_URL+"/overlaped");
		Response response = target.request().post(
				Entity.entity(opData,"application/json;charset=UTF-8"));

		List<FlatGeometry> overlapedUnionGeometries = response.readEntity(new GenericType<List<FlatGeometry>>(){});
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(overlapedUnionGeometries.size() == 2);
	}

}
