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

import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.error.ErrorEntity;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

public class EnvelopeTest {

	private final Logger log = Logger.getLogger(EnvelopeTest.class);

	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/envelope";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;
	private static final String ENVELOPE_WKT = "POLYGON ((-4.783172607421875 37.584527557100245, -4.783172607421875 37.63783370818002, -4.7055816650390625 37.63783370818002, -4.7055816650390625 37.584527557100245, -4.783172607421875 37.584527557100245))";

	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}

	@Test
	public void envelopePostRequestTest() {

		FlatGeometry sourceGeom = DataTestProvider.createFlatGeometry(DataTestProvider.POLYGON_1);

		Response response = target.request().post(Entity.entity(sourceGeom, "application/json;charset=UTF-8"));

		FlatGeometry envelopeGeometry = response.readEntity(FlatGeometry.class);
		log.info(envelopeGeometry);
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(ENVELOPE_WKT.equals(envelopeGeometry.getWkt()));
	}

	@Test
	public void envelopePostRequestNullBodyTest() {

		Response response = target.request().post(Entity.entity(null, "application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST.getStatusCode(), error.toString());

	}

	@Test
	public void envelopePostRequestEmptyGeometriesTest() {

		Response response = target.request().post(Entity.entity(new FlatGeometry(), "application/json;charset=UTF-8"));
		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST.getStatusCode(), error.toString());
	}

}
