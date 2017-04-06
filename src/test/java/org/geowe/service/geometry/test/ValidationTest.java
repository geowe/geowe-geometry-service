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
	private static final String CORRECT_POLYGON_WKT = "POLYGON((-640902.40268802 6553354.9916398,-961326.42520209 6582706.8104961,-985786.27424896 6335662.3351226,-794999.45168334 6267174.7577914,-640902.40268802 6553354.9916398))";
	private static final String TOPOLOGY_ERROR_POLYGON_WKT = "POLYGON((-716727.93313342 5097076.7274115,-237314.89181468 4999237.331224,-863487.02741468 4710611.1124709,-144367.46543655 4651907.4747584,-716727.93313342 5097076.7274115))";
	private static final String NOT_SIMPLE_LINESTRING_WKT ="LINESTRING(-765647.63122717 5092184.7576022,51311.326938454 5160672.3349334,31743.447700951 4627447.6257115,-379182.01628655 5415054.7650209,-599320.65770843 5405270.8254022)";
	private static final String REPEATED_VERTEX_LINESTRING_WTK ="LINESTRING(-790107.48027405 4722841.0369943,-790107.48027405 4722841.0369943,-486805.3520928 4801112.5539443,-242206.86162405 4566298.0030943,-188395.19372093 4649461.4898537)";
	private static final String INCORRECT_HOLE_ORIENTATION_WKT ="POLYGON((-320478.37857405 5816196.2893897,-1235276.7329272 6070578.7194772,-1186357.0348334 5473758.4027334,-613996.56713655 5214484.0028365,-320478.37857405 5816196.2893897),(-956434.4537928 5830872.1988178,-1108085.5178834 5850440.0780553,-1142329.306549 5605841.5875865,-1005354.1518865 5547137.949874,-956434.4537928 5830872.1988178),(-545508.9898053 5708572.9535834,-731403.84256155 5752600.6818678,-794999.45008342 5473758.4027334,-648240.35580217 5361243.0971178,-545508.9898053 5708572.9535834))";
	private static final String INCORRECT_POLYGON_ORIENTATION_WKT = "POLYGON((-46528.069249049 5654761.2856803,423101.03245095 5390594.915974,85555.115604077 5087292.7877928,-286234.58990842 5405270.8254022,-46528.069249049 5654761.2856803))";
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

		Response response = target.queryParam("validate",GeometryValidationGroupDef.TOPOLOGY.getName())
				.queryParam("validate",GeometryValidationGroupDef.SIMPLICITY.getName())
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
	public void validateRepeatedVertexLineStringPostRequestTest() {

		FlatGeometry sourceGeom = new FlatGeometry("2", "EPSG:3857", REPEATED_VERTEX_LINESTRING_WTK);
		
		Response response = target.request().post(
				Entity.entity(sourceGeom, "application/json;charset=UTF-8"));
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		
		ValidationResult result = response.readEntity(ValidationResult.class);
		log.info(result.getErrors());
		
		response.close();
		
		Assert.isTrue(result.isValid() == false);
	}
	
	@Test
	public void validatePolygonVertexOrientationPostRequestTest() {

		FlatGeometry sourceGeom = new FlatGeometry("2", "EPSG:3857", INCORRECT_POLYGON_ORIENTATION_WKT);
		
		Response response = target.request().post(
				Entity.entity(sourceGeom, "application/json;charset=UTF-8"));
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		
		ValidationResult result = response.readEntity(ValidationResult.class);
		log.info(result.getErrors());
		
		response.close();
		
		Assert.isTrue(result.isValid() == false);
	}

	@Test
	public void validateHoleVertexOrientationPostRequestTest() {

		FlatGeometry sourceGeom = new FlatGeometry("2", "EPSG:3857", INCORRECT_HOLE_ORIENTATION_WKT);
		
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
