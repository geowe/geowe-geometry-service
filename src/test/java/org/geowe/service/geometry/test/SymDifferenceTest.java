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

public class SymDifferenceTest {
	private final Logger log = Logger.getLogger(SymDifferenceTest.class);
	
	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/sym-difference";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;
	private static final double TOLERANCE = -0.00001;
	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}
	

	@Test
	public void symDifferencePostRequestTest() {

		OperationData opData = DataTestProvider.getIntersectionData();
		Response response = target.request().post(
				Entity.entity(opData, "application/json;charset=UTF-8"));

		FlatGeometry differenceGeometry = response
				.readEntity(FlatGeometry.class);
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
	}

	@Test
	public void symDifferencePostNullbodyTest() {

		Response response = target.queryParam("tolerance", TOLERANCE).request().post(
				Entity.entity(null, "application/json;charset=UTF-8"));

		ErrorEntity error = response.readEntity(ErrorEntity.class);
		log.info(error);
		response.close();
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST
				.getStatusCode());
	}

	@Test
	public void symDifferencePostEmptyOperationDataTest() {

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
	public void symDifferencePolygonsFeatureCollection(){
		OperationData opData = DataTestProvider.getPolygonsFCIntersectionData();
		Response response = target.request().post(
				Entity.entity(opData, "application/json;charset=UTF-8"));

		String differenceGeometry = response
				.readEntity(String.class);
		log.info(differenceGeometry);
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
	}
	
	@Test
	public void symDifferencePolygonsFeatureCollectionElements(){
		OperationData opData = DataTestProvider.getPolygonsFCIntersectionData();
		target = restClient.target(SERVICE_URL+"/elements");
		Response response = target.request().post(
				Entity.entity(opData,"application/json;charset=UTF-8"));

		List<FlatGeometry> differenceGeometries = response.readEntity(new GenericType<List<FlatGeometry>>(){});
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(differenceGeometries.size() == 13);
	}

	@Test
	public void symDifferenceLinesFeatureCollectionElements(){
		OperationData opData = DataTestProvider.getLinesFCIntersectionData();
		target = restClient.target(SERVICE_URL+"/elements");
		Response response = target.queryParam("tolerance", 0.0001).request().post(
				Entity.entity(opData,"application/json;charset=UTF-8"));

		List<FlatGeometry> differenceGeometries = response.readEntity(new GenericType<List<FlatGeometry>>(){});
		response.close();
		log.info("***"+differenceGeometries);
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(differenceGeometries.size() == 5);
	}
}
