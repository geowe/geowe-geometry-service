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

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.geowe.service.constraints.GeometryValidationGroupDef;
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.ValidationResult;
import org.geowe.service.model.error.ErrorEntity;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

public class ValidationTest {

	private final Logger log = Logger.getLogger(ValidationTest.class);
	
	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/validation";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;
	private static final String CORRECT_POLYGON_WKT = "POLYGON((-46528.069249049 5654761.2856803,423101.03245095 5390594.915974,85555.115604077 5087292.7877928,-286234.58990842 5405270.8254022,-46528.069249049 5654761.2856803))";
	private static final String TOPOLOGY_ERROR_POLYGON_WKT = "POLYGON((-716727.93313342 5097076.7274115,-237314.89181468 4999237.331224,-863487.02741468 4710611.1124709,-144367.46543655 4651907.4747584,-716727.93313342 5097076.7274115))";
	private static final String NOT_SIMPLE_LINESTRING_WKT ="LINESTRING(-765647.63122717 5092184.7576022,51311.326938454 5160672.3349334,31743.447700951 4627447.6257115,-379182.01628655 5415054.7650209,-599320.65770843 5405270.8254022)";
	
	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}


	@Test
	public void validateCorrectPolygonPostRequestTest() {

		FlatGeometry sourceGeom = new FlatGeometry("1", "EPSG:3857", CORRECT_POLYGON_WKT);
		
		Response response = target.request().post(
				Entity.entity(sourceGeom, "application/json;charset=UTF-8"));
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		
		ValidationResult result = response.readEntity(ValidationResult.class);
		response.close();
		
		Assert.isTrue(result.isValid() == true);
		
	}

	@Test
	public void validateTopologyErrorPolygonPostRequestTest() {

		FlatGeometry sourceGeom = new FlatGeometry("2", "EPSG:3857", TOPOLOGY_ERROR_POLYGON_WKT);
				
		
		Response response = target.queryParam("validate", GeometryValidationGroupDef.TOPOLOGY.getName())
				.request().post(
				Entity.entity(sourceGeom, "application/json;charset=UTF-8"));
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		
		ValidationResult result = response.readEntity(ValidationResult.class);
		log.info(result.getErrors());
		response.close();
		
		Assert.isTrue(result.isValid() == false);
		
	}
	
	@Test
	public void validateNotSimpleLineStringPostRequestTest() {

		FlatGeometry sourceGeom = new FlatGeometry("2", "EPSG:3857", NOT_SIMPLE_LINESTRING_WKT);
				
		
		Response response = target.request().post(
				Entity.entity(sourceGeom, "application/json;charset=UTF-8"));
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		
		ValidationResult result = response.readEntity(ValidationResult.class);
		log.info(result.getErrors());
		response.close();
		
		Assert.isTrue(result.isValid() == false);
		
	}

	@Test
	public void validationPostRequestNullBodyTest() {

		Response response = target.request().post(
				Entity.entity(null, "application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(
				response.getStatus() == Status.BAD_REQUEST.getStatusCode(),
				error.toString());

	}

	@Test
	public void validationPostRequestEmptyGeometriesTest() {
		
		Response response = target.request().post(
				Entity.entity(new FlatGeometry(), "application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		log.info(error);
		Assert.isTrue(
				response.getStatus() == Status.BAD_REQUEST.getStatusCode(),
				error.toString());

	}
}
