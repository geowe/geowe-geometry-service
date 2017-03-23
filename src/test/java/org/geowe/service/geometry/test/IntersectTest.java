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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.util.Assert;

public class IntersectTest {
	private final Logger log = Logger.getLogger(IntersectTest.class);
	
	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/intersect/elements";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;
	private static final double TOLERANCE = 0.001;
	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}
	

	
	@Test
	public void intersectPostNullbodyTest() {

		Response response = target.queryParam("tolerance", TOLERANCE).request().post(
				Entity.entity(null, "application/json;charset=UTF-8"));

		ErrorEntity error = response.readEntity(ErrorEntity.class);
		log.info(error);
		response.close();
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST
				.getStatusCode());
	}

	@Test
	public void intersectPostEmptyOperationDataTest() {

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
	public void intersectPolygonsFeatureCollectionElements(){
		OperationData opData = DataTestProvider.getPolygonsFCIntersectionData();
		Response response = target.request().post(
				Entity.entity(opData,"application/json;charset=UTF-8"));

		List<FlatGeometry> intersectedGeometries = response.readEntity(new GenericType<List<FlatGeometry>>(){});
		response.close();
		log.info(DataTestProvider.getGeometryCollection(intersectedGeometries));
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(intersectedGeometries.size() == 6);
	}
	
	
	@Test
	public void intersectLinesFeatureCollectionElements(){
		OperationData opData = DataTestProvider.getLinesFCIntersectionData();
		Response response = target.queryParam("tolerance", 0.001).request().post(
				Entity.entity(opData,"application/json;charset=UTF-8"));

		List<FlatGeometry> intersectedGeometries = response.readEntity(new GenericType<List<FlatGeometry>>(){});
		response.close();
		log.info(DataTestProvider.getGeometryCollection(intersectedGeometries));
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(intersectedGeometries.size() == 2);
	}
	
	
	private void printOperationData(OperationData data){
		ObjectMapper mapper = new ObjectMapper();
		try {
			log.info(mapper.writeValueAsString(data));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
