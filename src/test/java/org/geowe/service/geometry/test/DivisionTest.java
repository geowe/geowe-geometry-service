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

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.geowe.service.geometry.FlatGeometryBuilder;
import org.geowe.service.model.DivisionData;
import org.geowe.service.model.FlatGeometry;
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

public class DivisionTest {

	private final Logger log = Logger.getLogger(DivisionTest.class);

	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/division";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;
	private static final String POLYGON_WKT = "POLYGON ((-5.9685606135835565 38.333782907228816,-3.9141172545765723 38.31654526867596,-3.9360899108226595 37.52790590365409,-5.81475201986113 37.40582726607118,-5.9685606135835565 38.333782907228816))";
	private static final String LINE_WKT = "LINESTRING (-6.210259817917375 38.259416305016615,-5.188531302475292 37.679136788498276,-4.370049857309488 37.87018194277469,-3.463677787159337 38.272355099999935)";
	private static final String DIVISION_LINE_WKT = "LINESTRING(-4.85070171987867 37.840905948919264,-4.757317930832915 37.66935417624159)";
	private static final String GEOMETRY_COLLECTION ="GEOMETRYCOLLECTION(POLYGON((9.10125539332254 40.73595856620715,9.108894324595404 40.73914525646253,9.113529181772018 40.73452775766318,9.109838462168717 40.73017010572397,9.102199530894955 40.73049531348437,9.10125539332254 40.73595856620715)),LINESTRING(9.110267615610397 40.72782856293198,9.120052314095256 40.729454643056684,9.12236974268356 40.73426760734868,9.132326102545628 40.732316447569104,9.1341285470032 40.72847899974987),POINT(9.112928366953126 40.72054323623031))";
	
	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}


	private FlatGeometry getFlatGeometry(String wkt) {
		return new FlatGeometryBuilder().id("id-1").crs("WGS84").wkt(wkt).build();
	}
	
	@Test
	public void divideLine(){
		DivisionData divisionData = new DivisionData();
		divisionData.setDivisionLine(getFlatGeometry(DIVISION_LINE_WKT));
		divisionData.setGeomToDivide(getFlatGeometry(LINE_WKT));
		printDivisionData(divisionData);
		target = restClient.target(SERVICE_URL + "/line");
		Response response = target.request()
				.post(Entity.entity(divisionData, "application/json;charset=UTF-8"));
		
		List<FlatGeometry> dividedGeometries = response
				.readEntity(new GenericType<List<FlatGeometry>>() {});
		response.close();
		
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(dividedGeometries.size() == 2);
	}
	
	@Test
	public void divideLineBadDivisionLine(){
		DivisionData divisionData = new DivisionData();
		divisionData.setDivisionLine(getFlatGeometry(POLYGON_WKT));
		divisionData.setGeomToDivide(getFlatGeometry(LINE_WKT));
		
		target = restClient.target(SERVICE_URL + "/line");
		Response response = target.request()
				.post(Entity.entity(divisionData, "application/json;charset=UTF-8"));
		
		ErrorEntity error= response
				.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void divideLineBadLineToDivide(){
		DivisionData divisionData = new DivisionData();
		divisionData.setDivisionLine(getFlatGeometry(DIVISION_LINE_WKT));
		divisionData.setGeomToDivide(getFlatGeometry(POLYGON_WKT));
		
		target = restClient.target(SERVICE_URL + "/line");
		Response response = target.request()
				.post(Entity.entity(divisionData, "application/json;charset=UTF-8"));
		
		ErrorEntity error= response
				.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void dividePolygon(){
		DivisionData divisionData = new DivisionData();
		divisionData.setDivisionLine(getFlatGeometry(LINE_WKT));
		divisionData.setGeomToDivide(getFlatGeometry(POLYGON_WKT));
		printDivisionData(divisionData);
		target = restClient.target(SERVICE_URL + "/polygon");
		Response response = target.request()
				.post(Entity.entity(divisionData, "application/json;charset=UTF-8"));
		
		List<FlatGeometry> dividedGeometries = response
				.readEntity(new GenericType<List<FlatGeometry>>() {});
		response.close();
		
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(dividedGeometries.size() == 2);
	}
	
	@Test
	public void divideBadPolygonToDivide(){
		DivisionData divisionData = new DivisionData();
		divisionData.setDivisionLine(getFlatGeometry(LINE_WKT));
		divisionData.setGeomToDivide(getFlatGeometry(DIVISION_LINE_WKT));
		
		target = restClient.target(SERVICE_URL + "/polygon");
		Response response = target.request()
				.post(Entity.entity(divisionData, "application/json;charset=UTF-8"));
		
		ErrorEntity error= response
				.readEntity(ErrorEntity.class);
		response.close();
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST.getStatusCode());
	}
	
	
	@Test
	public void decomposeFeatureCollection(){
				
		Response response = target.request()
				.post(Entity.entity(getFlatGeometry(GEOMETRY_COLLECTION), "application/json;charset=UTF-8"));
		
		List<FlatGeometry> dividedGeometries = response
				.readEntity(new GenericType<List<FlatGeometry>>() {});
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(dividedGeometries.size() == 3);
	}
	
	private void printDivisionData(DivisionData divisionData){
		ObjectMapper mapper = new ObjectMapper();
		try {
			log.info(mapper.writeValueAsString(divisionData));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
