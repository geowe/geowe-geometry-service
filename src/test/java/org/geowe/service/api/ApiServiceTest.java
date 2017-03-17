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
package org.geowe.service.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

public class ApiServiceTest {
	private final Logger log = Logger.getLogger(ApiServiceTest.class);
	
	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/api";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;

	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}

	@Test
	public void ApiDetinitionTest() {

		Response response = target.request().options();
		
		String jsonString = response.readEntity(String.class);
		log.info(jsonString);
		response.close();
		Assert.isTrue(response.getStatus() == Status.OK.getStatusCode()
				&& !jsonString.isEmpty());
	}
}
