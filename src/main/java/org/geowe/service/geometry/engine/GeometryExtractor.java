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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.util.LineStringExtracter;
import com.vividsolutions.jts.geom.util.LinearComponentExtracter;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

/**
 * This class represents a Geometry extractor. It is responsible to extract
 * basic geometries form Multi Geometries.
 * 
 * @author rafa@geowe.org
 *
 */
public class GeometryExtractor {

	public List<String> getPolygons(MultiPolygon multiPolygon) {
		final List<String> polygonsWkt = new ArrayList<String>();
		for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
			final Polygon pol = (Polygon) multiPolygon.getGeometryN(i);
			polygonsWkt.add(pol.toText());
		}
		return polygonsWkt;
	}

	public List<String> getLineStrings(MultiLineString multiLine) {
		final List<String> lineStringsWkt = new ArrayList<String>();

		for (int i = 0; i < multiLine.getNumGeometries(); i++) {
			final LineString pol = (LineString) multiLine.getGeometryN(i);
			lineStringsWkt.add(pol.toText());
		}
		return lineStringsWkt;
	}

	public List<String> getPoints(MultiPoint multiPoint) {
		final List<String> pointsWkt = new ArrayList<String>();

		for (int i = 0; i < multiPoint.getNumGeometries(); i++) {
			final Point pol = (Point) multiPoint.getGeometryN(i);
			pointsWkt.add(pol.toText());
		}
		return pointsWkt;
	}

	public List<String> getMultiGeometries(GeometryCollection geometryCollection) {
		final List<String> geomsWkt = new ArrayList<String>();

		for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
			final Geometry geometry = geometryCollection.getGeometryN(i);
			geomsWkt.add(geometry.toText());
		}
		return geomsWkt;
	}

	@SuppressWarnings("unchecked")
	public Collection<Geometry> polygonize(Geometry geometry) {
		List<Geometry> lines = LineStringExtracter.getLines(geometry);
		Polygonizer polygonizer = new Polygonizer();
		polygonizer.add(lines);
		return polygonizer.getPolygons();
	}
	
	public List<LineString> linealize(Geometry geometry){
		LinearComponentExtracter extracter = new LinearComponentExtracter(
				getLineStrings((MultiLineString) geometry));
		return extracter.getLines(geometry);
	}
	
	public Geometry createGeometryCollection(Collection<Geometry> geometries){
		Geometry[] geomArray = GeometryFactory.toGeometryArray(geometries);
		GeometryFactory factory = new GeometryFactory();
		return factory.createGeometryCollection(geomArray);
	}
}
