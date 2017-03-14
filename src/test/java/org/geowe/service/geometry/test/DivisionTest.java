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
import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.geowe.service.geometry.FlatGeometryBuilder;
import org.geowe.service.geometry.JtsDivideService;
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

public class DivisionTest {

	private final Logger log = Logger.getLogger(DivisionTest.class);

	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/division/polygons";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;
	private static final String POLYGON_FC_WKT = "GEOMETRYCOLLECTION(POLYGON((-5.9685606135835565 38.333782907228816,-3.9141172545765723 38.31654526867596,-3.9360899108226595 37.52790590365409,-5.81475201986113 37.40582726607118,-5.9685606135835565 38.333782907228816)),POLYGON((-3.886651448642032 38.19360849133716,-3.5295957846435067 38.17201919361452,-3.8070005697500684 37.98392250848125,-3.886651448642032 38.19360849133716)))";
	private static final String LINE_WKT = "LINESTRING (-6.210259817917375 38.259416305016615,-5.188531302475292 37.679136788498276,-4.370049857309488 37.87018194277469,-3.463677787159337 38.272355099999935)";
	
	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}

	@Test
	public void divideServiceTest() {
		JtsDivideService service = new JtsDivideService();
		Response response = service.divide(getOperationData());
		@SuppressWarnings("unchecked")
		List<FlatGeometry> dividedGeometries =  (List<FlatGeometry>) response.getEntity();

		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(dividedGeometries.size() == 4);
	}

	@Test
	public void dividePostRequestTest() {
		Response response = target.request().post(Entity.entity(getOperationData(), "application/json;charset=UTF-8"));

		List<FlatGeometry> dividedGeometries = response.readEntity(new GenericType<List<FlatGeometry>>(){});
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(dividedGeometries.size() == 4);
		
	}

	private OperationData getOperationData() {
		OperationData opData = new OperationData();
		HashSet<FlatGeometry> source = new HashSet<FlatGeometry>();
		source.add(getPolygon());
		opData.setSourceData(source);

		HashSet<FlatGeometry> overlay = new HashSet<FlatGeometry>();
		overlay.add(getLine());
		opData.setOverlayData(overlay);
		return opData;
	}

	private FlatGeometry getPolygon() {
		return new FlatGeometryBuilder().id("pol1").crs("WGS84").wkt(POLYGON_FC_WKT).build();
	}

	private FlatGeometry getLine() {
		return new FlatGeometryBuilder().id("pol1").crs("WGS84").wkt(LINE_WKT).build();
	}

	@Test
	public void dividePostRequestTopologyErrorTest() {
		OperationData opData = new OperationData();
		
		
		opData.setSourceData(new HashSet<FlatGeometry>(DataTestProvider.getThreeEntities()));
		Set<FlatGeometry> overlayData = new HashSet<FlatGeometry>();
		overlayData.add(DataTestProvider.createFlatGeometry(DataTestProvider.DIVISION_LINE_4_3POLYGONS));
		opData.setOverlayData(overlayData);
		
		Response response = target.request().post(Entity.entity(opData, "application/json;charset=UTF-8"));

		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		log.info(error);
		Assert.isTrue(response.getStatus() == Status.CONFLICT.getStatusCode());
		
		
	}

	
	@Test
	public void dividePostRequestBadOverlayTest() {
		OperationData opData = DataTestProvider.getPolygonsFCIntersectionData();
		Response response = target.request().post(Entity.entity(opData, "application/json;charset=UTF-8"));

		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		log.info(error);
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST.getStatusCode());
	}

}
