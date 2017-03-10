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
package org.geowe.service.geometry.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.geowe.service.geometry.FlatGeometryBuilder;
import org.geowe.service.model.FlatGeometry;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.ReaderException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * This class represents the JTSGeoEngineer assistant. It is responsible to
 * perform simple geometries conversions
 * 
 * @author lotor
 *
 */
public class JTSGeoEngineerHelper {

	private final Logger log = Logger.getLogger(JTSGeoEngineerHelper.class);

	public Geometry getGeom(String wkt) {
		Geometry geom;
		try {
			WKTReader wktReader = new WKTReader();
			geom = wktReader.read(wkt);
		} catch (ParseException e) {
			throw new ReaderException("Error Reading wkt");
		}
		return geom;
	}

	public List<Geometry> toGeometries(Collection<FlatGeometry> entities) {
		List<Geometry> geometries = new ArrayList<Geometry>();
		for (FlatGeometry flatGeometry : entities) {
			geometries.add(getGeom(flatGeometry.getWkt()));
		}

		return geometries;
	}

	public List<String> getBasicGeometries(String wkt) {
		final List<String> elementsWkt = new ArrayList<String>();

		Geometry geomContorno = getGeom(wkt);

		if (geomContorno != null && geomContorno.isValid()) {
			GeometryExtractor extractor = new GeometryExtractor();
			if (geomContorno instanceof Polygon) {
				elementsWkt.add(geomContorno.toText());
			} else if (geomContorno instanceof MultiPolygon) {
				elementsWkt.addAll(extractor.getPolygons((MultiPolygon) geomContorno));
			} else if (geomContorno instanceof LineString) {
				elementsWkt.add(geomContorno.toText());
			} else if (geomContorno instanceof MultiLineString) {
				elementsWkt.addAll(extractor.getLineStrings((MultiLineString) geomContorno));
			} else if (geomContorno instanceof Point) {
				elementsWkt.add(geomContorno.toText());
			} else if (geomContorno instanceof MultiPoint) {
				elementsWkt.addAll(extractor.getPoints((MultiPoint) geomContorno));
			} else if (geomContorno instanceof GeometryCollection) {
				elementsWkt.addAll(extractor.getMultiGeometries((GeometryCollection) geomContorno));
			}
		}
		return elementsWkt;
	}

	public boolean intersects(final FlatGeometry fgeomToIntersect, final Collection<FlatGeometry> flatGeometries,
			double tolerance) {
		boolean intersects = false;
		Geometry geom = getGeom(fgeomToIntersect.getWkt());
		for (final FlatGeometry flatGeometry : flatGeometries) {
			if (geom.buffer(tolerance).intersects(getGeom(flatGeometry.getWkt()).buffer(tolerance))) {
				intersects = true;
				break;
			}
		}
		return intersects;
	}

	public FlatGeometry getIntersectedFlatGeomtetry(String wkt, Set<FlatGeometry> sourceFlatGeometries,
			double tolerance) {
		FlatGeometry intersectedFlatGeom = new FlatGeometryBuilder().wkt(wkt).build();
		for (FlatGeometry flatGeometry : sourceFlatGeometries) {
			if (getGeom(wkt).intersects(getGeom(flatGeometry.getWkt()).buffer(tolerance))) {
				intersectedFlatGeom.setCrs(flatGeometry.getCrs());
				intersectedFlatGeom.setId(flatGeometry.getId());
				break;
			}
		}
		return intersectedFlatGeom;
	}
	
	public List<FlatGeometry> getFilledFlatGeometries(Set<FlatGeometry> sourceFlatGeometries,
			List<String> intersectedWkts, double tolerance) {
		List<FlatGeometry> fGeoms = new ArrayList<FlatGeometry>();

		for (String intersectedWkt : intersectedWkts) {
			fGeoms.add(getIntersectedFlatGeomtetry(intersectedWkt, sourceFlatGeometries, tolerance));
		}
		return fGeoms;
	}

}
