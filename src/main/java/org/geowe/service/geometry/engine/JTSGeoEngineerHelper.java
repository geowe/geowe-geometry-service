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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * This class represents the JTSGeoEngineer assistant. It is responsible to
 * perform simple geometries conversions
 * 
 * @author rafa@geowe.org
 *
 */
public class JTSGeoEngineerHelper {

	private final Logger log = Logger.getLogger(JTSGeoEngineerHelper.class);

	public Geometry getGeom(String wkt) {
		Geometry geom;
		try {
			geom = new WKTReader().read(wkt);
		} catch (ParseException e) {
			throw new ReaderException("Error Reading wkt");
		}
		return geom;
	}

	public List<Geometry> toGeometries(Collection<FlatGeometry> entities) {
		return entities.stream().map(flatGeometry -> getGeom(flatGeometry.getWkt()))
				.collect(Collectors.toList());
	}

	public List<String> getBasicGeometries(String wkt) {
		final List<String> elementsWkt = new ArrayList<String>();

		Geometry geomContorno = getGeom(wkt);

		if (geomContorno != null && geomContorno.isValid()) {
			GeometryExtractor extractor = new GeometryExtractor();
			switch (geomContorno.getGeometryType()) {
			case "Polygon":
				elementsWkt.add(geomContorno.toText());
				break;
			case "MultiPolygon":
				elementsWkt.addAll(extractor.getPolygons((MultiPolygon) geomContorno));
				break;
			case "LineString":
				elementsWkt.add(geomContorno.toText());
				break;
			case "MultiLineString":
				elementsWkt.addAll(extractor.getLineStrings((MultiLineString) geomContorno));
				break;
			case "Point":
				elementsWkt.add(geomContorno.toText());
				break;
			case "MultiPoint":
				elementsWkt.addAll(extractor.getPoints((MultiPoint) geomContorno));
				break;
			default:
				elementsWkt.addAll(extractor.getMultiGeometries((GeometryCollection) geomContorno));
				break;
			}
		}
		return elementsWkt;
	}

	public Optional<FlatGeometry> getFirtsIntersected(final FlatGeometry fgeomToIntersect, final Collection<FlatGeometry> flatGeometries,
			double tolerance) {
		Geometry geom = getGeom(fgeomToIntersect.getWkt());
		Optional<FlatGeometry> optional = flatGeometries.parallelStream().filter(
				flatGeometry -> (geom.buffer(tolerance).intersects(getGeom(flatGeometry.getWkt()).buffer(tolerance))))
				.findFirst();

		return optional;
	}

	public Set<String> getWkts(List<FlatGeometry> overlapedUnionFlatGeometries) {
		return overlapedUnionFlatGeometries.stream().map(flatGeometry -> flatGeometry.getWkt())
				.collect(Collectors.toSet());
	}

	public List<String> getWkts(Collection<Geometry> geometries) {
		return geometries.stream().map(geometry -> geometry.toText())
				.collect(Collectors.toList());
	}

	public List<String> splitPolygon(Geometry poly, Geometry line) {
		Geometry nodedLinework = poly.getBoundary().union(line);
		GeometryExtractor geometryExtractor = new GeometryExtractor();
		Collection<Geometry> geometries = geometryExtractor.polygonize(nodedLinework);
		Geometry polys = geometryExtractor.createGeometryCollection(geometries);

		// Only keep polygons which are inside the input
		List<Geometry> splitedPolygons = new ArrayList<Geometry>();
		for (int i = 0; i < polys.getNumGeometries(); i++) {
			Polygon candpoly = (Polygon) polys.getGeometryN(i);
			if (poly.contains(candpoly.getInteriorPoint())) {
				splitedPolygons.add(candpoly);
			}
		}
		return getWkts(splitedPolygons);
	}

	public List<String> splitLines(Geometry sourceLines, Geometry unionGeom) {
		GeometryExtractor extractor = new GeometryExtractor();
		List<LineString> lines = extractor.linealize(unionGeom);

		List<Geometry> segments = getSegments(sourceLines, lines);
		return getWkts(segments);
	}

	private List<Geometry> getSegments(Geometry sourceLines, List<LineString> lines) {
		List<Geometry> segments = new ArrayList<Geometry>();
		lines.forEach(line -> {
			if (sourceLines.intersects(line) && !line.crosses(sourceLines)) {
				segments.add(line);
			}
		});
		return segments;
	}
}
