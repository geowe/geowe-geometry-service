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

import java.util.HashSet;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.geowe.service.geometry.JtsIntersectionService;
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

public class IntersectionTest {
	private final Logger log = Logger.getLogger(IntersectionTest.class);
	
	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/intersection";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;
	private static final String INTERSECTION_WKT = "POLYGON ((-4.776890766405977 37.59953857604632, -4.783158473202792 37.60085706996653, -4.768746321046889 37.63347410816212, -4.758777428165223 37.63424429972881, -4.75776057778995 37.63619261691262, -4.72050423716731 37.637201266895275, -4.712456824160618 37.63782300590933, -4.712404981850047 37.63742054000688, -4.67743357013068 37.63836732955289, -4.6410461453507175 37.597580901219175, -4.630749610620046 37.55351410381898, -4.63144176324856 37.553438549085996, -4.72549216536138 37.54317209523422, -4.787279223379428 37.579633966944684, -4.776890766405977 37.59953857604632))";
	private static final double TOLERANCE = -0.00001;
	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}
	

	@Test
	public void intersectionTest() {
		JtsIntersectionService service = new JtsIntersectionService();
		Response response = service.getIntersection(DataTestProvider
				.getIntersectionData(), TOLERANCE);
		FlatGeometry intersectionGeometry = (FlatGeometry) response.getEntity();

		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode(),
				intersectionGeometry.getWkt());
		Assert.isTrue(INTERSECTION_WKT.equals(intersectionGeometry.getWkt()));
	}

	
	@Test
	public void intersectinPostRequestTest() {

		OperationData opData = DataTestProvider.getIntersectionData();
		Response response = target.request().post(
				Entity.entity(opData, "application/json;charset=UTF-8"));

		FlatGeometry intersectionGeometry = response
				.readEntity(FlatGeometry.class);
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(INTERSECTION_WKT.equals(intersectionGeometry.getWkt()));
	}

	@Test
	public void intersectinPostNullbodyTest() {

		Response response = target.queryParam("tolerance", TOLERANCE).request().post(
				Entity.entity(null, "application/json;charset=UTF-8"));

		ErrorEntity error = response.readEntity(ErrorEntity.class);
		log.info(error);
		response.close();
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST
				.getStatusCode());
	}

	@Test
	public void intersectionPostEmptyOperationDataTest() {

		OperationData opData = DataTestProvider.getIntersectionData();
		opData.setSourceData(new HashSet<FlatGeometry>());
		opData.setOverlayData(new HashSet<FlatGeometry>());
		Response response = target.request().post(
				Entity.entity(opData, "application/json;charset=UTF-8"));

		String error = response.readEntity(String.class);
		log.info(error);
		response.close();
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST
				.getStatusCode());
	}
	
	@Test
	public void intersectionPolygonsFeatureCollection(){
		OperationData opData = DataTestProvider.getPolygonsFCIntersectionData();
		Response response = target.request().post(
				Entity.entity(opData, "application/json;charset=UTF-8"));

		String intersectionGeometry = response
				.readEntity(String.class);
		log.info(intersectionGeometry);
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
	}
	
	@Test
	public void intersectionPolygonsFeatureCollectionElements(){
		OperationData opData = DataTestProvider.getPolygonsFCIntersectionData();
		target = restClient.target(SERVICE_URL+"/elements");
		Response response = target.request().post(
				Entity.entity(opData,"application/json;charset=UTF-8"));

		List<FlatGeometry> intersectionGeometries = response.readEntity(new GenericType<List<FlatGeometry>>(){});
		response.close();
		log.info(intersectionGeometries);
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(intersectionGeometries.size() == 7);
	}
	
	@Test
	public void intersectionLinesFeatureCollectionElements(){
		OperationData opData = DataTestProvider.getLinesFCIntersectionData();
		target = restClient.target(SERVICE_URL+"/elements");
		Response response = target.queryParam("tolerance", 0.00001).request().post(
				Entity.entity(opData,"application/json;charset=UTF-8"));

		List<FlatGeometry> intersectionGeometries = response.readEntity(new GenericType<List<FlatGeometry>>(){});
		response.close();
		log.info(intersectionGeometries);
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(intersectionGeometries.size() == 2);
	}

}
