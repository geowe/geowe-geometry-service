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

import org.geowe.service.geometry.JtsService;
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.error.ErrorEntity;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

public class BufferTest {

	private final Logger log = Logger.getLogger(BufferTest.class);
	private static final double BUFFER_DISTANCE = 300;
	private static final int DEFAULT_SEGMENTS = 8;
	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/buffer";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;

	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}

	@Test
	public void bufferServiceTest() {
		JtsService service = new JtsService();

		FlatGeometry sourceGeom = DataTestProvider
				.createFlatGeometry(DataTestProvider.POLYGON_1);
		Response response = service.getBuffer(sourceGeom, BUFFER_DISTANCE, DEFAULT_SEGMENTS);

		FlatGeometry bufferedGeometry = (FlatGeometry) response.getEntity();

		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(isBufferEqual(sourceGeom.getWkt(),
				bufferedGeometry.getWkt(), DEFAULT_SEGMENTS));
	}

	@Test
	public void bufferPostRequestTest() {

		FlatGeometry sourceGeom = DataTestProvider
				.createFlatGeometry(DataTestProvider.POLYGON_1);

		Response response = target
				.queryParam("distance", BUFFER_DISTANCE)
				.request()
				.post(Entity.entity(sourceGeom,
						"application/json;charset=UTF-8"));

		FlatGeometry bufferedGeometry = response.readEntity(FlatGeometry.class);
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(isBufferEqual(sourceGeom.getWkt(),
				bufferedGeometry.getWkt(), 8));

	}
	
	@Test
	public void bufferPostRequestWithoutDistanceTest() {

		FlatGeometry sourceGeom = DataTestProvider
				.createFlatGeometry(DataTestProvider.POLYGON_1);

		Response response = target
				.request()
				.post(Entity.entity(sourceGeom,
						"application/json;charset=UTF-8"));

		String error = response.readEntity(String.class);
		log.info(error);
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		

	}
	
	@Test
	public void bufferPostRequestWithcustomSegmentsTest() {

		FlatGeometry sourceGeom = DataTestProvider
				.createFlatGeometry(DataTestProvider.POLYGON_1);

		Response response = target
				.queryParam("distance", BUFFER_DISTANCE)
				.queryParam("segments", 4)
				.request()
				.post(Entity.entity(sourceGeom,
						"application/json;charset=UTF-8"));

		FlatGeometry bufferedGeometry = response.readEntity(FlatGeometry.class);
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(isBufferEqual(sourceGeom.getWkt(),
				bufferedGeometry.getWkt(), 4));

	}

	private boolean isBufferEqual(String sourceGeom, String bufferedGeometry, int segments) {
		return bufferedGeometry.equals(DataTestProvider.getGeom(sourceGeom)
				.buffer(BUFFER_DISTANCE, segments).toText());

	}

	@Test
	public void bufferPostRequestNullBodyTest() {

		Response response = target.queryParam("distance", BUFFER_DISTANCE)
				.request()
				.post(Entity.entity(null, "application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		log.info(error.toString());
		response.close();
		Assert.isTrue(
				response.getStatus() == Status.BAD_REQUEST.getStatusCode(),
				error.toString());

	}

	@Test
	public void bufferPostRequestEmptyGeometryTest() {

		FlatGeometry sourceGeom = DataTestProvider.createFlatGeometry("");

		Response response = target
				.queryParam("distance", BUFFER_DISTANCE)
				.request()
				.post(Entity.entity(sourceGeom,
						"application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(
				response.getStatus() == Status.BAD_REQUEST.getStatusCode(),
				error.toString());

	}

	@Test
	public void bufferPostRequestNullGeometryTest() {

		FlatGeometry sourceGeom = DataTestProvider.createFlatGeometry(null);

		Response response = target
				.queryParam("distance", BUFFER_DISTANCE)
				.request()
				.post(Entity.entity(sourceGeom,
						"application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(
				response.getStatus() == Status.BAD_REQUEST.getStatusCode(),
				error.toString());
	}

}
