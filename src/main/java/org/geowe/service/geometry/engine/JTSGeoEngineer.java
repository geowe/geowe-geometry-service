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

import org.geowe.service.model.DivisionData;
import org.geowe.service.model.FlatGeometry;
import org.geowe.service.model.OperationData;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.util.GeometryCombiner;
import com.vividsolutions.jts.geom.util.LinearComponentExtracter;
import com.vividsolutions.jts.precision.EnhancedPrecisionOp;

/**
 * This class represents the JTS geometry expert. It is responsible for
 * performing the geometric operations
 * 
 * @author rafa@geowe.org
 *
 */
public class JTSGeoEngineer implements GeoEngineer {

	private final JTSGeoEngineerHelper helper = new JTSGeoEngineerHelper();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.topology.engine.GeoEngineer#calculateBuffer(java.lang
	 * .String, double, int)
	 */
	@Override
	public String calculateBuffer(String wkt, double distance, int segments) {
		Geometry geom = helper.getGeom(wkt);
		return geom.buffer(distance, segments).toText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.topology.engine.GeoEngineer#calculateUnion(java.util
	 * .Collection)
	 */
	@Override
	public String calculateUnion(Collection<FlatGeometry> entities) {
		List<Geometry> geometries = helper.toGeometries(entities);
		Geometry unionGeom = geometries.get(0).union();
		for (Geometry geom : geometries) {
			unionGeom = unionGeom.union(geom);
		}

		return unionGeom.toText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.topology.engine.GeoEngineer#calculateEnvelpe(java.lang
	 * .String
	 */
	@Override
	public String calculateEnvelope(String wkt) {
		Geometry geom = helper.getGeom(wkt);
		return geom.getEnvelope().toText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.topology.engine.GeoEngineer#calculateCentroid(java.lang
	 * .String
	 */
	@Override
	public String calculateCentroid(String wkt) {
		Geometry geom = helper.getGeom(wkt);
		return geom.getCentroid().toText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.topology.engine.GeoEngineer#calculateIntersection(org.
	 * geowe.service.model.OperationData, double)
	 */
	@Override
	public String calculateIntersection(OperationData operationData, double tolerance) {
		final Geometry sourceGeometry = GeometryCombiner.combine(helper.toGeometries(operationData.getSourceData()));
		final Geometry overlayGeometry = GeometryCombiner.combine(helper.toGeometries(operationData.getOverlayData()));
		Geometry geomContorno = EnhancedPrecisionOp.intersection(sourceGeometry.buffer(tolerance),
				overlayGeometry.buffer(tolerance));

		return geomContorno.toText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geowe.service.geometry.engine.GeoEngineer#
	 * calculateIntersectionElements(org.geowe.service.model.OperationData,
	 * double)
	 */

	@Override
	public List<String> calculateIntersectionElements(OperationData operationData, double tolerance) {
		return helper.getBasicGeometries(calculateIntersection(operationData, tolerance));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.geometry.engine.GeoEngineer#calculateDifference(org.
	 * geowe.service.model.OperationData, double)
	 */
	@Override
	public String calculateDifference(OperationData operationData, double tolerance) {
		final Geometry sourceGeometry = GeometryCombiner.combine(helper.toGeometries(operationData.getSourceData()));
		final Geometry overlayGeometry = GeometryCombiner.combine(helper.toGeometries(operationData.getOverlayData()));
		Geometry geomContorno = EnhancedPrecisionOp.difference(sourceGeometry.buffer(tolerance),
				overlayGeometry.buffer(tolerance));

		return geomContorno.toText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.geometry.engine.GeoEngineer#calculateDifferenceElements
	 * (org.geowe.service.model.OperationData, double)
	 */
	@Override
	public List<String> calculateDifferenceElements(OperationData operationData, double tolerance) {
		return helper.getBasicGeometries(calculateDifference(operationData, tolerance));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.geometry.engine.GeoEngineer#calculateSymDifference(org.
	 * geowe.service.model.OperationData, double)
	 */
	@Override
	public String calculateSymDifference(OperationData operationData, double tolerance) {
		final Geometry sourceGeometry = helper.getGeom(combine(operationData.getSourceData()));
		final Geometry overlayGeometry = helper.getGeom(combine(operationData.getOverlayData()));
		Geometry geomContorno = EnhancedPrecisionOp.symDifference(sourceGeometry.buffer(tolerance),
				overlayGeometry.buffer(tolerance));

		return geomContorno.toText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geowe.service.geometry.engine.GeoEngineer#
	 * calculateSymDifferenceElements(org.geowe.service.model.OperationData,
	 * double)
	 */
	@Override
	public List<String> calculateSymDifferenceElements(OperationData operationData, double tolerance) {
		return helper.getBasicGeometries(calculateSymDifference(operationData, tolerance));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.geometry.engine.GeoEngineer#calculateIntersect(org.
	 * geowe.service.model.OperationData, double)
	 */
	@Override
	public List<FlatGeometry> calculateIntersectedElements(OperationData operationData, double tolerance) {
		final List<FlatGeometry> intersectedElements = new ArrayList<FlatGeometry>();
		JTSGeoEngineerHelper helper = new JTSGeoEngineerHelper();
		for (final FlatGeometry flatGeometry : operationData.getSourceData()) {
			if (helper.intersects(flatGeometry, operationData.getOverlayData(), tolerance)) {
				intersectedElements.add(flatGeometry);
			}
		}

		return intersectedElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geowe.service.geometry.engine.GeoEngineer#combine(java.util.
	 * Collection)
	 */
	@Override
	public String combine(Collection<FlatGeometry> entities) {
		final Geometry combinedGeometry = GeometryCombiner.combine(helper.toGeometries(entities));
		return combinedGeometry.toText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geowe.service.geometry.engine.GeoEngineer#calculateOverlapedUnion(org
	 * .geowe.service.model.OperationData)
	 */
	@Override
	public List<String> calculateOverlapedUnion(OperationData operationData) {

		operationData.getSourceData().addAll(operationData.getOverlayData());
		final List<Geometry> linesList = new ArrayList<Geometry>();
		final LinearComponentExtracter lineFilter = new LinearComponentExtracter(linesList);
		for (final FlatGeometry flatGeometry : operationData.getSourceData()) {
			final Geometry geom = helper.getGeom(flatGeometry.getWkt());
			geom.apply(lineFilter);
		}
		LineNoder lineNoder = new LineNoder();
		final Collection<Geometry> polys = lineNoder.polygonizer(lineNoder.nodeLines(linesList));

		
		return getOverlapedPolygonsWkt(polys);
	}

	private List<String> getOverlapedPolygonsWkt(Collection<Geometry> polys) {
		List<String> wkts = new ArrayList<String>();
		for (final Geometry geom : polys) {
			wkts.add(geom.toText());
		}
		return wkts;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.geowe.service.geometry.engine.GeoEngineer#dividePolygons(org.geowe.service.model.DivisionData)
	 */
	@Override
	public List<String> dividePolygon(DivisionData divisionData) {
		Geometry polygon = helper.getGeom(divisionData.getGeomToDivide().getWkt());
		Geometry line = helper.getGeom(divisionData.getDivisionLine().getWkt());
		Geometry splitedPolygons = helper.splitPolygon(polygon, line); 
				
		return helper.getBasicGeometries(splitedPolygons.toText());
	}

	/*
	 * (non-Javadoc)
	 * @see org.geowe.service.geometry.engine.GeoEngineer#divideLine(org.geowe.service.model.DivisionData)
	 */
	@Override
	public List<String> divideLine(DivisionData divisionData) {
		Geometry sourceLine = helper.getGeom(divisionData.getGeomToDivide().getWkt());
		Geometry divisionLine = helper.getGeom(divisionData.getDivisionLine().getWkt());
		Geometry unionGeom = sourceLine.union(divisionLine);
		
		return helper.splitLines(sourceLine, unionGeom);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.geowe.service.geometry.engine.GeoEngineer#decompose(java.lang.String)
	 */
	@Override
	public List<String> decompose(String wkt) {
		return helper.getBasicGeometries(wkt);
	}
	
}
