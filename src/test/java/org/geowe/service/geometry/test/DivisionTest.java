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
import org.geowe.service.geometry.engine.GeometryExtractor;
import org.geowe.service.geometry.engine.JTSGeoEngineerHelper;
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.OperationData;
import org.geowe.service.model.error.ErrorEntity;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.Assert;

public class DivisionTest {

	private final Logger log = Logger.getLogger(DivisionTest.class);

	private static final String SERVICE_URL = "http://127.0.0.1:8080/ggs/operations/jts/division";
	private ResteasyClient restClient;
	private ResteasyWebTarget target;
	private static final String POLYGON_FC_WKT = "GEOMETRYCOLLECTION(POLYGON((-5.9685606135835565 38.333782907228816,-3.9141172545765723 38.31654526867596,-3.9360899108226595 37.52790590365409,-5.81475201986113 37.40582726607118,-5.9685606135835565 38.333782907228816)),POLYGON((-3.886651448642032 38.19360849133716,-3.5295957846435067 38.17201919361452,-3.8070005697500684 37.98392250848125,-3.886651448642032 38.19360849133716)))";
	private static final String LINE_WKT = "LINESTRING (-6.210259817917375 38.259416305016615,-5.188531302475292 37.679136788498276,-4.370049857309488 37.87018194277469,-3.463677787159337 38.272355099999935)";
	private static final String LINE_2_WKT = "LINESTRING(-4.8663841287263905 37.78385163362393,-4.840634922188039 37.77910307500333,-4.7997795144805435 37.7928052283946)";
	private static final String DIVISION_LINE_WKT = "LINESTRING(-4.85070171987867 37.840905948919264,-4.757317930832915 37.66935417624159)";
	private static final String LINE_GC = "GEOMETRYCOLLECTION(LINESTRING(-4.8663841287263905 37.78385163362393,-4.840634922188039 37.77910307500333,-4.7997795144805435 37.7928052283946),LINESTRING(-6.210259817917375 38.2594163050166,-5.188531302475292 37.67913678849826,-4.370049857309488 37.870181942774686,-3.463677787159337 38.27235509999992),LINESTRING(-4.8592848030977125 37.76576530568103,-4.84898512048239 37.77268578071596,-4.843663617797742 37.76834359756804,-4.824437543582451 37.77634929940774,-4.818429395390217 37.771735949940556,-4.809331342413306 37.78069101241037,-4.802979871467189 37.781098034930324),LINESTRING(-4.853276654905389 37.75219386689201,-4.801434919074896 37.76332263045088,-4.801434919074896 37.76332263045088,-4.792851850228719 37.76277980277709),LINESTRING(-4.854306623166948 37.78584646538825,-4.814481183720922 37.79222244923771))";
	private static final String CROSSED_LINES_1 ="LINESTRING(-4.8663841287263905 37.78385163362393,-4.840634922188039 37.77910307500333,-4.7997795144805435 37.7928052283946)";
	private static final String CROSSED_LINES_2 ="LINESTRING(-4.854306623166948 37.78584646538825,-4.8308196263025325 37.78963116592093,-4.818460007164181 37.790512938540985,-4.812366028283342 37.7831871235283)";
	@Before
	public void setUp() throws Exception {
		restClient = new ResteasyClientBuilder().build();
		target = restClient.target(SERVICE_URL);
	}

	@Test
	public void divideServiceTest() {
		JtsDivideService service = new JtsDivideService();
		Response response = service.dividePolygons(getOperationData());
		@SuppressWarnings("unchecked")
		List<FlatGeometry> dividedGeometries = (List<FlatGeometry>) response.getEntity();

		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(dividedGeometries.size() == 4);
	}

	@Test
	public void dividePolygonsPostRequestTest() {
		target = restClient.target(SERVICE_URL + "/polygons");
		Response response = target.request().post(Entity.entity(getOperationData(), "application/json;charset=UTF-8"));

		List<FlatGeometry> dividedGeometries = response.readEntity(new GenericType<List<FlatGeometry>>() {
		});
		response.close();
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(dividedGeometries.size() == 4);

	}

	private OperationData getOperationData() {
		OperationData opData = new OperationData();
		HashSet<FlatGeometry> source = new HashSet<FlatGeometry>();
		source.add(getFlatGeometry(POLYGON_FC_WKT));
		opData.setSourceData(source);

		HashSet<FlatGeometry> overlay = new HashSet<FlatGeometry>();
		overlay.add(getFlatGeometry(LINE_WKT));
		opData.setOverlayData(overlay);
		return opData;
	}

	private FlatGeometry getFlatGeometry(String wkt) {
		return new FlatGeometryBuilder().id("id-1").crs("WGS84").wkt(wkt).build();
	}

	@Test
	public void dividePostRequestTopologyErrorTest() {
		OperationData opData = new OperationData();

		opData.setSourceData(new HashSet<FlatGeometry>(DataTestProvider.getThreeEntities()));
		Set<FlatGeometry> overlayData = new HashSet<FlatGeometry>();
		overlayData.add(DataTestProvider.createFlatGeometry(DataTestProvider.DIVISION_LINE_4_3POLYGONS));
		opData.setOverlayData(overlayData);
		target = restClient.target(SERVICE_URL + "/polygons");
		Response response = target.request().post(Entity.entity(opData, "application/json;charset=UTF-8"));

		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		log.info(error);
		Assert.isTrue(response.getStatus() == Status.CONFLICT.getStatusCode());

	}

	@Test
	public void dividePostRequestBadOverlayTest() {
		OperationData opData = DataTestProvider.getPolygonsFCIntersectionData();
		target = restClient.target(SERVICE_URL + "/polygons");
		Response response = target.request().post(Entity.entity(opData, "application/json;charset=UTF-8"));

		ErrorEntity error = response.readEntity(ErrorEntity.class);
		response.close();
		log.info(error);
		Assert.isTrue(response.getStatus() == Status.BAD_REQUEST.getStatusCode());
	}

	@Test
	public void divideLinesPostRequestTest() {
		target = restClient.target(SERVICE_URL + "/lines");
		Response response = target.request()
				.post(Entity.entity(getLinesOperationData(LINE_GC), "application/json;charset=UTF-8"));

		List<FlatGeometry> dividedGeometries = response
				.readEntity(new GenericType<List<FlatGeometry>>() {});
		response.close();
		printGC(dividedGeometries);
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(dividedGeometries.size() == 10);

	}
	
	@Test
	@Ignore
	//TODO: Does not work with cross lines (not implemented)
	public void divideCrossedLinesPostRequestTest() {
		target = restClient.target(SERVICE_URL + "/lines");
		OperationData opData = getLinesOperationData(CROSSED_LINES_1);
		opData.getSourceData().add(getFlatGeometry(CROSSED_LINES_2));
		Response response = target.request()
				.post(Entity.entity(opData, "application/json;charset=UTF-8"));
		
		List<FlatGeometry> dividedGeometries = response
				.readEntity(new GenericType<List<FlatGeometry>>() {});
		response.close();
		log.info(dividedGeometries.size());
		printGC(dividedGeometries);
		Assert.isTrue(response.getStatus() == Status.CREATED.getStatusCode());
		Assert.isTrue(dividedGeometries.size() == 4);

	}

	private void printGC(List<FlatGeometry> fgeoms) {
		Geometry geom = new GeometryExtractor()
				.createGeometryCollection(new JTSGeoEngineerHelper().toGeometries(fgeoms));
		log.info("GEOMETRY COLLECTION: " + geom);
	}

	private OperationData getLinesOperationData(String sourceDataWkt) {
		OperationData opData = new OperationData();
		HashSet<FlatGeometry> source = new HashSet<FlatGeometry>();
		source.add(getFlatGeometry(sourceDataWkt));
		opData.setSourceData(source);

		HashSet<FlatGeometry> overlay = new HashSet<FlatGeometry>();
		overlay.add(getFlatGeometry(DIVISION_LINE_WKT));
		opData.setOverlayData(overlay);
		return opData;
	}

}
