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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geowe.service.geometry.FlatGeometryBuilder;
import org.geowe.service.geometry.engine.JTSGeoEngineerHelper;
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.OperationData;
import org.jboss.resteasy.spi.ReaderException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class DataTestProvider {

	public static final String POLYGON_1 = "POLYGON((-4.7687530517578125 37.633483617951576,-4.783172607421875 37.60084982441606,-4.7055816650390625 37.584527557100245,-4.7124481201171875 37.63783370818002,-4.7687530517578125 37.633483617951576))";
	public static final String POLYGON_2 = "POLYGON((-4.7577667236328125 37.636202454188854,-4.78729248046875 37.579630178849854,-4.725494384765625 37.54316179356405,-4.6307373046875 37.55350538772098,-4.6410369873046875 37.59758565735052,-4.67742919921875 37.63837745155179,-4.7577667236328125 37.636202454188854))";
	public static final String POLYGON_3 = "POLYGON((-4.634513854980469 37.63147161675051,-4.6657562255859375 37.60155704339839,-4.6369171142578125 37.55965642520303,-4.623527526855469 37.54441399087736,-4.6039581298828125 37.57516784429852,-4.634513854980469 37.63147161675051))";
	public static final String CRS = "WGS84";
	public static final String DEFAULT_ID = "1";

	
	public static Geometry getGeom(String wkt) {
		Geometry geom;
		try {
			WKTReader wktReader = new WKTReader();
			geom = wktReader.read(wkt);
		} catch (ParseException e) {
			throw new ReaderException("Error Reading wkt");
		}
		return geom;
	}
	
	public static List<FlatGeometry> getThreeEntities() {

		List<FlatGeometry> geoms = new ArrayList<FlatGeometry>();
		geoms.add(createFlatGeometry(POLYGON_1));
		geoms.add(createFlatGeometry(POLYGON_2));
		geoms.add(createFlatGeometry(POLYGON_3));
		return geoms;
	}

	public static List<FlatGeometry> getTwoEntities() {
		List<FlatGeometry> geoms = new ArrayList<FlatGeometry>();
		geoms.add(createFlatGeometry(POLYGON_1));
		geoms.add(createFlatGeometry(POLYGON_2));
		return geoms;
	}

	public static FlatGeometry createFlatGeometry(String wkt) {
		FlatGeometry geom = new FlatGeometryBuilder().wkt(wkt).crs(CRS).id(DEFAULT_ID).build();
		return geom;
	}


	public static OperationData getIntersectionData() {
		OperationData operationData = new OperationData();

		operationData.setSourceData(new HashSet<FlatGeometry>(getTwoEntities()));
		operationData.setOverlayData(new HashSet<FlatGeometry>(getThreeEntities()));
		return operationData;
	}

	public static OperationData getPolygonsFCIntersectionData() {
		OperationData operationData = new OperationData();
		
		Set<FlatGeometry> source = new HashSet<FlatGeometry>();
		source.addAll(getGeomsFromFile("feature-collection-polygons.wkt"));
		operationData.setSourceData(source);
		
		Set<FlatGeometry> overlay = new HashSet<FlatGeometry>();
		overlay.addAll(getGeomsFromFile("overlay.wkt"));
		operationData.setOverlayData(overlay);

		return operationData;
	}
	
	public static OperationData get2PolygonsOverlayFCData() {
		OperationData operationData = new OperationData();
		
		Set<FlatGeometry> source = new HashSet<FlatGeometry>();
		source.addAll(getGeomsFromFile("feature-collection-polygons.wkt"));
		operationData.setSourceData(source);
		
		Set<FlatGeometry> overlay = new HashSet<FlatGeometry>();
		overlay.addAll(getGeomsFromFile("overlay-2Polygons.wkt"));
		operationData.setOverlayData(overlay);

		return operationData;
	}
	
	private static Set<FlatGeometry> getGeomsFromFile(String fileName){
		JTSGeoEngineerHelper helper = new JTSGeoEngineerHelper();
		List<String> geomsWkt = helper.getBasicGeometries(getFile(fileName));
		Set<FlatGeometry> fGeoms = new HashSet<FlatGeometry>();
		int id = Integer.valueOf(DEFAULT_ID);
		for(String wkt: geomsWkt){
			fGeoms.add(new FlatGeometryBuilder().wkt(wkt).crs(CRS).id(""+id++).build());
		}
		return fGeoms;
	}
	
	private static String getFile(String fileName) {
		ClassLoader classLoader = new DataTestProvider().getClass().getClassLoader();
		
		String result = "";
		try {
			File file = new File(classLoader.getResource(fileName).getFile());
			result = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static OperationData getLinesFCIntersectionData() {
		OperationData operationData = new OperationData();
		
		Set<FlatGeometry> source = new HashSet<FlatGeometry>();
		source.addAll(getGeomsFromFile("feature-collection-lines.wkt"));
		operationData.setSourceData(source);
		
		Set<FlatGeometry> overlay = new HashSet<FlatGeometry>();
		overlay.addAll(getGeomsFromFile("overlay.wkt"));
		operationData.setOverlayData(overlay);

		return operationData;
	}

}
